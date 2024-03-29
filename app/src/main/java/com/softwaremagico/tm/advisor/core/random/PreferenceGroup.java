package com.softwaremagico.tm.advisor.core.random;

import com.softwaremagico.tm.random.selectors.AgePreferences;
import com.softwaremagico.tm.random.selectors.ArmourPreferences;
import com.softwaremagico.tm.random.selectors.BlessingNumberPreferences;
import com.softwaremagico.tm.random.selectors.BlessingPreferences;
import com.softwaremagico.tm.random.selectors.CashPreferences;
import com.softwaremagico.tm.random.selectors.CharacteristicsPreferences;
import com.softwaremagico.tm.random.selectors.CombatActionsGroupPreferences;
import com.softwaremagico.tm.random.selectors.CombatActionsPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.CurseNumberPreferences;
import com.softwaremagico.tm.random.selectors.CyberneticPointsPreferences;
import com.softwaremagico.tm.random.selectors.CyberneticTotalDevicesPreferences;
import com.softwaremagico.tm.random.selectors.CyberneticVisibilityPreferences;
import com.softwaremagico.tm.random.selectors.DifficultLevelPreferences;
import com.softwaremagico.tm.random.selectors.ExtraBeneficesNumberPreferences;
import com.softwaremagico.tm.random.selectors.FactionPreferences;
import com.softwaremagico.tm.random.selectors.GenderPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.NamesPreferences;
import com.softwaremagico.tm.random.selectors.OccultismLevelPreferences;
import com.softwaremagico.tm.random.selectors.OccultismPathLevelPreferences;
import com.softwaremagico.tm.random.selectors.OccultismTypePreferences;
import com.softwaremagico.tm.random.selectors.RacePreferences;
import com.softwaremagico.tm.random.selectors.ShieldPreferences;
import com.softwaremagico.tm.random.selectors.SkillGroupPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import com.softwaremagico.tm.random.selectors.RankPreferences;
import com.softwaremagico.tm.random.selectors.TechnologicalPreferences;
import com.softwaremagico.tm.random.selectors.TraitCostPreferences;
import com.softwaremagico.tm.random.selectors.WeaponsPreferences;

public enum PreferenceGroup {

    CHARACTER_DESCRIPTION(RacePreferences.class, FactionPreferences.class, AgePreferences.class, GenderPreferences.class,
            CombatPreferences.class, CombatActionsPreferences.class, CombatActionsGroupPreferences.class,
            DifficultLevelPreferences.class, SpecializationPreferences.class,
            NamesPreferences.class, RankPreferences.class, CashPreferences.class),

    CHARACTER_CREATION(CharacteristicsPreferences.class, SkillGroupPreferences.class, TraitCostPreferences.class,
            BlessingPreferences.class, BlessingNumberPreferences.class, CurseNumberPreferences.class, ExtraBeneficesNumberPreferences.class),

    EQUIPMENT(TechnologicalPreferences.class, WeaponsPreferences.class, ArmourPreferences.class, ShieldPreferences.class),

    CYBERNETICS(CyberneticPointsPreferences.class, CyberneticTotalDevicesPreferences.class, CyberneticVisibilityPreferences.class),

    PSI(OccultismTypePreferences.class, OccultismLevelPreferences.class, OccultismPathLevelPreferences.class);


    private final Class<? extends IRandomPreference>[] randomPreferences;

    PreferenceGroup(Class<? extends IRandomPreference>... randomPreferences) {
        this.randomPreferences = randomPreferences;
    }

    public Class<? extends IRandomPreference>[] getRandomPreferences() {
        return randomPreferences;
    }
}