package com.softwaremagico.tm.advisor.ui.main.character.info;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.configuration.ModuleManager;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.log.MachineLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CharacterInfoViewModel extends ViewModel {



    public List<Faction> getAvailableFactions() {
        try {
            return FactionsFactory.getInstance().getElements(Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE);
        } catch (InvalidXmlElementException e) {
            Log.wtf(this.getClass().getName(), e);
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }

    public List<Race> getAvailableRaces() {
        try {
            return RaceFactory.getInstance().getElements(Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE);
        } catch (InvalidXmlElementException e) {
            Log.wtf(this.getClass().getName(), e);
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }
}
