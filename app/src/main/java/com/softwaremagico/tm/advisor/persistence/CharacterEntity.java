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

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.json.CharacterJsonManager;
import com.softwaremagico.tm.json.InvalidJsonException;

import java.sql.Timestamp;
import java.util.Date;

@Entity(tableName = CharacterEntity.CHARACTER_PLAYER_TABLE)
public class CharacterEntity extends BaseEntity {
    public final static String CHARACTER_PLAYER_TABLE = "character_player";

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "race")
    public String race;

    @ColumnInfo(name = "faction")
    public String faction;

    @ColumnInfo(name = "character_as_json")
    public String json;

    public CharacterEntity() {
        creationTime = new Timestamp(new Date().getTime());
    }

    public CharacterEntity(CharacterPlayer characterPlayer) {
        this();
        setCharacterPlayer(characterPlayer);
    }

    public void setCharacterPlayer(CharacterPlayer characterPlayer) {
        updateTime = new Timestamp(new Date().getTime());
        setJson(CharacterJsonManager.toJson(characterPlayer));
        setName(characterPlayer.getCompleteNameRepresentation());
        setRace(characterPlayer.getRace().getNameRepresentation());
        setFaction(characterPlayer.getFaction().getNameRepresentation());
    }

    public CharacterPlayer getCharacterPlayer() {
        try {
            return CharacterJsonManager.fromJson(getJson());
        } catch (InvalidJsonException e) {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

}