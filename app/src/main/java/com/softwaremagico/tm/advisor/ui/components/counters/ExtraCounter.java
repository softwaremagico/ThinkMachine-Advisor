package com.softwaremagico.tm.advisor.ui.components.counters;

import android.content.Context;
import android.util.AttributeSet;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.CostCalculatorModificationHandler;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;

public class ExtraCounter extends Counter {
    private int gearColor = R.color.counterExtra;
    private int textColor = R.color.counterExtraText;
    private CostCalculatorModificationHandler.IExtraPointUpdatedListener listener;

    public ExtraCounter(Context context) {
        super(context);
    }

    public ExtraCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        setTag(R.string.counter_extra);
    }

    @Override
    public void setCharacter(CharacterPlayer character) {
        CharacterManager.getCostCalculator().getCostCharacterModificationHandler().removeExtraPointsUpdatedListeners(listener);
        listener = CharacterManager.getCostCalculator().getCostCharacterModificationHandler().addExtraPointsUpdatedListeners(() ->
                setValue(FreeStyleCharacterCreation.getFreeAvailablePoints(character.getInfo().getAge()) - Math.max(0, CharacterManager.getCostCalculator().getTotalExtraCost()), true)
        );
        setValue(FreeStyleCharacterCreation.getFreeAvailablePoints(character.getInfo().getAge()) - Math.max(0, CharacterManager.getCostCalculator().getTotalExtraCost()), false);
    }

    public int getGearColor() {
        return gearColor;
    }

    public int getTextColor() {
        return textColor;
    }

}
