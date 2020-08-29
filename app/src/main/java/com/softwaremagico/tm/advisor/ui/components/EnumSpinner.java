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
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;

public class EnumSpinner extends Component {

    public EnumSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.enum_spinner, this);
        initComponents(attrs);
    }

    private void initComponents(AttributeSet attrs) {
        TextView tagText = findViewById(R.id.translated_tag);
        TypedArray attributes = getContext().obtainStyledAttributes(attrs,
                R.styleable.translated_text, 0, 0);
        String tag = attributes.getString(R.styleable.translated_text_translation);
        tagText.setText(ThinkMachineTranslator.getTranslatedText(tag) + " ");
        tagText.setTextAppearance(R.style.CharacterInfo);
    }

    public <T extends Enum<?>> void setAdapter(EnumAdapter<T> adapter) {
        Spinner selector = findViewById(R.id.spinner);
        selector.setAdapter(adapter);
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        Spinner selector = findViewById(R.id.spinner);
        selector.setOnItemSelectedListener(onItemSelectedListener);
    }

    public <T extends Enum<?>> void setSelection(T selected) {
        Spinner selector = findViewById(R.id.spinner);
        selector.setSelection(((EnumAdapter<T>) selector.getAdapter()).indexOf(selected));
    }


}
