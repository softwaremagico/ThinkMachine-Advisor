package com.softwaremagico.tm.advisor.core;

import android.content.Context;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.persistence.AppDatabase;
import com.softwaremagico.tm.advisor.persistence.factories.WeaponsFactoryElements;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.file.modules.ModuleManager;

import java.util.Locale;

public class FactoryCacheLoader {

    public static int load(Context context) {
        //Checks if weapons are stored on database.
        WeaponsFactoryElements weaponsFactoryElements = AppDatabase.getInstance(context).getWeaponsFactoryElementsDao()
                .getByVersion(WeaponFactory.getInstance().getVersion(ModuleManager.DEFAULT_MODULE), Locale.getDefault().getLanguage(), PathManager.DEFAULT_MODULE_FOLDER);
        if (weaponsFactoryElements == null) {
            try {
                //Not stored, obtain from XML.
                weaponsFactoryElements = new WeaponsFactoryElements(WeaponFactory.getInstance().getVersion(PathManager.DEFAULT_MODULE_FOLDER),
                        WeaponFactory.getInstance().getNumberOfElements(PathManager.DEFAULT_MODULE_FOLDER),
                        Locale.getDefault().getLanguage(), PathManager.DEFAULT_MODULE_FOLDER,
                        WeaponFactory.getInstance().getElements(Locale.getDefault().getLanguage(), PathManager.DEFAULT_MODULE_FOLDER));
                //And store the for next use.
                AppDatabase.getInstance(context).getWeaponsFactoryElementsDao().persist(weaponsFactoryElements);
            } catch (InvalidXmlElementException e) {
                AdvisorLog.errorMessage(FactoryCacheLoader.class.getName(), e);
            }
        }
        WeaponFactory.getInstance().setElements(Locale.getDefault().getLanguage(), PathManager.DEFAULT_MODULE_FOLDER, weaponsFactoryElements.getElements());
        try {
            return WeaponFactory.getInstance().getElements(Locale.getDefault().getLanguage(), PathManager.DEFAULT_MODULE_FOLDER).size();
        } catch (InvalidXmlElementException e) {
            AdvisorLog.errorMessage(FactoryCacheLoader.class.getName(), e);
        }
        return 0;
    }
}
