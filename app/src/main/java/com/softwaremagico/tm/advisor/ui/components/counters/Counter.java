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

package com.softwaremagico.tm.advisor.ui.components.counters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.Component;
import com.softwaremagico.tm.character.CharacterPlayer;

public abstract class Counter extends Component {
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

    protected void initComponents(AttributeSet attrs) {
        tagText = findViewById(R.id.tag);
        gearImage = findViewById(R.id.gear);
        valueText = findViewById(R.id.value);
    }

    public void setValue(int value, boolean animation) {
        valueText.setText(value + "");
        if (animation) {
            if (currentValue != value) {
                rotate(45f * (float) (value - currentValue), gearImage);
            }
        }
        setCurrentValue(value);
        setColor();
    }

    protected int getCurrentValue() {
        return currentValue;
    }

    protected void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    private void setColor() {
        if (currentValue < 0) {
            tagText.setTextColor(ContextCompat.getColor(getContext(), R.color.counterError));
            valueText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorContrast));
            gearImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.counterError), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            tagText.setTextColor(ContextCompat.getColor(getContext(), getGearColor()));
            valueText.setTextColor(ContextCompat.getColor(getContext(), getTextColor()));
            gearImage.setColorFilter(ContextCompat.getColor(getContext(), getGearColor()), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }


    public abstract void setCharacter(CharacterPlayer character);

    protected abstract int getGearColor();

    protected abstract int getTextColor();

    public void setTag(int string) {
        tagText = findViewById(R.id.tag);
        tagText.setText(string);
    }

    private void rotate(float angle, ImageView gearImage) {
        final Animation animation = new RotateAnimation(0.0f, angle, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(DURATION);
        gearImage.startAnimation(animation);
    }


}
