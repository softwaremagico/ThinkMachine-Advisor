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

package com.softwaremagico.tm.advisor.persistence;

import android.content.Context;

public final class SettingsHandler {
    private static volatile SettingsHandler instance;
    private static SettingsEntity settingsEntity;

    public static SettingsHandler getInstance() {
        if (instance == null) {
            synchronized (SettingsHandler.class) {
                if (instance == null) {
                    instance = new SettingsHandler();
                }
            }
        }
        return instance;
    }

    public static void save(Context context) {
        save(context, SettingsHandler.settingsEntity);
    }

    public static void save(Context context, SettingsEntity settingsEntity) {
        if (SettingsHandler.settingsEntity == null) {
            AppDatabase.getInstance(context).getSettingsEntityDao().persist(settingsEntity);
            SettingsHandler.settingsEntity = settingsEntity;
        } else {
            AppDatabase.getInstance(context).getSettingsEntityDao().update(settingsEntity);
        }
    }

    private static SettingsEntity load(Context context) {
        SettingsEntity settingsEntity = AppDatabase.getInstance(context).getSettingsEntityDao().get();
        if (settingsEntity == null) {
            settingsEntity = new SettingsEntity();
        }
        return settingsEntity;
    }

    public static SettingsEntity getSettingsEntity() {
        return settingsEntity;
    }

    public static void setSettingsEntity(Context context) {
        SettingsHandler.settingsEntity = load(context);
    }
}
