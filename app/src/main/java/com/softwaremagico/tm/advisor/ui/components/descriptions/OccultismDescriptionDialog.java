package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;

public class OccultismDescriptionDialog extends ElementDescriptionDialog<OccultismPower> {

    public OccultismDescriptionDialog(OccultismPower element) {
        super(element);
    }

    @Override
    protected String getDetails(OccultismPower power) {
        boolean levelLimited = false;
        if (power.getLevel() > CharacterManager.getSelectedCharacter().getOccultismLevel(OccultismPathFactory.getInstance().getOccultismPath(power).getOccultismType())) {
            levelLimited = true;
        }
        return "<table cellpadding=\"" + TABLE_PADDING + "\" style=\"" + TABLE_STYLE + "\">" +
                "<tr>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("occultismTableLevel") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("occultismTableRoll") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("occultismTableRange") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("occultismTableDuration") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("occultismTableRequirements") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("occultismTableCost") + "</th>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"text-align:center\">" +
                (levelLimited ? "<font color=\"" + getColor(R.color.insufficientOccultimsLevel) + "\">" : "") +
                power.getLevel() +
                (levelLimited ? "</font>" : "") +
                "</td>" +
                "<td style=\"text-align:center\">" + power.getRoll() + "</td>" +
                "<td style=\"text-align:center\">" + (power.getRange() != null ? power.getRange().getName() : "") + "</td>" +
                "<td style=\"text-align:center\">" + (power.getDuration() != null ? power.getDuration().getName() : "") + "</td>" +
                "<td style=\"text-align:center\">" + (power.getComponentsRepresentation().length() > 0 ?
                power.getComponentsRepresentation() : "--") + "</td>" +
                "<td style=\"text-align:center\">" + power.getCost() + "</td>" +
                "</tr>" +
                "</table>"
                ;
    }

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
