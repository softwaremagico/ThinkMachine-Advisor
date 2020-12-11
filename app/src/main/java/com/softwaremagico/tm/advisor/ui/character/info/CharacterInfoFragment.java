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

package com.softwaremagico.tm.advisor.ui.character.info;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.counters.CharacteristicsCounter;
import com.softwaremagico.tm.advisor.ui.components.CustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.EnumAdapter;
import com.softwaremagico.tm.advisor.ui.components.EnumSpinner;
import com.softwaremagico.tm.advisor.ui.components.counters.ExtraCounter;
import com.softwaremagico.tm.advisor.ui.components.TranslatedEditText;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.InvalidFactionException;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.races.InvalidRaceException;
import com.softwaremagico.tm.character.races.Race;

public class CharacterInfoFragment extends CustomFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private CharacterInfoViewModel mViewModel;
    private CharacteristicsCounter characteristicsCounter;
    private ExtraCounter extraCounter;

    public static CharacterInfoFragment newInstance(int index) {
        final CharacterInfoFragment fragment = new CharacterInfoFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.character_info_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CharacterInfoViewModel.class);

        createNameText(root);
        createSurnameText(root);
        createGenderSpinner(root);
        createAgeText(root);
        createRaceSpinner(root);
        createFactionSpinner(root);
        createPlanetSpinner(root);

        characteristicsCounter = root.findViewById(R.id.characteristics_counter);
        extraCounter = root.findViewById(R.id.extra_counter);

        setCharacter(root, CharacterManager.getSelectedCharacter());

        return root;
    }

    @Override
    public void setCharacter(View root, CharacterPlayer character) {
        final TranslatedEditText nameTextEditor = root.findViewById(R.id.character_name);
        nameTextEditor.setText(character.getInfo().getNameRepresentation());
        final TranslatedEditText surnameTextEditor = root.findViewById(R.id.character_surname);
        if (CharacterManager.getSelectedCharacter().getInfo().getSurname() != null) {
            surnameTextEditor.setText(character.getInfo().getSurname().getName());
        } else {
            surnameTextEditor.setText("");
        }
        final EnumSpinner genderSelector = root.findViewById(R.id.character_gender);
        genderSelector.setSelection(character.getInfo().getGender());
        final TranslatedEditText ageTextEditor = root.findViewById(R.id.character_age);
        if (CharacterManager.getSelectedCharacter().getInfo().getAge() != null) {
            ageTextEditor.setText(CharacterManager.getSelectedCharacter().getInfo().getAge().toString());
        } else {
            ageTextEditor.setText("");
        }
        final ElementSpinner raceSelector = root.findViewById(R.id.character_race);
        raceSelector.setSelection(CharacterManager.getSelectedCharacter().getRace());
        final ElementSpinner factionsSelector = root.findViewById(R.id.character_faction);
        factionsSelector.setSelection(CharacterManager.getSelectedCharacter().getFaction());
        final ElementSpinner planetSelector = root.findViewById(R.id.character_planet);
        planetSelector.setSelection(CharacterManager.getSelectedCharacter().getInfo().getPlanet());

        characteristicsCounter.setCharacter(character);
        extraCounter.setCharacter(character);
    }

    private void createNameText(View root) {
        final TranslatedEditText nameTextEditor = root.findViewById(R.id.character_name);
        nameTextEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //Nothing
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                //Nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                CharacterManager.getSelectedCharacter().getInfo().setNames(nameTextEditor.getText());
            }
        });
    }

    private void createSurnameText(View root) {
        final TranslatedEditText surnameTextEditor = root.findViewById(R.id.character_surname);
        surnameTextEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //Nothing
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                //Nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                CharacterManager.getSelectedCharacter().getInfo().setSurname(surnameTextEditor.getText());
            }
        });
    }

    private void createGenderSpinner(View root) {
        final EnumSpinner genderSelector = root.findViewById(R.id.character_gender);
        genderSelector.setAdapter(new EnumAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mViewModel.getAvailableGenders()));
        genderSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                CharacterManager.getSelectedCharacter().getInfo().setGender(mViewModel.getAvailableGenders().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //Nothing
            }
        });
    }

    private void createAgeText(View root) {
        final TranslatedEditText ageTextEditor = root.findViewById(R.id.character_age);
        ageTextEditor.setAsNumberEditor();
        ageTextEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //Nothing
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                //Nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    CharacterManager.getSelectedCharacter().getInfo().setAge(Integer.parseInt(ageTextEditor.getText()));
                } catch (NumberFormatException e) {
                    //No age set.
                }
            }
        });

    }

    private void createRaceSpinner(View root) {
        final ElementSpinner raceSelector = root.findViewById(R.id.character_race);
        raceSelector.setAdapter(new ElementAdapter<>(getActivity(), mViewModel.getAvailableRaces(), false, Race.class));
        raceSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (mViewModel.getAvailableRaces().get(position).getId().equals(Element.DEFAULT_NULL_ID)) {
                    CharacterManager.setRace(null);
                } else {
                    CharacterManager.setRace(mViewModel.getAvailableRaces().get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                try {
                    CharacterManager.getSelectedCharacter().setRace(null);
                } catch (InvalidRaceException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
            }
        });
    }

    private void createFactionSpinner(View root) {
        final ElementSpinner factionsSelector = root.findViewById(R.id.character_faction);
        factionsSelector.setAdapter(new ElementAdapter<>(getActivity(), mViewModel.getAvailableFactions(), false, Faction.class));
        factionsSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (mViewModel.getAvailableFactions().get(position).getId().equals(Element.DEFAULT_NULL_ID)) {
                        CharacterManager.getSelectedCharacter().setFaction(null);
                    } else {
                        CharacterManager.getSelectedCharacter().setFaction(mViewModel.getAvailableFactions().get(position));
                    }
                } catch (InvalidFactionException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                try {
                    CharacterManager.getSelectedCharacter().setFaction(null);
                } catch (InvalidFactionException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
            }
        });
    }

    private void createPlanetSpinner(View root) {
        final ElementSpinner planetSelector = root.findViewById(R.id.character_planet);
        planetSelector.setAdapter(new ElementAdapter<>(getActivity(), mViewModel.getAvailablePlanets(), false, Planet.class));
        planetSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (mViewModel.getAvailablePlanets().get(position).getId().equals(Element.DEFAULT_NULL_ID)) {
                    CharacterManager.getSelectedCharacter().getInfo().setPlanet(null);
                } else {
                    CharacterManager.getSelectedCharacter().getInfo().setPlanet(mViewModel.getAvailablePlanets().get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                CharacterManager.getSelectedCharacter().getInfo().setPlanet(null);
            }
        });
    }


}
