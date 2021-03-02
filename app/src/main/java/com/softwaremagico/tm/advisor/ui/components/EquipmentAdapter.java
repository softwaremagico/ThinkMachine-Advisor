package com.softwaremagico.tm.advisor.ui.components;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.Equipment;

import java.util.List;

public class EquipmentAdapter<T extends Equipment<?>> extends ElementAdapter<T> {

    public EquipmentAdapter(@NonNull Context context, @NonNull List<T> objects, boolean nullAllowed, Class<T> clazz) {
        super(context, objects, nullAllowed, clazz);
    }

    @Override
    protected void setElementColor(TextView elementRepresentation, T element, int position) {
        if (isEnabled(position)) {
            if (CharacterManager.getSelectedCharacter().getCharacteristic(CharacteristicName.TECH).getValue() <
                    ((Equipment) element).getTechLevel()) {
                elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.insufficientTechnology));
            } else if (CharacterManager.getSelectedCharacter().getInitialMoney() < element.getCost()) {
                elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.unaffordableMoney));
            } else if (CharacterManager.getSelectedCharacter().getMoney() < element.getCost()) {
                elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.insufficientMoney));
            }
        } else {
            elementRepresentation.setTextColor(Color.LTGRAY);
        }
    }

    @Override
    public String getElementRepresentation(T element) {
        if (element.getId().equals(Element.DEFAULT_NULL_ID)) {
            return "";
        }
        return element.getName() + " (" + element.getCost() + " " + getContext().getResources().getString(R.string.firebird_abbrev) + ")";
    }
}
