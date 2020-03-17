package com.softwaremagico.tm.advisor.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;

public class TranslatedNumberPicker extends LinearLayout {

    public TranslatedNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.translated_number_picker, this);
        initComponents(attrs);
    }

    private void initComponents(AttributeSet attrs) {
        TextView tagText = (TextView) findViewById(R.id.translated_tag);
        TypedArray attributes = getContext().obtainStyledAttributes(attrs,
                R.styleable.translated_text, 0, 0);
        String tag = attributes.getString(R.styleable.translated_text_translation);
        if (tag != null) {
            tagText.setText(ThinkMachineTranslator.getTranslatedText(tag) + " ");
        }
    }

    public void setLabel(String text) {
        TextView tagText = (TextView) findViewById(R.id.translated_tag);
        if (tagText != null) {
            tagText.setText(text);
        }
    }

    public void setValue(int value) {
        NumberPicker picker = (NumberPicker) findViewById(R.id.picker);
        if (picker != null) {
            if (picker.getMaxValue() < value) {
                picker.setValue(picker.getMaxValue());
            } else {
                picker.setValue(value);
            }
        }
    }

    public int getValue() {
        NumberPicker picker = (NumberPicker) findViewById(R.id.picker);
        if (picker == null) {
            return -1;
        }
        return picker.getValue();
    }

    public void addValueChangeListener(NumberPicker.OnValueChangeListener listener) {
        NumberPicker picker = (NumberPicker) findViewById(R.id.picker);
        if (picker != null) {
            picker.setOnValueChangedListener(listener);
        }
    }

    public void setLimits(int minimum, int maximum) {
        NumberPicker picker = (NumberPicker) findViewById(R.id.picker);
        if (picker != null) {
            picker.setMaxValue(maximum);
            picker.setMinValue(minimum);
        }
    }

}
