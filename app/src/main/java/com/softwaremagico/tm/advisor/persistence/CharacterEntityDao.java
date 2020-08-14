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

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class CharacterEntityDao extends BaseEntityDao<CharacterEntity> {

    @Query("SELECT COUNT(*) FROM " + CharacterEntity.CHARACTER_PLAYER_TABLE)
    public abstract long getRowCount();

    @Query("SELECT * FROM " + CharacterEntity.CHARACTER_PLAYER_TABLE)
    public abstract List<CharacterEntity> getAll();

    @Query("SELECT * FROM " + CharacterEntity.CHARACTER_PLAYER_TABLE + " WHERE id IN (:ids)")
    public abstract List<CharacterEntity> loadAllByIds(long[] ids);

    @Query("SELECT * FROM " + CharacterEntity.CHARACTER_PLAYER_TABLE + " WHERE id=:id")
    public abstract CharacterEntity get(long id);

}
