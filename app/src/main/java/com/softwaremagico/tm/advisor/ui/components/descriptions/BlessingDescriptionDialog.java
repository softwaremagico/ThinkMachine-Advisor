package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.values.Bonification;

public class BlessingDescriptionDialog extends ElementDescriptionDialog<Blessing> {

    public BlessingDescriptionDialog(Blessing element) {
        super(element);
    }

    @Override
    protected String getDetails(Blessing blessing) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<b>").append(getString(R.string.cost)).append("</b> ").append(blessing.getCost()).append(" ");
        if (!blessing.getBonifications().isEmpty()) {
            stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("traits")).append(":</b> ");
        }
        for (Bonification bonification : blessing.getBonifications()) {
            stringBuilder.append(bonification.getBonification() != null ? bonification.getBonification() : "").append(" ").append(bonification.getAffects() != null ? bonification.getAffects().getName()
                    : "").append(" ").append((bonification.getSituation() != null ? "(" + bonification.getSituation() + ")" : ""));
        }
        return stringBuilder.toString();
    }
}
