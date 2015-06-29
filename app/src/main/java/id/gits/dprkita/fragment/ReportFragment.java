package id.gits.dprkita.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.IconButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.squareup.picasso.Picasso;

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
import id.gits.dprkita.activity.CommentActivity;
import id.gits.dprkita.activity.ListKomisiActivity;
import id.gits.dprkita.activity.LoginActivity;
import id.gits.dprkita.activity.PostActivity;
import id.gits.dprkita.adapter.AdapterReport;
import id.gits.dprkita.api.ApiAdapter;
import id.gits.dprkita.dao.PostMode;
import id.gits.dprkita.dao.jetpackdao.JetPackAttachment;
import id.gits.dprkita.dao.jetpackdao.JetPackCustomFields;
import id.gits.dprkita.dao.jetpackdao.JetPackPost;
import id.gits.dprkita.dao.jetpackdao.JetPackPosts;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.ProgressListener;
import id.gits.dprkita.utils.SharePreferences;
import id.gits.dprkita.utils.view.MyProgressView;
import id.gits.wplib.apis.LoginApi;
import id.gits.wplib.apis.WpCallback;
import id.gits.wplib.dao.PostDao;
import id.gits.wplib.utils.WPKey;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by yatnosudar on 2/14/15.
 */
public class ReportFragment extends Fragment implements ObservableScrollViewCallbacks {


    public static final String POSTMODE = "post_mode";
    private static final int ASPIRASI_SAYA = 3;
    @InjectView(R.id.re_aspirasi)
    ObservableRecyclerView mRecyclerView;
    @InjectView(R.id.view_progress)
    MyProgressView progressView;
    @InjectView(R.id.btn_post)
    Button post_data;
    @InjectView(R.id.lin_status)
    LinearLayout status;

    @InjectView(R.id.tv_filter)
    TextView tv_filter;
    @InjectView(R.id.lin_filter)
    LinearLayout lin_filter;

