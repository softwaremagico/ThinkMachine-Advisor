package com.softwaremagico.tm.advisor.ui.components.spinner.adapters;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;

import java.util.List;

public class WeaponAdapter extends EquipmentAdapter<Weapon> {
    public WeaponAdapter(@NonNull Context context, @NonNull List<Weapon> weapons, boolean nullAllowed) {
        super(context, weapons, nullAllowed, Weapon.class);
    }

    @Override
    protected void setElementColor(TextView elementRepresentation, Weapon weapon, int position) {
        if (CharacterManager.getSelectedCharacter().hasWeapon(weapon)) {
            elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.colorNormal));
        } else {
            super.setElementColor(elementRepresentation, weapon, position);
        }
    }
}
