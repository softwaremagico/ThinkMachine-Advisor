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

package com.softwaremagico.tm.advisor;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.softwaremagico.tm.advisor.persistence.AppDatabase;
import com.softwaremagico.tm.advisor.persistence.SettingsEntity;
import com.softwaremagico.tm.advisor.persistence.SettingsEntityDao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
public class SettingsEntityReadWriteTest {
    private static final String LANGUAGE = "en";
    private SettingsEntityDao settingsEntityDao;
    private AppDatabase appDatabase;

    @Before
    public void createDb() {
        final Context context = ApplicationProvider.getApplicationContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        settingsEntityDao = appDatabase.getSettingsEntityDao();
    }

    @After
    public void closeDb() {
        appDatabase.close();
    }

    @Test
    public void writeSettingsAndReadInList() {


        Assert.assertEquals(0, settingsEntityDao.getRowCount());

        final SettingsEntity settingsEntity = new SettingsEntity();
        settingsEntity.setOnlyOfficialAllowed(true);
        settingsEntity.setOnlyOfficialAllowed(true);
        final long id = settingsEntityDao.persist(settingsEntity);
        Assert.assertEquals(1, id);
        Assert.assertEquals(1, settingsEntityDao.getRowCount());
        final SettingsEntity storedSettingsEntity = settingsEntityDao.get();
        Assert.assertNotNull(storedSettingsEntity);
        Assert.assertEquals(storedSettingsEntity.getCreationTime(), settingsEntity.getCreationTime());
        Assert.assertEquals(storedSettingsEntity.isOnlyOfficialAllowed(), settingsEntity.isOnlyOfficialAllowed());
        Assert.assertEquals(storedSettingsEntity.isRestrictionsIgnored(), settingsEntity.isRestrictionsIgnored());

        //Check that if we insert a second time, only one is inserted.
        settingsEntityDao.persist(settingsEntity);
        Assert.assertEquals(1, settingsEntityDao.getRowCount());
    }

}
