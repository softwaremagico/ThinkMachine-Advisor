package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.cybernetics.CyberneticDevice;
import com.softwaremagico.tm.character.equipment.weapons.WeaponDamage;

public class CyberneticDeviceDescriptionDialog extends ElementDescriptionDialog<CyberneticDevice> {

    public CyberneticDeviceDescriptionDialog(CyberneticDevice element) {
        super(element);
    }

    @Override
    protected String getDetails(CyberneticDevice cyberneticDevice) {
        boolean techLimited = CharacterManager.getSelectedCharacter().getCharacteristic(CharacteristicName.TECH).getValue() <
                cyberneticDevice.getTechLevel();

        StringBuilder stringBuilder = new StringBuilder();

        if (cyberneticDevice.getWeapon() != null) {
            if (cyberneticDevice.getWeapon().isRangedWeapon()) {
                stringBuilder.append("<table cellpadding=\"" + TABLE_PADDING + "\" style=\"" + TABLE_STYLE + "\">");
                stringBuilder.append("<tr>");
                stringBuilder.append("<th>" + ThinkMachineTranslator.getTranslatedText("weaponGoal") + "</th>" +
                        "<th>" + ThinkMachineTranslator.getTranslatedText("weaponDamage") + "</th>" +
                        "<th>" + ThinkMachineTranslator.getTranslatedText("weaponRange") + "</th>" +
                        "<th>" + ThinkMachineTranslator.getTranslatedText("weaponShots") + "</th>" +
                        "<th>" + ThinkMachineTranslator.getTranslatedText("weaponRate") + "</th>" +
                        "</tr>");
                for (WeaponDamage weaponDamage : cyberneticDevice.getWeapon().getWeaponDamages()) {
                    boolean techDamageLimited = weaponDamage.getDamageTechLevel() != null &&
                            CharacterManager.getSelectedCharacter().getCharacteristic(CharacteristicName.TECH).getValue() <
                                    weaponDamage.getDamageTechLevel();
                    stringBuilder.append("<tr>");
                    stringBuilder.append(
                            "<td style=\"text-align:center\">" + weaponDamage.getGoal() + "</td>" +
                                    "<td style=\"text-align:center\" >" + getDamage(weaponDamage) + "</td>" +
                                    "<td style=\"text-align:center\" >" + weaponDamage.getRange() + "</td>" +
                                    "<td style=\"text-align:center\" >" + (weaponDamage.getShots() != null ? weaponDamage.getShots() : "") + "</td>" +
                                    "<td style=\"text-align:center\" >" + weaponDamage.getRate() + "</td>" +
                                    "</tr>");
                }
                stringBuilder.append("</table>");
            } else {
                stringBuilder.append("<table cellpadding=\"" + TABLE_PADDING + "\" style=\"" + TABLE_STYLE + "\">");
                stringBuilder.append("<tr>");
                stringBuilder.append("<th>" + ThinkMachineTranslator.getTranslatedText("weaponGoal") + "</th>" +
                        "<th>" + ThinkMachineTranslator.getTranslatedText("weaponDamage") + "</th>" +
                        "</tr>");
                for (WeaponDamage weaponDamage : cyberneticDevice.getWeapon().getWeaponDamages()) {
                    boolean techDamageLimited = weaponDamage.getDamageTechLevel() != null &&
                            CharacterManager.getSelectedCharacter().getCharacteristic(CharacteristicName.TECH).getValue() <
                                    weaponDamage.getDamageTechLevel();
                    stringBuilder.append("<tr>");
                    stringBuilder.append("<td style=\"text-align:center\">" + weaponDamage.getGoal() + "</td>" +
                            "<td style=\"text-align:center\" >" + getDamage(weaponDamage) + "</td>" +
                            "</tr>");
                }
                stringBuilder.append("</table>");
            }
        }

        stringBuilder.append("<b>" + ThinkMachineTranslator.getTranslatedText("techLevel") + ": " + "</b>" +
                (techLimited ? "<font color=\"" + getColor(R.color.insufficientTechnology) + "\">" : "") +
                cyberneticDevice.getTechLevel() +
                (techLimited ? "</font>" : "") +
                "<br><b>" + getString(R.string.incompatibility) + ": </b>" +
                cyberneticDevice.getIncompatibility() +
                "<br><b>" + getString(R.string.points) + ": </b>" +
                cyberneticDevice.getPoints() +
                "<br><b>" + getString(R.string.cost) + ": </b> " +
                cyberneticDevice.getCost() +
                " " + ThinkMachineTranslator.getTranslatedText("firebirds"));

        return stringBuilder.toString();
    }

    protected String getDamage(WeaponDamage weaponDamage) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(weaponDamage.getDamageWithoutArea());
        if (!weaponDamage.getDamageWithoutArea().endsWith("d")) {
            stringBuilder.append("d");
        }
        if (weaponDamage.getAreaMeters() > 0) {
            stringBuilder.append(" ");
            stringBuilder.append(weaponDamage.getAreaMeters());
        }
        return stringBuilder.toString();
    }
}