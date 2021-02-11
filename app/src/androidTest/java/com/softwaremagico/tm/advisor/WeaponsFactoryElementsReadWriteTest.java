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
import com.softwaremagico.tm.advisor.persistence.factories.WeaponsFactoryElements;
import com.softwaremagico.tm.advisor.persistence.factories.WeaponsFactoryElementsDao;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.file.PathManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
public class WeaponsFactoryElementsReadWriteTest {
    private static final String LANGUAGE = "en";
    private WeaponsFactoryElementsDao weaponsFactoryElementsDao;
    private AppDatabase appDatabase;

    @Before
    public void createDb() {
        final Context context = ApplicationProvider.getApplicationContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        weaponsFactoryElementsDao = appDatabase.getWeaponsFactoryElementsDao();
    }

    @After
    public void closeDb() {
        appDatabase.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        WeaponsFactoryElements weaponsFactoryElements = new WeaponsFactoryElements(WeaponFactory.getInstance().getVersion(PathManager.DEFAULT_MODULE_FOLDER),
                WeaponFactory.getInstance().getNumberOfElements(PathManager.DEFAULT_MODULE_FOLDER),
                LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER,
                WeaponFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));

        Assert.assertEquals(0, weaponsFactoryElementsDao.getRowCount());

        final long id = weaponsFactoryElementsDao.persist(weaponsFactoryElements);
        Assert.assertEquals(1, id);
        Assert.assertEquals(1, weaponsFactoryElementsDao.getRowCount());

        WeaponsFactoryElements storedWeaponsFactoryElements = weaponsFactoryElementsDao.get(id);
        Assert.assertEquals((int) WeaponFactory.getInstance().getNumberOfElements(PathManager.DEFAULT_MODULE_FOLDER), storedWeaponsFactoryElements.getElements().size());

        storedWeaponsFactoryElements = weaponsFactoryElementsDao.getByVersion(WeaponFactory.getInstance().getVersion(PathManager.DEFAULT_MODULE_FOLDER), LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertEquals((int) WeaponFactory.getInstance().getNumberOfElements(PathManager.DEFAULT_MODULE_FOLDER), storedWeaponsFactoryElements.getElements().size());
    }

}
