package com.softwaremagico.tm.advisor.persistence;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.softwaremagico.tm.character.Settings;

import java.sql.Timestamp;
import java.util.Date;

@Entity(tableName = SettingsEntity.SETTINGS_TABLE)
public class SettingsEntity extends BaseEntity {
    public static final String SETTINGS_TABLE = "settings";

    @ColumnInfo(name = "only_official_allowed")
    public boolean onlyOfficialAllowed = false;

    @ColumnInfo(name = "restrictions_checked")
    public boolean restrictionsChecked = true;

    public SettingsEntity() {
        super();
        creationTime = new Timestamp(new Date().getTime());
    }

    public SettingsEntity(Settings settings) {
        this();
        set(settings);
    }

    public void set(Settings settings) {
        this.setOnlyOfficialAllowed(settings.isOnlyOfficialAllowed());
        this.setRestrictionsChecked(settings.isRestrictionsChecked());
    }

    public Settings get() {
        final Settings settings = new Settings();
        settings.setOnlyOfficialAllowed(this.isOnlyOfficialAllowed());
        settings.setRestrictionsChecked(this.isRestrictionsChecked());
        return settings;
    }


    public boolean isOnlyOfficialAllowed() {
        return onlyOfficialAllowed;
    }

    public void setOnlyOfficialAllowed(boolean onlyOfficialAllowed) {
        this.onlyOfficialAllowed = onlyOfficialAllowed;
    }

    public boolean isRestrictionsChecked() {
        return restrictionsChecked;
    }

    public void setRestrictionsChecked(boolean restrictionsChecked) {
        this.restrictionsChecked = restrictionsChecked;
    }
}
