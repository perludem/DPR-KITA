package id.gits.dprkita.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import id.gits.dprkita.R;
import id.gits.dprkita.api.ApiAdapter;
import id.gits.dprkita.dao.BaseDao;
import id.gits.dprkita.dao.jetpackdao.JetPackPostPage;
import id.gits.dprkita.dao.jetpackdao.JetPackPostsPage;
import id.gits.dprkita.dao.news.NewsDao;
import id.gits.dprkita.dao.news.PagesDao;
import id.gits.dprkita.dao.selasar.DataSelasar;
import id.gits.dprkita.dao.selasar.Infografis;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.CropTopTransformation;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.ProgressListener;
import id.gits.dprkita.utils.view.MyProgressView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeNewsFragment extends Fragment {
    @InjectView(R.id.layout_kabardpr)
    LinearLayout layoutDPR;
    @InjectView(R.id.layout_news)
    LinearLayout layoutNews;
    @InjectView(R.id.view_progress_kabardpr)
    MyProgressView progressViewDPR;
    @InjectView(R.id.view_progress)
    MyProgressView progressViewNews;
    @InjectView(R.id.view_progress_infografis)
    MyProgressView progressViewInfografis;

    @InjectView(R.id.layout_infografis)
    LinearLayout layoutInfografis;
    ProgressListener listenerProgress;
    private List<PagesDao> dataNews;
    private ActionBar ab;

    public HomeNewsFragment() {
        dataNews = new ArrayList<>();
    }

    public static HomeNewsFragment newInstance() {
        HomeNewsFragment fragment = new HomeNewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listenerProgress = (ProgressListener) activity;
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

        callAPI();
        Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_home_news), null);

        return view;
    }

    public void callAPI() {
        ApiAdapter.callAPI().listBerita(Constant.TAG.TYPE_JSON, Constant.KEY.PEMILUAPI, 1, callbackNews);
        ApiAdapter.callAPI().listSelasar(Constant.KEY.PEMILUAPI,0, callbackInfografis);
        ApiAdapter.callAPIJetPack().listPostJetPack(Constant.TAG.BERITADPR, 1, 3, callbackBeritaDPR);
    }

    protected int getLayoutResource() {
        return R.layout.fragment_home_news;
    }

    @OnClick({R.id.btn_kabardpr, R.id.btn_kabardprall})
    public void onClickKabarDPR() {
        Helper.replaceFragment(getActivity(), MainDocumentFragment.newInstance(0), R.id.container,
                Helper.getFragmentManager(getActivity()));
    }

    @OnClick({R.id.btn_news, R.id.btn_newsall})
    public void onClickNews() {
        Helper.replaceFragment(getActivity(), MainDocumentFragment.newInstance(1), R.id.container,
                Helper.getFragmentManager(getActivity()));
    }

    @OnClick({R.id.btn_infografis, R.id.btn_infografisall})
    public void onClick() {
        Helper.replaceFragment(getActivity(), MainDocumentFragment.newInstance(2), R.id.container,
                Helper.getFragmentManager(getActivity()));
    }

    private Callback<JetPackPostsPage> callbackBeritaDPR = new Callback<JetPackPostsPage>() {
        @Override
        public void success(JetPackPostsPage jetPackPosts, Response response) {
            try {

                LayoutInflater inflater = LayoutInflater.from(getActivity());
                for (JetPackPostPage jetPackPost : jetPackPosts.getPosts()) {
                    PagesDao data = new PagesDao();
                    data.setId(jetPackPost.getID());
                    data.setTitle(jetPackPost.getTitle());
                    data.setTitle_plain(jetPackPost.getTitle());
                    data.setDate(Helper.dateParse(jetPackPost.getDate(), "yyyy-MM-dd'T'hh:mm:ssZ", "yyyy-MM-dd hh:mm:ss"));
                    data.setSlug(jetPackPost.getSlug());
                    data.setType(jetPackPost.getType());
                    data.setUrl(jetPackPost.getURL());
                    data.setStatus(jetPackPost.getStatus());
                    data.setExcerpt(jetPackPost.getExcerpt());
                    data.setContent(jetPackPost.getContent());
                    data.setModified(jetPackPost.getModified());

                    View v = inflater.inflate(R.layout.item_news, layoutNews, false);
                    TextView tv_title_news = (TextView) v.findViewById(R.id.tv_title_news);
                    TextView tv_date_news = (TextView) v.findViewById(R.id.tv_date_news);
                    ImageView img_item_news = (ImageView) v.findViewById(R.id.img_item_news);

                    tv_title_news.setText(Html.fromHtml(data.getTitle()));
                    String date2 = Html.fromHtml(data.getDate()).toString();
                    try {
                        tv_date_news.setText(Helper.dateParse(date2, "yyyy-MM-dd hh:mm:ss", "EEEE, dd MMMM yyyy"));
                    } catch (ParseException e) {
                    }

                    img_item_news.setImageDrawable(new IconDrawable(getActivity(), Iconify.IconValue.fa_image)
                            .colorRes(R.color.grey500).actionBarSize());
                    img_item_news.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    final PagesDao dataFinal = data;
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Helper.replaceFragment(getActivity(), DetailNewsFragment.newInstance(dataFinal), R.id.container, Helper.getFragmentManager(getActivity()));
                        }
                    });
                    layoutDPR.addView(v);
                }
                progressViewDPR.stopAndGone();
            } catch (Exception e) {
                progressViewDPR.stopAndError(e.getMessage(), false);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            try {
                progressViewDPR.stopAndError(getActivity().getResources().getString(R.string.errorNetwork), true);
                progressViewDPR.setRetryClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressViewDPR.startProgress();
                        ApiAdapter.callAPI().listPostJetPack(Constant.TAG.BERITADPR, 1, 3, callbackBeritaDPR);
                    }
                });
            } catch (Exception e) {
            }
        }
    };
    Callback<NewsDao> callbackNews = new Callback<NewsDao>() {
        @Override
        public void success(NewsDao newsDao, Response response) {
            try {

                LayoutInflater inflater = LayoutInflater.from(getActivity());
                for (int i = 0; i < 3; i++) {
                    final PagesDao data = newsDao.getPosts().get(i);
                    View v = inflater.inflate(R.layout.item_news, layoutNews, false);
                    TextView tv_title_news = (TextView) v.findViewById(R.id.tv_title_news);
                    TextView tv_date_news = (TextView) v.findViewById(R.id.tv_date_news);
                    ImageView img_item_news = (ImageView) v.findViewById(R.id.img_item_news);

                    tv_title_news.setText(Html.fromHtml(data.getTitle()));
                    String date = Html.fromHtml(data.getDate()).toString();
                    try {
                        tv_date_news.setText(Helper.dateParse(date, "yyyy-MM-dd hh:mm:ss", "EEEE, dd MMMM yyyy"));
                    } catch (ParseException e) {

                    }

                    img_item_news.setImageDrawable(new IconDrawable(getActivity(), Iconify.IconValue.fa_image)
                            .colorRes(R.color.grey500).actionBarSize());
                    img_item_news.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Helper.replaceFragment(getActivity(), DetailNewsFragment.newInstance(data), R.id.container, Helper.getFragmentManager(getActivity()));
                        }
                    });
                    layoutNews.addView(v);
                }

                progressViewNews.stopAndGone();
            } catch (Exception e) {
            }

