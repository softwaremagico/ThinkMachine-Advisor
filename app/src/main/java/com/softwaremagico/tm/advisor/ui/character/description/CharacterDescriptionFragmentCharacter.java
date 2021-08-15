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

package com.softwaremagico.tm.advisor.ui.character.description;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.TranslatedEditText;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;

public class CharacterDescriptionFragmentCharacter extends CharacterCustomFragment {
    private View root;

    public static CharacterDescriptionFragmentCharacter newInstance(int index) {
        final CharacterDescriptionFragmentCharacter fragment = new CharacterDescriptionFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {
        updateTranslatedTextField(root, R.id.character_player, value -> CharacterManager.getSelectedCharacter().getInfo().setPlayer(value));
        updateTranslatedTextField(root, R.id.character_hair, value -> CharacterManager.getSelectedCharacter().getInfo().setHair(value));
        updateTranslatedTextField(root, R.id.character_eyes, value -> CharacterManager.getSelectedCharacter().getInfo().setEyes(value));
        updateTranslatedTextField(root, R.id.character_complexion, value -> CharacterManager.getSelectedCharacter().getInfo().setComplexion(value));
        updateTranslatedTextField(root, R.id.character_weight, value -> CharacterManager.getSelectedCharacter().getInfo().setWeight(value));
        updateTextField(root, R.id.character_description, value -> CharacterManager.getSelectedCharacter().getInfo().setCharacterDescription(value));
        updateTextField(root, R.id.character_background, value -> CharacterManager.getSelectedCharacter().getInfo().setBackgroundDecription(value));
        setCharacter(root, CharacterManager.getSelectedCharacter());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_description_fragment, container, false);

        CharacterManager.addSelectedCharacterListener(characterPlayer -> setCharacter(root, characterPlayer));

        return root;
    }

    @Override
    public void setCharacter(View root, CharacterPlayer character) {
        if (getContext() != null) {
            final TranslatedEditText playerTextEditor = root.findViewById(R.id.character_player);
            playerTextEditor.setText(character.getInfo().getPlayer());
            final TranslatedEditText hairTextEditor = root.findViewById(R.id.character_hair);
            hairTextEditor.setText(character.getInfo().getHair());
            final TranslatedEditText eyesTextEditor = root.findViewById(R.id.character_eyes);
            eyesTextEditor.setText(character.getInfo().getEyes());
            final TranslatedEditText complexionTextEditor = root.findViewById(R.id.character_complexion);
            complexionTextEditor.setText(character.getInfo().getComplexion());
            final TranslatedEditText weightTextEditor = root.findViewById(R.id.character_weight);
            weightTextEditor.setText(character.getInfo().getWeight());
            final EditText characterDescription = root.findViewById(R.id.character_description);
            characterDescription.setText(character.getInfo().getCharacterDescription());
            final EditText characterBackground = root.findViewById(R.id.character_background);
            characterBackground.setText(character.getInfo().getBackgroundDecription());
        }
    }

    @Override
    protected void updateSettings(CharacterPlayer characterPlayer) {
        //Nothing changes.
    }

}
