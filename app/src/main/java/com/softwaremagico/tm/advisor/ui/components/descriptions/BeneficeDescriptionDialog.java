package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;

public class BeneficeDescriptionDialog extends ElementDescriptionDialog<AvailableBenefice> {

    public BeneficeDescriptionDialog(AvailableBenefice element) {
        super(element);
    }

    @Override
    protected String getDetails(AvailableBenefice benefice) {
        return "<b>" + getString(R.string.cost) + "</b> " + benefice.getCost();
    }
}
