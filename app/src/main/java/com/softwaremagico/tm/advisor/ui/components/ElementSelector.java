package com.softwaremagico.tm.advisor.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.spinner.HelpElement;

public class ElementSelector<T extends Element<T>> extends HelpElement<T> {
    private CheckBox checkBox;
    private T element;

    public ElementSelector(Context context, T element) {
        this(context, null, element);
    }

    public ElementSelector(Context context, AttributeSet attrs, T element) {
        super(context, attrs);
        setElement(element);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.element_selector, this);
        initComponents(attrs);
    }

    @Override
    protected void initComponents(AttributeSet attrs) {
        super.initComponents(attrs);
        checkBox = findViewById(R.id.checkbox);
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
        refreshElementColor(element, tagText::setTextColor);
    }

    public void setChecked(boolean checked) {
        checkBox.setChecked(checked);
    }

    public boolean isChecked() {
        return checkBox.isChecked();
    }

    @Override
    public T getSelection() {
        return element;
    }

    @Override
    public void setEnabled(boolean enabled) {
        checkBox.setEnabled(enabled);
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        checkBox.setOnCheckedChangeListener(listener);
    }

    @Override
    public String toString() {
        return element != null ? element.toString() : "null";
    }

}
