package com.softwaremagico.tm.advisor.ui.components;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class CustomFragment extends Fragment {

    protected void addSection(String title, LinearLayout linearLayout) {
        TextView textView = new TextView(getContext(), null);
        textView.setText(title);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        View space = new View(getContext(), null);
        space.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        space.setBackgroundColor(Color.parseColor("#ff000000"));

        if (linearLayout != null) {
            linearLayout.addView(textView);
            linearLayout.addView(space);
        }
    }
}
