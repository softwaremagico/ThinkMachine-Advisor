package com.softwaremagico.tm.advisor.ui.about;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.softwaremagico.tm.advisor.R;

public class AboutWindow extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_window, container, false);

        if (getDialog() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            //Set version number at the bottom.
            TextView versionText = view.findViewById(R.id.app_version);
            try {
                String versionName = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName;
                versionText.setText(versionName);
            } catch (PackageManager.NameNotFoundException e) {
                versionText.setText("");
            }
            versionText.setOnClickListener(v -> dismiss());
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AboutPagerAdapter aboutPagerAdapter = new AboutPagerAdapter(getActivity());
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(aboutPagerAdapter);
        viewPager.setOffscreenPageLimit(aboutPagerAdapter.getItemCount());

        final TabLayout tabs = view.findViewById(R.id.tabs);
        //tabs.setupWithViewPager(viewPager);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabs.selectTab(tabs.getTabAt(position));
            }
        });
    }

}