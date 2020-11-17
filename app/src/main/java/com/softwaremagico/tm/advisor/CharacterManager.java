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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.file.modules.ModuleManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CharacterManager {
    private static List<CharacterPlayer> characters = new ArrayList<>();
    private static CharacterPlayer selectedCharacter;
    private static CostCalculator costCalculator;
    private static Set<CharacterSelectedListener> characterSelectedListener = new HashSet<>();

    public interface CharacterSelectedListener {
        void selected(CharacterPlayer characterPlayer);
    }

    private static void launchSelectedCharacterListeners(CharacterPlayer characterPlayer) {
        for (CharacterSelectedListener listener : characterSelectedListener) {
            listener.selected(characterPlayer);
        }
    }

    public static void addSelectedCharacterListener(CharacterSelectedListener listener) {
        characterSelectedListener.add(listener);
    }

    public static CharacterPlayer getSelectedCharacter() {
        if (characters.isEmpty()) {
            addNewCharacter();
        }
        return selectedCharacter;
    }

    public static CostCalculator getCostCalculator() {
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

    public static void addNewCharacter() {
        CharacterPlayer characterPlayer = new CharacterPlayer(Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE);
        characters.add(characterPlayer);
        selectedCharacter = characterPlayer;
        costCalculator = new CostCalculator(characterPlayer);
        //launchSelectedCharacterListeners(characterPlayer);
    }

}
