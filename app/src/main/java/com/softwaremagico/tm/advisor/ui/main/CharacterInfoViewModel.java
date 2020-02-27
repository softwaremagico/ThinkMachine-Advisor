package com.softwaremagico.tm.advisor.ui.main;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.log.MachineLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CharacterInfoViewModel extends ViewModel {

    public List<String> getAvailableFactions(){
        try {
            Log.e(this.getClass().getName(), "#######################################33");
            return FactionsFactory.getInstance().getElements(Locale.getDefault().getLanguage(), "Fading Suns").stream().map(Object::toString)
                    .collect(Collectors.toList());
        } catch (InvalidXmlElementException e) {
            Log.wtf(this.getClass().getName(), e);
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }
}
