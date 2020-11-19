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

package com.softwaremagico.tm.advisor.ui.load;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.softwaremagico.tm.advisor.log.AdvisorLog;

import java.lang.reflect.Method;

public class ExpandAndCollapseViewUtil {

    public static void expand(final ViewGroup viewGroup, int duration) {
        slide(viewGroup, duration, true);
    }

    public static void collapse(final ViewGroup viewGroup, int duration) {
        slide(viewGroup, duration, false);
    }

    private static void slide(final ViewGroup viewGroup, int duration, final boolean expand) {
        try {
            //onmeasure method is protected
            final Method onMeasureMethod = viewGroup.getClass().getDeclaredMethod("onMeasure", int.class, int.class);
            onMeasureMethod.setAccessible(true);
            onMeasureMethod.invoke(
                    viewGroup,
                    View.MeasureSpec.makeMeasureSpec(((View) viewGroup.getParent()).getMeasuredWidth(), View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );
        } catch (Exception e) {
            AdvisorLog.errorMessage(ExpandAndCollapseViewUtil.class.getName(), e);
        }

        final int initialHeight = viewGroup.getMeasuredHeight();

        if (expand) {
            viewGroup.getLayoutParams().height = 0;
        } else {
            viewGroup.getLayoutParams().height = initialHeight;
        }
        viewGroup.setVisibility(View.VISIBLE);

        final Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int newHeight = 0;
                if (expand) {
                    newHeight = (int) (initialHeight * interpolatedTime);
                } else {
                    newHeight = (int) (initialHeight * (1 - interpolatedTime));
                }
                viewGroup.getLayoutParams().height = newHeight;
                viewGroup.requestLayout();

                if (interpolatedTime == 1 && !expand) {
                    viewGroup.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setDuration(duration);
        viewGroup.startAnimation(animation);
    }
}
