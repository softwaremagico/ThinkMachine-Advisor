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

package com.softwaremagico.tm.advisor.ui.random.profile;

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
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementSelector;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RestrictedElementException;
import com.softwaremagico.tm.character.creation.CharacterProgressionStatus;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.predefined.profile.RandomProfile;
import com.softwaremagico.tm.random.predefined.profile.RandomProfileFactory;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomProfilesFragment extends CharacterCustomFragment {
    private View root;
    private final Set<ElementSelector<RandomProfile>> optionsAvailable = new HashSet<>();

    public static RandomProfilesFragment newInstance(int index) {
        final RandomProfilesFragment fragment = new RandomProfilesFragment();
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
        final LinearLayout linearLayout = root.findViewById(R.id.profiles_container);
        if (linearLayout == null) {
            return;
        }

        RandomProfileFactory.getInstance().getGroups(Locale.getDefault().getLanguage(),
                ModuleManager.DEFAULT_MODULE).stream().sorted().forEach(group -> {
            try {
                addSection(getContext().getResources().getString(getContext().getResources().getIdentifier(getOptionTranslation(group), "string",
                        getContext().getPackageName())), linearLayout);
            } catch (Exception e) {
                addSection(group, linearLayout);
            }
            RandomProfileFactory.getInstance().getByGroup(group).stream().sorted().forEach(randomProfile -> {
                ElementSelector<RandomProfile> randomProfileSelector = new ElementSelector<>(getContext(), randomProfile);
                linearLayout.addView(randomProfileSelector);
                optionsAvailable.add(randomProfileSelector);
            });
            addSpace(linearLayout);
        });

        addFinalSpace(linearLayout);

        setCharacter(root, CharacterManager.getSelectedCharacter());
    }

    private String getOptionTranslation(String groupName) {
        return "profile_" + groupName.toLowerCase() + "_section";
    }

    public static <T extends Enum<T>> T getInstance(final String value, final Class<T> enumClass) {
        return Enum.valueOf(enumClass, value);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.random_profiles_fragment, container, false);

        final FloatingActionButton fab = root.findViewById(R.id.random);
        fab.setOnClickListener(view -> {
            if (CharacterManager.getCostCalculator().getStatus().ordinal() > CharacterProgressionStatus.IN_PROGRESS.ordinal()) {
                SnackbarGenerator.getWarningMessage(root, R.string.message_random_character_already_finished,
                        R.string.action_new, action -> {
                            generateCharacter();
                        }).show();
            } else {
                generateCharacter();
            }
        });

        return root;
    }

    private void generateCharacter() {
        try {
            CharacterManager.randomizeCharacterUsingProfiles(getSelectedOptions());
            SnackbarGenerator.getInfoMessage(root, R.string.message_random_character_success).show();
        } catch (InvalidXmlElementException | InvalidRandomElementSelectedException | RestrictedElementException e) {
            SnackbarGenerator.getErrorMessage(root, R.string.message_random_character_error).show();
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }
    }

    public Set<RandomProfile> getSelectedOptions() {
        return optionsAvailable.stream().filter(ElementSelector::isChecked).map(ElementSelector::getSelection).collect(Collectors.toSet());
    }


}
