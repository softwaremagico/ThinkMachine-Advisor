package com.softwaremagico.tm.advisor.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.persistence.SettingsHandler;

public class SettingsWindow extends DialogFragment {
    private SwitchCompat nonOfficialEnabled;
    private SwitchCompat restrictionsIgnored;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_window, container, false);

        nonOfficialEnabled = root.findViewById(R.id.official_selector);
        restrictionsIgnored = root.findViewById(R.id.restricted_selector);

        nonOfficialEnabled.setChecked(!SettingsHandler.getSettingsEntity().onlyOfficialAllowed);
        restrictionsIgnored.setChecked(!SettingsHandler.getSettingsEntity().restrictionsChecked);

        nonOfficialEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingsHandler.getSettingsEntity().setOnlyOfficialAllowed(!isChecked);
            SettingsHandler.save(getContext());
        });

        restrictionsIgnored.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingsHandler.getSettingsEntity().setRestrictionsChecked(!isChecked);
            SettingsHandler.save(getContext());
        });

        return root;
    }
}