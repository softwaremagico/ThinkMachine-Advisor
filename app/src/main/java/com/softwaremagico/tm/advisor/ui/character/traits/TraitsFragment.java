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

package com.softwaremagico.tm.advisor.ui.character.traits;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.advisor.CharacterManager;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.CustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.IncrementalElementsLayout;
import com.softwaremagico.tm.advisor.ui.components.TranslatedNumberPicker;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.InvalidBeneficeException;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.skills.AvailableSkill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraitsFragment extends CustomFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Map<AvailableSkill, TranslatedNumberPicker> translatedNumberPickers = new HashMap<>();
    private TraitsViewModel mViewModel;

    public static TraitsFragment newInstance(int index) {
        TraitsFragment fragment = new TraitsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.character_traits_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(TraitsViewModel.class);
        LinearLayout rootLayout = rootView.findViewById(R.id.root_container);

        addSection(ThinkMachineTranslator.getTranslatedText("blessingTable"), rootLayout);
        final IncrementalElementsLayout blessingsLayout = new BlessingLayout(getContext());
        rootLayout.addView(blessingsLayout);


        addSection(ThinkMachineTranslator.getTranslatedText("beneficesTable"), rootLayout);
        IncrementalElementsLayout beneficesLayout = new BeneficesLayout(getContext());
        rootLayout.addView(beneficesLayout);

        return rootView;
    }


    class BlessingLayout extends IncrementalElementsLayout {

        public BlessingLayout(Context context) {
            super(context);
        }

        @Override
        public ElementSpinner createElementSpinner() {
            ElementSpinner blessingSelector = new ElementSpinner(getContext());
            blessingSelector.setAdapter(new ElementAdapter<>(getActivity(), mViewModel.getAvailableBlessings(), true, Blessing.class));
            blessingSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    setBlessings(getElements());
                }


                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    setBlessings(getElements());
                }
            });
            return blessingSelector;
        }

        private void setBlessings(List<ElementSpinner> spinners) {
            List<Blessing> blessings = new ArrayList<>();
            for (ElementSpinner spinner : spinners) {
                blessings.add(spinner.getSelection());
            }
            try {
                CharacterManager.getSelectedCharacter().setBlessings(blessings);
            } catch (TooManyBlessingsException e) {

            }
        }
    }

    class BeneficesLayout extends IncrementalElementsLayout {

        public BeneficesLayout(Context context) {
            super(context);
        }

        @Override
        public ElementSpinner createElementSpinner() {
            ElementSpinner beneficesSelector = new ElementSpinner(getContext());
            beneficesSelector.setAdapter(new ElementAdapter<>(getActivity(), mViewModel.getAvailableBenefices(), true, AvailableBenefice.class));
            beneficesSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    setBenefice(getElements());
                }


                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    setBenefice(getElements());
                }
            });
            return beneficesSelector;
        }

        private void setBenefice(List<ElementSpinner> spinners) {
            List<AvailableBenefice> benefices = new ArrayList<>();
            for (ElementSpinner spinner : spinners) {
                benefices.add(spinner.getSelection());
            }
            try {
                CharacterManager.getSelectedCharacter().setBenefices(benefices);
            } catch (InvalidBeneficeException e) {

            }
        }
    }

}
