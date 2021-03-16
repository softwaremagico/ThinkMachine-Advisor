package com.softwaremagico.tm.advisor.ui.components.spinner.adapters;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.equipment.armours.Armour;

import java.util.List;
import java.util.Objects;

public class ArmourAdapter extends EquipmentAdapter<Armour> {
    public ArmourAdapter(@NonNull Context context, @NonNull List<Armour> armours, boolean nullAllowed) {
        super(context, armours, nullAllowed, Armour.class);
    }

    @Override
    protected void setElementColor(TextView elementRepresentation, Armour armour, int position) {
        if (Objects.equals(armour, CharacterManager.getSelectedCharacter().getArmour())) {
            elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.colorNormal));
        } else {
            super.setElementColor(elementRepresentation, armour, position);
        }
    }
}
