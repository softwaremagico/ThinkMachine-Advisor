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

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {CharacterEntity.class, SettingsEntity.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "ThinkMachine Database";

    private static volatile AppDatabase instance;

    public abstract CharacterEntityDao getCharacterEntityDao();

    public abstract SettingsEntityDao getSettingsEntityDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                            AppDatabase.class, DATABASE_NAME).allowMainThreadQueries()
                            .addMigrations(MIGRATION_1_2).addMigrations(MIGRATION_2_3).build();
                }
            }
        }
        return instance;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE " + CharacterEntity.CHARACTER_PLAYER_TABLE + " "
                    + "ADD COLUMN player TEXT");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS " + SettingsEntity.SETTINGS_TABLE + " "
                    + "(`creation_time` INTEGER DEFAULT CURRENT_TIMESTAMP, `update_time` INTEGER DEFAULT CURRENT_TIMESTAMP, `only_official_allowed` BOOLEAN, `restrictions_checked` BOOLEAN, " +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
        }
    };
}

