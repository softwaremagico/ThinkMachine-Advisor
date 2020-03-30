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

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class IncrementalElementsLayout extends LinearLayout {
    private List<ElementSpinner> elements;

    public IncrementalElementsLayout(Context context) {
        this(context, null);
    }

    public IncrementalElementsLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        elements = new ArrayList<>();
        setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        updateContent();
    }

    private void updateContent() {
        if(elements.isEmpty()){
            addElementSpinner(createElementSpinner());
            return;
        }
        int i = 0;
        while (i < elements.size()) {
            //Not the last spinner.
            if (i < elements.size() - 1) {
                if (elements.get(i).getSelection() == null) {
                    removeView(elements.get(i));
                    elements.remove(i);
                }
            } else {
                //Last must be an empty one.
                if (elements.get(i).getSelection() != null) {
                    addElementSpinner(createElementSpinner());
                }
            }
            i++;
        }
    }

    public List<ElementSpinner> getElements() {
        return elements;
    }

    private void addElementSpinner(ElementSpinner spinner) {
        setElementSpinnerProperties(spinner);
        super.addView(spinner);
        elements.add(spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateContent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                updateContent();
            }
        });
    }

    private void setElementSpinnerProperties(ElementSpinner spinner) {
        spinner.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
    }

    public abstract ElementSpinner createElementSpinner();
}
