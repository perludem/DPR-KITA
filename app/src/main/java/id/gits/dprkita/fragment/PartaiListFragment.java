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
import id.gits.dprkita.adapter.AdapterKomisi;
import id.gits.dprkita.api.ApiAdapter;
import id.gits.dprkita.dao.BaseDao;
import id.gits.dprkita.dao.anggota.Anggota;
import id.gits.dprkita.dao.anggota.DataAnggota;
import id.gits.dprkita.dao.partai.Partai;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.view.MyProgressView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PartaiListFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String LIST_PARTAI = "color1";
    private static final String POSITION = "position_list";
    @InjectView(R.id.re_home)
    RecyclerView mRecyclerView;
    @InjectView(R.id.view_progress)
    MyProgressView progressView;
    private int mIdPartai;
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
                        callApi(mIdPartai);
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
    private String color1, color2;
    private FragmentListener fragmentListener;
    private List<Partai> partais;
    private int position_partais;

    public static Fragment newInstance(int idPartai, List<Partai> partais, int position) {
        PartaiListFragment f = new PartaiListFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, idPartai);
        b.putInt(POSITION, position);
        f.setArguments(b);
        f.setPartais(partais);
        return f;
    }

    public void setPartais(List<Partai> partais) {
        this.partais = partais;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIdPartai = getArguments().getInt(ARG_POSITION);
        position_partais = getArguments().getInt(POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_anggota_fragment, container, false);
        ButterKnife.inject(this, v);
        fragmentListener = (FragmentListener) getActivity();
//        findColor();
        ab = Helper.getActionBarFragment(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AdapterKomisi(getActivity(), myDataset, false);
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


        callApi(mIdPartai);
        return v;
    }

    private void findColor() {
        for (Partai p : partais) {
            if (p.getId().equals(String.valueOf(mIdPartai))) {
                fragmentListener.setColor(p.getColors().get(0), p.getColors().get(1));
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ab.setTitle("Fraksi");
    }

    public void callApi(int mPosition) {
        ApiAdapter.callAPI().listAnggotaByPartai(Constant.KEY.PEMILUAPI, mPosition, resultAnggota);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface FragmentListener {
        public void setColor(String color, String color2);
    }

}