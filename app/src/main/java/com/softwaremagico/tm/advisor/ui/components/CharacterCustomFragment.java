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

package com.softwaremagico.tm.advisor.ui.components;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;

public abstract class CharacterCustomFragment extends CustomFragment {


    public CharacterCustomFragment() {
        super();

        CharacterManager.addSelectedCharacterListener(characterPlayer -> {
            if (getView() != null) {
                setCharacter(getView().getRootView(), characterPlayer);
            }
        });
    }


    protected abstract void setCharacter(View root, CharacterPlayer character);

    protected void addSection(String title, LinearLayout linearLayout) {
        final TextView textView = new TextView(getContext(), null);
        textView.setText(title);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextAppearance(R.style.TextSubtitle);


        final View space = new View(getContext(), null);
        space.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));

        if (linearLayout != null) {
            linearLayout.addView(textView);
            linearLayout.addView(space);
        }
    }

    protected void addSpace(LinearLayout linearLayout) {
        addSpace(linearLayout, 30);
    }

    protected void addSpace(LinearLayout linearLayout, int height) {
        final View space = new View(getContext(), null);
        space.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        linearLayout.addView(space);
    }


    protected interface CharacterValueUpdated {
        void valueUpdated(String value);
    }

    protected void updateTranslatedTextField(View root, int resource, CharacterValueUpdated callback) {
        final TranslatedEditText textField = root.findViewById(resource);
        textField.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //Nothing
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                //Nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                callback.valueUpdated(textField.getText());
            }
        });
    }

    protected void updateTextField(View root, int resource, CharacterValueUpdated callback) {
        final TextView textField = root.findViewById(resource);
        textField.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //Nothing
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                //Nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                callback.valueUpdated(textField.getText().toString());
            }
        });
    }


}
