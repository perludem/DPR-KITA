package id.gits.dprkita.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.joanzapata.android.iconify.Iconify;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;
import id.gits.dprkita.dao.news.PagesDao;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.view.DynamicShareActionProvider;

public class DetailNewsFragment extends BaseFragment implements ObservableScrollViewCallbacks {

    private static final String NEWSDAO = "news";
    private static PagesDao newsdao;
    @InjectView(R.id.scroll)
    ObservableScrollView mScrollView;
    @InjectView(R.id.body)
    WebView body;
    private int mParallaxImageHeight;
    private String url = "http://dpr.go.id/";

    public DetailNewsFragment() {
        // Required empty public constructor
    }

    public static DetailNewsFragment newInstance(PagesDao dao) {
        DetailNewsFragment fragment = new DetailNewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        args.putSerializable(NEWSDAO, dao);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            newsdao = (PagesDao) getArguments().get(NEWSDAO);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.inject(this, view);

        ab = Helper.getActionBarFragment(getActivity());
        ab.setDisplayHomeAsUpEnabled(true);
        mScrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);
        body.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        body.setVerticalScrollBarEnabled(false);
        body.setHorizontalScrollBarEnabled(false);
        body.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        body.setScrollContainer(false);

        initData();


        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_share, menu);
        menu.findItem(R.id.action_share).setIcon(Helper.makeOwnIcon(getActivity(), R.color.white, Iconify.IconValue.fa_share_alt));
        final String share_to = "Saya telah membaca artikel " + newsdao.getTitle() + " di aplikasi " + getResources().getString(R.string.app_name) + url;

        DynamicShareActionProvider mShareActionProvider =
                (DynamicShareActionProvider)
                        MenuItemCompat.getActionProvider
                                (menu.findItem(R.id.action_share));

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareDataType("text/plain");
            mShareActionProvider.setOnShareIntentUpdateListener(new DynamicShareActionProvider.OnShareIntentUpdateListener() {
                @Override
                public Bundle onShareIntentExtrasUpdate() {
                    Bundle extras = new Bundle();
                    extras.putString(android.content.Intent.EXTRA_TEXT, share_to);
                    return extras;
                }
            });
        }
    }

    public void initData() {
        try {
            body.loadData(Helper.loadHtmlBody(newsdao.getTitle(), newsdao.getContent(), Helper.dateParse(newsdao.getDate(), "yyyy-MM-dd hh:mm:ss", "EEEE, dd MMMM yyyy")), "text/html", "UTF-8");
        } catch (ParseException e) {
            body.loadData(Helper.loadHtmlBody(newsdao.getTitle(), newsdao.getContent(),newsdao.getDate()), "text/html", "UTF-8");
        }

        Document g = Jsoup.parse(newsdao.getContent());
        Elements links = g.select("a[href]");

        for (Element link : links) {
            url = link.attr("abs:href");
        }

        HashMap<String, String> props = new HashMap<>();
        props.put("title", newsdao.getTitle());
        props.put("date", newsdao.getDate());
        Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_home_news), props);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_detail_news;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean b, boolean b2) {
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
