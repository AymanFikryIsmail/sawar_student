package com.hesham.sawarstudent.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hesham.sawarstudent.ui.subjects.FirstTermFragment;
import com.hesham.sawarstudent.ui.subjects.SecondTermFragment;

public class SubjectViewPager extends FragmentPagerAdapter {

    private static final String[] TAB_TITLES = new String[]{"first term" ,"second term"};
    private final Context mContext;

    private int years;
    public SubjectViewPager(Context context, FragmentManager fm, int years) {
        super(fm);
        mContext = context;
        this.years=years;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FirstTermFragment(years);
                break;
            case 1:
                fragment = new SecondTermFragment(years);
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}