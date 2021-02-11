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

package com.softwaremagico.tm.advisor.persistence.factories;

import androidx.room.Dao;
import androidx.room.Query;

import com.softwaremagico.tm.advisor.persistence.BaseEntityDao;

import java.util.List;

@Dao
public abstract class WeaponsFactoryElementsDao extends BaseEntityDao<WeaponsFactoryElements> {

    @Query("SELECT COUNT(*) FROM " + WeaponsFactoryElements.WEAPONS_FACTORY_ELEMENTS_TABLE)
    public abstract long getRowCount();

    @Query("SELECT * FROM " + WeaponsFactoryElements.WEAPONS_FACTORY_ELEMENTS_TABLE)
    public abstract List<WeaponsFactoryElements> getAll();

    @Query("SELECT * FROM " + WeaponsFactoryElements.WEAPONS_FACTORY_ELEMENTS_TABLE + " WHERE id IN (:ids)")
    public abstract List<WeaponsFactoryElements> loadAllByIds(long... ids);

    @Query("SELECT * FROM " + WeaponsFactoryElements.WEAPONS_FACTORY_ELEMENTS_TABLE + " WHERE id=:id")
    public abstract WeaponsFactoryElements get(long id);

    @Query("SELECT * FROM " + WeaponsFactoryElements.WEAPONS_FACTORY_ELEMENTS_TABLE + " WHERE version=:version AND language=:language AND module_name=:moduleName")
    public abstract WeaponsFactoryElements getByVersion(int version, String language, String moduleName);

}
