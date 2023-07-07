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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.EnumSpinner;
import com.softwaremagico.tm.advisor.ui.components.TranslatedEditText;
import com.softwaremagico.tm.advisor.ui.components.counters.CharacteristicsCounter;
import com.softwaremagico.tm.advisor.ui.components.counters.ExtraCounter;
import com.softwaremagico.tm.advisor.ui.components.counters.FirebirdsCounter;
import com.softwaremagico.tm.advisor.ui.components.counters.SkillsCounter;
import com.softwaremagico.tm.advisor.ui.components.counters.TraitsCounter;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.EnumAdapter;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.RandomName;
import com.softwaremagico.tm.character.RandomSurname;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.exceptions.RestrictedElementException;
import com.softwaremagico.tm.character.exceptions.UnofficialCharacterException;
import com.softwaremagico.tm.character.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.InvalidFactionException;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.races.InvalidRaceException;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CharacterInfoFragmentCharacter extends CharacterCustomFragment {
    private CharacterInfoViewModel mViewModel;
    private CharacteristicsCounter characteristicsCounter;
    private SkillsCounter skillsCounter;
    private TraitsCounter traitsCounter;
    private ExtraCounter extraCounter;
    private FirebirdsCounter firebirdsCounter;
    private View root;
    private SwitchCompat nonOfficialEnabled;
    private SwitchCompat restrictionsIgnored;
    private ElementSpinner<Race> raceSelector;
    private ElementSpinner<Faction> factionsSelector;
    private ElementSpinner<Planet> planetSelector;
    private boolean updatingCharacter = false;

    public static CharacterInfoFragmentCharacter newInstance(int index) {
        final CharacterInfoFragmentCharacter fragment = new CharacterInfoFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {
        updateTranslatedTextField(root, R.id.character_name, value -> {
            if (!updatingCharacter) {
                CharacterManager.getSelectedCharacter().getInfo().setNames(value);
            }
        });
        updateTranslatedTextField(root, R.id.character_surname, value -> {
            if (!updatingCharacter) {
                CharacterManager.getSelectedCharacter().getInfo().setSurname(value);
            }
        });
        updateTranslatedTextField(root, R.id.character_age, value -> {
            try {
                if (!Objects.equals(CharacterManager.getSelectedCharacter().getInfo().getAge() + "", value)) {
                    CharacterManager.getSelectedCharacter().getInfo().setAge(Integer.parseInt(value));
                    //Force to update all costs.
                    if (!updatingCharacter) {
                        CharacterManager.launchCharacterAgeUpdatedListeners(CharacterManager.getSelectedCharacter());
                    }
                }
            } catch (NumberFormatException e) {
                CharacterManager.getSelectedCharacter().getInfo().setAge(null);
                CharacterManager.launchCharacterAgeUpdatedListeners(CharacterManager.getSelectedCharacter());
            }

        });
        createGenderSpinner(root);
        createRaceSpinner(true);
        createFactionSpinner(true);
        createPlanetSpinner(true);

        setCharacter(root, CharacterManager.getSelectedCharacter());

        ImageView randomNameButton = root.findViewById(R.id.button_random_name);
        if (randomNameButton != null) {
            randomNameButton.setOnClickListener(v -> {
                updatingCharacter = true;
                CharacterManager.getSelectedCharacter().getInfo().setNames(new ArrayList<>());
                final RandomName randomName;
                try {
                    randomName = new RandomName(CharacterManager.getSelectedCharacter(), null);
                    randomName.assign();
                    final TranslatedEditText nameTextEditor = root.findViewById(R.id.character_name);
                    nameTextEditor.setText(CharacterManager.getSelectedCharacter().getInfo().getNameRepresentation());
                } catch (InvalidXmlElementException | InvalidRandomElementSelectedException |
                         RestrictedElementException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.selectFactionAndMore).show();
                } catch (UnofficialElementNotAllowedException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.message_unofficial_element_not_allowed).show();
                }
                updatingCharacter = false;
            });
        }

        ImageView randomSurnameButton = root.findViewById(R.id.button_random_surname);
        if (randomSurnameButton != null) {
            randomSurnameButton.setOnClickListener(v -> {
                updatingCharacter = true;
                CharacterManager.getSelectedCharacter().getInfo().setSurname((Surname) null);
                final RandomSurname randomSurname;
                try {
                    randomSurname = new RandomSurname(CharacterManager.getSelectedCharacter(), null);
                    randomSurname.assign();
                    final TranslatedEditText surnameTextEditor = root.findViewById(R.id.character_surname);
                    if (CharacterManager.getSelectedCharacter().getInfo().getSurname() != null) {
                        surnameTextEditor.setText(CharacterManager.getSelectedCharacter().getInfo().getSurname().getName());
                    } else {
                        surnameTextEditor.setText("");
                    }
                } catch (InvalidXmlElementException | InvalidRandomElementSelectedException |
                         RestrictedElementException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.selectFactionAndMore).show();
                } catch (UnofficialElementNotAllowedException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.message_unofficial_element_not_allowed).show();
                }
                updatingCharacter = false;
            });
        }


        nonOfficialEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                try {
                    CharacterManager.getSelectedCharacter().checkIsOfficial();
                    CharacterManager.getSelectedCharacter().getSettings().setOnlyOfficialAllowed(!isChecked);
                    CharacterManager.updateSettings();
                } catch (UnofficialCharacterException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.message_setting_unofficial_not_changed).show();
                    CharacterManager.getSelectedCharacter().getSettings().setOnlyOfficialAllowed(false);
                    nonOfficialEnabled.setChecked(true);
                }
            } else {
                CharacterManager.getSelectedCharacter().getSettings().setOnlyOfficialAllowed(!isChecked);
            }
        });

        restrictionsIgnored.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                try {
                    CharacterManager.getSelectedCharacter().checkIsNotRestricted();
                    CharacterManager.getSelectedCharacter().getSettings().setRestrictionsChecked(!isChecked);
                    CharacterManager.updateSettings();
                } catch (RestrictedElementException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.message_setting_restriction_not_changed).show();
                    CharacterManager.getSelectedCharacter().getSettings().setRestrictionsChecked(false);
                    restrictionsIgnored.setChecked(true);
                }
            } else {
                CharacterManager.getSelectedCharacter().getSettings().setRestrictionsChecked(!isChecked);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_info_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CharacterInfoViewModel.class);

        raceSelector = root.findViewById(R.id.character_race);
        factionsSelector = root.findViewById(R.id.character_faction);
        planetSelector = root.findViewById(R.id.character_planet);

        characteristicsCounter = root.findViewById(R.id.characteristics_counter);
        skillsCounter = root.findViewById(R.id.skills_counter);
        traitsCounter = root.findViewById(R.id.traits_counter);
        extraCounter = root.findViewById(R.id.extra_counter);
        firebirdsCounter = root.findViewById(R.id.firebirds_counter);
        firebirdsCounter.setUnitHidden(true);

        nonOfficialEnabled = root.findViewById(R.id.official_selector);
        restrictionsIgnored = root.findViewById(R.id.restricted_selector);

        CharacterManager.addCharacterRaceUpdatedListener(this::updateCounters);
        CharacterManager.addCharacterAgeUpdatedListener(this::updateCounters);
        CharacterManager.addCharacterSettingsUpdateListeners(this::updateSettings);
        CharacterManager.addSelectedCharacterListener(characterPlayer -> setCharacter(root, characterPlayer));

        return root;
    }

    @Override
    protected void updateSettings(CharacterPlayer characterPlayer) {
        if (getContext() != null) {
            //Avoid to set a different value when changing the ElementAdapter.
            raceSelector.setOnItemSelectedListener(null);
            factionsSelector.setOnItemSelectedListener(null);
            planetSelector.setOnItemSelectedListener(null);

            //Storing old selected value.
            final Race selectedRace = raceSelector.getSelection();
            final Faction selectedFaction = factionsSelector.getSelection();
            final Planet selectedPlanet = planetSelector.getSelection();

            //Create new adapter with the new settings.
            createRaceSpinner(!characterPlayer.getSettings().isOnlyOfficialAllowed());
            createFactionSpinner(!characterPlayer.getSettings().isOnlyOfficialAllowed());
            createPlanetSpinner(!characterPlayer.getSettings().isOnlyOfficialAllowed());

            //Recovering old selected value.
            raceSelector.setSelection(selectedRace);
            factionsSelector.setSelection(selectedFaction);
            planetSelector.setSelection(selectedPlanet);
        }
    }

    @Override
    public void setCharacter(View root, CharacterPlayer character) {
        updatingCharacter = true;
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

        nonOfficialEnabled.setChecked(!character.getSettings().isOnlyOfficialAllowed());
        restrictionsIgnored.setChecked(!character.getSettings().isRestrictionsChecked());

        raceSelector.setSelection(CharacterManager.getSelectedCharacter().getRace());
        factionsSelector.setSelection(CharacterManager.getSelectedCharacter().getFaction());
        planetSelector.setSelection(CharacterManager.getSelectedCharacter().getInfo().getPlanet());

        updateCounters(character);

        updateSettings(character);

        updatingCharacter = false;
    }

    private void updateCounters(CharacterPlayer character) {
        characteristicsCounter.setCharacter(character);
        extraCounter.setCharacter(character);
        skillsCounter.setCharacter(character);
        traitsCounter.setCharacter(character);
        firebirdsCounter.setCharacter(character);
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

    private void createRaceSpinner(boolean nonOfficial) {
        List<Race> options = new ArrayList<>(mViewModel.getAvailableRaces(nonOfficial));
        options.add(0, null);
        raceSelector.setAdapter(new ElementAdapter<Race>(getActivity(), options, false, Race.class) {
            @Override
            public boolean isEnabled(int position) {
                //Faction limitations
                return getItem(position) == null || !CharacterManager.getSelectedCharacter().getSettings().isRestrictionsChecked() ||
                        ((CharacterManager.getSelectedCharacter().getFaction() == null ||
                                CharacterManager.getSelectedCharacter().getFaction().getRestrictedToRaces() == null ||
                                CharacterManager.getSelectedCharacter().getFaction().getRestrictedToRaces().contains(getItem(position))) &&

                                //Planet limitations
                                getItem(position).getPlanets().isEmpty() || CharacterManager.getSelectedCharacter().getInfo().getPlanet() == null ||
                                getItem(position).getPlanets().contains(CharacterManager.getSelectedCharacter().getInfo().getPlanet()));
            }
        });
        raceSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (position == 0 || mViewModel.getAvailableRaces(nonOfficial).get(position - 1).getId().equals(Element.DEFAULT_NULL_ID)) {
                        CharacterManager.setRace(null);
                    } else {
                        if (position > 0) {
                            CharacterManager.setRace(mViewModel.getAvailableRaces(nonOfficial).get(position - 1));
                        } else {
                            CharacterManager.setRace(null);
                        }
                        if (!updatingCharacter) {
                            updateCounters(CharacterManager.getSelectedCharacter());
                        }
                    }
                } catch (InvalidRaceException | RestrictedElementException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.invalidFactionAndRace).show();
                    raceSelector.setSelection(null);
                } catch (UnofficialElementNotAllowedException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.message_unofficial_element_not_allowed).show();
                    raceSelector.setSelection(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                try {
                    CharacterManager.getSelectedCharacter().setRace(null);
                } catch (InvalidRaceException | RestrictedElementException |
                         UnofficialElementNotAllowedException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
                updateCounters(CharacterManager.getSelectedCharacter());
            }
        });
    }

    private void createFactionSpinner(boolean nonOfficial) {
        List<Faction> options = new ArrayList<>(mViewModel.getAvailableFactions(nonOfficial));
        options.add(0, null);
        factionsSelector.setAdapter(new ElementAdapter<Faction>(getActivity(), options, false, Faction.class) {
            @Override
            public boolean isEnabled(int position) {
                return !CharacterManager.getSelectedCharacter().getSettings().isRestrictionsChecked() || CharacterManager.getSelectedCharacter().getRace() == null ||
                        getItem(position) == null || getItem(position).getRestrictedToRaces() == null ||
                        getItem(position).getRestrictedToRaces().contains(CharacterManager.getSelectedCharacter().getRace());
            }
        });
        factionsSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (position == 0 || mViewModel.getAvailableFactions(nonOfficial).get(position - 1).getId().equals(Element.DEFAULT_NULL_ID)) {
                        try {
                            CharacterManager.setFaction(null);
                        } catch (InvalidFactionException e) {
                            //Nothing
                        }
                    } else {
                        if (position > 0) {
                            CharacterManager.setFaction(mViewModel.getAvailableFactions(nonOfficial).get(position - 1));
                        } else {
                            CharacterManager.setFaction(null);
                        }
                    }
                } catch (InvalidFactionException | RestrictedElementException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.invalidFactionAndRace).show();
                    factionsSelector.setSelection(null);
                } catch (UnofficialElementNotAllowedException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.message_unofficial_element_not_allowed).show();
                    factionsSelector.setSelection(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                try {
                    CharacterManager.getSelectedCharacter().setFaction(null);
                } catch (InvalidFactionException | RestrictedElementException |
                         UnofficialElementNotAllowedException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
            }
        });
    }

    private void createPlanetSpinner(boolean nonOfficial) {
        List<Planet> options = new ArrayList<>(mViewModel.getAvailablePlanets(nonOfficial));
        options.add(0, null);
        planetSelector.setAdapter(new ElementAdapter<Planet>(getActivity(), options, false, Planet.class) {
            @Override
            public boolean isEnabled(int position) {
                return !CharacterManager.getSelectedCharacter().getSettings().isRestrictionsChecked() ||
                        CharacterManager.getSelectedCharacter().getRace() == null ||
                        CharacterManager.getSelectedCharacter().getRace().getPlanets().isEmpty() ||
                        CharacterManager.getSelectedCharacter().getRace().getPlanets().contains(getItem(position));
            }
        });
        planetSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0 || mViewModel.getAvailablePlanets(nonOfficial).get(position - 1).getId().equals(Element.DEFAULT_NULL_ID)) {
                    CharacterManager.setPlanet(null);
                } else {
                    if (position > 0) {
                        CharacterManager.setPlanet(mViewModel.getAvailablePlanets(nonOfficial).get(position - 1));
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
