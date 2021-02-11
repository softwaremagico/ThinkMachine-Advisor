package com.softwaremagico.tm.advisor.persistence.factories;

import androidx.room.ColumnInfo;

import com.google.gson.Gson;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.persistence.BaseEntity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public abstract class FactoryElements<E extends Element<?>> extends BaseEntity {

    @ColumnInfo(name = "version")
    public int version;

    @ColumnInfo(name = "total_elements")
    public int totalElements;

    @ColumnInfo(name = "language")
    public String language;

    @ColumnInfo(name = "module_name")
    public String moduleName;

    @ColumnInfo(name = "elements_as_json")
    public String json;

    public FactoryElements() {
        super();
        creationTime = new Timestamp(new Date().getTime());
    }

    public FactoryElements(List<E> elements) {
        this();
        setElements(elements);
    }

    public void setElements(List<E> elements) {
        final Gson gson = new Gson();
        json = gson.toJson(elements);
    }

    public abstract List<E> getElements();

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
