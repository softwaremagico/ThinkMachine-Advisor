package com.softwaremagico.tm.advisor.ui.components.counters;

import android.content.Context;
import android.util.AttributeSet;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.CostCalculatorModificationHandler;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;

public class CyberneticsIncompatibilityCounter extends Counter {
    private int gearColor = R.color.counterCyberneticsIncompatibility;
    private int textColor = R.color.counterCyberneticsIncompatibilityText;
    private CostCalculatorModificationHandler.ICurrentTraitsPointsUpdatedListener listener;

    public CyberneticsIncompatibilityCounter(Context context) {
        super(context);
    }

    public CyberneticsIncompatibilityCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        setTag(R.string.counter_cybernetics);
    }

    @Override
    public void setCharacter(CharacterPlayer character) {
        CharacterManager.getCostCalculator().getCostCharacterModificationHandler().removeTraitsPointsUpdatedListeners(listener);
        CharacterManager.getCostCalculator().getCostCharacterModificationHandler().addTraitsPointsUpdatedListeners(value -> {
            setValue(FreeStyleCharacterCreation.getTraitsPoints(character.getInfo().getAge()) - CharacterManager.getCostCalculator().getCurrentTraitsPoints(), true);
        });
        setValue(FreeStyleCharacterCreation.getTraitsPoints(character.getInfo().getAge()) - CharacterManager.getCostCalculator().getCurrentTraitsPoints(), false);
    }

    public int getGearColor() {
        return gearColor;
    }

    public int getTextColor() {
        return textColor;
    }

}
