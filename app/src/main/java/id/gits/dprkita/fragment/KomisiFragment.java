package id.gits.dprkita.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;
import id.gits.dprkita.activity.ListKomisiActivity;
import id.gits.dprkita.adapter.AdapterKomisi;
import id.gits.dprkita.api.ApiAdapter;
import id.gits.dprkita.dao.BaseDao;
import id.gits.dprkita.dao.anggota.Anggota;
import id.gits.dprkita.dao.anggota.DataAnggota;
import id.gits.dprkita.dao.komisi.Komisi;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.SharePreferences;
import id.gits.dprkita.utils.view.MyProgressView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class KomisiFragment extends BaseFragment implements ObservableScrollViewCallbacks, View.OnClickListener {

    public static Komisi komisi;
    @InjectView(R.id.re_komisi)
    ObservableRecyclerView mRecyclerView;
    @InjectView(R.id.tv_komisi)
    TextView komisiTitle;
    @InjectView(R.id.view_progress)
    MyProgressView progressView;
    @InjectView(R.id.list_komisi)
    LinearLayout list_komisi;
    @InjectView(R.id.img_arrow)
    ImageButton img_arrow;
    boolean isScrollDown = false;
    Callback<BaseDao<DataAnggota>> komisiListener = new Callback<BaseDao<DataAnggota>>() {
        @Override
        public void success(BaseDao<DataAnggota> dataAnggotaBaseDao, Response response) {
            try {
                progressView.stopAndGone();
                myDataset.clear();
                myDataset.addAll(dataAnggotaBaseDao.getData().getResults().getAnggota());
                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollVerticallyTo(0);

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
                        callApi();
                    }
                });
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

            } catch (Exception e) {
            }
        }
    };
    private List<Komisi> myObjectSpinnerData;
    private List<Anggota> myDataset = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public KomisiFragment() {
        // Required empty public constructor
    }

    public static KomisiFragment newInstance() {
        KomisiFragment fragment = new KomisiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.inject(this, view);
        ab = Helper.getActionBarFragment(getActivity());

        IconDrawable arrow = new IconDrawable(getActivity(), Iconify.IconValue.fa_angle_down).colorRes(R.color.bluegray700).actionBarSize();
        img_arrow.setImageDrawable(arrow);

        String komisi = SharePreferences.getString(getActivity(), Constant.TAG.WAS_SAVE_KOMISI);
        /*Type listType = new TypeToken<List<Komisi>>() {
        }.getType();
        List<Komisi> k = new Gson().fromJson(komisi, listType);
        komisiTitle.setText(k.get(0).getNama());*/
        komisiTitle.setText("Komisi I");

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setScrollViewCallbacks(this);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AdapterKomisi(getActivity(), myDataset, true);
        mRecyclerView.setAdapter(mAdapter);

        list_komisi.setOnClickListener(this);

        callApi();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ab.setTitle("Komisi");
    }

    public void callApi() {
        progressView.startProgress();
        if (komisi != null) {
            String[] kom = komisi.getNama().split("-");
            komisiTitle.setText("Komisi " + kom[0]);
            ApiAdapter.callAPI().listAnggotaByKomisi(Constant.KEY.PEMILUAPI, komisi.getId(), komisiListener);
        } else {
            ApiAdapter.callAPI().listAnggotaByKomisi(Constant.KEY.PEMILUAPI, 1, komisiListener);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_komisi;
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.list_komisi:
                ListKomisiActivity.startActivity(getActivity());
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ListKomisiActivity.REQ_CODE && resultCode == Activity.RESULT_OK) {
            callApi();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
