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
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwaremagico.tm.advisor.R;

public class Counter extends Component {
    private static final int DURATION = 500;
    private TextView tagText;
    private ImageView gearImage;
    private TextView valueText;
    private int currentValue = 0;

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
        tagText = findViewById(R.id.tag);
        gearImage = findViewById(R.id.gear);
        valueText = findViewById(R.id.value);
    }

    public void setValue(int value, boolean animation) {
        valueText = findViewById(R.id.value);
        valueText.setText(value + "");
        if (animation) {
            if (currentValue < value) {
                rotate(45f * (value - currentValue));
            } else {
                rotate(-45f * (currentValue - value));
            }
        }
        currentValue = value;
    }

    public void setTag(String tag) {
        tagText = findViewById(R.id.tag);
        tagText.setText(tag);
    }

    public void setTag(int string) {
        tagText = findViewById(R.id.tag);
        tagText.setText(string);
    }

    private void rotate(float angle) {
        final Animation animation = new RotateAnimation(0.0f, angle, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(DURATION);
        gearImage.startAnimation(animation);
    }


}
