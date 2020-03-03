package com.softwaremagico.tm.advisor.ui.main.info;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.softwaremagico.tm.advisor.CharacterManager;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.TranslatedEditText;
import com.softwaremagico.tm.character.factions.Faction;
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

        TranslatedEditText nameTextEditor = (TranslatedEditText) root.findViewById(R.id.character_name);
        TranslatedEditText ageTextEditor = (TranslatedEditText) root.findViewById(R.id.character_age);
        ageTextEditor.setAsNumberEditor();


        createRaceSpinner(root);
        createFactionSpinner(root);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CharacterInfoViewModel.class);
    }

    private void createRaceSpinner(View root) {
        ElementSpinner raceSelector = (ElementSpinner) root.findViewById(R.id.character_race);
        raceSelector.setAdapter(new ElementAdapter<Race>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mViewModel.getAvailableRaces()));
        raceSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    CharacterManager.getSelectedCharacter().setRace(mViewModel.getAvailableRaces().get(position));
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
        factionsSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                CharacterManager.getSelectedCharacter().setFaction(mViewModel.getAvailableFactions().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                CharacterManager.getSelectedCharacter().setFaction(null);
            }
        });
    }


}
