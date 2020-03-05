package com.softwaremagico.tm.advisor.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;

public class TranslatedEditText extends LinearLayout {

    public TranslatedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.translated_edit_text, this);
        initComponents(attrs);
    }

    private void initComponents(AttributeSet attrs) {
        TextView tagText = (TextView) findViewById(R.id.translated_tag);
        TypedArray attributes = getContext().obtainStyledAttributes(attrs,
                R.styleable.translated_text, 0, 0);
        String tag = attributes.getString(R.styleable.translated_text_translation);
        tagText.setText(ThinkMachineTranslator.getTranslatedText(tag) + " ");
    }

    public void setAsNumberEditor(){
        EditText tagText = (EditText) findViewById(R.id.input);
        tagText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public void setText(String text){
        EditText tagText = (EditText) findViewById(R.id.input);
        tagText.setText(text);
    }

    public String getText(){
        EditText tagText = (EditText) findViewById(R.id.input);
        return tagText.getText().toString();
    }

    public void addTextChangedListener(TextWatcher watcher){
        EditText tagText = (EditText) findViewById(R.id.input);
        tagText.addTextChangedListener(watcher);
    }



}
