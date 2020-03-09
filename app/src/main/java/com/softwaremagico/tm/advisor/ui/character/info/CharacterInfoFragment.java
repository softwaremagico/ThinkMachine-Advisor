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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.advisor.CharacterManager;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.EnumAdapter;
import com.softwaremagico.tm.advisor.ui.components.EnumSpinner;
import com.softwaremagico.tm.advisor.ui.components.TranslatedEditText;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.InvalidFactionException;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.races.InvalidRaceException;
import com.softwaremagico.tm.character.races.Race;

public class CharacterInfoFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private CharacterInfoViewModel mViewModel;

    public static CharacterInfoFragment newInstance(int index) {
        CharacterInfoFragment fragment = new CharacterInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.character_info_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CharacterInfoViewModel.class);

        createNameText(root);
        createSurnameText(root);
        createGenderSpinner(root);
        createAgeText(root);
        createRaceSpinner(root);
        createFactionSpinner(root);
        createPlanetSpinner(root);

        return root;
    }

    private void createNameText(View root) {
        final TranslatedEditText nameTextEditor = (TranslatedEditText) root.findViewById(R.id.character_name);
        nameTextEditor.setText(CharacterManager.getSelectedCharacter().getInfo().getNameRepresentation());
        nameTextEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CharacterManager.getSelectedCharacter().getInfo().setNames(nameTextEditor.getText());
            }
        });
    }

    private void createSurnameText(View root) {
        final TranslatedEditText surnameTextEditor = (TranslatedEditText) root.findViewById(R.id.character_surname);
        if (CharacterManager.getSelectedCharacter().getInfo().getSurname() != null) {
            surnameTextEditor.setText(CharacterManager.getSelectedCharacter().getInfo().getSurname().getName());
        }
        surnameTextEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CharacterManager.getSelectedCharacter().getInfo().setSurname(surnameTextEditor.getText());
            }
        });
    }

    private void createGenderSpinner(View root) {
        final EnumSpinner genderSelector = (EnumSpinner) root.findViewById(R.id.character_gender);
        genderSelector.setAdapter(new EnumAdapter<Gender>(getActivity(), android.R.layout.simple_spinner_item, mViewModel.getAvailableGenders()));
        genderSelector.setSelection(CharacterManager.getSelectedCharacter().getInfo().getGender());
        genderSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                CharacterManager.getSelectedCharacter().getInfo().setGender(mViewModel.getAvailableGenders().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void createAgeText(View root) {
        final TranslatedEditText ageTextEditor = (TranslatedEditText) root.findViewById(R.id.character_age);
        ageTextEditor.setAsNumberEditor();
        if (CharacterManager.getSelectedCharacter().getInfo().getAge() != null) {
            ageTextEditor.setText(CharacterManager.getSelectedCharacter().getInfo().getAge().toString());
        }
        ageTextEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    CharacterManager.getSelectedCharacter().getInfo().setAge(Integer.parseInt(ageTextEditor.getText()));
                } catch (NumberFormatException e) {

                }
            }
        });

    }

    private void createRaceSpinner(View root) {
        ElementSpinner raceSelector = (ElementSpinner) root.findViewById(R.id.character_race);
        raceSelector.setAdapter(new ElementAdapter<Race>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mViewModel.getAvailableRaces()));
        raceSelector.setSelection(CharacterManager.getSelectedCharacter().getRace());
        raceSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    CharacterManager.getSelectedCharacter().setRace(mViewModel.getAvailableRaces().get(position));
//                    Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.characteristics_frame);
//                    System.out.println("111111111111111" + currentFragment);
//                    if (currentFragment instanceof CharacteristicsFragment) {
//                        System.out.println("222222");
//                        ((CharacteristicsFragment) currentFragment).updateCharacteristicsLimits();
//                    }
                } catch (InvalidRaceException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
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
        ElementSpinner factionsSelector = (ElementSpinner) root.findViewById(R.id.character_faction);
        factionsSelector.setAdapter(new ElementAdapter<Faction>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mViewModel.getAvailableFactions()));
        factionsSelector.setSelection(CharacterManager.getSelectedCharacter().getFaction());
        factionsSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    CharacterManager.getSelectedCharacter().setFaction(mViewModel.getAvailableFactions().get(position));
                } catch (InvalidFactionException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    private void createPlanetSpinner(View root) {
        ElementSpinner planetSelector = (ElementSpinner) root.findViewById(R.id.character_planet);
        planetSelector.setAdapter(new ElementAdapter<Planet>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mViewModel.getAvailablePlanets()));
        planetSelector.setSelection(CharacterManager.getSelectedCharacter().getInfo().getPlanet());
        planetSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                CharacterManager.getSelectedCharacter().getInfo().setPlanet(mViewModel.getAvailablePlanets().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }


}
