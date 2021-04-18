package com.softwaremagico.tm.advisor.ui.components.counters;

import android.content.Context;
import android.util.AttributeSet;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.CostCalculatorModificationHandler;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;

public class SkillsExtraCounter extends SegmentCounter {
    private int gearColor = R.color.counterSkills;
    private int textColor = R.color.counterSkillsText;
    private CostCalculatorModificationHandler.IExtraPointUpdatedListener listener;

    public SkillsExtraCounter(Context context) {
        super(context);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        setTag(R.string.counter_extra);
    }

    public SkillsExtraCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCharacter(CharacterPlayer character) {
        CharacterManager.getCostCalculator().getCostCharacterModificationHandler().removeExtraPointsUpdatedListeners(listener);
        listener = CharacterManager.getCostCalculator().getCostCharacterModificationHandler().addExtraPointsUpdatedListeners(() ->
                setValue(CharacterManager.getCostCalculator().getCurrentSkillsExtraPoints() * CostCalculator.SKILL_EXTRA_POINTS_COST,
                        FreeStyleCharacterCreation.getFreeAvailablePoints(character.getInfo().getAge(), character.getRace())
                                - Math.max(0, CharacterManager.getCostCalculator().getTotalExtraCost()), true)
        );
        setValue(CharacterManager.getCostCalculator().getCurrentSkillsExtraPoints() * CostCalculator.SKILL_EXTRA_POINTS_COST,
                FreeStyleCharacterCreation.getFreeAvailablePoints(character.getInfo().getAge(), character.getRace())
                        - Math.max(0, CharacterManager.getCostCalculator().getTotalExtraCost()), false);
    }

    public int getGearColor() {
        return gearColor;
    }

    public int getTextColor() {
        return textColor;
    }
}
