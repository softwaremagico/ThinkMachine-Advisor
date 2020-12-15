package com.softwaremagico.tm.advisor.ui.about;

import android.os.Bundle;
import android.view.View;

import com.softwaremagico.tm.advisor.ui.components.CustomFragment;
import com.softwaremagico.tm.character.CharacterPlayer;

public class LicenseFragment extends CustomFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static LicenseFragment newInstance(int index) {
        final LicenseFragment fragment = new LicenseFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setCharacter(View root, CharacterPlayer character) {

    }
}