    String username, password;
    LoginApi.ApiDao sessionLogin;
    String id_anggota = null;
    ProgressListener listenerProgress;
    private boolean isCall = false;
    private int page = 1;
    private Callback<JetPackPosts> resultJetPackListener = new Callback<JetPackPosts>() {
        @Override
        public void success(JetPackPosts jetPackPosts, Response response) {

            try {
                if (page == 1) {
                    status.removeAllViews();
                    dataTimeline.clear();
                    progressView.stopAndGone();
                }
                if (jetPackPosts.getFound() > 0) {
                    status.removeAllViews();
                    for (JetPackPost jetPackPost : jetPackPosts.getPosts()) {
                        PostDao data = new PostDao();
                        data.setPost_id(jetPackPost.getID() + "");
                        data.setTitle(jetPackPost.getTitle());
                        data.setPost_author(jetPackPost.getAuthor().getName());
                        data.setPost_content(jetPackPost.getContent());
                        data.setUrl_image_profile(jetPackPost.getAuthor().getAvatar_URL());
                        data.setTotal_comment(jetPackPost.getDiscussion().getComment_count());
                        //2015-02-19T10:10:35+07:00
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

                        for (String key : jetPackPost.getAttachments().keySet()) {
                            JetPackAttachment jetPackAttachment = jetPackPost.getAttachments().get(key);
                            data.setAttachment_image(jetPackAttachment.getURL());
                            break;
                        }

                        if (!switchMode.getPost_type().equals(WPKey.CUSTOM_FIELD.ASPIRASI)) {
                            insertIntoView(data);
                        } else {
                            dataTimeline.add(data);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    progressView.stopAndError(getResources().getString(R.string.data_kosong), false);
                }
//            mAdapter.notifyDataSetChanged();

            /*if (!switchMode.getPost_type().equals(WPKey.CUSTOM_FIELD.ASPIRASI)) {
                int viewHeight = 200 * dataTimeline.size();
                mRecyclerView.getLayoutParams().height = viewHeight;
            }*/
                page++;
                isCall = true;

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
                        callAPI();
                    }
                });
            } catch (Exception e) {
            }

            isCall = true;
            listenerProgress.onStopProgress();
        }
    };
    private String filterKomisi = "I";
    private int filterStatus;
    private int KOMISI = 1;
    private int ANGGOTA = 2;
    private int ALL = 0;
    private boolean isHide = false;
    private AdapterReport mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<PostDao> dataTimeline = new ArrayList<>();
    private PostMode switchMode;
    private Activity activity;
    private WpCallback<List<PostDao>> resultListener = new WpCallback<List<PostDao>>() {
        @Override
        public void success(List<PostDao> apiDaos, String json) {
            try {
                dataTimeline.clear();
                dataTimeline.addAll(apiDaos);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        progressView.stopAndGone();
                        if (switchMode.getPost_type().equals(WPKey.CUSTOM_FIELD.BIOCOMMENT)) {
                            int viewHeight = 200 * dataTimeline.size();
                            mRecyclerView.getLayoutParams().height = viewHeight;
                        }
                    }
                });
            } catch (Exception e) {
            }
        }

        @Override
        public void failure(final String error) {
            Log.d("ini error", error);
            try {
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressView.stopAndError(error, true);
                            progressView.setRetryClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressView.startProgress();
                                    callAPI();
                                }
                            });
                        }
                    });
                }
            } catch (Exception e) {
            }

        }
    };

    public static ReportFragment newInstance(PostMode postMode) {
        ReportFragment fragment = new ReportFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(POSTMODE, postMode);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setPost(boolean isRefresh) {
        isCall = false;
        page = 1;
        callAPI();
        listenerProgress.onStartProgress();
    }

    public String getFilterKomisi() {
        return filterKomisi;
    }

    public void setFilterKomisi(String filterKomisi) {
        this.filterKomisi = filterKomisi;
        filterStatus = KOMISI;
        page = 1;
        isCall = false;
        progressView.startProgress();
        callAPI();
    }

    public void setSwitchMode(PostMode switchMode) {
        this.switchMode = switchMode;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            switchMode = (PostMode) getArguments().getSerializable(POSTMODE);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        listenerProgress = (ProgressListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.inject(this, view);
        sessionLogin = SharePreferences.sessionLogin(getActivity());
        username = SharePreferences.getString(activity, Constant.TAG.USERNAME);
        password = SharePreferences.getString(activity, Constant.TAG.PASSWORD);
        mRecyclerView.setScrollViewCallbacks(this);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));
        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AdapterReport(activity, dataTimeline);
        mRecyclerView.setAdapter(mAdapter);
        callAPI();
        if (switchMode.getPost_type().equals(WPKey.CUSTOM_FIELD.ASPIRASI)) {
            checkDapil();
            Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_aspirasi), null);
            id_anggota = SharePreferences.getString(getActivity(), Constant.TAG.ID_PEMILUAPI);
            if (id_anggota == null || !id_anggota.isEmpty()) {
                post_data.setVisibility(View.GONE);
            } else {
                post_data.setVisibility(View.VISIBLE);
            }
            status.setVisibility(View.GONE);
        } else {
            post_data.setVisibility(View.GONE);
            lin_filter.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            status.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.btn_post)
    public void btnPost() {
        if (sessionLogin != null) {
            PostActivity.startActivity(activity, switchMode.getTitle(), switchMode.getPost_type(), switchMode.getId_anggota_dewan(), switchMode.getName_anggota_dewan(), 300);
        } else {
            LoginActivity.startActivity(getActivity(), 0);
        }
    }

    public void checkDapil() {
        String id_dapil = SharePreferences.getString(getActivity(), Constant.TAG.ID_PEMILUAPI);

        if (id_dapil != null && !id_dapil.trim().isEmpty()) {
            post_data.setVisibility(View.VISIBLE);
        } else {
            post_data.setVisibility(View.VISIBLE);
        }
    }

    private void callAPI() {
        listenerProgress.onStartProgress();
        if (!switchMode.getPost_type().equals(WPKey.CUSTOM_FIELD.ASPIRASI)) {
            if(switchMode.getPost_type().equals(WPKey.CUSTOM_FIELD.BIOCOMMENT))
                    ApiAdapter.callAPIJetPack().listPostJetPackSearch(WPKey.CUSTOM_FIELD.ASPIRASI, switchMode.getName_anggota_dewan(), page, resultJetPackListener);
            else
                ApiAdapter.callAPIJetPack().listPostJetPack(switchMode.getPost_type(), WPKey.KEY.ID_DPR_PEMILU, switchMode.getId_anggota_dewan(), page, resultJetPackListener);
        } else {
            if (filterStatus == ALL) {
                ApiAdapter.callAPIJetPack().listPostJetPack(switchMode.getPost_type(), null, null, page, resultJetPackListener);
                tv_filter.setText("TAMPILKAN SEMUA");
            } else if (filterStatus == KOMISI) {
                ApiAdapter.callAPIJetPack().listPostJetPack(switchMode.getPost_type(), WPKey.KEY.KOMISI, filterKomisi, page, resultJetPackListener);
                tv_filter.setText("TAMPILKAN KOMISI");
            } else if (filterStatus == ANGGOTA) {
                ApiAdapter.callAPIJetPack().listPostJetPack(switchMode.getPost_type(), WPKey.KEY.KOMISI, "-", page, resultJetPackListener);
                tv_filter.setText("TAMPILKAN ANGGOTA DPR");
            } else if (filterStatus == ASPIRASI_SAYA) {
                ApiAdapter.callAPIJetPack().listPostJetPack(switchMode.getPost_type(), WPKey.KEY.USERNAME, sessionLogin.getDisplay_name(), page, resultJetPackListener);
                tv_filter.setText("TAMPILKAN ASPIRASI SAYA");
            }
        }

        isCall = false;
    }

    public void insertIntoView(final PostDao postDao) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_timeline_home, null);
        TextView nama_lengkap = ButterKnife.findById(v, R.id.nama_lengkap);
        TextView tv_posting = ButterKnife.findById(v, R.id.tv_posting);
        ImageView img_posting = ButterKnife.findById(v, R.id.img_posting);
        TextView tv_date_posting = ButterKnife.findById(v, R.id.tv_date_posting);
        ImageView img_profile = ButterKnife.findById(v, R.id.img_profile);
        IconButton btn_comment = ButterKnife.findById(v, R.id.btn_comment);
        IconButton btn_report = ButterKnife.findById(v, R.id.btn_report);

        btn_comment.setVisibility(View.VISIBLE);
        List<HashMap<String, String>> object = postDao.getCustom_fields();

        String komisi = "";
        String from = "";
        String to = "";
        if (postDao.getTitle() != null) {
            if (postDao.getTitle().contains(Constant.TAG.ASPIRASI)) {
                String[] kom = postDao.getTitle().split(":");
                komisi = " untuk " + kom[1];
            }
        }
        for (HashMap<String, String> objectHashMap : object) {
            String key = objectHashMap.get(WPKey.KEY.KEY);
            String val = objectHashMap.get(WPKey.KEY.VALUE);

            if (key.equals("username")) {
                nama_lengkap.setText(val + komisi);
                from = val;
            }
            if (key.equals("name_dpr"))
                nama_lengkap.setText(val);
            if (key.equals("id_dpr_pemiluapi"))
                to = val;
        }


        if (postDao.getAttachment_image() != null) {
            img_posting.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(postDao.getAttachment_image())
                    .resize(400, 400).centerInside()
                    .placeholder(R.drawable.gradient_header_background)
                    .error(R.drawable.gradien_black).into(img_posting);
        } else {
            img_posting.setVisibility(View.GONE);
        }
        tv_posting.setText(Html.fromHtml(postDao.getPost_content().replaceAll("\\r\\n|\\r|\\n", "")).toString().trim());
