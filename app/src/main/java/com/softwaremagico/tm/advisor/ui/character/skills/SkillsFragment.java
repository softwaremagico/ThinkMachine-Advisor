package com.softwaremagico.tm.advisor.ui.character.skills;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.CharacterManager;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CustomFragment;
import com.softwaremagico.tm.advisor.ui.components.TranslatedNumberPicker;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.InvalidSkillException;

import java.util.HashMap;
import java.util.Map;

public class SkillsFragment extends CustomFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Map<AvailableSkill, TranslatedNumberPicker> translatedNumberPickers = new HashMap<>();

    public static SkillsFragment newInstance(int index) {
        SkillsFragment fragment = new SkillsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.character_skills_fragment, container, false);
        LinearLayout linearLayout = root.findViewById(R.id.skills_container);
        addSection(ThinkMachineTranslator.getTranslatedText("naturalSkills"), linearLayout);

        try {
            for (AvailableSkill skill : CharacterManager.getSelectedCharacter().getNaturalSkills()) {
                createSkillEditText(root, linearLayout, skill);
            }
        } catch (InvalidXmlElementException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }

        addSection(ThinkMachineTranslator.getTranslatedText("learnedSkills"), linearLayout);

        try {
            for (AvailableSkill skill : CharacterManager.getSelectedCharacter().getLearnedSkills()) {
                createSkillEditText(root, linearLayout, skill);
            }
        } catch (InvalidXmlElementException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }

        updateSkillsLimits();

        return root;
    }

    private void createSkillEditText(View root, LinearLayout linearLayout, AvailableSkill skill) {
        TranslatedNumberPicker skillNumberPicker = new TranslatedNumberPicker(getContext(), null);
        translatedNumberPickers.put(skill, skillNumberPicker);
        skillNumberPicker.setLabel(skill.getCompleteName());
        skillNumberPicker.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //skillNumberPicker.setPadding(20, 20, 20, 20);

        // Add EditText to LinearLayout
        if (linearLayout != null) {
            linearLayout.addView(skillNumberPicker);
        }

        skillNumberPicker.setValue(CharacterManager.getSelectedCharacter().getSkillAssignedRanks(skill));

        skillNumberPicker.addValueChangeListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                try {
                    CharacterManager.getSelectedCharacter().setSkillRank(skill, newVal);
                } catch (InvalidSkillException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
            }
        });
    }


    private void updateSkillsLimits() {
        if (CharacterManager.getSelectedCharacter().getRace() != null) {
            for (Map.Entry<AvailableSkill, TranslatedNumberPicker> skillComponent : translatedNumberPickers.entrySet()) {
                if (skillComponent.getKey().getSkillDefinition().isNatural()) {
                    skillComponent.getValue().setLimits(FreeStyleCharacterCreation.getMinInitialNaturalSkillsValues(CharacterManager.getSelectedCharacter().getInfo().getAge()),
                            FreeStyleCharacterCreation.getMaxInitialSkillsValues(CharacterManager.getSelectedCharacter().getInfo().getAge()));
                } else {
                    skillComponent.getValue().setLimits(0, FreeStyleCharacterCreation.getMaxInitialSkillsValues(CharacterManager.getSelectedCharacter().getInfo().getAge()));
                }
            }
        }
    }

    public void refreshSkillsValues() {
        if (CharacterManager.getSelectedCharacter().getRace() != null) {
            for (Map.Entry<AvailableSkill, TranslatedNumberPicker> skillComponent : translatedNumberPickers.entrySet()) {
                skillComponent.getValue().setValue(CharacterManager.getSelectedCharacter().getSkillAssignedRanks(skillComponent.getKey()));
            }
        }
    }

}
