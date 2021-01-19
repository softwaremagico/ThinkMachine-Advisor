package com.softwaremagico.tm.advisor.ui.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.softwaremagico.tm.advisor.R;

public class ElementDescriptionDialog extends DialogFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final View rootView = inflater.inflate(R.layout.description_window, container, false);


        return rootView;
    }
}
