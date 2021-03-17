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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.core.random.PreferenceGroup;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.EnumSpinner;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RandomPreferencesFragment extends CharacterCustomFragment {
    private View root;
    private Set<EnumSpinner> optionsAvailable = new HashSet<>();

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
        if (linearLayout == null) {
            return;
        }
        for (PreferenceGroup preferenceGroup : PreferenceGroup.values()) {
            try {
                addSection(getResources().getString(getResources().getIdentifier(getGroupPreferenceStringResource(preferenceGroup), "string",
                        getContext().getPackageName())), linearLayout);
                for (Class<? extends IRandomPreference> preference : preferenceGroup.getRandomPreferences()) {
                    final EnumSpinner optionsSelector = new EnumSpinner(getContext(), null);
                    List<? extends IRandomPreference> options = new ArrayList<>(Arrays.asList(preference.getEnumConstants()));
                    if (preference.getEnumConstants().length == 0 || preference.getEnumConstants()[0].getDefault() == null) {
                        options.add(0, null);
                    }
                    optionsSelector.setAdapter(new RandomEnumAdapter(getActivity(), android.R.layout.simple_spinner_item, options));
                    try {
                        optionsSelector.setText(getResources().getString(getResources().getIdentifier(getPreferenceStringResource(preference), "string",
                                getContext().getPackageName())));
                    } catch (Resources.NotFoundException e) {
                        AdvisorLog.severe(this.getClass().getName(), "Preference option '" + preference + "' has no translation '" +
                                getPreferenceStringResource(preference) + "'.");
                        optionsSelector.setText(getPreferenceStringResource(preference));
                    }
                    if (preference.getEnumConstants().length > 0 && preference.getEnumConstants()[0].getDefault() != null) {
                        optionsSelector.setSelection(preference.getEnumConstants()[0].getDefault());
                    }
                    linearLayout.addView(optionsSelector);
                    optionsAvailable.add(optionsSelector);
                }
                addSpace(linearLayout);
            } catch (Resources.NotFoundException e) {
                AdvisorLog.severe(this.getClass().getName(), "Preference group '" + preferenceGroup.name() + "' has no translation '" +
                        getGroupPreferenceStringResource(preferenceGroup) + "'.");
            }
        }

        setCharacter(root, CharacterManager.getSelectedCharacter());
    }

    public static <T extends Enum<T>> T getInstance(final String value, final Class<T> enumClass) {
        return Enum.valueOf(enumClass, value);
    }


    private String getGroupPreferenceStringResource(PreferenceGroup preferenceGroup) {
        return "preference_group_" + preferenceGroup.name().toLowerCase();
    }

    private String getPreferenceStringResource(Class<? extends IRandomPreference> preference) {
        return "preference_" + preference.getSimpleName().toLowerCase();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.random_preferences_fragment, container, false);

        final FloatingActionButton fab = root.findViewById(R.id.random);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CharacterManager.randomizeCharacter(getSelectedOptions());
                    SnackbarGenerator.getInfoMessage(root, R.string.message_random_character_success).show();
                } catch (InvalidXmlElementException | TooManyBlessingsException | DuplicatedPreferenceException | InvalidRandomElementSelectedException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.message_random_character_error).show();
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
            }
        });

        return root;
    }

    public Set<IRandomPreference> getSelectedOptions() {
        Set<IRandomPreference> selectedPreferences = new HashSet<>();
        for (EnumSpinner optionSelected : optionsAvailable) {
            if (optionSelected.getSelectedItem() != null) {
                if (optionSelected.<IRandomPreference>getSelectedItem().getDefault() != optionSelected.<IRandomPreference>getSelectedItem()) {
                    selectedPreferences.add(optionSelected.getSelectedItem());
                }
            }
        }
        return selectedPreferences;
    }


}
