package id.gits.dprkita.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import id.gits.dprkita.R;
import id.gits.dprkita.activity.CalendarActivity;
import id.gits.dprkita.adapter.AdapterAgenda;
import id.gits.dprkita.api.ApiAdapter;
import id.gits.dprkita.dao.jetpackdao.JetPackCustomFields;
import id.gits.dprkita.dao.jetpackdao.JetPackPost;
import id.gits.dprkita.dao.jetpackdao.JetPackPosts;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.ProgressListener;
import id.gits.dprkita.utils.view.DividerItemDecoration;
import id.gits.dprkita.utils.view.MyProgressView;
import id.gits.wplib.dao.PostDao;
import id.gits.wplib.utils.WPKey;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends Fragment implements ObservableScrollViewCallbacks {
    public static final String DATE_AGENDA = "date_agenda";
    public static final int REQUEST_CODE = 200;
    public static String dateCalendar;
    public static String dateCalendarTemp;
    @InjectView(R.id.re_agenda)
    ObservableRecyclerView mRecyclerView;
    @InjectView(R.id.view_progress)
    MyProgressView progressView;
    @InjectView(R.id.lin_calendar)
    LinearLayout lin_calendar;
    @InjectView(R.id.tv_date)
    TextView tv_date;
    Activity activity;
    Locale id = new Locale("in", "ID");

    boolean isFirst = true;
    Callback<JetPackPosts> resultJetPackListener = new Callback<JetPackPosts>() {
        @Override
        public void success(JetPackPosts jetPackPosts, Response response) {
            try {

                dataAgenda.clear();
                if (jetPackPosts.getFound() > 0) {
                    progressView.stopAndGone();
                    for (JetPackPost jetPackPost : jetPackPosts.getPosts()) {
                        PostDao data = new PostDao();
                        data.setTitle(jetPackPost.getTitle());
                        data.setPost_author(jetPackPost.getAuthor().getName());
                        data.setPost_content(jetPackPost.getContent());
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
                        dataAgenda.add(data);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    progressView.stopAndError(getResources().getString(R.string.reses), false);
                }

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
                        progressView.startProgress();
                        callApi();
                    }
                });

            } catch (Exception e) {
            }
        }
    };
    private AdapterAgenda mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<PostDao> dataAgenda = new ArrayList<>();
    private ProgressListener listenerProgress;

    public AgendaFragment() {
        // Required empty public constructor
    }

    public static AgendaFragment newInstance() {
        AgendaFragment agendaFragment = new AgendaFragment();
        return agendaFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        listenerProgress = (ProgressListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);
        ButterKnife.inject(this, view);

        mRecyclerView.setScrollViewCallbacks(this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));
        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AdapterAgenda(activity, dataAgenda);
        mRecyclerView.setAdapter(mAdapter);

        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", id);
        dateCalendar = dateFormat.format(today);
        dateCalendarTemp = dateCalendar;
        callApi();
        progressView.stopAndError(getResources().getString(R.string.pilih_tanggal), false);
        //17 Februari 2015
        SimpleDateFormat dateFormatText = new SimpleDateFormat("dd MMMM yyyy", id);
        tv_date.setText(dateFormatText.format(today));
        Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_agenda), null);

        return view;
    }

    @OnClick(R.id.lin_calendar)
    public void gotoCalendar() {
        isFirst = false;
        Intent intent = new Intent(getActivity(), CalendarActivity.class);
        this.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst) {
            if (dateCalendarTemp != dateCalendar) {
                String dateParsing = dateCalendar;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", id);
                try {
                    Date d = dateFormat.parse(dateParsing);
                    SimpleDateFormat dateFormatText = new SimpleDateFormat("dd MMMM yyyy", id);
                    tv_date.setText(dateFormatText.format(d));
                    callApi();

                    HashMap<String, String> props = new HashMap<>();
                    props.put("tanggal_agenda", tv_date.getText().toString());
                    Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_agenda_tanggal), props);
                } catch (ParseException e) {
                }
                dateCalendarTemp = dateCalendar;
            }
        }
    }

    private void callApi() {
        progressView.startProgress();
        String dateParsing = dateCalendar;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", id);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(dateParsing));
        } catch (ParseException e) {
        }
        int day = calendar.getFirstDayOfWeek();
        if (day != 7 || day != 1) {
            ApiAdapter.callAPIJetPack().listPostJetPack(WPKey.CUSTOM_FIELD.AGENDA, WPKey.KEY.TGL_AGENDA, dateCalendar, "ASC", "ID", resultJetPackListener);
        } else {
            progressView.stopAndError(getResources().getString(R.string.weekend), true);
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //17 Februari 2015
            String dateParsing = data.getStringExtra(DATE_AGENDA);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", id);
            try {
                Date d = dateFormat.parse(dateParsing);
                SimpleDateFormat dateFormatText = new SimpleDateFormat("dd MMMM yyyy", id);
                tv_date.setText(dateFormatText.format(d));
                dateCalendar = data.getStringExtra(DATE_AGENDA);
                callApi();
            } catch (ParseException e) {
            }

        }
    }
}
