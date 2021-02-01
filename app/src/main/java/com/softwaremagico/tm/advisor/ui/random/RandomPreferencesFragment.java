/*
 *  Copyright (C) 2020 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.ui.random;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.random.selectors.PreferenceGroup;

public class RandomPreferencesFragment extends CharacterCustomFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private View root;

    public static RandomPreferencesFragment newInstance(int index) {
        final RandomPreferencesFragment fragment = new RandomPreferencesFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setCharacter(View root, CharacterPlayer character) {

    }

    @Override
    protected void initData() {
        final LinearLayout linearLayout = root.findViewById(R.id.preferences_container);

        for (PreferenceGroup preferenceGroup : PreferenceGroup.values()) {
            try {
                addSection(getResources().getString(getResources().getIdentifier(getGroupPreferenceStringResource(preferenceGroup), "string",
                        getContext().getPackageName())), linearLayout);
            } catch (Resources.NotFoundException e) {
                AdvisorLog.severe(this.getClass().getName(), "Preference group '" + preferenceGroup.name() + "' has no translation '" +
                        getGroupPreferenceStringResource(preferenceGroup) + "'.");
            }
        }

        setCharacter(root, CharacterManager.getSelectedCharacter());
    }

    private String getGroupPreferenceStringResource(PreferenceGroup preferenceGroup) {
        return "preference_group_" + preferenceGroup.name().toLowerCase();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.random_preferences_fragment, container, false);

        return root;
    }


}
