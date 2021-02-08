package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.shields.Shield;

public class ShieldDescriptionDialog extends ElementDescriptionDialog<Shield> {

    public ShieldDescriptionDialog(Shield element) {
        super(element);
    }

    @Override
    protected String getDetails(Shield shield) {
        boolean techLimited = CharacterManager.getSelectedCharacter().getCharacteristic(CharacteristicName.TECH).getValue() <
                shield.getTechLevel();
        boolean costLimited = CharacterManager.getSelectedCharacter().getMoney() < shield.getCost();
        boolean costProhibited = CharacterManager.getSelectedCharacter().getInitialMoney() < shield.getCost();
        return "<table cellpadding=\"8\" style=\"border:1px solid black;margin-left:auto;margin-right:auto;\">" +
                "<tr>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("techLevel") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("impactForce") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("shieldHits") + "</th>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"text-align:center\">" +
                (techLimited ? "<font color=\"" + getColor(R.color.insufficientTechnology) + "\">" : "") +
                shield.getTechLevel() +
                (techLimited ? "</font>" : "") +
                "</td>" +
                "<td style=\"text-align:center\">" + shield.getImpact() + "/" + shield.getForce() + "</td>" +
                "<td style=\"text-align:center\" >" + shield.getHits() + "</td>" +
                "</tr>" +
                "</table>" +
                "<br><b>" + getString(R.string.cost) + "</b> " +
                (costProhibited ? "<font color=\"" + getColor(R.color.unaffordableMoney) + "\">" :
                        (costLimited ? "<font color=\"" + getColor(R.color.insufficientMoney) + "\">" : "")) +
                shield.getCost() +
                (costLimited || costProhibited ? "</font>" : "") +
                " " + ThinkMachineTranslator.getTranslatedText("firebirds");
    }
}
