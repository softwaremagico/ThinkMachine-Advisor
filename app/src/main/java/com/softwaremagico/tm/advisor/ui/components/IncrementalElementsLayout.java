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

import com.softwaremagico.tm.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class IncrementalElementsLayout<T extends Element<?>> extends LinearLayout {
    private ElementAdapter<T> elementAdapter;
    private List<ElementSpinner> elementSpinners;
    private boolean enabled = true;
    private final boolean nullAllowed;
    private Set<AdapterView.OnItemSelectedListener> listeners;


    public IncrementalElementsLayout(Context context, boolean nullAllowed) {
        this(context, null, nullAllowed);
        listeners = new HashSet<>();
    }

    public IncrementalElementsLayout(Context context, @Nullable AttributeSet attrs, boolean nullAllowed) {
        super(context, attrs);
        this.nullAllowed = nullAllowed;
        elementSpinners = new ArrayList<>();
        setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        updateContent();
    }

    private void updateContent() {
        if (!enabled) {
            return;
        }
        if (elementSpinners.isEmpty()) {
            addElementSpinner(createElementSpinner());
            return;
        }
        int i = 0;
        while (i < elementSpinners.size()) {
            //Not the last spinner.
            if (i < elementSpinners.size() - 1) {
                if (elementSpinners.get(i).getSelection() == null) {
                    removeView(elementSpinners.get(i));
                    elementSpinners.remove(i);
                }
            } else {
                //Last must be an empty one.
                if (nullAllowed && elementSpinners.get(i).getSelection() != null) {
                    addElementSpinner(createElementSpinner());
                }
            }
            i++;
        }
    }

    private void clear() {
        removeAllViewsInLayout();
        elementSpinners.clear();
    }

    public void setElements(Collection<T> elements) {
        enabled = false;
        clear();
        for (T element : elements) {
            ElementSpinner spinner = createElementSpinner();
            spinner.setSelection(element);
            addElementSpinner(spinner);
        }
        //Last must be an empty one.
        if (nullAllowed) {
            addElementSpinner(createElementSpinner());
        }
        enabled = true;
    }

    public List<ElementSpinner> getElementSpinners() {
        return elementSpinners;
    }

    private void addElementSpinner(ElementSpinner spinner) {
        super.addView(spinner);
        enabled = false;
        setElementSpinnerProperties(spinner);
        elementSpinners.add(spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateContent();
                for (AdapterView.OnItemSelectedListener listener : listeners) {
                    listener.onItemSelected(parent, view, position, id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                updateContent();
                for (AdapterView.OnItemSelectedListener listener : listeners) {
                    listener.onNothingSelected(parent);
                }
            }
        });
        enabled = true;
    }

    private void setElementSpinnerProperties(ElementSpinner spinner) {
        spinner.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
    }

    public ElementSpinner createElementSpinner() {
        ElementSpinner blessingSelector = new ElementSpinner(getContext());
        blessingSelector.setAdapter(getElementAdapter());
        return blessingSelector;
    }

    protected abstract ElementAdapter<T> createElementAdapter();

    protected ElementAdapter<T> getElementAdapter() {
        if (elementAdapter == null) {
            elementAdapter = createElementAdapter();
        }
        return elementAdapter;
    }

    protected boolean isNullAllowed() {
        return nullAllowed;
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        listeners.add(listener);
    }
}
