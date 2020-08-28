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

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.softwaremagico.tm.advisor.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;

public class CustomFragment extends Fragment {

    public CustomFragment() {
        super();
        CustomFragment currentFragment = this;
        CharacterManager.addSelectedCharacterListener(new CharacterManager.CharacterSelectedListener() {
            @Override
            public void selected(CharacterPlayer characterPlayer) {
                //Refresh the fragment.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    getParentFragmentManager().beginTransaction().detach(currentFragment).commitNow();
                    getParentFragmentManager().beginTransaction().attach(currentFragment).commitNow();
                } else {
                    getParentFragmentManager().beginTransaction().detach(currentFragment).attach(currentFragment).commit();
                }
            }
        });
    }

    protected void addSection(String title, LinearLayout linearLayout) {
        TextView textView = new TextView(getContext(), null);
        textView.setText(title);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        View space = new View(getContext(), null);
        space.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        space.setBackgroundColor(Color.parseColor("#ff000000"));

        if (linearLayout != null) {
            linearLayout.addView(textView);
            linearLayout.addView(space);
        }
    }
}
