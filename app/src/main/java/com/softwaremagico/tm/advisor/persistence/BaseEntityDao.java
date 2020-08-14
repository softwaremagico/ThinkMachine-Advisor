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
import androidx.room.Update;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Dao
public abstract class BaseEntityDao<T extends BaseEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long actualInsert(T t);

    public long persist(T t) {
        if (t.getId() == 0) {
            update(t);
        }
        t.setCreationTime(new Timestamp(new Date().getTime()));
        t.setUpdateTime(new Timestamp(new Date().getTime()));
        long id = actualInsert(t);
        t.setId(id);
        return id;
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract List<Long> actualInsertAll(List<T> ts);

    public List<Long> insertAll(List<T> ts) {
        if (ts != null) {
            for (T t : ts) {
                t.setCreationTime(new Timestamp(new Date().getTime()));
                t.setUpdateTime(new Timestamp(new Date().getTime()));
            }
        }
        return actualInsertAll(ts);
    }

    @Update
    public abstract void actualUpdate(T t);

    public void update(T t) {
        t.setUpdateTime(new Timestamp(new Date().getTime()));
        actualUpdate(t);
    }

    @Update
    public abstract void actualUpdateAll(List<T> ts);

    public void updateAll(List<T> ts) {
        if (ts != null) {
            for (T t : ts) {
                t.setUpdateTime(new Timestamp(new Date().getTime()));
            }
        }
        actualUpdateAll(ts);
    }

    @Delete
    public abstract void delete(T t);

    @Delete
    public abstract void deleteAll(List<T> ts);
}