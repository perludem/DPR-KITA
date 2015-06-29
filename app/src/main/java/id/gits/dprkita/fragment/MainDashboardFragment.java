package id.gits.dprkita.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;
import id.gits.dprkita.R;
import id.gits.dprkita.adapter.AdapterViewPagerMainDashboard;
import id.gits.dprkita.db.DapilDB;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.view.SlidingTabLayout;

public class MainDashboardFragment extends BaseFragment {

    @InjectView(R.id.progressbar)
    public SmoothProgressBar progressBar;
    @InjectView(R.id.pager)
    ViewPager pager;
    @InjectView(R.id.tabs)
    SlidingTabLayout tabs;
    AdapterViewPagerMainDashboard adapter;
    CharSequence Titles[] = {"ANGGOTA", "AGENDA", "BERITA", "ASPIRASI"};
    int Numboftabs = 4;
    SmoothProgressDrawable.Callbacks mListener = new SmoothProgressDrawable.Callbacks() {
        @Override
        public void onStop() {
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onStart() {
            progressBar.setVisibility(View.VISIBLE);
        }
    };

    public MainDashboardFragment() {
        // Required empty public constructor
    }

    public static MainDashboardFragment newInstance() {
        MainDashboardFragment fragment = new MainDashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        //ab = Helper.getActionBarFragment(getActivity());
//        ab.setTitle(getResources().getString(R.string.app_name));
        ab.setTitle("");
        ab.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.inject(this, view);
//        progressBar.setProgressiveStartActivated(false);
        ab = Helper.getActionBarFragment(getActivity());
        adapter = new AdapterViewPagerMainDashboard(getChildFragmentManager(), Titles, Numboftabs);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);
        tabs.setDistributeEvenly(false);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);
        progressBar.setSmoothProgressDrawableCallbacks(mListener);
        return view;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_dashboard;
    }

    public void sendKomisiFromReportFragment(String komisi) {

        for (Fragment fragment : Helper.getFragmentFromViewPager(this)) {
            if (fragment instanceof ReportFragment) {
                ((ReportFragment) fragment).setFilterKomisi("Komisi " + komisi);
                break;
            }
        }
    }

    public void sendDapilFromList(DapilDB data) {
        for (Fragment fragment : Helper.getFragmentFromViewPager(this)) {
            if (fragment instanceof HomeListFragment) {
                ((HomeListFragment) fragment).setDataDapilResult(data);
                break;
            }
        }
    }

    public void sendPostFromPost(int totalComment) {
        boolean status = false;
        if (totalComment > 0)
            status = true;
        for (Fragment fragment : Helper.getFragmentFromViewPager(this)) {
            if (fragment instanceof ReportFragment) {
                ((ReportFragment) fragment).setPost(status);
                break;
            }
        }
        for (Fragment fragment : Helper.getFragmentFromViewPager(this)) {
            if (fragment instanceof HomeListFragment) {
                ((HomeListFragment) fragment).setPost(status);
                break;
            }
        }
    }


    public void startProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.progressiveStart();
    }

    public void stopProgressBar() {
        progressBar.progressiveStop();
    }


}
