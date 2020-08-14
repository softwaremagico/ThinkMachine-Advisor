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
import com.softwaremagico.tm.advisor.persistence.CharacterEntity;
import com.softwaremagico.tm.advisor.persistence.CharacterEntityDao;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RandomizeCharacter;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.random.selectors.DifficultLevelPreferences;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4ClassRunner.class)
public class CharacterEntityReadWriteTest {
    private static final String LANGUAGE = "en";
    private CharacterEntityDao characterEntityDao;
    private AppDatabase appDatabase;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        characterEntityDao = appDatabase.getCharacterEntityDao();
    }

    @After
    public void closeDb() throws IOException {
        appDatabase.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, DifficultLevelPreferences.HARD);
        randomizeCharacter.createCharacter();

        CharacterEntity characterEntity =  new CharacterEntity(characterPlayer);
        Assert.assertNotNull(characterEntity.getJson());
        long id = characterEntityDao.insert(characterEntity);
        CharacterEntity storedCharacterEntity = characterEntityDao.get(id);
        Assert.assertNotNull(storedCharacterEntity);
        Assert.assertEquals(storedCharacterEntity.getCreationTime(), characterEntity.getCreationTime());
        Assert.assertEquals(storedCharacterEntity.getJson(), characterEntity.getJson());
    }

}
