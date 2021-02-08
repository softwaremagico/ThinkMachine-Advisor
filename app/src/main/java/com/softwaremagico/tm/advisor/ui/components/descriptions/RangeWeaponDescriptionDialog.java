package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;

public class RangeWeaponDescriptionDialog extends ElementDescriptionDialog<Weapon> {

    public RangeWeaponDescriptionDialog(Weapon element) {
        super(element);
    }

    @Override
    protected String getDetails(Weapon weapon) {
        boolean techLimited = CharacterManager.getSelectedCharacter().getCharacteristic(CharacteristicName.TECH).getValue() <
                weapon.getTechLevel();
        boolean costLimited = CharacterManager.getSelectedCharacter().getMoney() < weapon.getCost();
        boolean costProhibited = CharacterManager.getSelectedCharacter().getInitialMoney() < weapon.getCost();
        return "<table cellpadding=\"8\" style=\"border:1px solid black;margin-left:auto;margin-right:auto;\">" +
                "<tr>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("techLevel") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponGoal") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponDamage") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponRange") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponShots") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponRate") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponSize") + "</th>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"text-align:center\">" +
                (techLimited ? "<font color=\"" + getColor(R.color.insufficientTechnology) + "\">" : "") +
                weapon.getTechLevel() +
                (techLimited ? "</font>" : "") +
                "</td>" +
                "<td style=\"text-align:center\">" + weapon.getGoal() + "</td>" +
                "<td style=\"text-align:center\" >" + getDamage(weapon) + "</td>" +
                "<td style=\"text-align:center\" >" + weapon.getRange() + "</td>" +
                "<td style=\"text-align:center\" >" + (weapon.getShots() != null ? weapon.getShots() : "") + "</td>" +
                "<td style=\"text-align:center\" >" + weapon.getRate() + "</td>" +
                "<td style=\"text-align:center\" >" + (weapon.getSize() != null ? weapon.getSize().toString() : "") + "</td>" +
                "</tr>" +
                "</table>" +
                (!weapon.getWeaponOthersText().isEmpty() ?
                        "<br><b>" + ThinkMachineTranslator.getTranslatedText("weaponsOthers") + ":</b> " +
                                weapon.getWeaponOthersText() : "") +
                "<br><b>" + getString(R.string.cost) + "</b> " +
                (costProhibited ? "<font color=\"" + getColor(R.color.unaffordableMoney) + "\">" :
                        (costLimited ? "<font color=\"" + getColor(R.color.insufficientMoney) + "\">" : "")) +
                weapon.getCost() +
                (costLimited || costProhibited ? "</font>" : "") +
                " " + ThinkMachineTranslator.getTranslatedText("firebirds");
    }

    private String getDamage(Weapon weapon) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(weapon.getDamageWithoutArea());
        if (!weapon.getDamageWithoutArea().endsWith("d")) {
            stringBuilder.append("d");
        }
        if (weapon.getAreaMeters() > 0) {
            stringBuilder.append(" ");
            stringBuilder.append(weapon.getAreaMeters());
        }
        return stringBuilder.toString();
    }
}
