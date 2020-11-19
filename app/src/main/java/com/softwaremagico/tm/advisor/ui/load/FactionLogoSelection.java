package com.softwaremagico.tm.advisor.ui.load;

import android.content.Context;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.character.factions.Faction;

public class FactionLogoSelection {

    public static int getLogo(Context context, Faction faction) {
        if (faction != null) {
            try {
                final int id = context.getResources().getIdentifier("ic_" + faction.getId().toLowerCase(), "drawable", context.getPackageName());
                if (id > 0) {
                    return id;
                }
                // return R.drawable.ic_almalik;
            } catch (Exception e) {
                //Logo does not exists.
            }
        }
        return R.drawable.ic_empty;
    }
}
