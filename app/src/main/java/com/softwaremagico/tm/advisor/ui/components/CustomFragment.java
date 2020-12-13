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

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;

public abstract class CustomFragment extends Fragment {
    // flag bit to determine whether the data is initialized
    private boolean isInitialized = false;
    // flags to determine whether fragments are visible
    private boolean isVisible = false;
    // flag bit to determine that view has been loaded to avoid null pointer operations
    private boolean isPrepareView = false;

    public CustomFragment() {
        super();

        CharacterManager.addSelectedCharacterListener(new CharacterManager.CharacterSelectedListener() {
            @Override
            public void selected(CharacterPlayer characterPlayer) {
                if (getView() != null) {
                    setCharacter(getView().getRootView(), characterPlayer);
                }
            }
        });
    }

    /**
     * Lazy Loading Method
     */
    /**
     * Lazy Loading Method
     */
    public void lazyInitData() {
        isInitialized = true;
        initData();
    }

    /**
     * Method of loading data, implemented by subclasses
     */
    protected abstract void initData();


    @Override
    public void onResume() {
        super.onResume();
        lazyInitData();
    }

    /**
     * Method after onViewCreated in fragment lifecycle calls a lazy load here to avoid the first visible unload of data
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lazyInitData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepareView = true; // At this point the view has been loaded and set to true
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