//        img_profile.setImageDrawable(Helper.makeOwnIcon(activity, R.color.bluegray700, Iconify.IconValue.fa_user));
        Date d = postDao.getPost_date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");
        tv_date_posting.setText(simpleDateFormat.format(d));

        Picasso.with(activity)
                .load(postDao.getUrl_image_profile())
                .resize(400, 400).centerInside()
                .placeholder(Helper.makeOwnIcon(activity, R.color.bluegray700, Iconify.IconValue.fa_user))
                .error(Helper.makeOwnIcon(activity, R.color.bluegray700, Iconify.IconValue.fa_user)).into(img_profile);

        boolean isToDPR = false;
        if (id_anggota != null) {
            if (id_anggota.equals(to)) {
                isToDPR = true;
            }
        }

        int visibility = View.GONE;
        boolean isComment = false;
        if (postDao.getTotal_comment() > 0) {
            btn_comment.setText("{fa_reply} " + postDao.getTotal_comment() + " TANGGAPAN");
            if (switchMode.getPost_type().equals(WPKey.CUSTOM_FIELD.STATUSANGGOTA)) {
                visibility = View.VISIBLE;
            } else if (switchMode.getPost_type().equals(WPKey.CUSTOM_FIELD.BIOCOMMENT)) {
                visibility = View.VISIBLE;
                if (!isToDPR == false) {
                    isComment = false;
                    if (sessionLogin.getDisplay_name().equals(postDao.getPost_author()))
                        isComment = true;
                } else {
                    isComment = true;
                }
            }
        } else {
            if (isToDPR == false) {
                visibility = View.GONE;
                isComment = false;
            } else {
                isComment = true;
                visibility = View.VISIBLE;
            }
            if (sessionLogin.getDisplay_name().equals(postDao.getPost_author())) {
                visibility = View.VISIBLE;
                isComment = true;
            }
            if (switchMode.getPost_type().equals(WPKey.CUSTOM_FIELD.STATUSANGGOTA)) {
                visibility = View.VISIBLE;
                isComment = true;
            }

            btn_comment.setText("{fa_reply} TANGGAPAN");
        }


        btn_comment.setVisibility(visibility);
        final boolean finalIsComment = isComment;
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentActivity.launchActivity(getActivity(), Integer.valueOf(postDao.getPost_id()), finalIsComment);
            }
        });

        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.dialogReport(getActivity(), postDao.getPost_author());
            }
        });
        status.addView(v);
    }

    protected int getLayoutResource() {
        return R.layout.fragment_report;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        id_anggota = SharePreferences.getString(getActivity(), Constant.TAG.ID_PEMILUAPI);
        LoginApi.ApiDao sessioLogin = SharePreferences.sessionLogin(getActivity());
        if (sessioLogin != null) {
            List<String> role = sessioLogin.getRoles();
            if (!role.isEmpty()) {
                if (Helper.isAnggotaDPR(role.get(0))) {
                    if (switchMode.getPost_type().equals(WPKey.CUSTOM_FIELD.STATUSANGGOTA) && id_anggota.equals(switchMode.getId_anggota_dewan())) {
                        inflater.inflate(R.menu.menu_report, menu);
                        menu.findItem(R.id.action_report).setIcon(new IconDrawable(activity, Iconify.IconValue.fa_pencil_square_o).colorRes(R.color.white).actionBarSize());
                    }
                } else {
                    if (switchMode.getPost_type().equals(WPKey.CUSTOM_FIELD.BIOCOMMENT)) {
                        inflater.inflate(R.menu.menu_report, menu);
                        menu.findItem(R.id.action_report).setIcon(new IconDrawable(activity, Iconify.IconValue.fa_pencil_square_o).colorRes(R.color.white).actionBarSize());
                    }
                }

            }
        } else {

            if (switchMode.getPost_type().equals(WPKey.CUSTOM_FIELD.BIOCOMMENT)) {
                inflater.inflate(R.menu.menu_report, menu);
                menu.findItem(R.id.action_report).setIcon(new IconDrawable(activity, Iconify.IconValue.fa_pencil_square_o).colorRes(R.color.white).actionBarSize());
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_report:
                if (sessionLogin != null) {
                    PostActivity.startActivity(activity, switchMode.getTitle(), switchMode.getPost_type(), switchMode.getId_anggota_dewan(), switchMode.getName_anggota_dewan(), 400);
                } else {
                    LoginActivity.startActivity(getActivity(), 0);
                }
                break;
            case R.id.action_filter:
                dialogFilter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.lin_filter)
    public void lin_filter() {
        dialogFilter();
    }


    public void dialogFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.filter_aspirasi))
                .setItems(getResources().getStringArray(R.array.arrayFilterAspirasi), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                page = 1;
                                filterStatus = ALL;
                                progressView.startProgress();
                                callAPI();
                                break;
                            case 1:
                                ListKomisiActivity.startActivity(getActivity(), 9);
                                break;
                            case 2:
                                filterStatus = ANGGOTA;
                                progressView.startProgress();
                                page = 1;
                                callAPI();
                                break;
                            case 3:
                                if (sessionLogin != null) {
                                    filterStatus = ASPIRASI_SAYA;
                                    progressView.startProgress();
                                    page = 1;
                                    callAPI();
                                }
                                break;
                        }
                    }
                });
        builder.create().show();
    }


    int iz = 0;
    boolean isScrollUp = false;
    boolean isScrollDown = false;
    @Override
    public void onResume() {
        super.onResume();
        sessionLogin = SharePreferences.sessionLogin(getActivity());
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {
        Log.d("i",i+" "+b+" "+b2);
        if (i>iz) {
            if (b == false && b2 == false && isCall == true && isScrollUp) {
                callAPI();
            }
            iz=i;
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        if (scrollState == ScrollState.UP) {
            isScrollUp = true;
            post_data.animate().translationY(post_data.getHeight()).setInterpolator(new AccelerateInterpolator()).start();
            lin_filter.animate().translationY(-lin_filter.getHeight()).setInterpolator(new AccelerateInterpolator()).start();
        } else if (scrollState == ScrollState.DOWN) {
            isScrollUp = false;
            post_data.animate().translationY(0).setInterpolator(new AccelerateInterpolator()).start();
            lin_filter.animate().translationY(0).setInterpolator(new AccelerateInterpolator()).start();
        }

    }


}
