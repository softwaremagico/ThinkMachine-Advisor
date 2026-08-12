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

import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.character.CharacterPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CharacterHandler {
    private Map<CharacterPlayer, CharacterEntity> entities = new HashMap<>();

    private static volatile CharacterHandler instance;

    public static CharacterHandler getInstance() {
        if (instance == null) {
            synchronized (CharacterHandler.class) {
                if (instance == null) {
                    instance = new CharacterHandler();
                }
            }
        }
        return instance;
    }

    public void save(Context context, CharacterPlayer characterPlayer) {
        if (entities.get(characterPlayer) == null) {
            final CharacterEntity characterEntity = new CharacterEntity(characterPlayer);
            AppDatabase.getInstance(context).getCharacterEntityDao().persist(characterEntity);
            entities.put(characterPlayer, characterEntity);
        } else {
            entities.get(characterPlayer).setCharacterPlayer(characterPlayer);
            AppDatabase.getInstance(context).getCharacterEntityDao().update(entities.get(characterPlayer));
        }
    }

    public void save(Context context, CharacterEntity characterEntity) {
        if (!entities.values().contains(characterEntity)) {
            AppDatabase.getInstance(context).getCharacterEntityDao().persist(characterEntity);
            entities.put(characterEntity.getCharacterPlayer(), characterEntity);
        } else {
            AppDatabase.getInstance(context).getCharacterEntityDao().update(characterEntity);
        }
    }

    public void delete(Context context, CharacterEntity characterEntity) {
        AppDatabase.getInstance(context).getCharacterEntityDao().delete(characterEntity);
        entities.remove(characterEntity.getCharacterPlayer());
    }

    public List<CharacterEntity> load(Context context) {
        List<CharacterEntity> loadedCharacters = AppDatabase.getInstance(context).getCharacterEntityDao().getAll();
        List<CharacterEntity> validCharacters = loadedCharacters.stream()
                .filter(characterEntity -> {
                    if (characterEntity.getCharacterPlayer() == null) {
                        AdvisorLog.warning(CharacterHandler.class.getName(), "Skipping character with invalid JSON data.");
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
        entities = validCharacters.stream().collect(Collectors.toMap(CharacterEntity::getCharacterPlayer, c -> c, (left, right) -> {
            AdvisorLog.warning(CharacterHandler.class.getName(), "Duplicated character entry found while loading; keeping the first one.");
            return left;
        }));
        return validCharacters;
    }

}
