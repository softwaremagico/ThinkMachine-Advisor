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

package com.softwaremagico.tm.advisor.ui.character.equipment;

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
import com.softwaremagico.tm.advisor.CharacterManager;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.IncrementalElementsLayout;
import com.softwaremagico.tm.advisor.ui.components.TranslatedEditText;
import com.softwaremagico.tm.advisor.ui.components.TranslatedNumberPicker;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.armours.InvalidArmourException;
import com.softwaremagico.tm.character.equipment.shields.InvalidShieldException;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.skills.AvailableSkill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipmentFragment extends CustomFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Map<AvailableSkill, TranslatedNumberPicker> translatedNumberPickers = new HashMap<>();
    private EquipmentViewModel mViewModel;

    IncrementalElementsLayout weaponsLayout;
    IncrementalElementsLayout armoursLayout;
    IncrementalElementsLayout shieldsLayout;


    public static EquipmentFragment newInstance(int index) {
        EquipmentFragment fragment = new EquipmentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setCharacter(View root, CharacterPlayer character) {
        weaponsLayout.setElements(CharacterManager.getSelectedCharacter().getSelectedWeapons());
        armoursLayout.setElement(CharacterManager.getSelectedCharacter().getSelectedArmour());
        shieldsLayout.setElement(CharacterManager.getSelectedCharacter().getSelectedShield());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.character_equipment_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(EquipmentViewModel.class);
        LinearLayout rootLayout = rootView.findViewById(R.id.root_container);

        addSection(ThinkMachineTranslator.getTranslatedText("weapons"), rootLayout);
        weaponsLayout = new WeaponsLayout(getContext(), true);
        rootLayout.addView(weaponsLayout);

        addSection(ThinkMachineTranslator.getTranslatedText("armor"), rootLayout);
        armoursLayout = new ArmourLayout(getContext(), true);
        rootLayout.addView(armoursLayout);

        addSection(ThinkMachineTranslator.getTranslatedText("shield"), rootLayout);
        shieldsLayout = new ShieldLayout(getContext(), true);
        rootLayout.addView(shieldsLayout);

        setCharacter(rootView, CharacterManager.getSelectedCharacter());

        return rootView;
    }

    class ShieldLayout extends IncrementalElementsLayout {
        private final static int MAX_ITEMS = 1;

        public ShieldLayout(Context context, boolean nullsAllowed) {
            super(context, nullsAllowed, MAX_ITEMS);

            setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setShield(getElementSpinners());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    setShield(getElementSpinners());
                }
            });
        }

        @Override
        protected ElementAdapter createElementAdapter() {
            return new ElementAdapter<>(getActivity(), mViewModel.getAvailableShields(), isNullAllowed(), Shield.class);
        }

        private void setShield(List<ElementSpinner> spinners) {
            List<Shield> shields = new ArrayList<>();
            for (ElementSpinner spinner : spinners) {
                if (spinner.getSelection() != null) {
                    shields.add(spinner.getSelection());
                }
            }
            if (!shields.isEmpty()) {
                try {
                    CharacterManager.getSelectedCharacter().setShield(shields.get(0));
                } catch (InvalidShieldException e) {
                    Snackbar
                            .make(this, R.string.message_invalid_shield_armour_combination, Snackbar.LENGTH_SHORT).show();
                }
            } else {
                try {
                    CharacterManager.getSelectedCharacter().setShield(null);
                } catch (InvalidShieldException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
            }
        }
    }


    class ArmourLayout extends IncrementalElementsLayout {
        private final static int MAX_ITEMS = 1;

        public ArmourLayout(Context context, boolean nullsAllowed) {
            super(context, nullsAllowed, MAX_ITEMS);

            setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setArmour(getElementSpinners());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    setArmour(getElementSpinners());
                }
            });
        }

        @Override
        protected ElementAdapter createElementAdapter() {
            return new ElementAdapter<>(getActivity(), mViewModel.getAvailableArmours(), isNullAllowed(), Armour.class);
        }

        private void setArmour(List<ElementSpinner> spinners) {
            List<Armour> armours = new ArrayList<>();
            for (ElementSpinner spinner : spinners) {
                if (spinner.getSelection() != null) {
                    armours.add(spinner.getSelection());
                }
            }
            if (!armours.isEmpty()) {
                try {
                    CharacterManager.getSelectedCharacter().setArmour(armours.get(0));
                } catch (InvalidArmourException e) {
                    Snackbar snackbar = Snackbar
                            .make(this, R.string.message_invalid_shield_armour_combination, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            } else {
                try {
                    CharacterManager.getSelectedCharacter().setArmour(null);
                } catch (InvalidArmourException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
            }
        }
    }

    class WeaponsLayout extends IncrementalElementsLayout {

        public WeaponsLayout(Context context, boolean nullsAllowed) {
            super(context, nullsAllowed);

            setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setWeapons(getElementSpinners());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    setWeapons(getElementSpinners());
                }
            });
        }

        @Override
        protected ElementAdapter createElementAdapter() {
            return new ElementAdapter<Weapon>(getActivity(), mViewModel.getAvailableWeapons(), isNullAllowed(), Weapon.class);
        }

        private void setWeapons(List<ElementSpinner> spinners) {
            List<Weapon> weapons = new ArrayList<>();
            for (ElementSpinner spinner : spinners) {
                if (spinner.getSelection() != null) {
                    weapons.add(spinner.getSelection());
                }
            }
            CharacterManager.getSelectedCharacter().setWeapons(weapons);
        }
    }

}
