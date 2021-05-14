package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.armours.ArmourSpecification;
import com.softwaremagico.tm.character.equipment.shields.Shield;

public class ArmourDescriptionDialog extends ElementDescriptionDialog<Armour> {

    public ArmourDescriptionDialog(Armour element) {
        super(element);
    }

    @Override
    protected String getDetails(Armour armour) {
        boolean techLimited = CharacterManager.getSelectedCharacter().getCharacteristic(CharacteristicName.TECH).getValue() <
                armour.getTechLevel();
        boolean costLimited = CharacterManager.getSelectedCharacter().getMoney() < armour.getCost();
        boolean costProhibited = CharacterManager.getSelectedCharacter().getInitialMoney() < armour.getCost();
        StringBuilder stringBuilder = new StringBuilder("<table cellpadding=\"" + TABLE_PADDING + "\" style=\"" + TABLE_STYLE + "\">" +
                "<tr>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("techLevel") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("armorRating") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("dexterityAbbreviature") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("strengthAbbreviature") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("iniciativeAbbreviature") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("enduranceAbbreviature") + "</th>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"text-align:center\">" +
                (techLimited ? "<font color=\"" + getColor(R.color.insufficientTechnology) + "\">" : "") +
                armour.getTechLevel() +
                (techLimited ? "</font>" : "") +
                "</td>" +
                "<td style=\"text-align:center\">" + armour.getProtection() + "D</td>" +
                "<td style=\"text-align:center\">" + (armour.getStandardPenalization() != null ? armour.getStandardPenalization().getDexterityModification() : "")
                + (armour.getSpecialPenalization() != null && armour.getSpecialPenalization().getDexterityModification() != 0 ? "/" +
                armour.getSpecialPenalization().getDexterityModification() : "") + "</td>" +
                "<td style=\"text-align:center\">" + (armour.getStandardPenalization() != null ? armour.getStandardPenalization().getStrengthModification() : "")
                + (armour.getSpecialPenalization() != null && armour.getSpecialPenalization().getStrengthModification() != 0 ? "/" +
                armour.getSpecialPenalization().getStrengthModification() : "") + "</td>" +
                "<td style=\"text-align:center\">" + (armour.getStandardPenalization() != null ? armour.getStandardPenalization().getInitiativeModification() : "")
                + (armour.getSpecialPenalization() != null && armour.getSpecialPenalization().getInitiativeModification() != 0 ? "/" +
                armour.getSpecialPenalization().getInitiativeModification() : "") + "</td>" +
                "<td style=\"text-align:center\">" + (armour.getStandardPenalization() != null ? armour.getStandardPenalization().getEnduranceModification() : "")
                + (armour.getSpecialPenalization() != null && armour.getSpecialPenalization().getEnduranceModification() != 0 ? "/" +
                armour.getSpecialPenalization().getEnduranceModification() : "") + "</td>" +
                "</tr>" +
                "</table>");
        if (!armour.getAllowedShields().isEmpty()) {
            stringBuilder.append("<br><b>").append(ThinkMachineTranslator.getTranslatedText("shield")).append(":</b> ");
            String separator = "";
            for (Shield shield : armour.getAllowedShields()) {
                stringBuilder.append(separator);
                stringBuilder.append(shield.getName());
                separator = ", ";
            }
        }
        if (!armour.getSpecifications().isEmpty()) {
            stringBuilder.append("<br><b>").append(ThinkMachineTranslator.getTranslatedText("othersTable")).append(":</b> ");
            String separator = "";
            for (ArmourSpecification specification : armour.getSpecifications()) {
                stringBuilder.append(separator);
                stringBuilder.append(specification.getName());
                if (specification.getDescription() != null && !specification.getDescription().isEmpty()) {
                    stringBuilder.append(" (").append(specification.getDescription()).append(")");
                }
                separator = ", ";
            }
        }
        if (!armour.getDamageTypes().isEmpty()) {
            stringBuilder.append("<br><b>").append(getString(R.string.properties)).append(":</b> ");
            String separator = "";
            for (DamageType damageType : armour.getDamageTypes()) {
                stringBuilder.append(separator);
                stringBuilder.append(damageType.getName());
                separator = ", ";
            }
        }
        stringBuilder.append("<br><b>").append(getString(R.string.cost)).append("</b> ").append(costProhibited ? "<font color=\"" + getColor(R.color.unaffordableMoney) + "\">" :
                (costLimited ? "<font color=\"" + getColor(R.color.insufficientMoney) + "\">" : "")).append(armour.getCost()).append(costLimited || costProhibited ? "</font>" : "").append(" ").append(ThinkMachineTranslator.getTranslatedText("firebirds"));
        return stringBuilder.toString();
    }
}
