package com.softwaremagico.tm.advisor.ui.about;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.CustomFragment;

public class AboutFragment extends CustomFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView content;

    public static AboutFragment newInstance(int index) {
        final AboutFragment fragment = new AboutFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.about_content, container, false);
        content = root.findViewById(R.id.content);
        Typeface typeface = ResourcesCompat.getFont(root.getContext(), R.font.dejavu_sans);
        content.setTypeface(typeface);
        content.setMovementMethod(LinkMovementMethod.getInstance());
        return root;
    }

    @Override
    protected void initData() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            content.setText(Html.fromHtml(getString(R.string.about), Html.FROM_HTML_MODE_LEGACY));
        } else {
            content.setText(Html.fromHtml(getString(R.string.about)));
        }
    }
}
