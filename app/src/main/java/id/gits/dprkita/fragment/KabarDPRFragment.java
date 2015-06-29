package id.gits.dprkita.fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;
import id.gits.dprkita.adapter.AdapterNews;
import id.gits.dprkita.api.ApiAdapter;
import id.gits.dprkita.dao.jetpackdao.JetPackPostPage;
import id.gits.dprkita.dao.jetpackdao.JetPackPostsPage;
import id.gits.dprkita.dao.news.NewsDao;
import id.gits.dprkita.dao.news.PagesDao;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.view.DividerItemDecoration;
import id.gits.dprkita.utils.view.MyProgressView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//import android.support.v7.app.ActionBar;

public class KabarDPRFragment extends Fragment implements ObservableScrollViewCallbacks {


    @InjectView(R.id.re_home)
    ObservableRecyclerView mRecyclerView;
    @InjectView(R.id.view_progress)
    MyProgressView progressView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<PagesDao> dataNews;

    private int stat = 3;
    private int position = 1;
    private boolean isCall = false;
    Callback<JetPackPostsPage> callbackDPR = new Callback<JetPackPostsPage>() {
        @Override
        public void success(JetPackPostsPage jetPackPosts, Response response) {
            List<PagesDao> pagesDaos = new ArrayList<>();
            for (JetPackPostPage jetPackPost : jetPackPosts.getPosts()) {
                PagesDao data = new PagesDao();
                data.setId(jetPackPost.getID());
                data.setTitle(jetPackPost.getTitle());
                data.setTitle_plain(jetPackPost.getTitle());
                String date = Html.fromHtml(jetPackPost.getDate()).toString();
                try {
                    data.setDate(Helper.dateParse(date, "yyyy-MM-dd'T'hh:mm:ssZ", "yyyy-MM-dd hh:mm:ss"));
                } catch (ParseException e) {
                }
                data.setSlug(jetPackPost.getSlug());
                data.setType(jetPackPost.getType());
                data.setUrl(jetPackPost.getURL());
                data.setStatus(jetPackPost.getStatus());
                data.setExcerpt(jetPackPost.getExcerpt());
                data.setContent(jetPackPost.getContent());
                data.setModified(jetPackPost.getModified());
                pagesDaos.add(data);
            }
            try {
                progressView.stopAndGone();
                dataNews.addAll(pagesDaos);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setVisibility(View.VISIBLE);
                stat = 3;
                position++;
            } catch (Exception e) {
            }
            isCall = true;
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d(error.getMessage(), error.getMessage());
            try {
                progressView.stopAndError(getActivity().getResources().getString(R.string.errorNetwork), true);
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
        }
    };
    Callback<NewsDao> callbackNews = new Callback<NewsDao>() {
        @Override
        public void success(NewsDao newsDao, Response response) {
            try {
                progressView.stopAndGone();
                dataNews.addAll(newsDao.getPosts());
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setVisibility(View.VISIBLE);
                stat = 3;
                position++;
            } catch (Exception e) {
            }
            isCall = true;

        }

        @Override
        public void failure(RetrofitError error) {
            try {
                progressView.stopAndError(getActivity().getResources().getString(R.string.errorNetwork), true);
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
        }
    };
    private ActionBar ab;

    public KabarDPRFragment() {
        dataNews = new ArrayList<>();
    }

    // TODO: Rename and change types and number of parameters
    public static KabarDPRFragment newInstance() {
        KabarDPRFragment fragment = new KabarDPRFragment();
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
        View view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.inject(this, view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setScrollViewCallbacks(this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AdapterNews(getActivity(), dataNews);
        mRecyclerView.setAdapter(mAdapter);
        callAPI();
        Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_kabardpr), null);
        return view;
    }

    public void callAPI() {
        ApiAdapter.callAPIJetPack().listPostJetPack(Constant.TAG.BERITADPR, position, 10, callbackDPR);
        isCall = false;
    }

    protected int getLayoutResource() {
        return R.layout.fragment_news;
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {
        Log.d("scroll ", mRecyclerView.getScrollY() + " int " + i + " boolean " + b + " boolean b2 " + b2);
        if (b == false && b2 == false && isCall == true) {
            callAPI();
        }
    }

    @Override
    public void onDownMotionEvent() {
        Log.d("onDownMotionEvent", "paling bawah");

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }


}
