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
    private final List<ElementSpinner<T>> elementSpinners;
    private boolean enabled = true;
    private final boolean nullAllowed;
    private final Set<AdapterView.OnItemSelectedListener> listeners;
    private final int maxElements;

    public IncrementalElementsLayout(Context context, boolean nullAllowed, int maxElements) {
        this(context, null, nullAllowed, maxElements);
    }


    public IncrementalElementsLayout(Context context, boolean nullAllowed) {
        this(context, null, nullAllowed, 100);
    }

    public IncrementalElementsLayout(Context context, @Nullable AttributeSet attrs, boolean nullAllowed, int maxElements) {
        super(context, attrs);
        this.nullAllowed = nullAllowed;
        this.maxElements = maxElements;
        elementSpinners = new ArrayList<>();
        listeners = new HashSet<>();
        setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        updateContent();
    }

    protected void updateContent() {
        if (!enabled) {
            return;
        }
        if (elementSpinners.isEmpty()) {
            addElementSpinner(createElementSpinner());
            return;
        }
        boolean previousIsNull = false;
        for (int i = elementSpinners.size() - 1; i >= 0; i--) {
            //Not the last spinner.
            if (i < elementSpinners.size() - 1) {
                if (elementSpinners.get(i).getSelection() == null) {
                    if (previousIsNull) {
                        removeView(elementSpinners.get(i + 1));
                        elementSpinners.remove(i + 1);
                    } else {
                        removeView(elementSpinners.get(i));
                        elementSpinners.remove(i);
                    }
                    previousIsNull = true;
                } else {
                    previousIsNull = false;
                }
            } else {
                //Last must be an empty one.
                if (nullAllowed && elementSpinners.get(i).getSelection() != null) {
                    addElementSpinner(createElementSpinner());
                    previousIsNull = false;
                } else {
                    previousIsNull = true;
                }
            }
        }
    }

    protected void removeDuplicates() {
        int i = 0;
        Set<T> selections = new HashSet<>();
        boolean removed = false;
        while (i < elementSpinners.size()) {
            if (elementSpinners.get(i).getSelection() != null) {
                if (!selections.contains(elementSpinners.get(i).getSelection())) {
                    selections.add(elementSpinners.get(i).getSelection());
                } else {
                    removeView(elementSpinners.get(i));
                    elementSpinners.remove(i);
                    removed = true;
                }
            }
            i++;
        }
        if (removed) {
            updateContent();
        }
    }

    private void clear() {
        removeAllViewsInLayout();
        elementSpinners.clear();
    }

    public void setElement(T element) {
        final List<T> elements = new ArrayList<>();
        elements.add(element);
        setElements(elements);
    }

    public void setElements(Collection<T> elements) {
        enabled = false;
        clear();
        for (final T element : elements) {
            final ElementSpinner<T> spinner = createElementSpinner();
            spinner.setSelection(element);
            addElementSpinner(spinner);
        }
        //Last must be an empty one.
        if (nullAllowed) {
            addElementSpinner(createElementSpinner());
        }
        enabled = true;
    }

    public List<ElementSpinner<T>> getElementSpinners() {
        return elementSpinners;
    }

    private void addElementSpinner(ElementSpinner<T> spinner) {
        if (elementSpinners.size() >= maxElements) {
            return;
        }
        super.addView(spinner);
        enabled = false;
        setElementSpinnerProperties(spinner);
        elementSpinners.add(spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (final AdapterView.OnItemSelectedListener listener : listeners) {
                    listener.onItemSelected(parent, view, position, id);
                }
                updateContent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                for (final AdapterView.OnItemSelectedListener listener : listeners) {
                    listener.onNothingSelected(parent);
                }
                updateContent();
            }
        });
        enabled = true;
    }

    private void setElementSpinnerProperties(ElementSpinner<T> spinner) {
        spinner.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
    }

    public final ElementSpinner<T> createElementSpinner() {
        final ElementSpinner<T> selector = new ElementSpinner<>(getContext());
        selector.setAdapter(getElementAdapter());
        return selector;
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