//            View item_more = inflater.inflate(R.layout.item_news, layoutNews, false);
//            TextView tv_title_news = ButterKnife.findById(item_more, R.id.tv_title_news);
//            TextView tv_date_news = ButterKnife.findById(item_more, R.id.tv_date_news);
//            ImageView img_item_news = ButterKnife.findById(item_more, R.id.img_item_news);
//            tv_date_news.setVisibility(View.GONE);
//            img_item_news.setVisibility(View.GONE);
//            tv_title_news.setText(getResources().getText(R.string.load_more));
//            item_more.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Helper.replaceFragment(getActivity(),MainDocumentFragment.newInstance(0) ,R.id.container,Helper.getFragmentManager(getActivity()));
//                }
//            });
//            layoutNews.addView(item_more);


        }

        @Override
        public void failure(RetrofitError error) {
            try {
                progressViewNews.stopAndError(getActivity().getResources().getString(R.string.errorNetwork), true);
                progressViewNews.setRetryClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressViewNews.startProgress();
                        ApiAdapter.callAPI().listBerita(Constant.TAG.TYPE_JSON, Constant.KEY.PEMILUAPI, 1, callbackNews);
                    }
                });
            } catch (Exception e) {
            }

        }
    };


    Callback<BaseDao<DataSelasar>> callbackInfografis = new Callback<BaseDao<DataSelasar>>() {
        @Override
        public void success(BaseDao<DataSelasar> dataSelasarBaseDao, Response response) {
            try {

                progressViewInfografis.stopAndGone();

                final Infografis selasar = dataSelasarBaseDao.getData().getResults().getInfografisList().get(0);

                LayoutInflater inflater = LayoutInflater.from(getActivity());

                View view = inflater.inflate(R.layout.item_infografis, null);
                TextView tv_title = ButterKnife.findById(view, R.id.tv_title);

                int width = getResources().getDisplayMetrics().widthPixels;
                ImageView imageView = ButterKnife.findById(view, R.id.img_selasar);
                imageView.setLayoutParams(new RelativeLayout.LayoutParams(width, width / 2));

                Picasso.with(getActivity())
                        .load(selasar.getUrl_infografis().replace(" ", "%20"))
                        .placeholder(R.drawable.gradient_header_background)
                        .error(R.drawable.gradien_black).transform(new CropTopTransformation(width))
                        .into(imageView);
                tv_title.setText(selasar.getJudul());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Helper.replaceFragment(getActivity(), ViewImageFragment.newInstance(selasar.getJudul(),
                                selasar.getUrl_infografis()), R.id.container, Helper.getFragmentManager(getActivity()));
                    }
                });
                layoutInfografis.addView(view);

            } catch (Exception e) {
            }
        }

        @Override
        public void failure(RetrofitError error) {
            try {

                progressViewInfografis.stopAndError(getActivity().getResources().getString(R.string.errorNetwork), true);
                progressViewInfografis.setRetryClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressViewInfografis.startProgress();
                        ApiAdapter.callAPI().listSelasar(Constant.KEY.PEMILUAPI,0, callbackInfografis);
                    }
                });

            } catch (Exception e) {
            }
        }
    };


}