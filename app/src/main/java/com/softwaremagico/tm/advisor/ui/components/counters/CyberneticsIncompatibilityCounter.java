package com.softwaremagico.tm.advisor.ui.components.counters;

import android.content.Context;
import android.util.AttributeSet;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.character.CharacterModificationHandler;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.cybernetics.Cybernetics;

public class CyberneticsIncompatibilityCounter extends Counter {
    private int gearColor = R.color.counterCyberneticsIncompatibility;
    private int textColor = R.color.counterCyberneticsIncompatibilityText;
    private CharacterModificationHandler.ICyberneticDeviceUpdated listener;

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
        character.getCharacterModificationHandler().removeCyberneticDeviceUpdatedListener(listener);
        listener = character.getCharacterModificationHandler().addCyberneticDeviceUpdatedListener((value, removed) ->
                setValue(Cybernetics.getMaxCyberneticIncompatibility(character) - character.getCyberneticsIncompatibility(), true));
        setValue(Cybernetics.getMaxCyberneticIncompatibility(character) - character.getCyberneticsIncompatibility(), false);
    }

    public int getGearColor() {
        return gearColor;
    }

    public int getTextColor() {
        return textColor;
    }

}
