package com.softwaremagico.tm.advisor.ui.visualization;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itextpdf.text.DocumentException;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.CharacterManager;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.pdf.complete.CharacterSheet;
import com.softwaremagico.tm.pdf.complete.EmptyPdfBodyException;

public class CompletePdfVisualizationFragment extends PdfVisualizationFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static CompletePdfVisualizationFragment newInstance(int index) {
        CompletePdfVisualizationFragment fragment = new CompletePdfVisualizationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View getFragmentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.visualization_pdf_fragment, container, false);
    }

    @Override
    protected byte[] generatePdf() {
        final CharacterSheet characterSheet = new CharacterSheet(CharacterManager.getSelectedCharacter());
        try {
            return (characterSheet.generate());
        } catch (EmptyPdfBodyException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        } catch (DocumentException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        } catch (InvalidXmlElementException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }
        return new byte[0];
    }
}
