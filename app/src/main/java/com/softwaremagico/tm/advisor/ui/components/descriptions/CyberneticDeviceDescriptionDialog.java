package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.cybernetics.CyberneticDevice;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceTrait;
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
                    stringBuilder.append("<tr>");
                    stringBuilder.append("<td style=\"text-align:center\">").append(weaponDamage.getGoal()).append("</td>").
                            append("<td style=\"text-align:center\" >").append(getDamage(weaponDamage)).append("</td>").
                            append("<td style=\"text-align:center\" >").append(weaponDamage.getRange()).append("</td>").
                            append("<td style=\"text-align:center\" >").append(weaponDamage.getShots() != null ? weaponDamage.getShots() : "").append("</td>").
                            append("<td style=\"text-align:center\" >").append(weaponDamage.getRate()).append("</td>").
                            append("</tr>");
                }
                stringBuilder.append("</table>");
            } else {
                stringBuilder.append("<table cellpadding=\"" + TABLE_PADDING + "\" style=\"" + TABLE_STYLE + "\">");
                stringBuilder.append("<tr>");
                stringBuilder.append("<th>").append(ThinkMachineTranslator.getTranslatedText("weaponGoal")).append("</th>").
                        append("<th>").append(ThinkMachineTranslator.getTranslatedText("weaponDamage")).append("</th>").append("</tr>");
                for (WeaponDamage weaponDamage : cyberneticDevice.getWeapon().getWeaponDamages()) {
                    stringBuilder.append("<tr>");
                    stringBuilder.append("<td style=\"text-align:center\">").append(weaponDamage.getGoal()).append("</td>").
                            append("<td style=\"text-align:center\" >").append(getDamage(weaponDamage)).append("</td>").
                            append("</tr>");
                }
                stringBuilder.append("</table>");
            }
            stringBuilder.append("<br>");
        }

        //Set traits
        if (!cyberneticDevice.getTraits().isEmpty()) {
            stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("cyberneticsTraits")).append(": ").append("</b>");
            String separator = "";
            for (CyberneticDeviceTrait trait : cyberneticDevice.getTraits()) {
                stringBuilder.append(separator);
                stringBuilder.append(trait.getName());
                separator = ", ";
            }
            stringBuilder.append("<br>");
        }

        //Set costs.
        stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("techLevel")).append(": ").append("</b>").
                append(techLimited ? "<font color=\"" + getColor(R.color.insufficientTechnology) + "\">" : "").
                append(cyberneticDevice.getTechLevel()).append(techLimited ? "</font>" : "").append("<br><b>").
                append(getString(R.string.incompatibility)).append(": </b>").append(cyberneticDevice.getIncompatibility()).append("<br><b>").
                append(getString(R.string.points)).append(": </b>").append(cyberneticDevice.getPoints()).append("<br><b>").
                append(getString(R.string.cost)).append(": </b> ").append(cyberneticDevice.getCost()).append(" ").
                append(ThinkMachineTranslator.getTranslatedText("firebirds"));

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