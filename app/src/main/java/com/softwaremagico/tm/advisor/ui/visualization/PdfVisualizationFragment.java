/*
 *  Copyright (C) 2020 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.ui.visualization;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.TextVariablesManager;
import com.softwaremagico.tm.log.MachineLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class PdfVisualizationFragment extends Fragment implements VisualizationFragment {
    protected static final String ARG_SECTION_NUMBER = "section_number";
    private static final int FILE_IDENTIFICATION = 42;
    protected CharacterPdfViewModel mViewModel;
    private File characterSheetAsPdf;
    private View root;
    private LinearLayout layout;

    protected abstract View getFragmentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    protected abstract byte[] generatePdf();

    protected abstract void generatePdfFile(String path);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = getFragmentView(inflater, container);
        mViewModel = new ViewModelProvider(this).get(CharacterPdfViewModel.class);
        layout = root.findViewById(R.id.content);

        final FloatingActionButton fab = root.findViewById(R.id.share);
        fab.setOnClickListener(view -> sharePdf());

        return root;
    }

    protected void initData() {
        try {
            layout.removeAllViews();
            final List<Bitmap> images = pdfRender(generatePdf(), root.getWidth());
            for (Bitmap image : images) {
                if (image != null) {
                    final ImageView imageView = new ImageView(getContext());
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    if (root.getWidth() > 0) {
                        imageView.setImageBitmap(Bitmap.createScaledBitmap(image, root.getWidth(), root.getHeight(), false));
                    } else {
                        imageView.setImageBitmap(image);
                    }
                    layout.addView(imageView);
                }
            }
        } catch (IOException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }
    }

    private void sharePdf() {
        final File imagePath = new File(getContext().getCacheDir(), "pdf");
        characterSheetAsPdf = new File(imagePath, !CharacterManager.getSelectedCharacter().getCompleteNameRepresentation().isEmpty() ?
                CharacterManager.getSelectedCharacter().getCompleteNameRepresentation() + "_sheet.pdf" :
                "pdf_sheet.pdf");
        final Uri contentUri = FileProvider.getUriForFile(getContext(), "com.softwaremagico.tm.advisor", characterSheetAsPdf);

        if (contentUri != null) {
            imagePath.mkdirs();
            characterSheetAsPdf.getParentFile().mkdirs();
            generatePdfFile(characterSheetAsPdf.getAbsolutePath());

            final Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setType(getActivity().getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + (!CharacterManager.getSelectedCharacter().getCompleteNameRepresentation().isEmpty() ?
                    ": " + CharacterManager.getSelectedCharacter().getCompleteNameRepresentation() : ""));
            shareIntent.putExtra(Intent.EXTRA_TEXT, TextVariablesManager.replace(getString(R.string.share_body)));

            final Intent chooser = Intent.createChooser(shareIntent, "Share File");
            final List<ResolveInfo> resInfoList = getContext().getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
            for (final ResolveInfo resolveInfo : resInfoList) {
                final String packageName = resolveInfo.activityInfo.packageName;
                getContext().grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivity(chooser);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_IDENTIFICATION && characterSheetAsPdf != null) {
            if (characterSheetAsPdf.delete()) {
                MachineLog.debug(this.getClass().getName(), "File deleted");
            }
        }
    }

    @Override
    public void updateData() {
        initData();
    }


    private List<Bitmap> pdfRender(byte[] byteArray, int width) throws IOException {
        // create a new renderer
        PdfRenderer renderer = new PdfRenderer(getFileDescriptor(byteArray));

        List<Bitmap> gallery = new ArrayList<>();

        // let us just render all pages
        final int pageCount = renderer.getPageCount();
        for (int i = 0; i < pageCount; i++) {
            gallery.add(pdfRender(renderer, i, width));
        }

        // close the renderer
        renderer.close();

        return gallery;
    }

    private Bitmap pdfRender(PdfRenderer renderer, int pageNum, int width) {
        // let us just render all pages
        final int pageCount = renderer.getPageCount();
        final PdfRenderer.Page page = renderer.openPage(pageNum);

        // create bitmap at appropriate size
        final float ratio = (float) page.getHeight() / page.getWidth();
        final float newHeight = width * ratio;
        final Bitmap bitmap = Bitmap.createBitmap(width != 0 ? width : page.getWidth(), width != 0 ? (int) newHeight : page.getHeight(), Bitmap.Config.ARGB_8888);

        // render PDF page to bitmap
        //var rect = new Rect(0, 0, width, height);
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);

        // close the page
        page.close();

        //Compress image.
        return bitmap;
    }

    private ParcelFileDescriptor getFileDescriptor(byte[] byteArray) throws IOException {
        File file = File.createTempFile("temp_sheet", "pdf");
        try (FileOutputStream output = new FileOutputStream(file, true)) {
            output.write(byteArray);
        }
        file.deleteOnExit();

        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
    }
}
