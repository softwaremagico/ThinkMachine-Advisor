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

package com.softwaremagico.tm.advisor.ui.session;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.persistence.SettingsHandler;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RandomizeCharacter;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.exceptions.RestrictedElementException;
import com.softwaremagico.tm.character.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.InvalidFactionException;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.races.InvalidRaceException;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.predefined.characters.Npc;
import com.softwaremagico.tm.random.predefined.profile.RandomProfile;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class CharacterManager {
    private static final List<CharacterPlayer> characters = new ArrayList<>();
    private static CharacterPlayer selectedCharacter;
    private static CostCalculator costCalculator;
    private static final Set<CharacterSelectedListener> characterSelectedListener = new HashSet<>();
    private static final Set<CharacterUpdatedListener> characterUpdatedListener = new HashSet<>();
    private static final Set<CharacterRaceUpdatedListener> characterRaceUpdatedListener = new HashSet<>();
    private static final Set<CharacterAgeUpdatedListener> characterAgeUpdatedListener = new HashSet<>();
    private static final Set<CharacterPlanetUpdatedListener> characterPlanetUpdatedListener = new HashSet<>();
    private static final Set<CharacterFactionUpdatedListener> characterFactionUpdatedListener = new HashSet<>();
    private static final Set<CharacterCharacteristicUpdatedListener> characterCharacteristicUpdatedListener = new HashSet<>();
    private static final Set<CharacterSettingsUpdatedListener> characterSettingsUpdatedListeners = new HashSet<>();
    private static boolean updatingCharacter = false;

    public interface CharacterSelectedListener {
        void selected(CharacterPlayer characterPlayer);
    }

    public interface CharacterUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface CharacterRaceUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface CharacterAgeUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface CharacterPlanetUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface CharacterFactionUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface CharacterCharacteristicUpdatedListener {
        void updated(CharacterPlayer characterPlayer, CharacteristicName characteristic);
    }

    public interface CharacterSettingsUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    private CharacterManager() {

    }

    public static void updateSettings() {
        launchCharacterSettingsUpdateListeners(getSelectedCharacter());
    }

    public static void launchCharacterSettingsUpdateListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterSettingsUpdatedListener listener : characterSettingsUpdatedListeners) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchSelectedCharacterListeners(CharacterPlayer characterPlayer) {
        updatingCharacter = true;
        for (final CharacterSelectedListener listener : characterSelectedListener) {
            listener.selected(characterPlayer);
        }
        updatingCharacter = false;
    }

    private static void launchCharacterUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterUpdatedListener listener : characterUpdatedListener) {
                listener.updated(characterPlayer);
            }
        }
    }

    private static void launchCharacterRaceUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterRaceUpdatedListener listener : characterRaceUpdatedListener) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchCharacterAgeUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterAgeUpdatedListener listener : characterAgeUpdatedListener) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchCharacterPlanetUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterPlanetUpdatedListener listener : characterPlanetUpdatedListener) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchCharacterFactionUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterFactionUpdatedListener listener : characterFactionUpdatedListener) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchCharacterCharacteristicsUpdatedListeners(CharacterPlayer characterPlayer, CharacteristicName characteristicName) {
        if (!updatingCharacter) {
            for (final CharacterCharacteristicUpdatedListener listener : characterCharacteristicUpdatedListener) {
                listener.updated(characterPlayer, characteristicName);
            }
        }
    }

    public static void addCharacterSettingsUpdateListeners(CharacterSettingsUpdatedListener listener) {
        characterSettingsUpdatedListeners.add(listener);
    }

    public static void addSelectedCharacterListener(CharacterSelectedListener listener) {
        characterSelectedListener.add(listener);
    }

    public static void addCharacterUpdatedListener(CharacterUpdatedListener listener) {
        characterUpdatedListener.add(listener);
    }

    public static void addCharacterRaceUpdatedListener(CharacterRaceUpdatedListener listener) {
        characterRaceUpdatedListener.add(listener);
    }

    public static void addCharacterAgeUpdatedListener(CharacterAgeUpdatedListener listener) {
        characterAgeUpdatedListener.add(listener);
    }

    public static void addCharacterPlanetUpdatedListener(CharacterPlanetUpdatedListener listener) {
        characterPlanetUpdatedListener.add(listener);
    }

    public static void addCharacterFactionUpdatedListener(CharacterFactionUpdatedListener listener) {
        characterFactionUpdatedListener.add(listener);
    }

    public static void addCharacterCharacteristicUpdatedListener(CharacterCharacteristicUpdatedListener listener) {
        characterCharacteristicUpdatedListener.add(listener);
    }

    public static boolean isStarted() {
        return !characters.isEmpty();
    }


    public synchronized static CharacterPlayer getSelectedCharacter() {
        if (characters.isEmpty()) {
            addNewCharacter();
        }
        return selectedCharacter;
    }

    public static void setCharacteristic(CharacteristicName characteristicName, int value) {
        getSelectedCharacter().setCharacteristic(characteristicName, value);
        launchCharacterCharacteristicsUpdatedListeners(getSelectedCharacter(), characteristicName);
    }

    public static void setRace(Race race) throws InvalidRaceException, RestrictedElementException, UnofficialElementNotAllowedException {
        getSelectedCharacter().setRace(race);
        if (costCalculator != null) {
            costCalculator.updateCost();
        }
        launchCharacterRaceUpdatedListeners(getSelectedCharacter());
        launchCharacterUpdatedListeners(getSelectedCharacter());
    }

    public static void setPlanet(Planet planet) {
        getSelectedCharacter().getInfo().setPlanet(planet);
        launchCharacterPlanetUpdatedListeners(getSelectedCharacter());
    }

    public static void setFaction(Faction faction) throws InvalidFactionException, RestrictedElementException, UnofficialElementNotAllowedException {
        getSelectedCharacter().setFaction(faction);
        launchCharacterFactionUpdatedListeners(getSelectedCharacter());
    }

    public static CostCalculator getCostCalculator() {
        if (costCalculator == null) {
            addNewCharacter();
        }
        return costCalculator;
    }

    public static void setSelectedCharacter(CharacterPlayer characterPlayer) {
        if (!characters.contains(characterPlayer)) {
            characters.add(characterPlayer);
        }
        selectedCharacter = characterPlayer;
        costCalculator = new CostCalculator(selectedCharacter);
        launchSelectedCharacterListeners(characterPlayer);
    }

    private static CharacterPlayer createNewCharacter() {
        final CharacterPlayer characterPlayer = new CharacterPlayer(Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE);
        characterPlayer.getSettings().copy(SettingsHandler.getSettingsEntity().get());
        characters.add(characterPlayer);
        return characterPlayer;
    }

    public static void addNewCharacter() {
        setSelectedCharacter(createNewCharacter());
    }

    public static void randomizeCharacter(Set<IRandomPreference> randomPreferences) throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(getSelectedCharacter(), 0, randomPreferences.toArray(new IRandomPreference[0]));
        randomizeCharacter.createCharacter();
        setSelectedCharacter(getSelectedCharacter());
    }

    public static void randomizeCharacterUsingProfiles(Set<RandomProfile> randomProfiles) throws InvalidXmlElementException, InvalidRandomElementSelectedException,
            RestrictedElementException, UnofficialElementNotAllowedException {
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(getSelectedCharacter(), randomProfiles.toArray(new RandomProfile[0]));
        randomizeCharacter.createCharacter();
        setSelectedCharacter(getSelectedCharacter());
    }

    public static void randomizeCharacterUsingNpc(Set<Npc> randomProfiles) throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        CharacterPlayer characterPlayer = createNewCharacter();
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, randomProfiles.toArray(new Npc[0]));
        randomizeCharacter.createCharacter();
        setSelectedCharacter(characterPlayer);
    }

}
