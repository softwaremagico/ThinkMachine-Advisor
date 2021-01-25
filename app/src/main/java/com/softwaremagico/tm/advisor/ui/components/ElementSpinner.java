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
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.descriptions.BeneficeDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.ElementDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.ShieldDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.equipment.shields.Shield;

public class ElementSpinner<T extends Element<?>> extends Component {

    private ImageView helpButton;
    private Spinner selector;

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

    private void initComponents(AttributeSet attrs) {
        final TextView tagText = findViewById(R.id.translated_tag);
        final TypedArray attributes = getContext().obtainStyledAttributes(attrs,
                R.styleable.translated_text, 0, 0);
        final String tag = attributes.getString(R.styleable.translated_text_translation);
        if (tag != null) {
            tagText.setText(ThinkMachineTranslator.getTranslatedText(tag) + " ");
        }
        tagText.setTextAppearance(R.style.CharacterInfo);

        helpButton = findViewById(R.id.button_help);
        if (helpButton != null) {
            helpButton.setOnClickListener(v -> {
                openDescriptionWindow(getSelection());
            });
        }

        selector = findViewById(R.id.spinner);
        selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selector.getItemAtPosition(0) == null ||
                        ((Element) selector.getItemAtPosition(0)).getDescription() == null ||
                        ((Element) selector.getItemAtPosition(0)).getDescription().isEmpty()) {
                    helpButton.setVisibility(ImageView.INVISIBLE);
                } else {
                    helpButton.setVisibility(ImageView.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                helpButton.setVisibility(ImageView.INVISIBLE);
            }
        });

    }

    public <T extends Element<?>> void setAdapter(ElementAdapter<T> adapter) {
        selector.setAdapter(adapter);
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        selector = findViewById(R.id.spinner);
        selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedListener.onItemSelected(parent, view, position, id);
                if (selector.getItemAtPosition(position) == null ||
                        ((Element<?>) selector.getItemAtPosition(position)).getDescription() == null ||
                        ((Element<?>) selector.getItemAtPosition(position)).getDescription().isEmpty()) {
                    helpButton.setVisibility(ImageView.INVISIBLE);
                } else {
                    helpButton.setVisibility(ImageView.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                onItemSelectedListener.onNothingSelected(parent);
                helpButton.setVisibility(ImageView.INVISIBLE);
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

    public T getSelection() {
        final Spinner selector = findViewById(R.id.spinner);
        final T selectedItem = (T) selector.getSelectedItem();
        if (Element.isNull(selectedItem)) {
            return null;
        }
        return selectedItem;
    }

    public <E extends Element<?>> E getSelection(Class<E> elementClass) {
        final Spinner selector = findViewById(R.id.spinner);
        final E selectedItem = elementClass.cast(selector.getSelectedItem());
        if (Element.isNull(selectedItem)) {
            return null;
        }
        return selectedItem;
    }


    protected void openDescriptionWindow(T element) {
        if (element != null) {
            if (element instanceof AvailableBenefice) {
                new BeneficeDescriptionDialog((AvailableBenefice) element).show(((FragmentActivity) getContext()).getSupportFragmentManager(), "");
            } else if (element instanceof Shield) {
                new ShieldDescriptionDialog((Shield) element).show(((FragmentActivity) getContext()).getSupportFragmentManager(), "");
            } else {
                new ElementDescriptionDialog(element).show(((FragmentActivity) getContext()).getSupportFragmentManager(), "");
            }
        }
    }


}
