package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.cybernetics.CyberneticDevice;

public class CyberneticDeviceDescriptionDialog extends ElementDescriptionDialog<CyberneticDevice> {

    public CyberneticDeviceDescriptionDialog(CyberneticDevice element) {
        super(element);
    }

    @Override
    protected String getDetails(CyberneticDevice cyberneticDevice) {
        boolean techLimited = CharacterManager.getSelectedCharacter().getCharacteristic(CharacteristicName.TECH).getValue() <
                cyberneticDevice.getTechLevel();
        return "<b>" + ThinkMachineTranslator.getTranslatedText("techLevel") + ": " + "</b>" +
                (techLimited ? "<font color=\"" + getColor(R.color.insufficientTechnology) + "\">" : "") +
                cyberneticDevice.getTechLevel() +
                (techLimited ? "</font>" : "") +
                "<br><b>" + getString(R.string.incompatibility) + ": " + "</b>" +
                cyberneticDevice.getIncompatibility() +
                "<br><b>" + getString(R.string.points) + ": " + "</b>" +
                cyberneticDevice.getPoints() +
                "<br><b>" + getString(R.string.cost) + "</b> " +
                cyberneticDevice.getCost() +
                " " + ThinkMachineTranslator.getTranslatedText("firebirds");
    }
}