package com.softwaremagico.tm.advisor.persistence.factories;

import androidx.room.Entity;

import com.google.gson.Gson;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity(tableName = WeaponsFactoryElements.WEAPONS_FACTORY_ELEMENTS_TABLE)
public class WeaponsFactoryElements extends FactoryElements<Weapon> {
    public static final String WEAPONS_FACTORY_ELEMENTS_TABLE = "weapons_factory_elements";

    public WeaponsFactoryElements() {
        super();
        creationTime = new Timestamp(new Date().getTime());
    }

    public WeaponsFactoryElements(int version, int totalElements, String language, String moduleName, List<Weapon> elements) {
        this();
        setLanguage(language);
        setModuleName(moduleName);
        setElements(elements);
        setVersion(version);
        setTotalElements(totalElements);
    }

    @Override
    public List<Weapon> getElements() {
        final Gson gson = new Gson();
        Weapon[] elements = gson.fromJson(json, Weapon[].class);
        return Arrays.asList(elements);
    }
}
