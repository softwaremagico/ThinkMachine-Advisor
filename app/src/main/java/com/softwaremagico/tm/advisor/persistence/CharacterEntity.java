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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.ThreatLevel;
import com.softwaremagico.tm.json.CharacterJsonManager;
import com.softwaremagico.tm.json.InvalidJsonException;

import java.sql.Timestamp;
import java.util.Date;

@Entity(tableName = CharacterEntity.CHARACTER_PLAYER_TABLE)
public class CharacterEntity extends BaseEntity {
    public static final String CHARACTER_PLAYER_TABLE = "character_player";

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "player")
    public String player;

    @ColumnInfo(name = "race")
    public String race;

    @ColumnInfo(name = "faction")
    public String faction;

    @ColumnInfo(name = "threat")
    public int threat;

    @ColumnInfo(name = "character_as_json")
    public String json;

    public CharacterEntity() {
        super();
        creationTime = new Timestamp(new Date().getTime());
    }

    public CharacterEntity(CharacterPlayer characterPlayer) {
        this();
        setCharacterPlayer(characterPlayer);
    }

    public final void setCharacterPlayer(CharacterPlayer characterPlayer) {
        updateTime = new Timestamp(new Date().getTime());
        setJson(CharacterJsonManager.toJson(characterPlayer));
        setName(characterPlayer.getCompleteNameRepresentation());
        if (characterPlayer.getRace() != null) {
            setRace(characterPlayer.getRace().getNameRepresentation());
        }
        if (characterPlayer.getFaction() != null) {
            setFaction(characterPlayer.getFaction().getNameRepresentation());
        }
        setPlayer(characterPlayer.getInfo().getPlayer());
        try {
            setThreat(ThreatLevel.getThreatLevel(characterPlayer));
        } catch (InvalidXmlElementException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }
    }

    public CharacterPlayer getCharacterPlayer() {
        try {
            return CharacterJsonManager.fromJson(getJson());
        } catch (InvalidJsonException e) {
            return null;
        }
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String getRace() {
        return race;
    }

    public final void setRace(String race) {
        this.race = race;
    }

    public final String getFaction() {
        return faction;
    }

    public final void setFaction(String faction) {
        this.faction = faction;
    }

    public final String getJson() {
        return json;
    }

    public final void setJson(String json) {
        this.json = json;
    }

    public final int getThreat() {
        return threat;
    }

    public final void setThreat(int threat) {
        this.threat = threat;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }
}
