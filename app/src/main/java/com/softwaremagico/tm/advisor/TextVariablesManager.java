package com.softwaremagico.tm.advisor;

public class TextVariablesManager {
    private static final String CHARACTER_NAME = "${CHARACTER_NAME}";

    public static String replace(String input) {
        return input.replace(CHARACTER_NAME, CharacterManager.getSelectedCharacter().getCompleteNameRepresentation());
    }
}
