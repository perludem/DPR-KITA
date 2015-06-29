package id.gits.dprkita.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import id.gits.dprkita.adapter.AdapterNews;
import id.gits.dprkita.api.ApiAdapter;
import id.gits.dprkita.dao.news.NewsDao;
import id.gits.dprkita.dao.news.PagesDao;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.view.DividerItemDecoration;
import id.gits.dprkita.utils.view.MyProgressView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewsFragment extends Fragment implements ObservableScrollViewCallbacks {


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

    public NewsFragment() {
        dataNews = new ArrayList<>();
    }

    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
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
        Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_news), null);
        return view;
    }

    public void callAPI() {
        ApiAdapter.callAPI().listBerita(Constant.TAG.TYPE_JSON, Constant.KEY.PEMILUAPI, position, callbackNews);
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
