package com.softwaremagico.tm.advisor.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;

public class ElementSpinner extends LinearLayout {

    public ElementSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.element_spinner, this);
        initComponents(attrs);
    }

    private void initComponents(AttributeSet attrs) {
        TextView tagText = (TextView) findViewById(R.id.translated_tag);
        TypedArray attributes = getContext().obtainStyledAttributes(attrs,
                R.styleable.translated_text, 0, 0);
        String tag = attributes.getString(R.styleable.translated_text_translation);
        tagText.setText(ThinkMachineTranslator.getTranslatedText(tag) + " ");
    }

    public <T extends Element<?>> void setAdapter(ElementAdapter<T> adapter) {
        Spinner selector = (Spinner) findViewById(R.id.spinner);
        selector.setAdapter(adapter);
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        Spinner selector = (Spinner) findViewById(R.id.spinner);
        selector.setOnItemSelectedListener(onItemSelectedListener);
    }

    public <T extends Element<?>> void setSelection(T selected) {
        Spinner selector = (Spinner) findViewById(R.id.spinner);
        selector.setSelection(((ElementAdapter<T>) selector.getAdapter()).indexOf(selected));
    }


}