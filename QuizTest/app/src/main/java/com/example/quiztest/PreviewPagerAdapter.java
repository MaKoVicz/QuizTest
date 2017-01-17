package com.example.quiztest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class PreviewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> previewFragmentList;

    public PreviewPagerAdapter(FragmentManager fm, List<Fragment> listFragment) {
        super(fm);
        this.previewFragmentList = listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return previewFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return previewFragmentList.size();
    }
}
