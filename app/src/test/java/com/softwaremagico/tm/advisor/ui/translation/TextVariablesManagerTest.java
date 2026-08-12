package com.softwaremagico.tm.advisor.ui.translation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextVariablesManagerTest {

    @Test
    public void replaceReturnsEmptyStringForNullInput() {
        assertEquals("", TextVariablesManager.replace(null));
    }
}
