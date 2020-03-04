package com.softwaremagico.tm.advisor.ui.main.visualization;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.softwaremagico.tm.advisor.R;

public class TextVisualizationFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static TextVisualizationFragment newInstance(int index) {
        TextVisualizationFragment fragment = new TextVisualizationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.visualization_text_fragment, container, false);

        return root;
    }


}
