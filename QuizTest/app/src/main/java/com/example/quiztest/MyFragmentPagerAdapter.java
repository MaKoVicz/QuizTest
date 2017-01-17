package com.example.quiztest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    List<Fragment> listFragment;
    private int tabIcon[] = {R.drawable.ic_vector_practice_selected, R.drawable.ic_vector_exam_selected, R.drawable.ic_vector_interactive_selected};

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> listFragment) {
        super(fm);
        this.listFragment = listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Override
    public int getPageIconResId(int position) {
        return tabIcon[position];
    }
}
