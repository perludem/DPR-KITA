package id.gits.dprkita.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;
import id.gits.dprkita.adapter.AdapterAnggotaSearchResult;
import id.gits.dprkita.api.ApiAdapter;
import id.gits.dprkita.dao.BaseDao;
import id.gits.dprkita.dao.anggota.Anggota;
import id.gits.dprkita.dao.anggota.DataAnggota;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.view.MyProgressView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchResultFragment extends BaseFragment {

    @InjectView(R.id.rv)
    RecyclerView mRecyclerView;
    @InjectView(R.id.view_progress)
    MyProgressView progressView;
    Callback<BaseDao<DataAnggota>> resultAnggota = new Callback<BaseDao<DataAnggota>>() {
        @Override
        public void success(BaseDao<DataAnggota> dataAnggotaBaseDao, Response response) {
            try {

                if (!dataAnggotaBaseDao.getData().getResults().getAnggota().isEmpty()) {
                    progressView.stopAndGone();
                    myDataset.clear();
                    myDataset.addAll(dataAnggotaBaseDao.getData().getResults().getAnggota());
                    mAdapter.notifyDataSetChanged();
                } else {
                    progressView.stopAndError(getResources().getString(R.string.data_kosong), false);
                    myDataset.clear();
                    myDataset.addAll(dataAnggotaBaseDao.getData().getResults().getAnggota());
                    mAdapter.notifyDataSetChanged();
                }

            } catch (Exception e) {
            }
        }

        @Override
        public void failure(RetrofitError error) {
            try {

                progressView.stopAndError(error.getMessage(), true);
                progressView.setRetryClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callApi();
                    }
                });
                Log.d("ini error", error.getMessage());

            } catch (Exception e) {
            }
        }
    };
    private Activity activity;
    private ActionBar ab;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Anggota> myDataset = new ArrayList<>();
    private String mQuery = "";

    public static Fragment newInstance(String query) {
        SearchResultFragment f = new SearchResultFragment();
        Bundle b = new Bundle();
        b.putString("query", query);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuery = getArguments().getString("query");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutResource(), container, false);
        view = v;
        ButterKnife.inject(this, v);
        ab = Helper.getActionBarFragment(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AdapterAnggotaSearchResult(getActivity(), myDataset);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        callApi();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ab.setTitle("Pencarian '" + mQuery + "'");
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_search_result;
    }

    public void callApi() {
        ApiAdapter.callAPI().listAnggotaByName(Constant.KEY.PEMILUAPI, mQuery, resultAnggota);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}