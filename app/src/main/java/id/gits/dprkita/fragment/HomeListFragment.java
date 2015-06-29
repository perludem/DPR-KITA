package id.gits.dprkita.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.joanzapata.android.iconify.Iconify;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import id.gits.dprkita.R;
import id.gits.dprkita.activity.ListDapilActivity;
import id.gits.dprkita.activity.PostActivity;
import id.gits.dprkita.adapter.AdapterTimeline;
import id.gits.dprkita.api.ApiAdapter;
import id.gits.dprkita.dao.BaseDao;
import id.gits.dprkita.dao.anggota.Anggota;
import id.gits.dprkita.dao.anggota.DataAnggota;
import id.gits.dprkita.dao.geographic.AreaGeo;
import id.gits.dprkita.dao.geographic.DataGeo;
import id.gits.dprkita.dao.jetpackdao.JetPackCustomFields;
import id.gits.dprkita.dao.jetpackdao.JetPackPost;
import id.gits.dprkita.dao.jetpackdao.JetPackPosts;
import id.gits.dprkita.db.DapilDB;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.GPSTracker;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.ProgressListener;
import id.gits.dprkita.utils.SharePreferences;
import id.gits.dprkita.utils.view.MyProgressView;
import id.gits.wplib.apis.LoginApi;
import id.gits.wplib.dao.PostDao;
import id.gits.wplib.utils.WPKey;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeListFragment extends Fragment implements ObservableScrollViewCallbacks {


    private static AreaGeo areaDapil;
    private static String lat, lng;
    public DapilDB dataDapil;
    @InjectView(R.id.re_home)
    ObservableRecyclerView mRecyclerView;
    @InjectView(R.id.img_mylocation)
    ImageView my_location;
    @InjectView(R.id.img_arrow)
    ImageView img_arrow;
    @InjectView(R.id.tv_dapil_list)
    TextView dapil_list;
    @InjectView(R.id.card_location)
    LinearLayout card_location;
    @InjectView(R.id.view_progress)
    MyProgressView progressView;
    @InjectView(R.id.view_progress2)
    MyProgressView progressView2;
    @InjectView(R.id.btn_post)
    Button post_data;
    boolean isCallFromPost = false;
    Callback<BaseDao<DataAnggota>> anggotaListener = new Callback<BaseDao<DataAnggota>>() {
        @Override
        public void success(BaseDao<DataAnggota> dataAnggotaBaseDao, Response response) {

            try {
                if (!isCallFromPost)
                    mRecyclerView.scrollVerticallyTo(0);
                progressView.stopAndGone();
                dataAnggota.clear();
                dataAnggota.addAll(dataAnggotaBaseDao.getData().getResults().getAnggota());
                mAdapter.notifyDataSetChanged();
                checkDapil();

            } catch (Exception e) {
                if (e != null) {
                    Log.d("HOME LIST", e.getMessage());
                }
            }

        }

        @Override
        public void failure(RetrofitError error) {
            try {

                progressView.stopAndError(getResources().getString(R.string.errorNetwork), true);
                progressView.setRetryClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressView.startProgress();
                        callApi();
                    }
                });
            } catch (Exception e) {
                if (e != null) {
                    Log.d("HOME LIST", e.getMessage());
                }
            }
            listenerProgress.onStopProgress();
        }
    };
    ProgressListener listenerProgress;
    Callback<JetPackPosts> resultJetPackListener = new Callback<JetPackPosts>() {
        @Override
        public void success(JetPackPosts jetPackPosts, Response response) {

            dataTimeline.clear();
            progressView2.stopAndGone();
            try {
                if (jetPackPosts.getFound() > 0) {
                    for (JetPackPost jetPackPost : jetPackPosts.getPosts()) {
                        PostDao data = new PostDao();
                        data.setPost_id(jetPackPost.getID() + "");
                        data.setUrl_image_profile(jetPackPost.getAuthor().getAvatar_URL());
                        data.setPost_author(jetPackPost.getAuthor().getName());
                        data.setPost_content(jetPackPost.getContent());
                        data.setTotal_comment(jetPackPost.getDiscussion().getComment_count());
                        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
                        Date d = null;
                        try {
                            d = date.parse(jetPackPost.getDate());
                        } catch (ParseException e) {
                        }
                        data.setPost_date(d);
                        data.setPost_status("status");

                        List<HashMap<String, String>> custom_fields = new ArrayList<>();
                        for (JetPackCustomFields j : jetPackPost.getMetadata()) {
                            custom_fields.add(Helper.valueCustomField(j.getKey(), j.getValue()));
                        }
                        data.setCustom_fields(custom_fields);
                        dataTimeline.add(data);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    progressView2.stopAndError(getResources().getString(R.string.data_kosong), false);
                }
                mAdapter.notifyDataSetChanged();
                checkDapil();

            } catch (Exception e) {
            }
            listenerProgress.onStopProgress();
        }

        @Override
        public void failure(RetrofitError error) {
            try {
                progressView.stopAndError(getResources().getString(R.string.errorNetwork), true);
                progressView.setRetryClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressView.startProgress();
                        callApi();
                    }
                });

            } catch (Exception e) {
            }
            listenerProgress.onStopProgress();
        }
    };
    Callback<BaseDao<DataGeo>> positionListener = new Callback<BaseDao<DataGeo>>() {
        @Override
        public void success(BaseDao<DataGeo> dataGeoBaseDao, Response response) {
            try {

                List<AreaGeo> areaGeos = dataGeoBaseDao.getData().getResults().getAreas();
                for (AreaGeo areaGeo : areaGeos) {
                    if (areaGeo.getLembaga().equals(Constant.TAG.DPR)) {
                        areaDapil = areaGeo;
                        ApiAdapter.callAPI().listAnggotaByDapil(Constant.KEY.PEMILUAPI, areaGeo.getId(), anggotaListener);

                        ApiAdapter.callAPIJetPack().listPostJetPack(WPKey.CUSTOM_FIELD.STATUSANGGOTA, null, null, null, resultJetPackListener);
                        dapil_list.setText(areaGeo.getNama());
                        Toast.makeText(getActivity(), getResources().getString(R.string.location) + " " + areaGeo.getNama(), Toast.LENGTH_LONG).show();

                        HashMap<String, String> props = new HashMap<>();
                        props.put("lat", lat);
                        props.put("lng", lng);
                        props.put("dapil", areaGeo.getNama());
                        Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_home_list), props);
                    }
                }

            } catch (Exception e) {
                Log.d("HOME LIST", e.getMessage());
            }

        }

        @Override
        public void failure(RetrofitError error) {
            try {
                progressView.stopAndError(getResources().getString(R.string.errorNetwork), true);
                progressView.setRetryClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressView.startProgress();
                        callApi();
                    }
                });

            } catch (Exception e) {
            }
            listenerProgress.onStopProgress();
        }
    };
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<PostDao> dataTimeline = new ArrayList<>();
    private List<Anggota> dataAnggota = new ArrayList<>();
    private LoginApi.ApiDao sessionLogin;
    private ActionBar ab;

    public HomeListFragment() {
        dataTimeline = new ArrayList<>();
    }

    //    private HomeListener fragmentListener;
    public static HomeListFragment newInstance() {
        HomeListFragment fragment = new HomeListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setPost(boolean isRefresh) {
        isCallFromPost = true;
        callApi();
        listenerProgress.onStartProgress();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listenerProgress = (ProgressListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.inject(this, view);

        lat = SharePreferences.getString(getActivity(), Constant.TAG.LATITUDE);
        lng = SharePreferences.getString(getActivity(), Constant.TAG.LONGITUDE);

        img_arrow.setImageDrawable(Helper.makeOwnIcon(getActivity(), R.color.bluegray700, Iconify.IconValue.fa_angle_down));
        my_location.setImageDrawable(Helper.makeOwnIcon(getActivity(), R.color.yellow, Iconify.IconValue.fa_compass));

        ab = Helper.getActionBarFragment(getActivity());
        mRecyclerView.setScrollViewCallbacks(this);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AdapterTimeline(getActivity(), dataTimeline, dataAnggota);
        mRecyclerView.setAdapter(mAdapter);

        sessionLogin = SharePreferences.sessionLogin(getActivity());
        progressView.startProgress();
        callApi();
        checkDapil();
        return view;
    }

    @OnClick(R.id.btn_post)
    public void postOnClick() {
        String id_dapil = SharePreferences.getString(getActivity(), Constant.TAG.ID_DAPIL);
        String id_pemilu = SharePreferences.getString(getActivity(), Constant.TAG.ID_PEMILUAPI);
        PostActivity.startActivity(getActivity(), Constant.TAG.TIMELINE, WPKey.CUSTOM_FIELD.STATUSANGGOTA, id_pemilu, sessionLogin.getDisplay_name(), 500);
    }

    @OnClick(R.id.list_komisi)
    public void dapilOnclick() {
        ListDapilActivity.startActivitiResult(getActivity(), 100);
    }

    @OnClick(R.id.img_mylocation)
    public void locationOnClick() {
        dataDapil = null;
        my_location.setImageDrawable(Helper.makeOwnIcon(getActivity(), R.color.yellow, Iconify.IconValue.fa_compass));
        GPSTracker  myGps = new GPSTracker(getActivity());
        if (myGps.canGetLocation()){
            if(myGps.getLocation()!=null) {
                SharePreferences.saveString(getActivity(), Constant.TAG.LONGITUDE, myGps.getLongitude() + "");
                SharePreferences.saveString(getActivity(), Constant.TAG.LATITUDE, myGps.getLatitude()+ "");
            }
        }
        callApi();
    }


    public void setDataDapilResult(DapilDB dataDapilResult) {
        progressView.startProgress();
        dataDapil = dataDapilResult;
        if (dataDapil != null) {
            dapil_list.setText(dataDapil.getNama());
            if (areaDapil != null)
                if (dataDapil.getIdDapil().equals(areaDapil.getId()))
                    my_location.setImageDrawable(Helper.makeOwnIcon(getActivity(), R.color.yellow, Iconify.IconValue.fa_compass));
                else
                    my_location.setImageDrawable(Helper.makeOwnIcon(getActivity(), R.color.bluegray700, Iconify.IconValue.fa_compass));

            callApi();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sessionLogin = SharePreferences.sessionLogin(getActivity());
        checkDapil();
    }


    protected int getLayoutResource() {
        return R.layout.fragment_list_home;
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {
        Log.d("onDownMotionEvent", "paling bawah");
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        if (scrollState == ScrollState.UP) {
            post_data.animate().translationY(post_data.getHeight()).setInterpolator(new AccelerateInterpolator()).start();
            card_location.animate().translationY(-card_location.getHeight()).setInterpolator(new AccelerateInterpolator()).start();
        } else if (scrollState == ScrollState.DOWN) {
            post_data.animate().translationY(0).setInterpolator(new AccelerateInterpolator()).start();
            card_location.animate().translationY(0).setInterpolator(new AccelerateInterpolator()).start();
        }

    }

    public void callApi() {
        listenerProgress.onStartProgress();
        try {
            if (dataDapil == null) {
                if (areaDapil == null) {
                    ApiAdapter.callAPI().getDapilPosition(Constant.KEY.PEMILUAPI, lat, lng, positionListener);
                }
                else {
                    List<DapilDB> dapilDBs = DapilDB.find(DapilDB.class, "ID_DAPIL = ?", areaDapil.getId());
                    if (!dapilDBs.isEmpty()) {
                        dapil_list.setText(dapilDBs.get(0).getNama());
                        ApiAdapter.callAPI().listAnggotaByDapil(Constant.KEY.PEMILUAPI, areaDapil.getId(), anggotaListener);
                        ApiAdapter.callAPIJetPack().listPostJetPack(WPKey.CUSTOM_FIELD.STATUSANGGOTA, null, null, null, resultJetPackListener);
                        HashMap<String, String> props = new HashMap<>();
                        props.put("lat", lat);
                        props.put("lng", lng);
                        props.put("dapil", dataDapil.getNama());
                        Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_home_list), props);
                    } else {
                        listenerProgress.onStopProgress();
                        Toast.makeText(getActivity(), getResources().getString(R.string.data_tidak_tersedia), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                dapil_list.setText(dataDapil.getNama());
                ApiAdapter.callAPI().listAnggotaByDapil(Constant.KEY.PEMILUAPI, dataDapil.getIdDapil(), anggotaListener);
                ApiAdapter.callAPIJetPack().listPostJetPack(WPKey.CUSTOM_FIELD.STATUSANGGOTA, null, null, null, resultJetPackListener);

                HashMap<String, String> props = new HashMap<>();
                props.put("lat", lat);
                props.put("lng", lng);
                props.put("dapil", dataDapil.getNama());
                Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_home_list), props);
            }
        } catch (Exception e) {
                    progressView.stopAndError(getResources().getString(R.string.data_kosong), true);
                    progressView.setRetryClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressView.startProgress();
                            callApi();
                        }
                    });
            listenerProgress.onStopProgress();
        }
    }

    public void checkDapil() {
        String id_dapil = SharePreferences.getString(getActivity(), Constant.TAG.ID_PEMILUAPI);
        if (id_dapil != null && !id_dapil.trim().isEmpty()) {
            post_data.setVisibility(View.VISIBLE);
        } else {
            post_data.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        areaDapil = null;
        dataAnggota = null;
        areaDapil = null;
    }
}
