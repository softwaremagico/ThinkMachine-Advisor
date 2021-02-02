package com.softwaremagico.tm.advisor.core.random;

import com.softwaremagico.tm.random.selectors.AgePreferences;
import com.softwaremagico.tm.random.selectors.ArmourPreferences;
import com.softwaremagico.tm.random.selectors.BlessingNumberPreferences;
import com.softwaremagico.tm.random.selectors.BlessingPreferences;
import com.softwaremagico.tm.random.selectors.CharacteristicsPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.CurseNumberPreferences;
import com.softwaremagico.tm.random.selectors.CyberneticPointsPreferences;
import com.softwaremagico.tm.random.selectors.CyberneticTotalDevicesPreferences;
import com.softwaremagico.tm.random.selectors.CyberneticVisibilityPreferences;
import com.softwaremagico.tm.random.selectors.DifficultLevelPreferences;
import com.softwaremagico.tm.random.selectors.ExtraBeneficesNumberPreferences;
import com.softwaremagico.tm.random.selectors.FactionPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.NamesPreferences;
import com.softwaremagico.tm.random.selectors.PsiqueLevelPreferences;
import com.softwaremagico.tm.random.selectors.PsiquePathLevelPreferences;
import com.softwaremagico.tm.random.selectors.RacePreferences;
import com.softwaremagico.tm.random.selectors.ShieldPreferences;
import com.softwaremagico.tm.random.selectors.SkillGroupPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import com.softwaremagico.tm.random.selectors.StatusPreferences;
import com.softwaremagico.tm.random.selectors.TechnologicalPreferences;
import com.softwaremagico.tm.random.selectors.TraitCostPreferences;
import com.softwaremagico.tm.random.selectors.WeaponsPreferences;

public enum PreferenceGroup {

    EQUIPMENT(WeaponsPreferences.class, ArmourPreferences.class, ShieldPreferences.class, TechnologicalPreferences.class),

    CYBERNETICS(CyberneticPointsPreferences.class, CyberneticTotalDevicesPreferences.class, CyberneticVisibilityPreferences.class),

    PSI(PsiqueLevelPreferences.class, PsiquePathLevelPreferences.class),

    CHARACTER_CREATION(BlessingNumberPreferences.class, CharacteristicsPreferences.class, CurseNumberPreferences.class,
            ExtraBeneficesNumberPreferences.class, SkillGroupPreferences.class, TraitCostPreferences.class),

    CHARACTER_DESCRIPTION(AgePreferences.class, BlessingPreferences.class, CombatPreferences.class, DifficultLevelPreferences.class,
            FactionPreferences.class, NamesPreferences.class, RacePreferences.class, SpecializationPreferences.class, StatusPreferences.class);

    private final Class<? extends IRandomPreference>[] randomPreferences;

    PreferenceGroup(Class<? extends IRandomPreference>... randomPreferences) {
        this.randomPreferences = randomPreferences;
    }

    public Class<? extends IRandomPreference>[] getRandomPreferences() {
        return randomPreferences;
    }
}