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
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.EnumAdapter;
import com.softwaremagico.tm.advisor.ui.components.EnumSpinner;
import com.softwaremagico.tm.advisor.ui.components.TranslatedEditText;
import com.softwaremagico.tm.advisor.ui.components.counters.CharacteristicsCounter;
import com.softwaremagico.tm.advisor.ui.components.counters.ExtraCounter;
import com.softwaremagico.tm.advisor.ui.components.counters.FirebirdsCounter;
import com.softwaremagico.tm.advisor.ui.components.counters.SkillsCounter;
import com.softwaremagico.tm.advisor.ui.components.counters.TraitsCounter;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.InvalidFactionException;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.races.InvalidRaceException;
import com.softwaremagico.tm.character.races.Race;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CharacterInfoFragmentCharacter extends CharacterCustomFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private CharacterInfoViewModel mViewModel;
    private CharacteristicsCounter characteristicsCounter;
    private SkillsCounter skillsCounter;
    private TraitsCounter traitsCounter;
    private ExtraCounter extraCounter;
    private FirebirdsCounter firebirdsCounter;
    private View root;

    public static CharacterInfoFragmentCharacter newInstance(int index) {
        final CharacterInfoFragmentCharacter fragment = new CharacterInfoFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {
        createNameText(root);
        createSurnameText(root);
        createGenderSpinner(root);
        createAgeText(root);
        createRaceSpinner(root);
        createFactionSpinner(root);
        createPlanetSpinner(root);

        setCharacter(root, CharacterManager.getSelectedCharacter());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_info_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CharacterInfoViewModel.class);

        characteristicsCounter = root.findViewById(R.id.characteristics_counter);
        skillsCounter = root.findViewById(R.id.skills_counter);
        traitsCounter = root.findViewById(R.id.traits_counter);
        extraCounter = root.findViewById(R.id.extra_counter);
        firebirdsCounter = root.findViewById(R.id.firebirds_counter);

        CharacterManager.addCharacterRaceUpdatedListener(characterPlayer -> updateCounters(characterPlayer));
        CharacterManager.addCharacterAgeUpdatedListener(characterPlayer -> updateCounters(characterPlayer));

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

        updateCounters(character);
    }

    private void updateCounters(CharacterPlayer character) {
        characteristicsCounter.setCharacter(character);
        extraCounter.setCharacter(character);
        skillsCounter.setCharacter(character);
        traitsCounter.setCharacter(character);
        firebirdsCounter.setCharacter(character);
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
        List<Gender> options = new ArrayList<>(mViewModel.getAvailableGenders());
        options.add(0, null);
        genderSelector.setAdapter(new EnumAdapter<>(getActivity(), android.R.layout.simple_spinner_item, options));
        genderSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position > 0) {
                    CharacterManager.getSelectedCharacter().getInfo().setGender(mViewModel.getAvailableGenders().get(position - 1));
                } else {
                    CharacterManager.getSelectedCharacter().getInfo().setGender(null);
                }
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
                    if (!Objects.equals(CharacterManager.getSelectedCharacter().getInfo().getAge() + "", ageTextEditor.getText())) {
                        CharacterManager.getSelectedCharacter().getInfo().setAge(Integer.parseInt(ageTextEditor.getText()));
                        //Force to update all costs.
                        CharacterManager.launchCharacterAgeUpdatedListeners(CharacterManager.getSelectedCharacter());
                    }
                } catch (NumberFormatException e) {
                    CharacterManager.getSelectedCharacter().getInfo().setAge(null);
                    CharacterManager.launchCharacterAgeUpdatedListeners(CharacterManager.getSelectedCharacter());
                }
            }
        });

    }

    private void createRaceSpinner(View root) {
        final ElementSpinner raceSelector = root.findViewById(R.id.character_race);
        List<Race> options = new ArrayList<>(mViewModel.getAvailableRaces());
        options.add(0, null);
        raceSelector.setAdapter(new ElementAdapter<>(getActivity(), options, false, Race.class));
        raceSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (mViewModel.getAvailableRaces().get(position).getId().equals(Element.DEFAULT_NULL_ID)) {
                    CharacterManager.setRace(null);
                } else {
                    if (position > 0) {
                        CharacterManager.setRace(mViewModel.getAvailableRaces().get(position - 1));
                    } else {
                        CharacterManager.setRace(null);
                    }
                    updateCounters(CharacterManager.getSelectedCharacter());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                try {
                    CharacterManager.getSelectedCharacter().setRace(null);
                } catch (InvalidRaceException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
                updateCounters(CharacterManager.getSelectedCharacter());
            }
        });
    }

    private void createFactionSpinner(View root) {
        final ElementSpinner factionsSelector = root.findViewById(R.id.character_faction);
        List<Faction> options = new ArrayList<>(mViewModel.getAvailableFactions());
        options.add(0, null);
        factionsSelector.setAdapter(new ElementAdapter<>(getActivity(), options, false, Faction.class));
        factionsSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (mViewModel.getAvailableFactions().get(position).getId().equals(Element.DEFAULT_NULL_ID)) {
                    CharacterManager.setFaction(null);
                } else {
                    if (position > 0) {
                        CharacterManager.setFaction(mViewModel.getAvailableFactions().get(position - 1));
                    } else {
                        CharacterManager.setFaction(null);
                    }
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
        List<Planet> options = new ArrayList<>(mViewModel.getAvailablePlanets());
        options.add(0, null);
        planetSelector.setAdapter(new ElementAdapter<>(getActivity(), options, false, Planet.class));
        planetSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (mViewModel.getAvailablePlanets().get(position).getId().equals(Element.DEFAULT_NULL_ID)) {
                    CharacterManager.setPlanet(null);
                } else {
                    if (position > 0) {
                        CharacterManager.setPlanet(mViewModel.getAvailablePlanets().get(position - 1));
                    } else {
                        CharacterManager.setPlanet(null);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                CharacterManager.getSelectedCharacter().getInfo().setPlanet(null);
            }
        });
    }


}
