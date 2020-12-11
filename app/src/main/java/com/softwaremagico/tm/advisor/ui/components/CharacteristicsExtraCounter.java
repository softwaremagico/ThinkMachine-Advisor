package com.softwaremagico.tm.advisor.ui.components;

import android.content.Context;
import android.util.AttributeSet;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;

public class CharacteristicsExtraCounter extends SegmentCounter {
    private int gearColor = R.color.counterCharacteristics;
    private int textColor = R.color.counterCharacteristicsText;
    private int gear2Color = R.color.counterExtra;
    private int text2Color = R.color.counterExtraText;


    public CharacteristicsExtraCounter(Context context) {
        super(context);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        setTag(R.string.counter_extra);
    }

    public CharacteristicsExtraCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCharacter(CharacterPlayer character) {
        CharacterManager.getCostCalculator().getCostCharacterModificationHandler().addExtraPointsUpdatedListeners(() ->
                setValue(CharacterManager.getCostCalculator().getCurrentCharacteristicExtraPoints() * CostCalculator.CHARACTERISTIC_EXTRA_POINTS_COST,
                        FreeStyleCharacterCreation.getFreeAvailablePoints(character.getInfo().getAge()) - Math.max(0, CharacterManager.getCostCalculator().getTotalExtraCost()), true)
        );
        setValue(CharacterManager.getCostCalculator().getCurrentCharacteristicExtraPoints() * CostCalculator.CHARACTERISTIC_EXTRA_POINTS_COST,
                FreeStyleCharacterCreation.getFreeAvailablePoints(character.getInfo().getAge()) - Math.max(0, CharacterManager.getCostCalculator().getTotalExtraCost()), false);
    }

    public int getGearColor() {
        return gearColor;
    }

    public int getTextColor() {
        return textColor;
    }

    @Override
    public int getGearColor2() {
        return gear2Color;
    }

    @Override
    public int getTextColor2() {
        return text2Color;
    }
}
