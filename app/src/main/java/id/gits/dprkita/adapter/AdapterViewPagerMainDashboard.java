package id.gits.dprkita.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import id.gits.dprkita.dao.PostMode;
import id.gits.dprkita.fragment.AgendaFragment;
import id.gits.dprkita.fragment.HomeListFragment;
import id.gits.dprkita.fragment.HomeNewsFragment;
import id.gits.dprkita.fragment.ReportFragment;
import id.gits.dprkita.utils.Constant;
import id.gits.wplib.utils.WPKey;

/**
 * Created by yatnosudar on 2/5/15.
 */
public class AdapterViewPagerMainDashboard extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;

    public AdapterViewPagerMainDashboard(FragmentManager fm, CharSequence[] titles, int numbOfTabs) {
        super(fm);
        Titles = titles;
        NumbOfTabs = numbOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = HomeListFragment.newInstance();
                break;
            case 1:
                fragment = AgendaFragment.newInstance();
                break;
            case 2:
                fragment = HomeNewsFragment.newInstance();
                break;
            case 3:
                fragment = ReportFragment.newInstance(new PostMode(Constant.TAG.ASPIRASI, WPKey.CUSTOM_FIELD.ASPIRASI, "", ""));
                break;
        }
        return fragment;
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
