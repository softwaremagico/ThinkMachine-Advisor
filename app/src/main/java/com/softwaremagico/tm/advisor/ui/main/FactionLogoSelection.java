package com.softwaremagico.tm.advisor.ui.main;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.character.factions.Faction;

public class FactionLogoSelection {

    public static int getLogo(Faction faction) {
        if (faction != null) {
            return R.drawable.ic_almalik;
        }
        return R.drawable.ic_launcher;
    }
}
