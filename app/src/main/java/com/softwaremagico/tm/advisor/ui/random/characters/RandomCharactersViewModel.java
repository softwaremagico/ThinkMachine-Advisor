package com.softwaremagico.tm.advisor.ui.random.characters;

import androidx.lifecycle.ViewModel;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.random.predefined.characters.Npc;
import com.softwaremagico.tm.random.predefined.characters.NpcFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class RandomCharactersViewModel extends ViewModel {

    public List<String> getNpcGroups(boolean nonOfficial) {
        return NpcFactory.getInstance().getGroups(nonOfficial, Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE).
                stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<Npc> getAvailableNpcs(boolean nonOfficial, String group) {
        try {
            if (nonOfficial) {
                return NpcFactory.getInstance().getByGroup(group).
                        stream().filter(Objects::nonNull).collect(Collectors.toList());
            } else {
                return NpcFactory.getInstance().getByGroup(group).
                        stream().filter(Objects::nonNull).filter(Element::isOfficial).collect(Collectors.toList());
            }
        } catch (NullPointerException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }
}
