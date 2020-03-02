package com.softwaremagico.tm.advisor.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;

public class TranslatedTextView extends androidx.appcompat.widget.AppCompatTextView {

    public TranslatedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TranslatedText, 0, 0);
        String tag = a.getString(R.styleable.TranslatedText_translation);
        this.setHint(ThinkMachineTranslator.getTranslatedText(tag) + " ");
        a.recycle();
    }


}
