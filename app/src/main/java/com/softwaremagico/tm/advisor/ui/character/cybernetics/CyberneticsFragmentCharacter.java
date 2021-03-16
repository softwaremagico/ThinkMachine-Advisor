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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.IncrementalElementsLayout;
import com.softwaremagico.tm.advisor.ui.components.counters.CyberneticsExtraCounter;
import com.softwaremagico.tm.advisor.ui.components.counters.CyberneticsIncompatibilityCounter;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.cybernetics.CyberneticDevice;
import com.softwaremagico.tm.character.cybernetics.RequiredCyberneticDevicesException;
import com.softwaremagico.tm.character.cybernetics.SelectedCyberneticDevice;
import com.softwaremagico.tm.character.cybernetics.TooManyCyberneticDevicesException;

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

    @Override
    protected void initData() {
        final LinearLayout rootLayout = root.findViewById(R.id.root_container);

        addSection(ThinkMachineTranslator.getTranslatedText("cybernetics"), rootLayout);
        cyberneticLayout = new CyberneticLayout(getContext(), true);
        rootLayout.addView(cyberneticLayout);

        setCharacter(root, CharacterManager.getSelectedCharacter());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_cybernetics_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CyberneticsViewModel.class);

        incompatibilityCounter = root.findViewById(R.id.incompatibility_counter);
        extraCounter = root.findViewById(R.id.extra_counter);

//        CharacterManager.addCharacterRaceUpdatedListener(characterPlayer -> setCharacter(root, characterPlayer));
//        CharacterManager.addCharacterAgeUpdatedListener(characterPlayer -> setCharacter(root, characterPlayer));

        return root;
    }


    class CyberneticLayout extends IncrementalElementsLayout<CyberneticDevice> {

        public CyberneticLayout(Context context, boolean nullAllowed) {
            super(context, nullAllowed);

            setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setCyberneticDevice(getElementSpinners());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    setCyberneticDevice(getElementSpinners());
                }
            });
        }

        @Override
        protected ElementAdapter<CyberneticDevice> createElementAdapter() {
            return new ElementAdapter<CyberneticDevice>(getActivity(), mViewModel.getAvailableCybernetics(), isNullAllowed(), CyberneticDevice.class) {

                @Override
                public String getElementRepresentation(CyberneticDevice element) {
                    if (element.getId().equals(Element.DEFAULT_NULL_ID)) {
                        return "";
                    }
                    return element.getName() + " [" + element.getIncompatibility() + "]" + " (" + element.getPoints() + ")";
                }

                @Override
                public boolean isEnabled(int position) {
                    if (getItem(position).getRequirement() != null) {
                        return CharacterManager.getSelectedCharacter().hasCyberneticDevice(getItem(position).getRequirement());
                    }
                    return true;
                }
            };
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
            }
        }
    }

}
