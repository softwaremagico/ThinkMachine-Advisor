package com.softwaremagico.tm.advisor.ui.components.counters;

import android.content.Context;
import android.util.AttributeSet;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.CostCalculatorModificationHandler;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;

public class CharacteristicsCounter extends Counter {
    private int gearColor = R.color.counterCharacteristics;
    private int textColor = R.color.counterCharacteristicsText;
    private CostCalculatorModificationHandler.ICurrentCharacteristicPointsUpdatedListener listener;

    public CharacteristicsCounter(Context context) {
        super(context);
    }

    public CharacteristicsCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        setTag(R.string.counter_characteristics);
    }

    @Override
    public void setCharacter(CharacterPlayer character) {
        CharacterManager.getCostCalculator().getCostCharacterModificationHandler().removeCharacteristicPointsUpdatedListeners(listener);
        listener = CharacterManager.getCostCalculator().getCostCharacterModificationHandler().addCharacteristicPointsUpdatedListeners(value -> {
            setValue(FreeStyleCharacterCreation.getCharacteristicsPoints(character.getInfo().getAge()) - CharacterManager.getCostCalculator().getCurrentCharacteristicPoints(), true);
        });
        setValue(FreeStyleCharacterCreation.getCharacteristicsPoints(character.getInfo().getAge()) - CharacterManager.getCostCalculator().getCurrentCharacteristicPoints(), false);
    }

    public int getGearColor() {
        return gearColor;
    }

    public int getTextColor() {
        return textColor;
    }

}
