package com.softwaremagico.tm.advisor.ui.character.info;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterInfoFragmentCharacterTest {

    @Test
    public void matchesRestrictionAllowsNullOrEmptyRestrictions() {
        assertTrue(CharacterInfoFragmentCharacter.matchesRestriction(null, "value"));
        assertTrue(CharacterInfoFragmentCharacter.matchesRestriction(Collections.emptyList(), "value"));
    }

    @Test
    public void matchesRestrictionAllowsNullSelectedValue() {
        assertTrue(CharacterInfoFragmentCharacter.matchesRestriction(Arrays.asList("value"), null));
    }

    @Test
    public void matchesRestrictionRejectsValuesOutsideRestrictionList() {
        assertFalse(CharacterInfoFragmentCharacter.matchesRestriction(Arrays.asList("value"), "other"));
    }
}
