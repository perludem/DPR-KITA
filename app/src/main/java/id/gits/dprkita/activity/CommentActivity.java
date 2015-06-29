package id.gits.dprkita.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.IconButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import id.gits.dprkita.R;
import id.gits.dprkita.adapter.AdapterComment;
import id.gits.dprkita.api.ApiAdapter;
import id.gits.dprkita.dao.jetpackdao.JetPackComment;
import id.gits.dprkita.dao.jetpackdao.JetPackComments;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.SharePreferences;
import id.gits.dprkita.utils.view.MyProgressView;
import id.gits.wplib.apis.LoginApi;
import id.gits.wplib.apis.NewCommentApi;
import id.gits.wplib.apis.WpCallback;
import id.gits.wplib.utils.WPKey;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CommentActivity extends BaseActivity implements ObservableScrollViewCallbacks {
    private static final String ID_POST = "idpost";
    private static final String ISCOMMENT = "iscomment";
    @InjectView(R.id.re_comment)
    ObservableRecyclerView re_comment;
    @InjectView(R.id.lin_comment)
    LinearLayout lin_comment;
    @InjectView(R.id.commentText)
    EditText inpComment;
    @InjectView(R.id.view_progress)
    MyProgressView progressView;
    @InjectView(R.id.buttonSend)
    IconButton btnSend;
    @InjectView(R.id.progressbar)
    SmoothProgressBar mProgress;
    int tot_comment = 0;
    int id_post;
    int page = 1;
    boolean isScroll = false;
    boolean isHide = false;
    boolean isClear = true;
    WpCallback<Integer> litenerComment = new WpCallback<Integer>() {
        @Override
        public void success(Integer integer, String json) {
            page = 1;
            tot_comment++;
            isClear = true;
            callApi();
        }

        @Override
        public void failure(final String error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgress.progressiveStop();
                    Toast.makeText(CommentActivity.this, error, Toast.LENGTH_LONG).show();
                }
            });
        }
    };
    Callback<JetPackComments> listenerComment = new Callback<JetPackComments>() {
        @Override
        public void success(JetPackComments jetPackPosts, Response response) {
            mProgress.progressiveStop();
            if (jetPackPosts.getComments().size() > 0) {
                progressView.stopAndGone();
                if (isClear) {
                    data.clear();
                }
                data.addAll(jetPackPosts.getComments());
                adapter.notifyDataSetChanged();
                isClear = false;
                isScroll = true;
                page++;
            } else {
                isClear = false;
                isScroll = true;
                progressView.stopAndError(getResources().getString(R.string.belum_ada_tanggapan), false);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            mProgress.progressiveStop();
            isScroll = true;
            progressView.stopAndError(error.getMessage(), true);
            progressView.setRetryClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressView.startProgress();
                    callApi();
                }
            });
        }
    };
    private AdapterComment adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<JetPackComment> data;
    private String username, password;
    private LoginApi.ApiDao sessionLogin;

    public static void launchActivity(Activity context, int id, boolean isComment) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra(ID_POST, id);
        intent.putExtra(ISCOMMENT, isComment);
        context.startActivityForResult(intent, 120);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id_post = getIntent().getIntExtra(ID_POST, 0);
        ButterKnife.inject(this);
        mProgress.progressiveStop();

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Tanggapan");

        data = new ArrayList<>();
        adapter = new AdapterComment(this, data);
        linearLayoutManager = new LinearLayoutManager(this);
        re_comment.setLayoutManager(linearLayoutManager);
        re_comment.setScrollViewCallbacks(this);
        re_comment.setAdapter(adapter);

        username = SharePreferences.getString(this, Constant.TAG.USERNAME);
        password = SharePreferences.getString(this, Constant.TAG.PASSWORD);

        boolean isComment = getIntent().getBooleanExtra(ISCOMMENT, false);

        if (!isComment) {
            lin_comment.setVisibility(View.GONE);
        }
        if (username == null || password == null) {
            lin_comment.setVisibility(View.GONE);
        }
        sessionLogin = SharePreferences.sessionLogin(this);
        callApi();
        progressView.startProgress();
    }

    @OnClick(R.id.buttonSend)
    public void sendComment() {
        String comment = inpComment.getText().toString();
        if (comment.length() > 10) {
            mProgress.progressiveStart();
            HashMap<String, Object> content = new HashMap<>();
            content.put(WPKey.KEY.CONTENT, comment);
            content.put(WPKey.KEY.AUTHOR, sessionLogin.getUsername());
            NewCommentApi.newInstance().callApi(username, password, id_post, content, litenerComment);
            inpComment.setText("");
        } else {
            Toast.makeText(CommentActivity.this, getResources().getString(R.string.long_post), Toast.LENGTH_LONG).show();
        }
    }

    public void callApi() {
        ApiAdapter.callAPIJetPack().getCommentJetPack(id_post, page, listenerComment);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_comment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {
        if (b == false && b2 == false && isScroll == true) {
            callApi();
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("isComment", tot_comment);
        setResult(RESULT_OK, intent);
        finish();
    }
}
