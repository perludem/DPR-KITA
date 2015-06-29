package id.gits.dprkita.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;
import id.gits.dprkita.adapter.AdapterSelasar;
import id.gits.dprkita.api.ApiAdapter;
import id.gits.dprkita.dao.BaseDao;
import id.gits.dprkita.dao.selasar.DataSelasar;
import id.gits.dprkita.dao.selasar.Infografis;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.view.MyProgressView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SelasarFragment extends Fragment implements ObservableScrollViewCallbacks {

    @InjectView(R.id.re_home)
    ObservableRecyclerView mRecyclerView;
    @InjectView(R.id.view_progress)
    MyProgressView progressView;

    private static int limit = 10;
    private int offset = 0;
    private boolean isCall = false;
    boolean isScrollUp = false;

    Callback<BaseDao<DataSelasar>> selasarListener = new Callback<BaseDao<DataSelasar>>() {
        @Override
        public void success(BaseDao<DataSelasar> resultSelasarBaseDao, Response response) {
            try {

                progressView.stopAndGone();
//                myDataset.clear();
                myDataset.addAll(resultSelasarBaseDao.getData().getResults().getInfografisList());
                mAdapter.notifyDataSetChanged();
                offset = limit+offset;
                isCall=true;
            } catch (Exception e) {
            }

        }

        @Override
        public void failure(RetrofitError error) {
            try {
                progressView.stopAndError(getResources().getString(R.string.errorNetwork), true);
                progressView.setRetryClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callAPi();
                    }
                });
                isCall=true;
            } catch (Exception e) {
            }

        }
    };
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Infografis> myDataset = new ArrayList<>();

    public SelasarFragment() {
        // Required empty public constructor
    }

    public static SelasarFragment newInstance() {
        SelasarFragment fragment = new SelasarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selsar, container, false);
        ButterKnife.inject(this, view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setScrollViewCallbacks(this);

        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new AdapterSelasar(getActivity(), myDataset);
        mRecyclerView.setAdapter(mAdapter);

        progressView.startProgress();
        callAPi();

        Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_selasar), null);
        return view;
    }

    public void callAPi() {
        isCall = false;
        ApiAdapter.callAPI().listSelasar(Constant.KEY.PEMILUAPI,offset, selasarListener);
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {
            if (b == false && b2 == false && isCall && isScrollUp) {
                callAPi();
            }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.UP) {
            isScrollUp = true;
        } else if (scrollState == ScrollState.DOWN) {
            isScrollUp = false;
        }
    }
}
