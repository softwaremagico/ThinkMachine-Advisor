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

package com.softwaremagico.tm.advisor.ui.character.equipment;

import androidx.lifecycle.ViewModel;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.log.MachineLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class EquipmentViewModel extends ViewModel {

    public List<Weapon> getAvailableMeleeWeapons(boolean nonOfficial) {
        try {
            if (nonOfficial) {
                return WeaponFactory.getInstance().getMeleeWeapons(Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE).
                        stream().filter(Objects::nonNull).collect(Collectors.toList());
            } else {
                return WeaponFactory.getInstance().getMeleeWeapons(Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE).
                        stream().filter(Objects::nonNull).filter(Element::isOfficial).collect(Collectors.toList());
            }
        } catch (InvalidXmlElementException | NullPointerException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }


    public List<Weapon> getAvailableRangedWeapons(boolean nonOfficial) {
        try {
            if (nonOfficial) {
                return WeaponFactory.getInstance().getRangedWeapons(Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE).
                        stream().filter(Objects::nonNull).collect(Collectors.toList());
            } else {
                return WeaponFactory.getInstance().getRangedWeapons(Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE).
                        stream().filter(Objects::nonNull).filter(Element::isOfficial).collect(Collectors.toList());
            }
        } catch (InvalidXmlElementException | NullPointerException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }

    public List<Armour> getAvailableArmours(boolean nonOfficial) {
        try {
            if (nonOfficial) {
                return new ArrayList<>(ArmourFactory.getInstance().getElements(Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE)).
                        stream().filter(Objects::nonNull).collect(Collectors.toList());
            } else {
                return new ArrayList<>(ArmourFactory.getInstance().getElements(Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE)).
                        stream().filter(Objects::nonNull).filter(Element::isOfficial).collect(Collectors.toList());
            }
        } catch (InvalidXmlElementException | NullPointerException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }

    public List<Shield> getAvailableShields(boolean nonOfficial) {
        try {
            if (nonOfficial) {
                return new ArrayList<>(ShieldFactory.getInstance().getElements(Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE)).
                        stream().filter(Objects::nonNull).collect(Collectors.toList());
            } else {
                return new ArrayList<>(ShieldFactory.getInstance().getElements(Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE)).
                        stream().filter(Objects::nonNull).filter(Element::isOfficial).collect(Collectors.toList());
            }
        } catch (InvalidXmlElementException | NullPointerException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }


}
