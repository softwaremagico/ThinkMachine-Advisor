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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.IncrementalElementsLayout;
import com.softwaremagico.tm.advisor.ui.components.counters.FirebirdsCounter;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.armours.InvalidArmourException;
import com.softwaremagico.tm.character.equipment.shields.InvalidShieldException;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;

import java.util.ArrayList;
import java.util.List;

public class EquipmentFragmentCharacter extends CharacterCustomFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private EquipmentViewModel mViewModel;
    private IncrementalElementsLayout<Weapon> meleeWeaponsLayout;
    private IncrementalElementsLayout<Weapon> rangeWeaponsLayout;
    private IncrementalElementsLayout<Armour> armoursLayout;
    private IncrementalElementsLayout<Shield> shieldsLayout;
    private FirebirdsCounter firebirdsCounter;
    private View root;


    public static EquipmentFragmentCharacter newInstance(int index) {
        final EquipmentFragmentCharacter fragment = new EquipmentFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setCharacter(View root, CharacterPlayer character) {
        if (meleeWeaponsLayout != null) {
            meleeWeaponsLayout.setElements(CharacterManager.getSelectedCharacter().getSelectedWeapons());
        }
        if (rangeWeaponsLayout != null) {
            rangeWeaponsLayout.setElements(CharacterManager.getSelectedCharacter().getSelectedWeapons());
        }
        if (armoursLayout != null) {
            armoursLayout.setElement(CharacterManager.getSelectedCharacter().getSelectedArmour());
        }
        if (shieldsLayout != null) {
            shieldsLayout.setElement(CharacterManager.getSelectedCharacter().getSelectedShield());
        }
        firebirdsCounter.setCharacter(character);
    }

    @Override
    protected void initData() {
        final LinearLayout rootLayout = root.findViewById(R.id.root_container);

        addSection(ThinkMachineTranslator.getTranslatedText("rangedWeapons"), rootLayout);

        rangeWeaponsLayout = new RangedWeaponsLayout(getContext(), true);
        rootLayout.addView(rangeWeaponsLayout);
        addSpace(rootLayout);

        addSection(ThinkMachineTranslator.getTranslatedText("meleeWeapons"), rootLayout);

        meleeWeaponsLayout = new MeleeWeaponsLayout(getContext(), true);
        rootLayout.addView(meleeWeaponsLayout);
        addSpace(rootLayout);

        addSection(ThinkMachineTranslator.getTranslatedText("armor"), rootLayout);
        armoursLayout = new ArmourLayout(getContext(), true);
        rootLayout.addView(armoursLayout);
        addSpace(rootLayout);

        addSection(ThinkMachineTranslator.getTranslatedText("shield"), rootLayout);
        shieldsLayout = new ShieldLayout(getContext(), true);
        rootLayout.addView(shieldsLayout);

        setCharacter(root, CharacterManager.getSelectedCharacter());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_equipment_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(EquipmentViewModel.class);
        firebirdsCounter = root.findViewById(R.id.firebirds_counter);
        return root;
    }

    class ShieldLayout extends IncrementalElementsLayout<Shield> {
        private static final int MAX_ITEMS = 1;

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
        protected ElementAdapter<Shield> createElementAdapter() {
            return new ElementAdapter<>(getActivity(), mViewModel.getAvailableShields(), isNullAllowed(), Shield.class);
        }

        private void setShield(List<ElementSpinner<Shield>> spinners) {
            final List<Shield> shields = new ArrayList<>();
            for (final ElementSpinner<Shield> spinner : spinners) {
                if (spinner.getSelection() != null) {
                    shields.add(spinner.getSelection());
                }
            }
            if (shields.isEmpty()) {
                try {
                    CharacterManager.getSelectedCharacter().setShield(null);
                } catch (InvalidShieldException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
            } else {
                try {
                    CharacterManager.getSelectedCharacter().setShield(shields.get(0));
                } catch (InvalidShieldException e) {
                    SnackbarGenerator.getErrorMessage(this, R.string.message_invalid_shield_armour_combination).show();
                }
            }
        }
    }


    class ArmourLayout extends IncrementalElementsLayout<Armour> {
        private static final int MAX_ITEMS = 1;

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
        protected ElementAdapter<Armour> createElementAdapter() {
            return new ElementAdapter<>(getActivity(), mViewModel.getAvailableArmours(), isNullAllowed(), Armour.class);
        }

        private void setArmour(List<ElementSpinner<Armour>> spinners) {
            final List<Armour> armours = new ArrayList<>();
            for (final ElementSpinner<Armour> spinner : spinners) {
                if (spinner.getSelection() != null) {
                    armours.add(spinner.getSelection());
                }
            }
            if (armours.isEmpty()) {
                try {
                    CharacterManager.getSelectedCharacter().setArmour(null);
                } catch (InvalidArmourException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
            } else {
                try {
                    CharacterManager.getSelectedCharacter().setArmour(armours.get(0));
                } catch (InvalidArmourException e) {
                    SnackbarGenerator.getErrorMessage(this, R.string.message_invalid_shield_armour_combination).show();
                }
            }
        }
    }

    abstract class WeaponsLayout extends IncrementalElementsLayout<Weapon> {

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
        protected abstract ElementAdapter<Weapon> createElementAdapter();

        private void setWeapons(List<ElementSpinner<Weapon>> spinners) {
            final List<Weapon> weapons = new ArrayList<>();
            for (final ElementSpinner<Weapon> spinner : spinners) {
                if (spinner.getSelection() != null) {
                    weapons.add(spinner.getSelection());
                }
            }
            assignWeapons(weapons);
        }

        protected abstract void assignWeapons(List<Weapon> weapons);
    }

    class MeleeWeaponsLayout extends WeaponsLayout {
        public MeleeWeaponsLayout(Context context, boolean nullsAllowed) {
            super(context, nullsAllowed);
        }

        @Override
        protected ElementAdapter<Weapon> createElementAdapter() {
            return new ElementAdapter<>(getActivity(), mViewModel.getAvailableMeleeWeapons(), isNullAllowed(), Weapon.class);
        }

        @Override
        protected void assignWeapons(List<Weapon> weapons) {
            CharacterManager.getSelectedCharacter().setMeleeWeapons(weapons);
        }
    }

    class RangedWeaponsLayout extends WeaponsLayout {
        public RangedWeaponsLayout(Context context, boolean nullsAllowed) {
            super(context, nullsAllowed);
        }

        @Override
        protected ElementAdapter<Weapon> createElementAdapter() {
            return new ElementAdapter<Weapon>(getActivity(), mViewModel.getAvailableRangedWeapons(), isNullAllowed(), Weapon.class) {

                @Override
                public String getElementRepresentation(Weapon element) {
                    if (element.getId().equals(Element.DEFAULT_NULL_ID)) {
                        return "";
                    }
                    return element.getName() + " (" + element.getCost() + ")";
                }
            };
        }

        @Override
        protected void assignWeapons(List<Weapon> weapons) {
            CharacterManager.getSelectedCharacter().setRangedWeapons(weapons);
        }
    }

}
