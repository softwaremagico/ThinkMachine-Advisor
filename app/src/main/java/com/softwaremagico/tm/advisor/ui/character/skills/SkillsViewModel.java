package com.softwaremagico.tm.advisor.ui.character.skills;

import androidx.lifecycle.ViewModel;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.log.MachineLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SkillsViewModel extends ViewModel {


    public List<AvailableSkill> getNaturalSkills(boolean nonOfficial) {
        try {
            if (nonOfficial) {
                return CharacterManager.getSelectedCharacter().getNaturalSkills();
            } else {
                return CharacterManager.getSelectedCharacter().getNaturalSkills().
                        stream().filter(Objects::nonNull).filter(Element::isOfficial).collect(Collectors.toList());
            }
        } catch (NullPointerException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }

    public List<AvailableSkill> getLearnedSkills(boolean nonOfficial) {
        try {
            if (nonOfficial) {
                return CharacterManager.getSelectedCharacter().getLearnedSkills();
            } else {
                return CharacterManager.getSelectedCharacter().getLearnedSkills().
                        stream().filter(Objects::nonNull).filter(Element::isOfficial).collect(Collectors.toList());
            }
        } catch (NullPointerException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }
}
