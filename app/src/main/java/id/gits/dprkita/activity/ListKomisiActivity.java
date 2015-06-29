package id.gits.dprkita.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import id.gits.dprkita.R;
import id.gits.dprkita.adapter.AdapterListKomisi;
import id.gits.dprkita.dao.komisi.Komisi;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.SharePreferences;
import id.gits.dprkita.utils.view.DividerItemDecoration;


public class ListKomisiActivity extends ActionBarActivity implements ObservableScrollViewCallbacks {
    public static final int REQ_CODE = 900;
    public Toolbar mToolbar;
    public ActionBar ab;
    private ObservableRecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Komisi> dataKomisi = new ArrayList<>();

    public static void startActivity(Activity fragment) {
        fragment.startActivityForResult(new Intent(fragment, ListKomisiActivity.class), REQ_CODE);
    }

    public static void startActivity(Activity fragment, int req_code) {
        fragment.startActivityForResult(new Intent(fragment, ListKomisiActivity.class), req_code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dapil);
        parsing();

        mRecyclerView = (ObservableRecyclerView) findViewById(R.id.re_dapil);
        mRecyclerView.setScrollViewCallbacks(this);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new AdapterListKomisi(this, dataKomisi);
        mRecyclerView.setAdapter(mAdapter);

        addToAdapter();
    }

    public void parsing() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addToAdapter() {
        String komisi = SharePreferences.getString(ListKomisiActivity.this, Constant.TAG.WAS_SAVE_KOMISI);
        Type listType = new TypeToken<List<Komisi>>() {
        }.getType();
        List<Komisi> k = new Gson().fromJson(komisi, listType);
        dataKomisi.clear();
        dataKomisi.addAll(k);
        mAdapter.notifyDataSetChanged();
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
}
