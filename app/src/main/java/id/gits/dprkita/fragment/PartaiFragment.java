package id.gits.dprkita.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;
import id.gits.dprkita.adapter.AdapterPartai;
import id.gits.dprkita.dao.partai.DataPartai;
import id.gits.dprkita.dao.partai.Partai;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.json.PartaiJSON;
import id.gits.dprkita.utils.view.MyProgressView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartaiFragment extends BaseFragment implements ObservableScrollViewCallbacks {

    @InjectView(R.id.re_home)
    ObservableRecyclerView mRecyclerView;
    @InjectView(R.id.view_progress)
    MyProgressView progressView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Partai> dataPartai = new ArrayList<>();

    public PartaiFragment() {
        // Required empty public constructor
    }

    public static PartaiFragment newInstance() {
        PartaiFragment fragment = new PartaiFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        ab.setTitle("Daftar Fraksi");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_fragment, container, false);
        ButterKnife.inject(this, view);
        ab = Helper.getActionBarFragment(getActivity());
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setScrollViewCallbacks(this);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        callApi();
        mAdapter = new AdapterPartai(getActivity(), dataPartai);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_main_fragment;
    }

    public void callApi() {
        progressView.stopAndGone();
        generatePartai();
//        ApiAdapter.callAPI().listPartai(Constant.KEY.PEMILUAPI, partaiListener);
    }

    public void generatePartai() {

        BaseDao partai = new Gson().fromJson(PartaiJSON.partai, BaseDao.class);
        dataPartai.addAll(partai.getData().getResults().getPartai());
        if (dataPartai.size() > 0) {
            Collections.sort(dataPartai, new Comparator<Partai>() {
                @Override
                public int compare(final Partai object1, final Partai object2) {
                    int val1 = Integer.valueOf(object1.getKursi());
                    int val2 = Integer.valueOf(object2.getKursi());
                    if (val1 > val2) return -1;
                    if (val1 == val2) return 0;
                    return 1;
                }
            });
        }


//        mAdapter.notifyDataSetChanged();
    }



    /*Callback<BaseDao<DataPartai>> partaiListener = new Callback<BaseDao<DataPartai>>() {
        @Override
        public void success(BaseDao<DataPartai> dataPartaiBaseDao, Response response) {
            progressView.stopAndGone();
            dataPartai.clear();
            dataPartai.addAll(dataPartaiBaseDao.getData().getResults().getPartai());
            if (dataPartai.size() > 0) {
                Collections.sort(dataPartai, new Comparator<PartaiJSON>() {
                    @Override
                    public int compare(final PartaiJSON object1, final PartaiJSON object2) {
                        int val1 = Integer.valueOf(object1.getId());
                        int val2 = Integer.valueOf(object2.getId());
                        if (val1 < val2) return -1;
                        if (val1 == val2) return 0;
                        return 1;
                    }
                });
            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void failure(RetrofitError error) {
            progressView.stopAndError(error.getMessage(), true);
            progressView.setRetryClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callApi();
                }
            });
        }
    };*/

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }


    public class BaseDao implements Serializable {
        DataPartai data;

        public DataPartai getData() {
            return data;
        }
    }
}
