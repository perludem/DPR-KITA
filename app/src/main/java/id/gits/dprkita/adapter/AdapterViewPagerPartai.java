package id.gits.dprkita.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import id.gits.dprkita.dao.partai.Partai;
import id.gits.dprkita.fragment.PartaiListFragment;

/**
 * Created by yatnosudar on 2/5/15.
 */
public class AdapterViewPagerPartai extends FragmentStatePagerAdapter {

    List<Partai> partais;

    public AdapterViewPagerPartai(FragmentManager fm, List<Partai> partais) {
        super(fm);
        this.partais = partais;
    }

    @Override
    public Fragment getItem(int position) {

        return PartaiListFragment.newInstance(Integer.parseInt(partais.get(position).getId()), partais, position);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return partais.get(position).getNama();
    }

    @Override
    public int getCount() {
        return partais.size();
    }
}
