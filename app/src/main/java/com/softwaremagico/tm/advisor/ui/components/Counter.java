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
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;

public class Counter extends Component {

    public Counter(Context context) {
        this(context, null);
    }

    public Counter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.counter, this);
        initComponents(attrs);
    }

    private void initComponents(AttributeSet attrs) {
        final TextView tagText = findViewById(R.id.value);

        tagText.setTextAppearance(R.style.CharacterInfo);
    }

    public <T extends Element<?>> void setAdapter(ElementAdapter<T> adapter) {
        final Spinner selector = findViewById(R.id.spinner);
        selector.setAdapter(adapter);
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        final Spinner selector = findViewById(R.id.spinner);
        selector.setOnItemSelectedListener(onItemSelectedListener);
    }

    public <T extends Element<?>> void setSelection(T selected) {
        final Spinner selector = findViewById(R.id.spinner);
        if (selected == null) {
            selector.setSelection(0);
        } else {
            selector.setSelection(((ElementAdapter<T>) selector.getAdapter()).indexOf(selected));
        }
    }

    public <T extends Element<?>> T getSelection() {
        final Spinner selector = findViewById(R.id.spinner);
        final T selectedItem = (T) selector.getSelectedItem();
        if (Element.isNull(selectedItem)) {
            return null;
        }
        return selectedItem;
    }


}
