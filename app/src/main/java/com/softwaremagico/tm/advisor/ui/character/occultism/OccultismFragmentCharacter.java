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

package com.softwaremagico.tm.advisor.ui.character.occultism;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.TranslatedNumberPicker;
import com.softwaremagico.tm.advisor.ui.components.counters.OccultismExtraCounter;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.occultism.InvalidPsiqueLevelException;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;

import java.util.HashMap;
import java.util.Map;

public class OccultismFragmentCharacter extends CharacterCustomFragment {
    private OccultismViewModel mViewModel;
    private final Map<String, TranslatedNumberPicker> translatedNumberPickers = new HashMap<>();

    private OccultismExtraCounter extraCounter;

    private View root;

    public static OccultismFragmentCharacter newInstance(int index) {
        final OccultismFragmentCharacter fragment = new OccultismFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setCharacter(View root, CharacterPlayer character) {
        if (extraCounter != null) {
            extraCounter.setCharacter(character);
        }
        try {
            for (OccultismType occultismType : OccultismTypeFactory.getInstance().getElements(CharacterManager.getSelectedCharacter().getLanguage(),
                    CharacterManager.getSelectedCharacter().getModuleName())) {
                if (translatedNumberPickers.get(occultismType.getName()) != null) {
                    translatedNumberPickers.get(occultismType.getName()).setValue(CharacterManager.getSelectedCharacter().getOccultismLevel(occultismType));
                }
            }
        } catch (InvalidXmlElementException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }
    }

    @Override
    protected void initData() {
        final LinearLayout rootLayout = root.findViewById(R.id.root_container);

        addSection(ThinkMachineTranslator.getTranslatedText("occultism"), rootLayout);
        createOccultismSelector(rootLayout, OccultismTypeFactory.getPsi(CharacterManager.getSelectedCharacter().getLanguage(),
                CharacterManager.getSelectedCharacter().getModuleName()));
        createOccultismSelector(rootLayout, OccultismTypeFactory.getTheurgy(CharacterManager.getSelectedCharacter().getLanguage(),
                CharacterManager.getSelectedCharacter().getModuleName()));
        addSection(ThinkMachineTranslator.getTranslatedText("psi"), rootLayout);
        addSection(ThinkMachineTranslator.getTranslatedText("theurgy"), rootLayout);

        setCharacter(root, CharacterManager.getSelectedCharacter());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_occultism_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(OccultismViewModel.class);

        extraCounter = root.findViewById(R.id.extra_counter);

        return root;
    }

    private void createOccultismSelector(LinearLayout linearLayout, OccultismType occultismType) {
        final TranslatedNumberPicker occultismNumberPicker = new TranslatedNumberPicker(getContext(), null);
        translatedNumberPickers.put(occultismType.getName(), occultismNumberPicker);
        occultismNumberPicker.setLimits(0, 10);
        occultismNumberPicker.setLabel(ThinkMachineTranslator.getTranslatedText(occultismType.getId()));
        occultismNumberPicker.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        occultismNumberPicker.setPadding(50, 20, 20, 20);

        // Add EditText to LinearLayout
        if (linearLayout != null) {
            linearLayout.addView(occultismNumberPicker);
        }

        occultismNumberPicker.setValue(CharacterManager.getSelectedCharacter().getOccultismLevel(occultismType));

        //Set listeners to counter
        occultismNumberPicker.addValueChangeListener(newValue -> {
            try {
                CharacterManager.getSelectedCharacter().setOccultismLevel(occultismType, newValue);
            } catch (InvalidPsiqueLevelException e) {
                if (CharacterManager.getSelectedCharacter().getFaction() == null) {
                    SnackbarGenerator.getErrorMessage(root, R.string.selectFaction).show();
                }
                occultismNumberPicker.setValue(0);
            }
        });

    }

}
