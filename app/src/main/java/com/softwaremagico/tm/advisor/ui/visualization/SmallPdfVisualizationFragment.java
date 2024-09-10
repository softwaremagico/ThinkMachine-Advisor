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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.pdf.small.SmallCharacterSheet;

public class SmallPdfVisualizationFragment extends PdfVisualizationFragment {

    public static SmallPdfVisualizationFragment newInstance(int index) {
        final SmallPdfVisualizationFragment fragment = new SmallPdfVisualizationFragment();
        final Bundle bundle = new Bundle();
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
        return mViewModel.generateSmallPdf();
    }

    @Override
    protected void generatePdfFile(String path) {
        final SmallCharacterSheet characterSheet = new SmallCharacterSheet(CharacterManager.getSelectedCharacter());
        characterSheet.createFile(path);
    }
}
