package id.gits.dprkita.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;
import id.gits.dprkita.api.ApiAdapter;
import id.gits.dprkita.dao.TOSDao;
import id.gits.dprkita.utils.view.MyProgressView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TOSActivy extends ActionBarActivity {

    @InjectView(R.id.tos)
    TextView textView;

    @InjectView(R.id.view_progress)
    MyProgressView view_progress;

    public static void startActivity(Context ctx) {
        Intent i = new Intent(ctx, TOSActivy.class);
        ((Activity) ctx).startActivityForResult(i, 99);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tosactivy);
        ButterKnife.inject(this);


        callApi();
    }

    public void callApi() {
        ApiAdapter.callAPIJetPack().detailPostJetPack("syarat-dan-ketentuan", new Callback<TOSDao>() {
            @Override
            public void success(TOSDao jetPackPosts, Response response) {
                view_progress.stopAndGone();
                textView.setText(Html.fromHtml(jetPackPosts.getContent()));
            }

            @Override
            public void failure(RetrofitError error) {
                view_progress.stopAndError(error.getMessage(), true);
                view_progress.setRetryClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view_progress.startProgress();
                        callApi();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tosactivy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
