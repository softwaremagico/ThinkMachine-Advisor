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

import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.advisor.R;

public class SegmentCounter extends Counter {
    private static final int DURATION = 500;
    private TextView tagText2;
    private ImageView gearImage2;
    private TextView valueText2;
    private int totalValue;
    private int gearColor2 = R.color.colorPrimaryDark;
    private int textColor2 = R.color.colorContrast;

    public SegmentCounter(Context context) {
        super(context, null);
    }

    public SegmentCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.segment_counter, this);
        initComponents(attrs);
    }

    @Override
    protected void initComponents(AttributeSet attrs) {
        super.initComponents(attrs);
        tagText2 = findViewById(R.id.tag2);
        gearImage2 = findViewById(R.id.gear2);
        valueText2 = findViewById(R.id.value2);
    }

    public void setValue(int value, int totalValue, boolean animation) {
        int currentValue = super.getCurrentValue();
        valueText2.setText(totalValue + "");
        super.setValue(value, animation);
        if (animation) {
            //First gear rotate, rotate this one but clockwise.
            if (currentValue != value) {
                rotate(-45f * (float) (value - currentValue), gearImage2);
            }
        }
        setColor();
        setTotalValue(totalValue);
    }

    protected int getTotalValue() {
        return totalValue;
    }

    protected void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    private void setColor() {
        if (getTotalValue() < 0) {
            tagText2.setTextColor(ContextCompat.getColor(getContext(), R.color.counterError));
            valueText2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorContrast));
            gearImage2.setColorFilter(ContextCompat.getColor(getContext(), R.color.counterError), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            tagText2.setTextColor(ContextCompat.getColor(getContext(), getGearColor2()));
            valueText2.setTextColor(ContextCompat.getColor(getContext(), getTextColor2()));
            gearImage2.setColorFilter(ContextCompat.getColor(getContext(), getGearColor2()), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    public int getGearColor2() {
        return gearColor2;
    }

    public void setGearColor2(int gearColor) {
        this.gearColor2 = gearColor;
    }

    public int getTextColor2() {
        return textColor2;
    }

    public void setTextColor2(int textColor) {
        this.textColor2 = textColor;
    }

    public void setTag2(String tag) {
        tagText2 = findViewById(R.id.tag2);
        tagText2.setText(tag);
    }

    public void setTag2(int string) {
        tagText2 = findViewById(R.id.tag2);
        tagText2.setText(string);
    }

    private void rotate(float angle, ImageView gearImage) {
        final Animation animation = new RotateAnimation(0.0f, angle, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(DURATION);
        gearImage.startAnimation(animation);
    }


}
