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

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.softwaremagico.tm.advisor.R;
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
    private DecoView mDecoView;
    private int mBackIndex;
    private int mSeries1Index;

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
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.character_characteristics_fragment, container, false);
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

        //Create counters
        mDecoView = (DecoView) root.findViewById(R.id.characteristics_points);

        // Create required data series on the DecoView
        createBackSeries();
        createDataSeries1(root);

        CharacterManager.addCharacterRaceUpdatedListener(characterPlayer -> updateCharacteristicsLimits(characterPlayer));

        return root;
    }

    private void createBackSeries() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, 10, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries1(View root) {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FF6699FF"))
                .setRange(0, 10, 0)
                .setInitialVisibility(false)
                .build();

        final TextView textActivity3 = (TextView) root.findViewById(R.id.textRemaining);

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textActivity3.setText(String.format("%.2f Km", currentPosition));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries1Index = mDecoView.addSeries(seriesItem);
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
        characteristicsNumberPicker.addValueChangeListener((picker, oldVal, newVal) -> CharacterManager.getSelectedCharacter().getCharacteristic(characteristicDefinition.getCharacteristicName()).setValue(newVal));

    }

}
