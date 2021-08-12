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

package com.softwaremagico.tm.advisor.ui.random.characters;

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
import com.softwaremagico.tm.advisor.ui.components.ElementRadio;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.exceptions.RestrictedElementException;
import com.softwaremagico.tm.character.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.creation.CharacterProgressionStatus;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.predefined.characters.Npc;
import com.softwaremagico.tm.random.predefined.characters.NpcFactory;

import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomCharactersFragment extends CharacterCustomFragment {
    private View root;
    private final Set<ElementRadio<Npc>> optionsAvailable = new HashSet<>();

    public static RandomCharactersFragment newInstance(int index) {
        final RandomCharactersFragment fragment = new RandomCharactersFragment();
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
        final LinearLayout linearLayout = root.findViewById(R.id.npc_container);
        if (linearLayout == null) {
            return;
        }

        NpcFactory.getInstance().getGroups(Locale.getDefault().getLanguage(),
                ModuleManager.DEFAULT_MODULE).stream().sorted().forEach(group -> {
            try {
                addSection(getContext().getResources().getString(getContext().getResources().getIdentifier(getOptionTranslation(group), "string",
                        getContext().getPackageName())), linearLayout);
            } catch (Exception e) {
                addSection(group, linearLayout);
            }
            NpcFactory.getInstance().getByGroup(group).stream().sorted().forEach(npc -> {
                ElementRadio<Npc> randomProfileSelector = new ElementRadio<>(getContext(), npc);
                linearLayout.addView(randomProfileSelector);
                optionsAvailable.add(randomProfileSelector);
                randomProfileSelector.setOnCheckedChangeListener((compoundButton, checked) -> {
                    //Unselect all other radio buttons.
                    if (checked) {
                        optionsAvailable.stream().filter(npcElementRadio -> !Objects.equals(npcElementRadio, randomProfileSelector)).
                                forEach(npcElementRadio -> npcElementRadio.setChecked(false));
                    }
                });
            });
            addSpace(linearLayout);
        });

        addFinalSpace(linearLayout);

        setCharacter(root, CharacterManager.getSelectedCharacter());
    }

    @Override
    protected void updateSettings(CharacterPlayer characterPlayer) {
        // Nothing yet.
    }

    private String getOptionTranslation(String groupName) {
        return "npc_" + groupName.toLowerCase() + "_section";
    }

    public static <T extends Enum<T>> T getInstance(final String value, final Class<T> enumClass) {
        return Enum.valueOf(enumClass, value);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.random_npc_fragment, container, false);

        final FloatingActionButton fab = root.findViewById(R.id.random);
        fab.setOnClickListener(view -> {
            if (CharacterManager.getCostCalculator().getStatus().ordinal() > CharacterProgressionStatus.NOT_STARTED.ordinal()) {
                SnackbarGenerator.getWarningMessage(root, R.string.message_random_character_already_started,
                        R.string.action_proceed, action -> {
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
            CharacterManager.addNewCharacter();
            CharacterManager.randomizeCharacterUsingNpc(getSelectedOptions());
            SnackbarGenerator.getInfoMessage(root, R.string.message_random_character_success).show();
        } catch (InvalidXmlElementException | InvalidRandomElementSelectedException | RestrictedElementException | UnofficialElementNotAllowedException e) {
            SnackbarGenerator.getErrorMessage(root, R.string.message_random_character_error).show();
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }
    }

    public Set<Npc> getSelectedOptions() {
        return optionsAvailable.stream().filter(ElementRadio::isChecked).map(ElementRadio::getSelection).collect(Collectors.toSet());
    }


}
