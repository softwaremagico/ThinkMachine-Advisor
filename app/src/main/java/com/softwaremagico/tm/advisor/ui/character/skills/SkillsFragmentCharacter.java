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

package com.softwaremagico.tm.advisor.ui.character.skills;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.TranslatedNumberPicker;
import com.softwaremagico.tm.advisor.ui.components.counters.SkillsCounter;
import com.softwaremagico.tm.advisor.ui.components.counters.SkillsExtraCounter;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.exceptions.RestrictedElementException;
import com.softwaremagico.tm.character.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.InvalidRanksException;
import com.softwaremagico.tm.character.skills.InvalidSkillException;
import com.softwaremagico.tm.character.skills.SkillDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillsFragmentCharacter extends CharacterCustomFragment {
    private SkillsViewModel mViewModel;

    private final Map<AvailableSkill, TranslatedNumberPicker<AvailableSkill>> translatedNumberPickers = new HashMap<>();
    private SkillsCounter skillsCounter;
    private SkillsExtraCounter extraCounter;
    private View root;

    public static SkillsFragmentCharacter newInstance(int index) {
        final SkillsFragmentCharacter fragment = new SkillsFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setCharacter(View root, CharacterPlayer character) {
        updateSkillsLimits(character);
        refreshSkillsValues(character);
        refreshSkillsColors();
        skillsCounter.setCharacter(character);
        extraCounter.setCharacter(character);
        updateDynamicSkills(character);
    }

    @Override
    protected void initData() {
        addContent(root.findViewById(R.id.skills_container));
        setCharacter(root, CharacterManager.getSelectedCharacter());
    }

    private void addContent(LinearLayout linearLayout) {
        addSection(ThinkMachineTranslator.getTranslatedText("naturalSkills"), linearLayout);
        List<AvailableSkill> naturalSkills = mViewModel.getNaturalSkills(!CharacterManager.getSelectedCharacter().getSettings().isOnlyOfficialAllowed());

        for (final AvailableSkill skill : naturalSkills) {
            createSkillEditText(root, linearLayout, skill);
        }

        addSpace(linearLayout);
        addSection(ThinkMachineTranslator.getTranslatedText("learnedSkills"), linearLayout);

        List<AvailableSkill> learnedSkills = mViewModel.getLearnedSkills(!CharacterManager.getSelectedCharacter().getSettings().isOnlyOfficialAllowed());
        for (final AvailableSkill skill : learnedSkills) {
            createSkillEditText(root, linearLayout, skill);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_skills_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(SkillsViewModel.class);

        skillsCounter = root.findViewById(R.id.skills_counter);
        extraCounter = root.findViewById(R.id.extra_counter);

        CharacterManager.addCharacterRaceUpdatedListener(this::updateSkillsLimits);
        CharacterManager.addCharacterAgeUpdatedListener(characterPlayer -> setCharacter(root, characterPlayer));

        CharacterManager.addCharacterFactionUpdatedListener(this::updateDynamicSkills);
        CharacterManager.addCharacterPlanetUpdatedListener(this::updateDynamicSkills);
        CharacterManager.addSelectedCharacterListener(this::updateDynamicSkills);
        CharacterManager.addCharacterSettingsUpdateListeners(this::updateSettings);

        return root;
    }

    @Override
    protected void updateSettings(CharacterPlayer characterPlayer) {
        if (getContext() != null) {
            final LinearLayout linearLayout = root.findViewById(R.id.skills_container);
            linearLayout.removeAllViews();
            addContent(linearLayout);
            setCharacter(root, characterPlayer);
        }
    }

    private void updateDynamicSkills(CharacterPlayer characterPlayer) {
        for (Map.Entry<AvailableSkill, TranslatedNumberPicker<AvailableSkill>> skillsEntry : translatedNumberPickers.entrySet()) {
            if (skillsEntry.getKey().getSkillDefinition().getId().equalsIgnoreCase(SkillDefinition.PLANETARY_LORE_ID)) {
                if (characterPlayer.getInfo().getPlanet() != null) {
                    skillsEntry.getValue().setLabel(skillsEntry.getKey().getName() + " [" + characterPlayer.getInfo().getPlanet().getName() + "]");
                } else {
                    skillsEntry.getValue().setLabel(skillsEntry.getKey().getCompleteName());
                }
            } else if (skillsEntry.getKey().getSkillDefinition().getId().equalsIgnoreCase(SkillDefinition.FACTION_LORE_ID)) {
                if (characterPlayer.getFaction() != null) {
                    skillsEntry.getValue().setLabel(skillsEntry.getKey().getName() + " [" + characterPlayer.getFaction().getName() + "]");
                } else {
                    skillsEntry.getValue().setLabel(skillsEntry.getKey().getCompleteName());
                }
            }
        }
    }

    private void createSkillEditText(View root, LinearLayout linearLayout, AvailableSkill skill) {
        final TranslatedNumberPicker<AvailableSkill> skillNumberPicker = new TranslatedNumberPicker<>(getContext(), null, skill);
        translatedNumberPickers.put(skill, skillNumberPicker);
        skillNumberPicker.setLabel(skill.getCompleteName());
        skillNumberPicker.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        skillNumberPicker.setPadding(20, 20, 20, 20);

        // Add EditText to LinearLayout
        if (linearLayout != null) {
            linearLayout.addView(skillNumberPicker);
        }

        skillNumberPicker.setValue(CharacterManager.getSelectedCharacter().getSkillAssignedRanks(skill));

        skillNumberPicker.addValueChangeListener(newValue -> {
            try {
                CharacterManager.getSelectedCharacter().setSkillRank(skill, newValue);
            } catch (InvalidSkillException e) {
                SnackbarGenerator.getErrorMessage(root, R.string.message_duplicated_item_removed).show();
                AdvisorLog.errorMessage(this.getClass().getName(), e);
            } catch (InvalidRanksException e) {
                SnackbarGenerator.getInfoMessage(root, R.string.message_duplicated_item_removed).show();
                skillNumberPicker.setValue(CharacterManager.getSelectedCharacter().getSkillAssignedRanks(skill));
            } catch (RestrictedElementException e) {
                SnackbarGenerator.getErrorMessage(root, R.string.message_restricted_element).show();
                skillNumberPicker.setValue(0);
            } catch (UnofficialElementNotAllowedException e) {
                SnackbarGenerator.getErrorMessage(root, R.string.message_unofficial_element_not_allowed).show();
            }
        });
    }


    private void updateSkillsLimits(CharacterPlayer character) {
        if (character != null && character.getRace() != null) {
            for (final Map.Entry<AvailableSkill, TranslatedNumberPicker<AvailableSkill>> skillComponent : translatedNumberPickers.entrySet()) {
                if (skillComponent.getKey().getSkillDefinition().isNatural()) {
                    skillComponent.getValue().setLimits(FreeStyleCharacterCreation.getMinInitialNaturalSkillsValues(character.getInfo().getAge()),
                            FreeStyleCharacterCreation.getMaxInitialSkillsValues(character.getInfo().getAge()));
                } else {
                    skillComponent.getValue().setLimits(0, FreeStyleCharacterCreation.getMaxInitialSkillsValues(character.getInfo().getAge()));
                }
            }
        }
    }

    public void refreshSkillsValues(CharacterPlayer characterPlayer) {
        if (CharacterManager.getSelectedCharacter().getRace() != null) {
            for (final Map.Entry<AvailableSkill, TranslatedNumberPicker<AvailableSkill>> skillComponent : translatedNumberPickers.entrySet()) {
                skillComponent.getValue().setValue(characterPlayer.getSkillAssignedRanks(skillComponent.getKey()));
            }
        }
    }

    public void refreshSkillsColors() {
        for (final Map.Entry<AvailableSkill, TranslatedNumberPicker<AvailableSkill>> skillComponent : translatedNumberPickers.entrySet()) {
            skillComponent.getValue().update();
        }
    }
}
