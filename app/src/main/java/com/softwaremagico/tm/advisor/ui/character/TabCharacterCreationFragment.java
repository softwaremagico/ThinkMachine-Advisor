package com.softwaremagico.tm.advisor.ui.character;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.character.characteristics.CharacteristicsFragment;
import com.softwaremagico.tm.advisor.ui.character.skills.SkillsFragment;

public class TabCharacterCreationFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.character_creation_fragment, container, false);
        CharacterSectionsPagerAdapter characterSectionsPagerAdapter = new CharacterSectionsPagerAdapter(getContext(), getChildFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(characterSectionsPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment selectedFragment = ((CharacterSectionsPagerAdapter) viewPager.getAdapter()).getRegisteredFragment(position);
                if (selectedFragment != null) {
                    if (selectedFragment instanceof CharacteristicsFragment) {
                        ((CharacteristicsFragment) selectedFragment).updateCharacteristicsLimits();
                        ((CharacteristicsFragment) selectedFragment).refreshCharacteristicValues();
                    } else if (selectedFragment instanceof SkillsFragment) {
                        ((SkillsFragment) selectedFragment).refreshSkillsValues();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        return view;
    }


}
