package com.softwaremagico.tm.advisor.ui.about;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.CustomFragment;

public class PrivacyFragment extends CustomFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView content;

    public static PrivacyFragment newInstance(int index) {
        final PrivacyFragment fragment = new PrivacyFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.privacy_content, container, false);
        content = root.findViewById(R.id.content);
        content.setMovementMethod(LinkMovementMethod.getInstance());
        return root;
    }

    @Override
    protected void initData() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            content.setText(Html.fromHtml(getString(R.string.privacy), Html.FROM_HTML_MODE_LEGACY));
        } else {
            content.setText(Html.fromHtml(getString(R.string.privacy)));
        }
    }
}
