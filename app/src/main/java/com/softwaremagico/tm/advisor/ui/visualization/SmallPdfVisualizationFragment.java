package com.softwaremagico.tm.advisor.ui.visualization;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;
import com.itextpdf.text.DocumentException;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.CharacterManager;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.pdf.complete.EmptyPdfBodyException;
import com.softwaremagico.tm.pdf.small.SmallCharacterSheet;

public class SmallPdfVisualizationFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static SmallPdfVisualizationFragment newInstance(int index) {
        SmallPdfVisualizationFragment fragment = new SmallPdfVisualizationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.visualization_small_pdf_fragment, container, false);

        PDFView pdfViewer = (PDFView) root.findViewById(R.id.pdf_viewer);
        final SmallCharacterSheet characterSheet = new SmallCharacterSheet(CharacterManager.getSelectedCharacter());
        try {
            pdfViewer.fromBytes(characterSheet.generate()).load();
        } catch (EmptyPdfBodyException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        } catch (DocumentException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        } catch (InvalidXmlElementException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }

        return root;
    }


}
