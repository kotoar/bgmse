package com.kanae.bgmse.ui.main;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kanae.bgmse.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2,R.string.tab_text_3};
    private final Context mContext;
    private FragmentManager fragmentManager;

    List<String> tags = new ArrayList<>();

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a Fragment (defined as a static inner class below).
        if(position == 1) {
            return new Fragment1();
        }
        if(position ==2) {
            return new Fragment3();
        }
        return new Fragment2();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
        //return getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int pageIndex) {
        //创建tag并存储到集合中
        String fragmentNameTag = makeFragmentName(container.getId(), pageIndex);
        if (!tags.contains(fragmentNameTag)) {
            tags.add(fragmentNameTag);
        }
        return super.instantiateItem(container, pageIndex);
    }

    /**
     * 创建Fragment的名称也就是tag,该方法源自 FragmentPagerAdapter的源码
     */
    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }


/*
    public void refreshMofifiedPageData(int position, int curPageIndex) {
        for (int i = 0; i < tags.size(); i++) {
            Fragment1 fragment = (Fragment1) fragmentManager.findFragmentByTag(tags.get(i));
            //fragment.refreshData(position, curPageIndex);
        }
    }
*/
}