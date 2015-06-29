package id.gits.dprkita.fragment;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.Gson;
import com.joanzapata.android.iconify.Iconify;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import id.gits.dprkita.R;
import id.gits.dprkita.activity.LoginActivity;
import id.gits.dprkita.api.ApiAdapter;
import id.gits.dprkita.dao.BaseDao;
import id.gits.dprkita.dao.PostMode;
import id.gits.dprkita.dao.anggota.Anggota;
import id.gits.dprkita.dao.anggota.DataAnggota;
import id.gits.dprkita.dao.jetpackdao.JetPackPosts;
import id.gits.dprkita.dao.partai.DataPartai;
import id.gits.dprkita.dao.partai.Partai;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.SharePreferences;
import id.gits.dprkita.utils.json.PartaiJSON;
import id.gits.wplib.apis.LoginApi;
import id.gits.wplib.apis.NewPostApi;
import id.gits.wplib.apis.WpCallback;
import id.gits.wplib.utils.WPKey;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileFragment extends BaseFragment implements ObservableScrollViewCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_IDANGGOTA = "param1";
    private static final String ARG_NAMA = "param2";
    @InjectView(R.id.scroll)
    ObservableScrollView scroll;
    @InjectView(R.id.img_profile)
    ImageView img_profile;
    @InjectView(R.id.img_background)
    ImageView img_background;
    @InjectView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @InjectView(R.id.tv_fraksi_profile)
    TextView tv_fraksi_name;
    @InjectView(R.id.tv_komisi_profile)
    TextView tv_komisi_name;
    @InjectView(R.id.btn_biodata)
    Button btn_biodata;
    @InjectView(R.id.btn_timeline)
    Button btn_timeline;
    @InjectView(R.id.btn_like)
    Button btn_like;
    @InjectView(R.id.btn_comment)
    Button btn_comment;
    @InjectView(R.id.cover_profile)
    RelativeLayout cover_profile;


    int total = 0;
    Callback<JetPackPosts> listenerTotalDukung = new Callback<JetPackPosts>() {
        @Override
        public void success(JetPackPosts jetPackPosts, Response response) {
            try {
                total = jetPackPosts.getFound();
                if (total > 0) {
                    btn_like.setText(total + " dukung");
                }
            } catch (Exception e) {
            }

        }

        @Override
        public void failure(RetrofitError error) {

        }
    };
    Bundle savedInstanceState;
    Callback<JetPackPosts> listenerDukung = new Callback<JetPackPosts>() {
        @Override
        public void success(JetPackPosts jetPackPosts, Response response) {
            try {
                if (jetPackPosts.getFound() > 0) {

                    btn_like.setEnabled(false);
                    btn_like.setCompoundDrawablesWithIntrinsicBounds(null, Helper.makeOwnIcon(getActivity(), R.color.blue600, Iconify.IconValue.fa_thumbs_up), null, null);
                } else {
                    btn_like.setEnabled(true);
                }
            } catch (Exception e) {
            }

        }

        @Override
        public void failure(RetrofitError error) {

        }
    };
    Callback<BaseDao<DataAnggota>> listenerProfile = new Callback<BaseDao<DataAnggota>>() {
        @Override
        public void success(BaseDao<DataAnggota> dataAnggotaBaseDao, Response response) {
            try {
                initData(dataAnggotaBaseDao.getData().getResults().getAnggota().get(0));
                btn_timeline.setEnabled(true);
                btn_comment.setEnabled(true);


            } catch (Exception e) {
            }

        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    BaseDaoPartai partai;
    List<Partai> dataPartai = new ArrayList<>();
    LoginApi.ApiDao sessionLogin;
    // TODO: Rename and change types of parameters
    private String id_anggota_dpr;
    private String nama_anggota;
    private Anggota anggota;
    private FragmentListener fragmentListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id   Parameter 1.
     * @param nama Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */

    public static ProfileFragment newInstance(String id, String nama) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IDANGGOTA, id);
        args.putString(ARG_NAMA, nama);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentListener = (FragmentListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_anggota_dpr = getArguments().getString(ARG_IDANGGOTA);
            nama_anggota = getArguments().getString(ARG_NAMA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        ((NavDrawerActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // Inflate the layout for this fragment
        this.savedInstanceState = savedInstanceState;
        view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.inject(this, view);
        ab = Helper.getActionBarFragment(getActivity());
        ab.setDisplayHomeAsUpEnabled(true);
        sessionLogin = SharePreferences.sessionLogin(getActivity());
        partai = new Gson().fromJson(PartaiJSON.partai, BaseDaoPartai.class);
        resetButton();
        btn_comment.setEnabled(false);
        btn_like.setEnabled(false);
        btn_timeline.setEnabled(false);

        btn_biodata.setCompoundDrawablesWithIntrinsicBounds(null, Helper.makeOwnIcon(getActivity(), R.color.red, Iconify.IconValue.fa_user), null, null);
        scroll.setScrollViewCallbacks(this);
        callApi();


        return view;

    }

    public int generateColor(String idPartai) {
        String result = "#fff";
        dataPartai.addAll(partai.getData().getResults().getPartai());
        for (Partai partai : dataPartai) {
            if (partai.getId().equals(idPartai)) {
                result = partai.getColors().get(0);

                break;
            }
        }
        fragmentListener.changeColorToolbar(result);
        return Color.parseColor(result);
    }

    private void resetButton() {
        btn_biodata.setCompoundDrawablesWithIntrinsicBounds(null, Helper.makeOwnIcon(getActivity(), R.color.bluegray700, Iconify.IconValue.fa_user), null, null);
        btn_comment.setCompoundDrawablesWithIntrinsicBounds(null, Helper.makeOwnIcon(getActivity(), R.color.bluegray700, Iconify.IconValue.fa_comment), null, null);
        btn_like.setCompoundDrawablesWithIntrinsicBounds(null, Helper.makeOwnIcon(getActivity(), R.color.bluegray700, Iconify.IconValue.fa_thumbs_up), null, null);
        btn_timeline.setCompoundDrawablesWithIntrinsicBounds(null, Helper.makeOwnIcon(getActivity(), R.color.bluegray700, Iconify.IconValue.fa_clock_o), null, null);

    }

    @OnClick(R.id.btn_comment)
    public void btnComment() {
        resetButton();
        btn_comment.setCompoundDrawablesWithIntrinsicBounds(null, Helper.makeOwnIcon(getActivity(), R.color.red, Iconify.IconValue.fa_comment), null, null);
        Helper.replaceFragmentR(getActivity(), ReportFragment.newInstance(
                        new PostMode(Constant.TAG.COMMENT, WPKey.CUSTOM_FIELD.BIOCOMMENT, anggota.getId(), anggota.getNama())),
                R.id.container_profile, getChildFragmentManager());

        HashMap<String, String> props = new HashMap<>();
        props.put("nama", anggota.getNama());
        props.put("fraksi", anggota.getPartai().getName());
        props.put("komisi", anggota.getKomisi().getName() != null ? anggota.getKomisi().getName() : "");
        Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_aspirasi_anggota), props);
    }

    @OnClick(R.id.btn_biodata)
    public void btnBiodata() {
        resetButton();
        btn_biodata.setCompoundDrawablesWithIntrinsicBounds(null, Helper.makeOwnIcon(getActivity(), R.color.red, Iconify.IconValue.fa_user), null, null);
        Helper.replaceFragmentR(getActivity(), BiodataFragment.newInstance(anggota), R.id.container_profile, getChildFragmentManager());

        HashMap<String, String> props = new HashMap<>();
        props.put("nama", anggota.getNama());
        props.put("fraksi", anggota.getPartai().getName());
        props.put("komisi", anggota.getKomisi().getName() != null ? anggota.getKomisi().getName() : "");
        Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_biodata), props);
    }

    @OnClick(R.id.btn_timeline)
    public void btnTimeline() {
        resetButton();
        btn_timeline.setCompoundDrawablesWithIntrinsicBounds(null, Helper.makeOwnIcon(getActivity(), R.color.red, Iconify.IconValue.fa_clock_o), null, null);
        Helper.replaceFragmentR(getActivity(), ReportFragment.newInstance(
                        new PostMode(Constant.TAG.TIMELINE, WPKey.CUSTOM_FIELD.STATUSANGGOTA, anggota.getId(), anggota.getNama())),
                R.id.container_profile, getChildFragmentManager());

        HashMap<String, String> props = new HashMap<>();
        props.put("nama", anggota.getNama());
        props.put("fraksi", anggota.getPartai().getName());
        props.put("komisi", anggota.getKomisi().getName() != null ? anggota.getKomisi().getName() : "");
        Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_timeline), props);
    }

    @OnClick(R.id.btn_like)
    public void btn_like() {
        if (sessionLogin != null) {
            String username = SharePreferences.getString(getActivity(), Constant.TAG.USERNAME);
            String password = SharePreferences.getString(getActivity(), Constant.TAG.PASSWORD);

            HashMap<String, Object> content = new HashMap<>();
            content.put(WPKey.KEY.POST_TITLE, id_anggota_dpr);
            content.put(WPKey.KEY.POST_STATUS, Constant.TAG.PUBLISH);
            content.put(WPKey.KEY.POST_TYPE, WPKey.CUSTOM_FIELD.DUKUNG);

            List<HashMap<String, String>> customFields = new ArrayList<>();
            customFields.add(Helper.valueCustomField(WPKey.KEY.ID_DPR_PEMILU, id_anggota_dpr));
            customFields.add(Helper.valueCustomField(WPKey.KEY.USERNAME, username));

            content.put(WPKey.KEY.CUSTOM_FIELD, customFields);
            NewPostApi.newInstance().callApi(username, password, content, new WpCallback<String>() {
                @Override
                public void success(String s, String json) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_like.setEnabled(false);
                            total++;
                            btn_like.setText(total + " dukung");
                            btn_like.setCompoundDrawablesWithIntrinsicBounds(null, Helper.makeOwnIcon(getActivity(), R.color.blue600, Iconify.IconValue.fa_thumbs_up), null, null);
                        }
                    });


                    HashMap<String, String> props = new HashMap<>();
                    props.put("nama", anggota.getNama());
                    props.put("fraksi", anggota.getPartai().getName());
                    props.put("komisi", anggota.getKomisi().getName() != null ? anggota.getKomisi().getName() : "");
                    Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_like), props);

                }

                @Override
                public void failure(String error) {
                    Log.d("error like button'", error);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.errorNetwork), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });
        } else {
            LoginActivity.startActivity(getActivity(), 0);
        }
    }

    public void initData(Anggota anggota) {
        this.anggota = anggota;
        tv_profile_name.setText(anggota.getNama());
        tv_fraksi_name.setText(anggota.getPartai().getName());
        tv_komisi_name.setText(anggota.getKomisi().getName() != null ? anggota.getKomisi().getName() : "");

        HashMap<String, String> props = new HashMap<>();
        props.put("nama", anggota.getNama());
        props.put("fraksi", anggota.getPartai().getName());
        props.put("komisi", anggota.getKomisi().getName() != null ? anggota.getKomisi().getName() : "");
        Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_profile), props);

        Picasso.with(getActivity())
                .load(anggota.getFoto_url())
                .placeholder(R.drawable.gradient_header_background)
                .error(R.drawable.gradien_black)
                .into(img_profile);

        /*int[] imageBackground = new int[]{R.drawable.nasdem1, R.drawable.pkb1, R.drawable.pks2, R.drawable.pdi1, R.drawable.golkar1, R.drawable.gerindra1, R.drawable.demokrat1, R.drawable.pan1, R.drawable.ppp1, R.drawable.hanura1};
        for (int i = 0; i < imageBackground.length; i++) {
            if (Integer.valueOf(anggota.getPartai().getId()) == (i + 1)) {
                img_background.setImageDrawable(getResources().getDrawable(imageBackground[i]));
            }
        }*/

        int color = generateColor(anggota.getPartai().getId());
        img_background.setBackgroundColor(color);
        cover_profile.setBackgroundColor(color);


        if (savedInstanceState == null) {
            Helper.addFragment(getActivity(), BiodataFragment.newInstance(anggota), R.id.container_profile, getChildFragmentManager());
        } else {
            Helper.replaceFragment(getActivity(), BiodataFragment.newInstance(anggota), R.id.container_profile, getChildFragmentManager());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ab.setTitle("");
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_profile;
    }

    public void callApi() {

        if (sessionLogin != null) {
            ApiAdapter.callAPIJetPack().listPostJetPack(WPKey.CUSTOM_FIELD.DUKUNG, WPKey.KEY.ID_DPR_PEMILU, id_anggota_dpr, sessionLogin.getUser_id(), listenerDukung);
        }
        ApiAdapter.callAPIJetPack().listPostJetPack(WPKey.CUSTOM_FIELD.DUKUNG, WPKey.KEY.ID_DPR_PEMILU, id_anggota_dpr, null, listenerTotalDukung);
        ApiAdapter.callAPI().detailAnggota(id_anggota_dpr, Constant.KEY.PEMILUAPI, listenerProfile);
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
    public void onDestroy() {
        super.onDestroy();
        fragmentListener.resetColorToolbar();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void sendPostFromPost() {
        for (Fragment fragment : Helper.getFragmentFromViewPager(this)) {
            if (fragment instanceof ReportFragment) {
                ((ReportFragment) fragment).setPost(false);
                break;
            }
        }
    }

    public interface FragmentListener {
        public void changeColorToolbar(String color);

        public void resetColorToolbar();
    }

    public class BaseDaoPartai implements Serializable {
        DataPartai data;

        public DataPartai getData() {
            return data;
        }
    }
}
