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
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CharacterEntityDao {
    @Query("SELECT * FROM " + CharacterEntity.CHARACTER_PLAYER_TABLE)
    List<CharacterEntity> getAll();

    @Query("SELECT * FROM " + CharacterEntity.CHARACTER_PLAYER_TABLE + " WHERE uid IN (:userIds)")
    List<CharacterEntity> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM " + CharacterEntity.CHARACTER_PLAYER_TABLE + " WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    CharacterEntity findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CharacterEntity characterEntity);

    @Update
    public void update(CharacterEntity characterEntity);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CharacterEntity... characterEntities);

    @Delete
    void delete(CharacterEntity characterEntity);

}
