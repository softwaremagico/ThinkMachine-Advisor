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
import android.widget.ImageView;
import android.widget.Spinner;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.spinner.HelpElement;
import com.softwaremagico.tm.advisor.ui.components.spinner.SearchableSpinner;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.ElementAdapter;

public class ElementSpinner<T extends Element<T>> extends HelpElement<T> {

    private SearchableSpinner<T> selector;

    public ElementSpinner(Context context) {
        this(context, null);
    }

    public ElementSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.element_spinner, this);
        initComponents(attrs);
    }

    @Override
    protected void initComponents(AttributeSet attrs) {
        super.initComponents(attrs);
        selector = findViewById(R.id.spinner);
        selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selector.getItemAtPosition(position) == null ||
                        ((T) selector.getItemAtPosition(position)).getDescription() == null ||
                        ((T) selector.getItemAtPosition(position)).getDescription().isEmpty()) {
                    getHelpButton().setVisibility(ImageView.INVISIBLE);
                } else {
                    getHelpButton().setVisibility(ImageView.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getHelpButton().setVisibility(ImageView.INVISIBLE);
            }
        });
    }

    public <E extends Element<?>> void setAdapter(ElementAdapter<E> adapter) {
        selector.setAdapter(adapter);
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        selector = findViewById(R.id.spinner);
        selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedListener.onItemSelected(parent, view, position, id);
                if (selector.getItemAtPosition(position) == null ||
                        ((T) selector.getItemAtPosition(position)).getDescription() == null ||
                        ((T) selector.getItemAtPosition(position)).getDescription().isEmpty()) {
                    getHelpButton().setVisibility(ImageView.INVISIBLE);
                } else {
                    getHelpButton().setVisibility(ImageView.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                onItemSelectedListener.onNothingSelected(parent);
                getHelpButton().setVisibility(ImageView.INVISIBLE);
            }
        });
    }

    public void setSelection(T selected) {
        final Spinner selector = findViewById(R.id.spinner);
        if (selected == null) {
            selector.setSelection(0);
        } else {
            selector.setSelection(((ElementAdapter<T>) selector.getAdapter()).indexOf(selected));
        }
    }

    @Override
    public T getSelection() {
        final T selectedItem = (T) selector.getSelectedItem();
        if (Element.isNull(selectedItem)) {
            return null;
        }
        return selectedItem;
    }

    @Override
    public void setEnabled(boolean enabled){
        final Spinner selector = findViewById(R.id.spinner);
        selector.setEnabled(enabled);
    }
}
