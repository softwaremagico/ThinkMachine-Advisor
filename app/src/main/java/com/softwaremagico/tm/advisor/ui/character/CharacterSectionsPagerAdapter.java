package com.softwaremagico.tm.advisor.ui.character;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.character.characteristics.CharacteristicsFragment;
import com.softwaremagico.tm.advisor.ui.character.info.CharacterInfoFragment;
import com.softwaremagico.tm.advisor.ui.character.skills.SkillsFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class CharacterSectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_info, R.string.tab_char, R.string.tab_skills};
    private final Context mContext;
    private SparseArray<Fragment> fragments = new SparseArray<>();

    public CharacterSectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fragments.put(position, fragment);
        return fragment;
    }

    public Fragment getRegisteredFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        fragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0) {
            return CharacterInfoFragment.newInstance(position + 1);
        }

        if (position == 1) {
            return CharacteristicsFragment.newInstance(position + 1);
        }

        if (position == 2) {
            return SkillsFragment.newInstance(position + 1);
        }

        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}