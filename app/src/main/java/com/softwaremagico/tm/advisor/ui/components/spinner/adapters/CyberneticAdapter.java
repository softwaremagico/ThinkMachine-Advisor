package com.softwaremagico.tm.advisor.ui.components.spinner.adapters;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.cybernetics.CyberneticDevice;
import com.softwaremagico.tm.character.cybernetics.Cybernetics;

import java.util.List;

public class CyberneticAdapter extends ElementAdapter<CyberneticDevice> {

    public CyberneticAdapter(@NonNull Context context, @NonNull List<CyberneticDevice> cyberneticDevices, boolean nullAllowed) {
        super(context, cyberneticDevices, nullAllowed, CyberneticDevice.class);
    }

    @Override
    protected void setElementColor(TextView elementRepresentation, CyberneticDevice cyberneticDevice, int position) {
        if (isEnabled(position)) {
            if (CharacterManager.getSelectedCharacter().hasCyberneticDevice(cyberneticDevice)) {
                elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.colorNormal));
            } else if (CharacterManager.getSelectedCharacter().getCharacteristic(CharacteristicName.TECH).getValue() <
                    cyberneticDevice.getTechLevel()) {
                elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.insufficientTechnology));
            } else if (CharacterManager.getSelectedCharacter().getCyberneticsIncompatibility() + cyberneticDevice.getIncompatibility() > Cybernetics
                    .getMaxCyberneticIncompatibility(CharacterManager.getSelectedCharacter())) {
                elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.unaffordableMoney));
            } else {
                elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.colorNormal));
            }
        } else {
            elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));
        }
    }

    @Override
    public String getElementRepresentation(CyberneticDevice element) {
        if (element.getId().equals(Element.DEFAULT_NULL_ID)) {
            return "";
        }
        return element.getName() + " [" + element.getIncompatibility() + "]" + " (" + element.getPoints() + ")";
    }

    @Override
    public boolean isEnabled(int position) {
        if (getItem(position).getRequirement() != null) {
            return CharacterManager.getSelectedCharacter().hasCyberneticDevice(getItem(position).getRequirement());
        }
        return true;
    }
}
