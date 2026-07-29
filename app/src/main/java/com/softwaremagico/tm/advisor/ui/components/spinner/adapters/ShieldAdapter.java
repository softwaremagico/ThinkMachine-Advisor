package com.softwaremagico.tm.advisor.ui.components.spinner.adapters;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.equipment.shields.Shield;

import java.util.List;
import java.util.Objects;

public class ShieldAdapter extends EquipmentAdapter<Shield> {
    public ShieldAdapter(@NonNull Context context, @NonNull List<Shield> shields, boolean nullAllowed) {
        super(context, shields, nullAllowed, Shield.class);
    }

    @Override
    protected void setElementColor(TextView elementRepresentation, Shield shield, int position) {
        if (Objects.equals(shield, CharacterManager.getSelectedCharacter().getShield())) {
            elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.colorNormal));
        } else {
            super.setElementColor(elementRepresentation, shield, position);
        }
    }
}
