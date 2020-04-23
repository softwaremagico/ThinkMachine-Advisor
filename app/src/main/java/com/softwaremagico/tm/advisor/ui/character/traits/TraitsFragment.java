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

import com.google.android.material.snackbar.Snackbar;
import com.softwaremagico.tm.Element;
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
        final IncrementalElementsLayout blessingsLayout = new BlessingLayout(getContext(), true);
        blessingsLayout.setElements(CharacterManager.getSelectedCharacter().getSelectedBlessings());
        rootLayout.addView(blessingsLayout);

        addSection(ThinkMachineTranslator.getTranslatedText("beneficesTable"), rootLayout);
        IncrementalElementsLayout beneficesLayout = new BeneficesLayout(getContext(), true);
        beneficesLayout.setElements(CharacterManager.getSelectedCharacter().getSelectedBenefices());
        rootLayout.addView(beneficesLayout);

        return rootView;
    }


    class BlessingLayout extends IncrementalElementsLayout {
        private final static int MAX_BENEFICES = 7;

        public BlessingLayout(Context context, boolean nullAllowed) {
            super(context, nullAllowed, MAX_BENEFICES);

            setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setBlessings(getElementSpinners());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    setBlessings(getElementSpinners());
                }
            });
        }

        @Override
        protected ElementAdapter createElementAdapter() {
            return new ElementAdapter<Blessing>(getActivity(), mViewModel.getAvailableBlessings(), isNullAllowed(), Blessing.class) {

                @Override
                public String getElementRepresentation(Blessing element) {
                    if (element.getId().equals(Element.DEFAULT_NULL_ID)) {
                        return "";
                    }
                    return element.getName() + " (" + element.getCost() + ")";
                }
            };
        }

        private void setBlessings(List<ElementSpinner> spinners) {
            List<Blessing> blessings = new ArrayList<>();
            for (ElementSpinner spinner : spinners) {
                if (spinner.getSelection() != null) {
                    blessings.add(spinner.getSelection());
                }
            }
            try {
                CharacterManager.getSelectedCharacter().setBlessings(blessings);
            } catch (TooManyBlessingsException e) {
                Snackbar snackbar = Snackbar
                        .make(this, R.string.message_too_many_blessings, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
    }

    class BeneficesLayout extends IncrementalElementsLayout {

        public BeneficesLayout(Context context, boolean nullAllowed) {
            super(context, nullAllowed);

            setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setBenefice(getElementSpinners());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    setBenefice(getElementSpinners());
                }
            });
        }

        @Override
        protected ElementAdapter createElementAdapter() {
            return new ElementAdapter<AvailableBenefice>(getActivity(), mViewModel.getAvailableBenefices(), isNullAllowed(), AvailableBenefice.class) {

                @Override
                public String getElementRepresentation(AvailableBenefice element) {
                    if (element.getId().equals(Element.DEFAULT_NULL_ID)) {
                        return "";
                    }
                    if (element.getSpecialization() == null) {
                        return element.getName() + " (" + element.getCost() + ")";
                    }
                    return element.getName() + " [" + element.getSpecialization().getName() + "]" + " (" + element.getCost() + ")";
                }
            };
        }

        private void setBenefice(List<ElementSpinner> spinners) {
            List<AvailableBenefice> benefices = new ArrayList<>();
            for (ElementSpinner spinner : spinners) {
                if (spinner.getSelection() != null) {
                    benefices.add(spinner.getSelection());
                }
            }
            try {
                CharacterManager.getSelectedCharacter().setBenefices(benefices);
            } catch (InvalidBeneficeException e) {
                Snackbar snackbar = Snackbar
                        .make(this, R.string.message_invalid_benefice, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
    }

}
