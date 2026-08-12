package com.softwaremagico.tm.advisor.ui.session;

import com.softwaremagico.tm.advisor.persistence.SettingsHandler;

import org.junit.After;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertNotNull;

public class CharacterManagerTest {

    @After
    public void tearDown() throws Exception {
        CharacterManager.getCharacters().clear();
        setStaticField(SettingsHandler.class, "settingsEntity", null);
        setStaticField(CharacterManager.class, "selectedCharacter", null);
        setStaticField(CharacterManager.class, "costCalculator", null);
    }

    @Test
    public void getSelectedCharacterCreatesCharacterWhenNoneExists() throws Exception {
        CharacterManager.getCharacters().clear();
        setStaticField(SettingsHandler.class, "settingsEntity", null);
        setStaticField(CharacterManager.class, "selectedCharacter", null);
        setStaticField(CharacterManager.class, "costCalculator", null);

        assertNotNull(CharacterManager.getSelectedCharacter());
        assertNotNull(SettingsHandler.getSettingsEntity());
    }

    private static void setStaticField(Class<?> type, String fieldName, Object value) throws Exception {
        final Field field = type.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }
}
