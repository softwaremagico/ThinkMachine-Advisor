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

import com.softwaremagico.tm.advisor.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;

import java.util.HashMap;
import java.util.Map;

public class CharacterHandler {
    private Map<CharacterPlayer, CharacterEntity> entities;

    private static volatile CharacterHandler INSTANCE;

    public static CharacterHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (CharacterHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CharacterHandler();
                }
            }
        }
        return INSTANCE;
    }

    private void CharacterHander() {
        entities = new HashMap<>();
    }

    public void save(Context context, CharacterPlayer characterPlayer) {
        if (entities.get(characterPlayer) != null) {
            entities.get(characterPlayer).setCharacterPlayer(characterPlayer);
            AppDatabase.getInstance(context).getCharacterEntityDao().update(entities.get(characterPlayer));
        } else {
            AppDatabase.getInstance(context).getCharacterEntityDao().insert(new CharacterEntity(CharacterManager.getSelectedCharacter()));
        }
    }

}
