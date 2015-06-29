package id.gits.dprkita.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;
import id.gits.dprkita.adapter.AdapterViewPagerDocument;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.view.SlidingTabLayout;

public class MainDocumentFragment extends BaseFragment {

    private static final String POSITION = "position";
    @InjectView(R.id.pager)
    ViewPager pager;
    @InjectView(R.id.tabs)
    SlidingTabLayout tabs;
    AdapterViewPagerDocument adapter;
    CharSequence Titles[] = {"KABAR DPR", "BERITA", "INFOGRAFIS"};
    int Numboftabs = 3;
    int postition = 0;


    public MainDocumentFragment() {
        // Required empty public constructor
    }

    public static MainDocumentFragment newInstance(int postition) {
        MainDocumentFragment fragment = new MainDocumentFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, postition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            postition = getArguments().getInt(POSITION);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_document, container, false);
        ButterKnife.inject(this, view);
        ActionBar ab = Helper.getActionBarFragment(getActivity());
        adapter = new AdapterViewPagerDocument(getChildFragmentManager(), Titles, Numboftabs);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);
        pager.setCurrentItem(postition);
        return view;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_document;
    }
}
