package com.softwaremagico.tm.advisor;

import com.softwaremagico.tm.advisor.ui.configuration.ModuleManager;
import com.softwaremagico.tm.character.CharacterPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CharacterManager {
    private static List<CharacterPlayer> characters = new ArrayList<>();
    private static CharacterPlayer selectedCharacter;

    public static CharacterPlayer getSelectedCharacter() {
        if (characters.isEmpty()) {
            addNewCharacter();
        }
        return selectedCharacter;
    }

    public static void addNewCharacter() {
        CharacterPlayer characterPlayer = new CharacterPlayer(Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE);
        characters.add(characterPlayer);
        selectedCharacter = characterPlayer;
    }

}
