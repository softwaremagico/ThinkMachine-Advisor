/*
 *  Copyright (C) 2020 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.ui.random;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.random.characters.RandomCharactersFragment;
import com.softwaremagico.tm.advisor.ui.random.preferences.RandomPreferencesFragment;
import com.softwaremagico.tm.advisor.ui.random.profile.RandomProfilesFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class RandomSectionsPagerAdapter extends FragmentStateAdapter {

    @StringRes
    public static final int[] TAB_TITLES = new int[]{R.string.tab_random_preferences, R.string.tab_random_profiles, R.string.tab_random_npc};

    public RandomSectionsPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0) {
            return RandomPreferencesFragment.newInstance(position + 1);
        }

        if (position == 1) {
            return RandomProfilesFragment.newInstance(position + 1);
        }

        if (position == 2) {
            return RandomCharactersFragment.newInstance(position + 1);
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return TAB_TITLES.length;
    }
}
