package com.softwaremagico.tm.advisor.ui.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.log.MachineLog;

import java.io.IOException;
import java.io.InputStream;

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

       Spinner factionsSelector = (Spinner)root.findViewById(R.id.characterFaction);
       mViewModel = ViewModelProviders.of(this).get(CharacterInfoViewModel.class);

       factionsSelector.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mViewModel.getAvailableFactions()));

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CharacterInfoViewModel.class);
        // TODO: Use the ViewModel
    }

}
