package com.softwaremagico.tm.advisor.persistence;

import org.junit.After;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SettingsHandlerTest {

    @After
    public void tearDown() throws Exception {
        setStaticField("settingsEntity", null);
    }

    @Test
    public void getSettingsEntityInitializesDefaultSettings() throws Exception {
        setStaticField("settingsEntity", null);

        final SettingsEntity settingsEntity = SettingsHandler.getSettingsEntity();

        assertNotNull(settingsEntity);
        assertFalse(settingsEntity.isOnlyOfficialAllowed());
        assertTrue(settingsEntity.isRestrictionsChecked());
    }

    private static void setStaticField(String fieldName, Object value) throws Exception {
        final Field field = SettingsHandler.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }
}
