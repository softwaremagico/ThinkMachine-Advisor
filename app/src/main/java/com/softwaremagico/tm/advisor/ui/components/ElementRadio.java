package com.softwaremagico.tm.advisor.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.spinner.HelpElement;

public class ElementRadio<T extends Element<T>> extends HelpElement<T> {
    private RadioButton radioButton;
    private T element;

    public ElementRadio(Context context, T element) {
        this(context, null, element);
    }

    public ElementRadio(Context context, AttributeSet attrs, T element) {
        super(context, attrs);
        setElement(element);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.element_radio, this);
        initComponents(attrs);
    }

    @Override
    protected void initComponents(AttributeSet attrs) {
        super.initComponents(attrs);
        radioButton = findViewById(R.id.radio);
    }

    public void setElement(T element) {
        this.element = element;
        final TextView tagText = findViewById(R.id.translated_tag);
        tagText.setText(element.getName());
        if (element.getDescription() == null || element.getDescription().isEmpty()) {
            getHelpButton().setVisibility(ImageView.INVISIBLE);
        } else {
            getHelpButton().setVisibility(ImageView.VISIBLE);
        }
    }

    public void setChecked(boolean checked) {
        radioButton.setChecked(checked);
    }

    public boolean isChecked() {
        return radioButton.isChecked();
    }

    @Override
    public T getSelection() {
        return element;
    }

    @Override
    public void setEnabled(boolean enabled) {
        radioButton.setEnabled(enabled);
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        radioButton.setOnCheckedChangeListener(listener);
    }

}
