package com.softwaremagico.tm.advisor.ui.visualization;

import android.content.Intent;
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
import com.itextpdf.text.DocumentException;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.CharacterManager;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.pdf.complete.CharacterSheet;
import com.softwaremagico.tm.pdf.complete.EmptyPdfBodyException;

import java.io.File;
import java.io.IOException;

public class PdfVisualizationFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int FILE_IDENTIFICATOR = 42;
    File characterSheetAsPdf;

    public static PdfVisualizationFragment newInstance(int index) {
        PdfVisualizationFragment fragment = new PdfVisualizationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.visualization_pdf_fragment, container, false);

        PDFView pdfViewer = (PDFView) root.findViewById(R.id.pdf_viewer);
        final CharacterSheet characterSheet = new CharacterSheet(CharacterManager.getSelectedCharacter());
        try {
            pdfViewer.fromBytes(characterSheet.generate()).load();
        } catch (EmptyPdfBodyException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        } catch (DocumentException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        } catch (InvalidXmlElementException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }

        FloatingActionButton fab = root.findViewById(R.id.share);
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

    private void sharePdf() throws IOException {
        File imagePath = new File(getContext().getCacheDir(), "pdf");
        characterSheetAsPdf = new File(imagePath, "pdf_sheet.pdf");
        Uri contentUri = FileProvider.getUriForFile(getContext(), "com.softwaremagico.tm.advisor.ui.fileprovider", characterSheetAsPdf);
        final CharacterSheet characterSheet = new CharacterSheet(CharacterManager.getSelectedCharacter());
        characterSheet.createFile(characterSheetAsPdf.getAbsolutePath());

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getActivity().getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, "asd");
            startActivityForResult(Intent.createChooser(shareIntent, "Choose an app"), FILE_IDENTIFICATOR);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_IDENTIFICATOR) {
            if (characterSheetAsPdf != null) {
                characterSheetAsPdf.delete();
            }
        }
    }


}
