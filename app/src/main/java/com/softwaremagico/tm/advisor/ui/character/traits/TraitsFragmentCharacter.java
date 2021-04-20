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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.IncrementalElementsLayout;
import com.softwaremagico.tm.advisor.ui.components.counters.TraitsCounter;
import com.softwaremagico.tm.advisor.ui.components.counters.TraitsExtraCounter;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.benefices.BeneficeGroup;
import com.softwaremagico.tm.character.benefices.InvalidBeneficeException;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.BlessingGroup;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TraitsFragmentCharacter extends CharacterCustomFragment {
    private TraitsViewModel mViewModel;

    private TraitsCounter traitsCounter;
    private TraitsExtraCounter extraCounter;

    private IncrementalElementsLayout<Blessing> blessingsLayout;
    private IncrementalElementsLayout<AvailableBenefice> beneficesLayout;

    private View root;

    public static TraitsFragmentCharacter newInstance(int index) {
        final TraitsFragmentCharacter fragment = new TraitsFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setCharacter(View root, CharacterPlayer character) {
        if (blessingsLayout != null) {
            blessingsLayout.setElements(character.getSelectedBlessings(), character.getDefaultBlessings());
        }
        if (beneficesLayout != null) {
            beneficesLayout.setElements(character.getSelectedBenefices(), character.getDefaultBenefices());
        }
        if (traitsCounter != null) {
            traitsCounter.setCharacter(character);
        }
        if (extraCounter != null) {
            extraCounter.setCharacter(character);
        }
    }

    @Override
    protected void initData() {
        final LinearLayout rootLayout = root.findViewById(R.id.root_container);

        addSection(ThinkMachineTranslator.getTranslatedText("blessingTable"), rootLayout);
        blessingsLayout = new BlessingLayout(getContext(), true);
        rootLayout.addView(blessingsLayout);
        addSpace(rootLayout);

        addSection(ThinkMachineTranslator.getTranslatedText("beneficesTable"), rootLayout);
        beneficesLayout = new BeneficesLayout(getContext(), true);
        rootLayout.addView(beneficesLayout);

        setCharacter(root, CharacterManager.getSelectedCharacter());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_traits_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(TraitsViewModel.class);

        traitsCounter = root.findViewById(R.id.traits_counter);
        extraCounter = root.findViewById(R.id.extra_counter);

        CharacterManager.addCharacterRaceUpdatedListener(characterPlayer -> setCharacter(root, characterPlayer));
        CharacterManager.addCharacterFactionUpdatedListener(characterPlayer -> setCharacter(root, characterPlayer));
        CharacterManager.addCharacterAgeUpdatedListener(characterPlayer -> setCharacter(root, characterPlayer));

        return root;
    }


    class BlessingLayout extends IncrementalElementsLayout<Blessing> {
        private static final int MAX_BENEFICES = 7;

        public BlessingLayout(Context context, boolean nullAllowed) {
            super(context, nullAllowed, MAX_BENEFICES);

            setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (removeDuplicates()) {
                        SnackbarGenerator.getInfoMessage(root, R.string.message_duplicated_item_removed).show();
                    }
                    setBlessings(getElementSpinners());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    removeDuplicates();
                    setBlessings(getElementSpinners());
                }
            });
        }

        @Override
        protected ElementAdapter<Blessing> createElementAdapter() {
            return new ElementAdapter<Blessing>(getActivity(), mViewModel.getAvailableBlessings(), isNullAllowed(), Blessing.class) {

                @Override
                public String getElementRepresentation(Blessing element) {
                    if (element.getId().equals(Element.DEFAULT_NULL_ID)) {
                        return "";
                    }
                    return element.getName() + " (" + element.getCost() + ")";
                }

                @Override
                public boolean isEnabled(int position) {
                    return getItem(position).getBlessingGroup() != BlessingGroup.RESTRICTED;
                }
            };
        }

        private void setBlessings(List<ElementSpinner<Blessing>> spinners) {
            final List<Blessing> blessings = new ArrayList<>();
            for (final ElementSpinner<Blessing> spinner : spinners) {
                if (spinner.getSelection() != null) {
                    blessings.add(spinner.getSelection());
                }
            }
            try {
                CharacterManager.getSelectedCharacter().setBlessings(blessings);
            } catch (TooManyBlessingsException e) {
                SnackbarGenerator.getErrorMessage(this, R.string.message_too_many_blessings).show();
            }
        }
    }

    class BeneficesLayout extends IncrementalElementsLayout<AvailableBenefice> {

        public BeneficesLayout(Context context, boolean nullAllowed) {
            super(context, nullAllowed);

            setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (removeDuplicates()) {
                        SnackbarGenerator.getInfoMessage(root, R.string.message_duplicated_item_removed).show();
                    }
                    setBenefice(getElementSpinners());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    removeDuplicates();
                    setBenefice(getElementSpinners());
                }
            });
        }

        @Override
        protected ElementAdapter<AvailableBenefice> createElementAdapter() {
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

                @Override
                public boolean isEnabled(int position) {
                    return (getItem(position).getBeneficeDefinition() == null ||
                            getItem(position).getBeneficeDefinition().getRestrictedFactionGroup() == null ||
                            (CharacterManager.getSelectedCharacter().getFaction() != null &&
                                    getItem(position).getBeneficeDefinition().getRestrictedFactionGroup() ==
                                            CharacterManager.getSelectedCharacter().getFaction().getFactionGroup())) &&
                            (getItem(position).getBeneficeDefinition() == null ||
                                    !Objects.equals(getItem(position).getBeneficeDefinition().getGroup(), BeneficeGroup.RESTRICTED));
                }
            };
        }

        private void setBenefice(List<ElementSpinner<AvailableBenefice>> spinners) {
            final List<AvailableBenefice> benefices = new ArrayList<>();
            for (final ElementSpinner<AvailableBenefice> spinner : spinners) {
                if (spinner.getSelection() != null) {
                    benefices.add(spinner.getSelection());
                }
            }
            try {
                CharacterManager.getSelectedCharacter().setBenefices(benefices);
            } catch (InvalidBeneficeException e) {
                SnackbarGenerator.getErrorMessage(this, R.string.message_invalid_benefice).show();
            }
        }

        @Override
        protected boolean removeDuplicates() {
            int i = 0;
            Set<BeneficeDefinition> selections = new HashSet<>();
            boolean removed = false;
            while (i < getElementSpinners().size()) {
                if ((getElementSpinners().get(i)).getSelection() != null) {
                    if (!selections.contains(((getElementSpinners().get(i)).getSelection()).getBeneficeDefinition())) {
                        selections.add(((getElementSpinners().get(i)).getSelection()).getBeneficeDefinition());
                    } else {
                        removeView(getElementSpinners().get(i));
                        getElementSpinners().remove(i);
                        removed = true;
                    }
                }
                i++;
            }
            if (removed) {
                updateContent();
            }
            return removed;
        }
    }

}
