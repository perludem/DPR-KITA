package id.gits.dprkita.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import id.gits.dprkita.fragment.KabarDPRFragment;
import id.gits.dprkita.fragment.NewsFragment;
import id.gits.dprkita.fragment.SelasarFragment;

/**
 * Created by yatnosudar on 2/5/15.
 */
public class AdapterViewPagerDocument extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;

    public AdapterViewPagerDocument(FragmentManager fm, CharSequence[] titles, int numbOfTabs) {
        super(fm);
        Titles = titles;
        NumbOfTabs = numbOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return KabarDPRFragment.newInstance();
        } else if (position == 1) {
            return NewsFragment.newInstance();
        } else {
            return SelasarFragment.newInstance();
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
