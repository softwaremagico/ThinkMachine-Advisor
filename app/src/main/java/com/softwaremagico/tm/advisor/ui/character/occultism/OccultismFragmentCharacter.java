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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementSelector;
import com.softwaremagico.tm.advisor.ui.components.TranslatedNumberPicker;
import com.softwaremagico.tm.advisor.ui.components.counters.OccultismExtraCounter;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.occultism.InvalidOccultismPowerException;
import com.softwaremagico.tm.character.occultism.InvalidPsiqueLevelException;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class OccultismFragmentCharacter extends CharacterCustomFragment {
    private final Map<OccultismType, TranslatedNumberPicker> translatedNumberPickers = new HashMap<>();
    private final Map<OccultismPath, LinearLayout> occultismPathLayout = new HashMap<>();
    private final Map<OccultismPath, Set<ElementSelector<OccultismPower>>> selectors = new HashMap<>();
    private boolean enabled = true;
    private TranslatedNumberPicker wyrdNumberPicker;

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
            for (OccultismType occultismType : OccultismTypeFactory.getInstance().getElements(character.getLanguage(),
                    character.getModuleName())) {
                if (translatedNumberPickers.get(occultismType) != null) {
                    translatedNumberPickers.get(occultismType).setValue(character.getOccultismLevel(occultismType));
                }
            }
        } catch (InvalidXmlElementException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }
        enabled = false;
        selectors.values().forEach(elementSelectors -> elementSelectors.forEach(occultismPowerElementSelector ->
                occultismPowerElementSelector.setChecked(character.canAddOccultismPower(occultismPowerElementSelector.getSelection()))));
        if (wyrdNumberPicker != null) {
            wyrdNumberPicker.setValue(character.getWyrdValue());
        }
        enabled = true;

        updateContent();
    }

    private void updateContent() {
        enableOccultismPowers();
        updateVisibility();
        updateWyrdLimits();
    }

    @Override
    protected void initData() {
        final LinearLayout rootLayout = root.findViewById(R.id.root_container);

        addSection(ThinkMachineTranslator.getTranslatedText("occultism"), rootLayout);
        createOccultismSelector(rootLayout, OccultismTypeFactory.getPsi(CharacterManager.getSelectedCharacter().getLanguage(),
                CharacterManager.getSelectedCharacter().getModuleName()));
        createOccultismSelector(rootLayout, OccultismTypeFactory.getTheurgy(CharacterManager.getSelectedCharacter().getLanguage(),
                CharacterManager.getSelectedCharacter().getModuleName()));
        createWyrdSelector(rootLayout);

        setOccultismPaths(rootLayout);
        updateContent();

        setCharacter(root, CharacterManager.getSelectedCharacter());
    }

    private void setOccultismPaths(LinearLayout rootLayout) {
        try {
            OccultismPathFactory.getInstance().getElements(CharacterManager.getSelectedCharacter().getLanguage(),
                    CharacterManager.getSelectedCharacter().getModuleName()).stream().sorted(
                    Comparator.comparing(OccultismPath::getOccultismType).thenComparing(OccultismPath::getName))
                    .forEach(
                            occultismPath -> setOccultismPathLayout(occultismPath, rootLayout)
                    );
        } catch (InvalidXmlElementException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }
    }

    private void setOccultismPathLayout(OccultismPath occultismPath, LinearLayout rootLayout) {
        LinearLayout occultismLayout = new LinearLayout(getContext());
        occultismLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        occultismLayout.setOrientation(LinearLayout.VERTICAL);
        addSection(occultismPath.getName(), occultismLayout);
        selectors.put(occultismPath, new HashSet<>());
        occultismPath.getOccultismPowers().values().stream().sorted(
                Comparator.comparing(OccultismPower::getLevel).thenComparing(OccultismPower::getName)).forEach(occultismPower -> {
            ElementSelector<OccultismPower> occultismPowerSelector = new ElementSelector<>(getContext(), occultismPower);
            occultismLayout.addView(occultismPowerSelector);
            occultismPowerSelector.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (enabled) {
                    if (isChecked) {
                        try {
                            CharacterManager.getSelectedCharacter().addOccultismPower(occultismPowerSelector.getSelection());
                        } catch (InvalidOccultismPowerException e) {
                            occultismPowerSelector.setChecked(false);
                        }
                    } else {
                        CharacterManager.getSelectedCharacter().removeOccultismPower(occultismPowerSelector.getSelection());
                    }
                    enableOccultismPowers(occultismPath);
                }
            });
            selectors.get(occultismPath).add(occultismPowerSelector);
        });
        addSpace(occultismLayout);
        rootLayout.addView(occultismLayout);
        occultismPathLayout.put(occultismPath, occultismLayout);
    }

    private void enableOccultismPowers() {
        selectors.keySet().forEach(this::enableOccultismPowers);
    }

    private void enableOccultismPowers(OccultismPath occultismPath) {
        selectors.get(occultismPath).forEach(occultismPowerElementSelector ->
                occultismPowerElementSelector.setEnabled(CharacterManager.getSelectedCharacter().canAddOccultismPower(occultismPowerElementSelector.getSelection())));
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_occultism_fragment, container, false);

        extraCounter = root.findViewById(R.id.extra_counter);

        return root;
    }


    private void updateVisibility() {
        final OccultismType characterOccultismType = CharacterManager.getSelectedCharacter().getOccultismType();
        translatedNumberPickers.entrySet().forEach(occultismTypeTranslatedNumberPickerEntry -> {
            if (characterOccultismType == null || Objects.equals(occultismTypeTranslatedNumberPickerEntry.getKey(), characterOccultismType)) {
                occultismTypeTranslatedNumberPickerEntry.getValue().setVisibility(View.VISIBLE);
            } else {
                occultismTypeTranslatedNumberPickerEntry.getValue().setVisibility(View.GONE);
            }
        });
        occultismPathLayout.entrySet().forEach(occultismPathLinearLayoutEntry -> {
            if (characterOccultismType == null || Objects.equals(occultismPathLinearLayoutEntry.getKey().getOccultismType(), characterOccultismType)) {
                occultismPathLinearLayoutEntry.getValue().setVisibility(View.VISIBLE);
            } else {
                occultismPathLinearLayoutEntry.getValue().setVisibility(View.GONE);
            }
        });
    }

    private void createOccultismSelector(LinearLayout linearLayout, OccultismType occultismType) {
        final TranslatedNumberPicker occultismNumberPicker = new TranslatedNumberPicker(getContext(), null);
        translatedNumberPickers.put(occultismType, occultismNumberPicker);
        if (occultismType.getId().equals(OccultismTypeFactory.PSI_TAG)) {
            occultismNumberPicker.setLimits(CharacterManager.getSelectedCharacter().getRace().getPsi(), 10);
        } else if (occultismType.getId().equals(OccultismTypeFactory.THEURGY_TAG)) {
            occultismNumberPicker.setLimits(CharacterManager.getSelectedCharacter().getRace().getTheurgy(), 10);
        }

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
                try {
                    CharacterManager.getSelectedCharacter().setOccultismLevel(occultismType, 0);
                } catch (InvalidPsiqueLevelException e1) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e1);
                }
            }
            updateContent();
        });
    }

    private void updateWyrdLimits() {
        if (wyrdNumberPicker != null) {
            wyrdNumberPicker.setLimits(CharacterManager.getSelectedCharacter().getBasicWyrdValue(), CharacterManager.getSelectedCharacter().getMaxWyrdValue());
        }
    }

    private void createWyrdSelector(LinearLayout linearLayout) {
        wyrdNumberPicker = new TranslatedNumberPicker(getContext(), null);
        updateWyrdLimits();
        wyrdNumberPicker.setLabel(ThinkMachineTranslator.getTranslatedText("wyrd"));
        wyrdNumberPicker.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        wyrdNumberPicker.setPadding(50, 20, 20, 20);

        // Add EditText to LinearLayout
        if (linearLayout != null) {
            linearLayout.addView(wyrdNumberPicker);
        }

        wyrdNumberPicker.setValue(CharacterManager.getSelectedCharacter().getWyrdValue());

        //Set listeners to counter
        wyrdNumberPicker.addValueChangeListener(newValue ->
                CharacterManager.getSelectedCharacter().setWyrd(newValue)
        );
    }

}
