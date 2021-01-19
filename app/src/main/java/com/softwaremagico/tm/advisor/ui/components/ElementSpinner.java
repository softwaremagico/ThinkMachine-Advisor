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
import androidx.fragment.app.FragmentTransaction;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;

public class ElementSpinner extends Component {

    private ImageView helpButton;

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
                openDescriptionWindow();
            });
        }

    }

    public <T extends Element<?>> void setAdapter(ElementAdapter<T> adapter) {
        final Spinner selector = findViewById(R.id.spinner);
        selector.setAdapter(adapter);
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        final Spinner selector = findViewById(R.id.spinner);
        selector.setOnItemSelectedListener(onItemSelectedListener);
        selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 && selector.getItemAtPosition(0) == null) {
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

    public <T extends Element<?>> void setSelection(T selected) {
        final Spinner selector = findViewById(R.id.spinner);
        if (selected == null) {
            selector.setSelection(0);
        } else {
            selector.setSelection(((ElementAdapter<T>) selector.getAdapter()).indexOf(selected));
        }
    }

    public <T extends Element<?>> T getSelection() {
        final Spinner selector = findViewById(R.id.spinner);
        final T selectedItem = (T) selector.getSelectedItem();
        if (Element.isNull(selectedItem)) {
            return null;
        }
        return selectedItem;
    }


    protected void openDescriptionWindow() {
        final ElementDescriptionDialog elementDescriptionDialog = new ElementDescriptionDialog();

        final boolean isLargeLayout = true;

        if (isLargeLayout) {
            // The device is using a large layout, so show the fragment as a dialog
            elementDescriptionDialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "dialog");
        } else {
            // The device is smaller, so show the fragment fullscreen
            final FragmentTransaction transaction = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction.add(android.R.id.content, elementDescriptionDialog)
                    .addToBackStack(null).commit();
        }
    }


}
