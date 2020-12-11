package com.softwaremagico.tm.advisor.ui.components.counters;

import android.content.Context;
import android.util.AttributeSet;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;

public class TraitsCounter extends Counter {
    private int gearColor = R.color.counterTraits;
    private int textColor = R.color.counterTraitsText;

    public TraitsCounter(Context context) {
        super(context);
    }

    public TraitsCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        setTag(R.string.counter_traits);
    }

    @Override
    public void setCharacter(CharacterPlayer character) {
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
