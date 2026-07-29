package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.character.Numbers;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponDamage;

public class RangeWeaponDescriptionDialog extends WeaponDescriptionDialog {

    public RangeWeaponDescriptionDialog(Weapon element) {
        super(element);
    }

    @Override
    protected String getDetails(Weapon weapon) {
        boolean techLimited = CharacterManager.getSelectedCharacter().getCharacteristic(CharacteristicName.TECH).getValue() <
                weapon.getTechLevel();
        boolean costLimited = CharacterManager.getSelectedCharacter().getMoney() < weapon.getCost();
        boolean costProhibited = CharacterManager.getSelectedCharacter().getInitialMoney() < weapon.getCost();
        StringBuilder stringBuilder = new StringBuilder("<table cellpadding=\"" + TABLE_PADDING + "\" style=\"" + TABLE_STYLE + "\">");
        stringBuilder.append("<tr>");
        if (weapon.getWeaponDamages().size() > 1) {
            stringBuilder.append("<th>" + ThinkMachineTranslator.getTranslatedText("weapon") + "</th>");
        }
        stringBuilder.append("<th>" + ThinkMachineTranslator.getTranslatedText("techLevel") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponGoal") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponDamage") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponRange") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponShots") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponRate") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponSize") + "</th>" +
                "</tr>");
        for (WeaponDamage weaponDamage : weapon.getWeaponDamages()) {
            boolean techDamageLimited = weaponDamage.getDamageTechLevel() != null &&
                    CharacterManager.getSelectedCharacter().getCharacteristic(CharacteristicName.TECH).getValue() <
                            weaponDamage.getDamageTechLevel();
            stringBuilder.append("<tr>");
            if (weapon.getWeaponDamages().size() > 1) {
                stringBuilder.append("<td style=\"text-align:center\">" + (weaponDamage.getName() != null ? weaponDamage.getName() : weapon.getName()) + "</td>");
            }
            stringBuilder.append("<td style=\"text-align:center\">" +
                    (techLimited || techDamageLimited ? "<font color=\"" + getColor(R.color.insufficientTechnology) + "\">" : "") +
                    (weaponDamage.getDamageTechLevel() == null ? weapon.getTechLevel() : weaponDamage.getDamageTechLevel()) +
                    (techLimited || techDamageLimited ? "</font>" : "") +
                    "</td>" +
                    "<td style=\"text-align:center\">" + weaponDamage.getGoal() + "</td>" +
                    "<td style=\"text-align:center\" >" + getDamage(weaponDamage) + "</td>" +
                    "<td style=\"text-align:center\" >" + weaponDamage.getRange() + "</td>" +
                    "<td style=\"text-align:center\" >" + (weaponDamage.getShots() != null ? weaponDamage.getShots() : "") + "</td>" +
                    "<td style=\"text-align:center\" >" + weaponDamage.getRate() + "</td>" +
                    "<td style=\"text-align:center\" >" + (weapon.getSize() != null ? weapon.getSize().toString() : "") + "</td>" +
                    "</tr>");
        }
        stringBuilder.append("</table>" +
                (!weapon.getWeaponOthersText().isEmpty() ?
                        "<br><b>" + ThinkMachineTranslator.getTranslatedText("weaponsOthers") + ":</b> " +
                                weapon.getWeaponOthersText() : "") +
                "<br><b>" + getString(R.string.cost) + "</b> " +
                (costProhibited ? "<font color=\"" + getColor(R.color.unaffordableMoney) + "\">" :
                        (costLimited ? "<font color=\"" + getColor(R.color.insufficientMoney) + "\">" : "")) +
                Numbers.PRICE_FORMAT.format(weapon.getCost()) +
                (costLimited || costProhibited ? "</font>" : "") +
                " " + ThinkMachineTranslator.getTranslatedText("firebirds"));
        return stringBuilder.toString();
    }


}
