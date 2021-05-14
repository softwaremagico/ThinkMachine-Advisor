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
import android.os.Build;
import android.util.AttributeSet;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;

public class TranslatedNumberPicker extends Component {
    private int oldValue = 0;
    private int selectedValue = 0;
    private NumberPicker picker;
    private Element<?> element;

    public TranslatedNumberPicker(Context context, AttributeSet attrs, Element<?> element) {
        super(context, attrs);
        this.element = element;
        refreshElementColor();
        setEnabled();
    }

    public interface OnValueChanged {
        void update(int newValue);
    }

    protected void setEnabled() {
        if (element == null) {
            return;
        }
        picker.setEnabled(!element.isRestricted(CharacterManager.getSelectedCharacter()));
    }

    protected void refreshElementColor() {
        if (element == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (element.isRestricted() || element.isRestricted(CharacterManager.getSelectedCharacter())) {
                picker.setTextColor(ContextCompat.getColor(getContext(), R.color.restricted));
            } else if (!element.isOfficial()) {
                picker.setTextColor(ContextCompat.getColor(getContext(), R.color.unofficialElement));
            } else {
                picker.setTextColor(ContextCompat.getColor(getContext(), R.color.colorNormal));
            }
        }
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
        picker = findViewById(R.id.picker);
        picker.setWrapSelectorWheel(false);
    }

    public void setLabel(String text) {
        final TextView tagText = findViewById(R.id.translated_tag);
        if (tagText != null) {
            tagText.setText(text);
        }
    }

    public int getValue() {
        if (picker == null) {
            return -1;
        }
        return picker.getValue();
    }

    public void setValue(int value) {
        if (picker != null) {
            if (value > picker.getMaxValue()) {
                picker.setValue(picker.getMaxValue());
            } else {
                picker.setValue(value);
            }
        }
    }

    public void addValueChangeListener(OnValueChanged listener) {
        // When spinning, the picker launches lot of events for each value. We updated the value but only launch the listeners when stop spinning.
        if (picker != null) {
            picker.setOnValueChangedListener((picker1, oldVal, newVal) -> {
                selectedValue = newVal;
            });
            picker.setOnScrollListener((view, scrollState) -> {
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE && oldValue != picker.getValue()) {
                    listener.update(selectedValue);
                    oldValue = selectedValue;

                    picker.clearFocus();
                    //picker.setValue(selectedValue);
                }
            });
        }
    }

    public void setLimits(int minimum, int maximum) {
        if (picker != null) {
            picker.setMaxValue(maximum);
            picker.setMinValue(minimum);
        }
    }

    public void update() {
        setEnabled();
        refreshElementColor();
    }

}
