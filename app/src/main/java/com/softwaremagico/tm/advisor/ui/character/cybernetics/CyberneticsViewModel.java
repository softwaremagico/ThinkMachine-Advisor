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

package com.softwaremagico.tm.advisor.ui.character.cybernetics;

import androidx.lifecycle.ViewModel;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.cybernetics.CyberneticDevice;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceFactory;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.log.MachineLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CyberneticsViewModel extends ViewModel {


    public List<CyberneticDevice> getAvailableCybernetics() {
        try {
            return CyberneticDeviceFactory.getInstance().getElements(Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE);
        } catch (InvalidXmlElementException | NullPointerException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }


}
