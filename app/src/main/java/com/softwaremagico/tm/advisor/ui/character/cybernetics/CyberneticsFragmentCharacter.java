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

package com.softwaremagico.tm.advisor.ui.character.cybernetics;

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

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.IncrementalElementsLayout;
import com.softwaremagico.tm.advisor.ui.components.counters.CyberneticsExtraCounter;
import com.softwaremagico.tm.advisor.ui.components.counters.CyberneticsIncompatibilityCounter;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.CyberneticAdapter;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.cybernetics.CyberneticDevice;
import com.softwaremagico.tm.character.cybernetics.RequiredCyberneticDevicesException;
import com.softwaremagico.tm.character.cybernetics.SelectedCyberneticDevice;
import com.softwaremagico.tm.character.cybernetics.TooManyCyberneticDevicesException;
import com.softwaremagico.tm.character.exceptions.UnofficialElementNotAllowedException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CyberneticsFragmentCharacter extends CharacterCustomFragment {
    private CyberneticsViewModel mViewModel;

    private CyberneticsIncompatibilityCounter incompatibilityCounter;
    private CyberneticsExtraCounter extraCounter;

    private IncrementalElementsLayout<CyberneticDevice> cyberneticLayout;

    private View root;

    public static CyberneticsFragmentCharacter newInstance(int index) {
        final CyberneticsFragmentCharacter fragment = new CyberneticsFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setCharacter(View root, CharacterPlayer character) {
        if (getContext() != null) {
            if (cyberneticLayout != null) {
                List<CyberneticDevice> cyberneticDevices = character.getCybernetics().stream().map(SelectedCyberneticDevice::getCyberneticDevice).collect(Collectors.toList());
                cyberneticLayout.setElements(cyberneticDevices);
            }
            if (incompatibilityCounter != null) {
                incompatibilityCounter.setCharacter(character);
            }
            if (extraCounter != null) {
                extraCounter.setCharacter(character);
            }
        }
    }

    @Override
    protected void initData() {
        final LinearLayout rootLayout = root.findViewById(R.id.root_container);

        addSection(ThinkMachineTranslator.getTranslatedText("cybernetics"), rootLayout);
        cyberneticLayout = new CyberneticLayout(getContext(), true);
        rootLayout.addView(cyberneticLayout);

        setCharacter(root, CharacterManager.getSelectedCharacter());
    }

    @Override
    protected void updateSettings(CharacterPlayer characterPlayer) {
        if (getContext() != null) {
            cyberneticLayout.updateElementAdapter(!characterPlayer.getSettings().isOnlyOfficialAllowed());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_cybernetics_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CyberneticsViewModel.class);

        incompatibilityCounter = root.findViewById(R.id.incompatibility_counter);
        extraCounter = root.findViewById(R.id.extra_counter);

        CharacterManager.addCharacterCharacteristicUpdatedListener((characterPlayer, characteristic) -> {
            if (characteristic == CharacteristicName.WILL) {
                incompatibilityCounter.setCharacter(characterPlayer);
            }
        });

        CharacterManager.addSelectedCharacterListener(characterPlayer -> setCharacter(root, characterPlayer));

        return root;
    }

    private void checkTechLevel(List<ElementSpinner<CyberneticDevice>> spinners) {
        if (spinners.stream().anyMatch(spinner -> spinner.getSelection() != null && spinner.getSelection().getTechLevel() >
                CharacterManager.getSelectedCharacter().getCharacteristic(CharacteristicName.TECH).getValue())) {
            SnackbarGenerator.getWarningMessage(root, R.string.message_invalid_tech_level).show();
        }
    }


    class CyberneticLayout extends IncrementalElementsLayout<CyberneticDevice> {

        public CyberneticLayout(Context context, boolean nullAllowed) {
            super(context, nullAllowed);

            setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (removeDuplicates()) {
                        SnackbarGenerator.getInfoMessage(root, R.string.message_duplicated_item_removed).show();
                    }
                    setCyberneticDevice(getElementSpinners());
                    checkTechLevel(getElementSpinners());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    removeDuplicates();
                    setCyberneticDevice(getElementSpinners());
                }
            });
        }

        @Override
        protected ElementAdapter<CyberneticDevice> createElementAdapter(boolean nonOfficial) {
            return new CyberneticAdapter(getActivity(), mViewModel.getAvailableCybernetics(nonOfficial), isNullAllowed());
        }

        private void setCyberneticDevice(List<ElementSpinner<CyberneticDevice>> spinners) {
            final List<CyberneticDevice> cyberneticDevices = new ArrayList<>();
            for (final ElementSpinner<CyberneticDevice> spinner : spinners) {
                if (spinner.getSelection() != null) {
                    cyberneticDevices.add(spinner.getSelection());
                }
            }
            try {
                CharacterManager.getSelectedCharacter().setCybernetics(cyberneticDevices);
            } catch (TooManyCyberneticDevicesException e) {
                SnackbarGenerator.getErrorMessage(this, R.string.message_too_many_cybernetics).show();
                final LinearLayout rootLayout = root.findViewById(R.id.root_container);
                setCharacter(rootLayout, CharacterManager.getSelectedCharacter());
            } catch (RequiredCyberneticDevicesException e) {
                SnackbarGenerator.getErrorMessage(this, R.string.message_required_cybernetic_missing).show();
            } catch (UnofficialElementNotAllowedException e) {
                SnackbarGenerator.getErrorMessage(this, R.string.message_unofficial_element_not_allowed).show();
            }
        }
    }

}
