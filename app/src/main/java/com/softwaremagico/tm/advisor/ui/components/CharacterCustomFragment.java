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
        //space.setBackgroundColor(Color.parseColor("#ff000000"));

        if (linearLayout != null) {
            linearLayout.addView(textView);
            linearLayout.addView(space);
        }
    }
}