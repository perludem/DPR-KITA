package id.gits.dprkita.activity;

import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import id.gits.dprkita.R;
import id.gits.dprkita.adapter.AdapterDapil;
import id.gits.dprkita.db.DapilDB;
import id.gits.dprkita.utils.view.DividerItemDecoration;


public class ListDapilActivity extends ActionBarActivity implements ObservableScrollViewCallbacks {

    public Toolbar mToolbar;
    public ActionBar ab;
    private ObservableRecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<DapilDB> dataDapil = new ArrayList<>();

    public static void startActivity(Context ctx) {
        ctx.startActivity(new Intent(ctx, ListDapilActivity.class));
    }

    public static void startActivitiResult(Activity ctx, int code) {
        Intent i = new Intent(ctx, ListDapilActivity.class);
        ctx.startActivityForResult(i, code);
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
        mAdapter = new AdapterDapil(this, dataDapil);
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
        List<DapilDB> dapil = DapilDB.listAll(DapilDB.class);
        dataDapil.clear();
        dataDapil.addAll(dapil);
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
