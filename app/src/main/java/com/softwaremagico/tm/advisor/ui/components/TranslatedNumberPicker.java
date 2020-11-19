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
import android.widget.NumberPicker;
import android.widget.TextView;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;

public class TranslatedNumberPicker extends Component {

    public TranslatedNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.translated_number_picker, this);
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
    }

    public void setLabel(String text) {
        final TextView tagText = findViewById(R.id.translated_tag);
        if (tagText != null) {
            tagText.setText(text);
        }
    }

    public int getValue() {
        final NumberPicker picker = findViewById(R.id.picker);
        if (picker == null) {
            return -1;
        }
        return picker.getValue();
    }

    public void setValue(int value) {
        final NumberPicker picker = findViewById(R.id.picker);
        if (picker != null) {
            if (picker.getMaxValue() < value) {
                picker.setValue(picker.getMaxValue());
            } else {
                picker.setValue(value);
            }
        }
    }

    public void addValueChangeListener(NumberPicker.OnValueChangeListener listener) {
        final NumberPicker picker = findViewById(R.id.picker);
        if (picker != null) {
            picker.setOnValueChangedListener(listener);
        }
    }

    public void setLimits(int minimum, int maximum) {
        final NumberPicker picker = findViewById(R.id.picker);
        if (picker != null) {
            picker.setMaxValue(maximum);
            picker.setMinValue(minimum);
        }
    }

}
