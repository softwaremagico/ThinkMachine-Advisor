package com.softwaremagico.tm.advisor.ui.components.counters;

import android.content.Context;
import android.util.AttributeSet;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.CostCalculatorModificationHandler;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;

public class OccultismExtraCounter extends SegmentCounter {
    private int gearColor = R.color.counterOccultism;
    private int textColor = R.color.counterOccultismText;
    private CostCalculatorModificationHandler.IExtraPointUpdatedListener listener;

    public OccultismExtraCounter(Context context) {
        super(context);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        setTag(R.string.counter_extra);
    }

    public OccultismExtraCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCharacter(CharacterPlayer character) {
        CharacterManager.getCostCalculator().getCostCharacterModificationHandler().removeExtraPointsUpdatedListeners(listener);
        listener = CharacterManager.getCostCalculator().getCostCharacterModificationHandler().addExtraPointsUpdatedListeners(() ->
                setValue(CharacterManager.getCostCalculator().getCurrentOccultismLevelExtraPoints() * CostCalculator.PSIQUE_LEVEL_COST +
                                CharacterManager.getCostCalculator().getCurrentOccultismPowersExtraPoints() * CostCalculator.OCCULTISM_POWER_LEVEL_COST +
                                CharacterManager.getCostCalculator().getCurrentWyrdExtraPoints() * CostCalculator.EXTRA_WYRD_COST,
                        FreeStyleCharacterCreation.getFreeAvailablePoints(character.getInfo().getAge(), character.getRace())
                                - Math.max(0, CharacterManager.getCostCalculator().getTotalExtraCost()), true)
        );
        setValue(CharacterManager.getCostCalculator().getCurrentOccultismLevelExtraPoints() * CostCalculator.PSIQUE_LEVEL_COST +
                        CharacterManager.getCostCalculator().getCurrentOccultismPowersExtraPoints() * CostCalculator.OCCULTISM_POWER_LEVEL_COST +
                        CharacterManager.getCostCalculator().getCurrentWyrdExtraPoints() * CostCalculator.EXTRA_WYRD_COST,
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
