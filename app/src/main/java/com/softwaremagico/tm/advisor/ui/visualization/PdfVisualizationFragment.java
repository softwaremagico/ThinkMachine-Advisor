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
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.translation.TextVariablesManager;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.pdf.complete.CharacterSheet;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class PdfVisualizationFragment extends Fragment {
    private static final int FILE_IDENTIFICATOR = 42;
    private File characterSheetAsPdf;

    protected abstract View getFragmentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    protected abstract byte[] generatePdf();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = getFragmentView(inflater, container);

        final PDFView pdfViewer = root.findViewById(R.id.pdf_viewer);
        pdfViewer.fromBytes(generatePdf()).load();

        final FloatingActionButton fab = root.findViewById(R.id.share);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sharePdf();
                } catch (IOException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
            }
        });


        return root;
    }

    protected void sharePdf() throws IOException {
        final File imagePath = new File(getContext().getCacheDir(), "pdf");
        characterSheetAsPdf = new File(imagePath, CharacterManager.getSelectedCharacter().getCompleteNameRepresentation().length() > 0 ?
                CharacterManager.getSelectedCharacter().getCompleteNameRepresentation() + "_sheet.pdf" :
                "pdf_sheet.pdf");
        final Uri contentUri = FileProvider.getUriForFile(getContext(), "com.softwaremagico.tm.advisor", characterSheetAsPdf);

        if (contentUri != null) {
            //getContext().grantUriPermission("com.softwaremagico.tm.advisor", contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            final CharacterSheet characterSheet = new CharacterSheet(CharacterManager.getSelectedCharacter());
            characterSheetAsPdf.getParentFile().mkdirs();
            characterSheet.createFile(characterSheetAsPdf.getAbsolutePath());

            final Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setType(getActivity().getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + (CharacterManager.getSelectedCharacter().getCompleteNameRepresentation().length() > 0 ?
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

        if (requestCode == FILE_IDENTIFICATOR && characterSheetAsPdf != null) {
            characterSheetAsPdf.delete();
        }
    }
}
