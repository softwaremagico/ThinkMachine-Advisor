package com.softwaremagico.tm.advisor.ui.character.traits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.CustomFragment;
import com.softwaremagico.tm.advisor.ui.components.TranslatedNumberPicker;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.skills.AvailableSkill;

import java.util.HashMap;
import java.util.Map;

public class TraitsFragment extends CustomFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Map<AvailableSkill, TranslatedNumberPicker> translatedNumberPickers = new HashMap<>();

    public static TraitsFragment newInstance(int index) {
        TraitsFragment fragment = new TraitsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.character_traits_fragment, container, false);
        LinearLayout linearLayout = root.findViewById(R.id.traits_container);
        addSection(ThinkMachineTranslator.getTranslatedText("naturalSkills"), linearLayout);

        return root;
    }

}
