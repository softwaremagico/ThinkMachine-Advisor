package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponDamage;

public class MeleeWeaponDescriptionDialog extends WeaponDescriptionDialog {

    public MeleeWeaponDescriptionDialog(Weapon element) {
        super(element);
    }

    @Override
    protected String getDetails(Weapon weapon) {
        boolean techLimited = CharacterManager.getSelectedCharacter().getCharacteristic(CharacteristicName.TECH).getValue() <
                weapon.getTechLevel();
        boolean costLimited = CharacterManager.getSelectedCharacter().getMoney() < weapon.getCost();
        boolean costProhibited = CharacterManager.getSelectedCharacter().getInitialMoney() < weapon.getCost();
        StringBuilder stringBuilder = new StringBuilder("<table cellpadding=\"8\" style=\"border:1px solid black;margin-left:auto;margin-right:auto;\">" +
                "<tr>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("techLevel") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponGoal") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponDamage") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponStrength") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponSize") + "</th>" +
                "</tr>");
        for (WeaponDamage weaponDamage : weapon.getWeaponDamages()) {
            stringBuilder.append("<tr>" +
                    "<td style=\"text-align:center\">" +
                    (techLimited ? "<font color=\"" + getColor(R.color.insufficientTechnology) + "\">" : "") +
                    weapon.getTechLevel() +
                    (techLimited ? "</font>" : "") +
                    "</td>" +
                    "<td style=\"text-align:center\">" + weaponDamage.getGoal() + "</td>" +
                    "<td style=\"text-align:center\" >" + getDamage(weaponDamage) + "</td>" +
                    "<td style=\"text-align:center\" >" + weaponDamage.getStrength() + "</td>" +
                    "<td style=\"text-align:center\" >" + (weapon.getSize() != null ? weapon.getSize().toString() : "") + "</td>" +
                    "</tr>");
        }
        stringBuilder.append(
                "</table>" +
                        (!weapon.getWeaponOthersText().isEmpty() ?
                                "<br><b>" + ThinkMachineTranslator.getTranslatedText("weaponsOthers") + ":</b> " +
                                        weapon.getWeaponOthersText() : "") +
                        "<br><b>" + getString(R.string.cost) + "</b> " +
                        (costProhibited ? "<font color=\"" + getColor(R.color.unaffordableMoney) + "\">" :
                                (costLimited ? "<font color=\"" + getColor(R.color.insufficientMoney) + "\">" : "")) +
                        weapon.getCost() +
                        (costLimited || costProhibited ? "</font>" : "") +
                        " " + ThinkMachineTranslator.getTranslatedText("firebirds"));
        return stringBuilder.toString();
    }
}
