package com.softwaremagico.tm.advisor.ui.main;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.TranslatedEditText;
import com.softwaremagico.tm.advisor.ui.components.TranslatedTextView;
import com.softwaremagico.tm.character.factions.Faction;

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

        TranslatedEditText nameTextEditor = (TranslatedEditText) root.findViewById(R.id.characterName);

        ElementSpinner factionsSelector = (ElementSpinner) root.findViewById(R.id.characterFaction);
        factionsSelector.setAdapter(new ElementAdapter<Faction>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mViewModel.getAvailableFactions()));

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CharacterInfoViewModel.class);
    }



}
