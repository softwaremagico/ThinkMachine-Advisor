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

package com.softwaremagico.tm.advisor.ui.character.characteristics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.counters.CharacteristicsCounter;
import com.softwaremagico.tm.advisor.ui.components.counters.CharacteristicsExtraCounter;
import com.softwaremagico.tm.advisor.ui.components.CustomFragment;
import com.softwaremagico.tm.advisor.ui.components.TranslatedNumberPicker;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.file.modules.ModuleManager;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CharacteristicsFragment extends CustomFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private final Map<CharacteristicName, TranslatedNumberPicker> translatedNumberPickers = new HashMap<>();
    private CharacteristicsCounter characteristicsCounter;
    private CharacteristicsExtraCounter extraCounter;
    private View root;

    public static CharacteristicsFragment newInstance(int index) {
        final CharacteristicsFragment fragment = new CharacteristicsFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setCharacter(View root, CharacterPlayer character) {
        updateCharacteristicsLimits(character);
        for (final CharacteristicType type : CharacteristicType.values()) {
            if (type == CharacteristicType.OTHERS) {
                continue;
            }
            for (final CharacteristicDefinition characteristicDefinition : CharacteristicsDefinitionFactory.getInstance().getAll(type, Locale.getDefault().getLanguage(),
                    ModuleManager.DEFAULT_MODULE)) {
                translatedNumberPickers.get(characteristicDefinition.getCharacteristicName()).setValue(character.getCharacteristicValue(characteristicDefinition.getCharacteristicName()));
            }
        }

        characteristicsCounter.setCharacter(character);
        extraCounter.setCharacter(character);
    }

    @Override
    protected void initData() {
        final LinearLayout linearLayout = root.findViewById(R.id.characteristics_container);
        for (final CharacteristicType type : CharacteristicType.values()) {
            if (type == CharacteristicType.OTHERS) {
                continue;
            }

            addSection(ThinkMachineTranslator.getTranslatedText(type.name().toLowerCase(Locale.getDefault()) + "Characteristics"), linearLayout);
            for (final CharacteristicDefinition characteristicDefinition : CharacteristicsDefinitionFactory.getInstance().getAll(type, Locale.getDefault().getLanguage(),
                    ModuleManager.DEFAULT_MODULE)) {
                createCharacteristicsEditText(linearLayout, characteristicDefinition);
            }
        }

        setCharacter(root, CharacterManager.getSelectedCharacter());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_characteristics_fragment, container, false);
        characteristicsCounter = root.findViewById(R.id.characteristics_counter);
        extraCounter = root.findViewById(R.id.extra_counter);

        CharacterManager.addCharacterRaceUpdatedListener(characterPlayer -> setCharacter(root, characterPlayer));

        return root;
    }

    public void updateCharacteristicsLimits(CharacterPlayer characterPlayer) {
        if (characterPlayer != null && characterPlayer.getRace() != null) {
            for (final Map.Entry<CharacteristicName, TranslatedNumberPicker> characteristicComponent : translatedNumberPickers.entrySet()) {
                characteristicComponent.getValue().setLimits(CharacterManager.getSelectedCharacter().getStartingValue(characteristicComponent.getKey()),
                        FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(characteristicComponent.getKey(),
                                CharacterManager.getSelectedCharacter().getInfo().getAge(), CharacterManager.getSelectedCharacter().getRace()));
            }
        }
    }

    public void refreshCharacteristicValues(CharacterPlayer characterPlayer) {
        if (characterPlayer != null && characterPlayer.getRace() != null) {
            for (final Map.Entry<CharacteristicName, TranslatedNumberPicker> characteristicComponent : translatedNumberPickers.entrySet()) {
                characteristicComponent.getValue().setValue(CharacterManager.getSelectedCharacter().getValue(characteristicComponent.getKey()));
            }
        }
    }

    private void createCharacteristicsEditText(LinearLayout linearLayout, CharacteristicDefinition characteristicDefinition) {
        final TranslatedNumberPicker characteristicsNumberPicker = new TranslatedNumberPicker(getContext(), null);
        translatedNumberPickers.put(characteristicDefinition.getCharacteristicName(), characteristicsNumberPicker);
        characteristicsNumberPicker.setLabel(ThinkMachineTranslator.getTranslatedText(characteristicDefinition.getId()));
        characteristicsNumberPicker.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        characteristicsNumberPicker.setPadding(50, 20, 20, 20);

        // Add EditText to LinearLayout
        if (linearLayout != null) {
            linearLayout.addView(characteristicsNumberPicker);
        }

        if (CharacterManager.getSelectedCharacter().getValue(characteristicDefinition.getCharacteristicName()) != null) {
            characteristicsNumberPicker.setValue(CharacterManager.getSelectedCharacter().getValue(characteristicDefinition.getCharacteristicName()));
        }

        //Set listeners to counter
        characteristicsNumberPicker.addValueChangeListener(newValue -> CharacterManager.getSelectedCharacter().setCharacteristic(characteristicDefinition.getCharacteristicName(), newValue));

    }

}
