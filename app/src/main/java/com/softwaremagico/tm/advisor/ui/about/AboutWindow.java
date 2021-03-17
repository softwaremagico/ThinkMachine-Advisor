package com.softwaremagico.tm.advisor.ui.about;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.softwaremagico.tm.advisor.R;

public class AboutWindow extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_window, container, false);
        AboutPagerAdapter aboutPagerAdapter = new AboutPagerAdapter(getContext(), getChildFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(aboutPagerAdapter);
        viewPager.setOffscreenPageLimit(aboutPagerAdapter.getCount());
        final TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        //Set version number at the bottom.
        TextView versionText = view.findViewById(R.id.app_version);
        try {
            String versionName = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName;
            versionText.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            versionText.setText("");
        }

        return view;
    }
}