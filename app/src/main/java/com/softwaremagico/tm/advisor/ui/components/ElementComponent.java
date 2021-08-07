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
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.spinner.HelpElement;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;

public abstract class ElementComponent<T extends Element<T>> extends Component {

    public interface SetElementColor {
        void setColor(int color);
    }

    public ElementComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void refreshElementColor(T element, HelpElement.SetElementColor colorSetter) {
        if (element == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (element.isRestricted() || element.isRestricted(CharacterManager.getSelectedCharacter())) {
                colorSetter.setColor(ContextCompat.getColor(getContext(), R.color.restricted));
            } else if (!element.isOfficial()) {
                colorSetter.setColor(ContextCompat.getColor(getContext(), R.color.unofficialElement));
            } else {
           //     colorSetter.setColor(ContextCompat.getColor(getContext(), R.color.colorNormal));
            }
        }
    }
}
