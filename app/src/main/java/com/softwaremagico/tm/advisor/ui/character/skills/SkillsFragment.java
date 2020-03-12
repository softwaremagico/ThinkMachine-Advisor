package com.softwaremagico.tm.advisor.ui.character.skills;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.CharacterManager;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.TranslatedNumberPicker;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.InvalidSkillException;

import java.util.HashMap;
import java.util.Map;

public class SkillsFragment extends Fragment {
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

        TextView textView = new TextView(getContext(), null);
        textView.setText(ThinkMachineTranslator.getTranslatedText("naturalSkills"));
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //textView.setPadding(20, 20, 20, 20);

        //Create a separation line.
        View space = new View(getContext(), null);
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        space.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        space.setBackgroundColor(Color.parseColor("#ff000000"));

        if (linearLayout != null) {
            linearLayout.addView(textView);
            linearLayout.addView(space);
        }


        try {
            for (AvailableSkill skill : CharacterManager.getSelectedCharacter().getNaturalSkills()) {
                createSkillEditText(root, linearLayout, skill);
            }
        } catch (InvalidXmlElementException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }

        textView = new TextView(getContext(), null);
        textView.setText(ThinkMachineTranslator.getTranslatedText("learnedSkills"));
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //textView.setPadding(20, 20, 20, 20);

        //Create a separation line.
        space = new View(getContext(), null);
        params = linearLayout.getLayoutParams();
        space.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        space.setBackgroundColor(Color.parseColor("#ff000000"));

        if (linearLayout != null) {
            linearLayout.addView(textView);
            linearLayout.addView(space);
        }

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


    public void updateSkillsLimits() {
        if (CharacterManager.getSelectedCharacter().getRace() != null) {
            for (Map.Entry<AvailableSkill, TranslatedNumberPicker> skillComponent : translatedNumberPickers.entrySet()) {
                if(skillComponent.getKey().getSkillDefinition().isNatural()){
                    skillComponent.getValue().setLimits(FreeStyleCharacterCreation.MIN_INITIAL_NATURAL_SKILL_VALUE, FreeStyleCharacterCreation.MAX_INITIAL_SKILL_VALUE);
                }else {
                    skillComponent.getValue().setLimits(0, FreeStyleCharacterCreation.MAX_INITIAL_SKILL_VALUE);
                }
            }
        }
    }

    public void refreshCharacteristicValue() {
        if (CharacterManager.getSelectedCharacter().getRace() != null) {
            for (Map.Entry<AvailableSkill, TranslatedNumberPicker> skillComponent : translatedNumberPickers.entrySet()) {
                skillComponent.getValue().setValue(CharacterManager.getSelectedCharacter().getSkillAssignedRanks(skillComponent.getKey()));
            }
        }
    }

}
