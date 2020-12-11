package com.softwaremagico.tm.advisor.ui.components.counters;

import android.content.Context;
import android.util.AttributeSet;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;

public class SkillsCounter extends Counter {
    private int gearColor = R.color.counterSkills;
    private int textColor = R.color.counterSkillsText;

    public SkillsCounter(Context context) {
        super(context);
    }

    public SkillsCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        setTag(R.string.counter_skills);
    }

    @Override
    public void setCharacter(CharacterPlayer character) {
        CharacterManager.getCostCalculator().getCostCharacterModificationHandler().addSkillsPointsUpdatedListeners(value -> {
            setValue(FreeStyleCharacterCreation.getSkillsPoints(character.getInfo().getAge()) - CharacterManager.getCostCalculator().getCurrentSkillsPoints(), true);
        });
        setValue(FreeStyleCharacterCreation.getSkillsPoints(character.getInfo().getAge()) - CharacterManager.getCostCalculator().getCurrentSkillsPoints(), false);
    }

    public int getGearColor() {
        return gearColor;
    }

    public int getTextColor() {
        return textColor;
    }

}
