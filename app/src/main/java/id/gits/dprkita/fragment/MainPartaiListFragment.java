package id.gits.dprkita.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;
import id.gits.dprkita.adapter.AdapterViewPagerPartai;
import id.gits.dprkita.dao.partai.Partai;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.view.SlidingTabLayout;

public class MainPartaiListFragment extends BaseFragment {

    private static final String PARTAIS = "partai";
    private static final String POSITION = "position";
    @InjectView(R.id.pager)
    ViewPager pager;
    @InjectView(R.id.tabs)
    SlidingTabLayout tabs;
    int position = 0;
    private AdapterViewPagerPartai adapter;
    private Bundle savedInstanceState;
    private List<Partai> partais;


    public MainPartaiListFragment() {
        // Required empty public constructor
    }

    public static MainPartaiListFragment newInstance(ArrayList<Partai> partais, int position) {
        MainPartaiListFragment fragment = new MainPartaiListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        args.putSerializable(PARTAIS, partais);
        args.putInt(POSITION, position);
        return fragment;
    }

    public void setPartais(List<Partai> partais) {
        this.partais = partais;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onResume() {
        super.onResume();
        ab.setTitle("Fraksi");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            partais = (List<Partai>) getArguments().getSerializable(PARTAIS);
            position = getArguments().getInt(POSITION);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_document, container, false);
        ButterKnife.inject(this, view);
        ab = Helper.getActionBarFragment(getActivity());
        ab.setDisplayHomeAsUpEnabled(true);
        adapter = new AdapterViewPagerPartai(getChildFragmentManager(), partais);
        pager.setAdapter(adapter);
        pager.setCurrentItem(position);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);
        return view;
    }

    public void setColorBackgroundTab(String color, final String color2) {
        tabs.setBackgroundColor(Color.parseColor(color));
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.parseColor(color2);
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_document;
    }
}
