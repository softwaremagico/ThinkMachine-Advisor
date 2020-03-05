package com.softwaremagico.tm.advisor.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;

public class EnumSpinner extends LinearLayout {

    public EnumSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.enum_spinner, this);
        initComponents(attrs);
    }

    private void initComponents(AttributeSet attrs) {
        TextView tagText = (TextView) findViewById(R.id.translated_tag);
        TypedArray attributes = getContext().obtainStyledAttributes(attrs,
                R.styleable.translated_text, 0, 0);
        String tag = attributes.getString(R.styleable.translated_text_translation);
        tagText.setText(ThinkMachineTranslator.getTranslatedText(tag) + " ");
    }

    public <T extends Enum<?>> void setAdapter(EnumAdapter<T> adapter) {
        Spinner selector = (Spinner) findViewById(R.id.spinner);
        selector.setAdapter(adapter);
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        Spinner selector = (Spinner) findViewById(R.id.spinner);
        selector.setOnItemSelectedListener(onItemSelectedListener);
    }

    public <T extends Enum<?>> void setSelection(T selected) {
        Spinner selector = (Spinner) findViewById(R.id.spinner);
        selector.setSelection(((EnumAdapter<T>) selector.getAdapter()).indexOf(selected));
    }


}
