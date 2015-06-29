package id.gits.dprkita.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import id.gits.dprkita.R;

/**
 * Created by yatnosudar on 1/28/15.
 */
public abstract class BaseActivity extends ActionBarActivity {

    public Toolbar toolbar;
//    private static TextView mTitle;
//    private static ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                        finish();
                    } else
                        getSupportFragmentManager().popBackStack();
                }
            });
        }
        //        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        icon = (ImageView) findViewById(R.id.icon);
//        mTitle = (TextView) findViewById(R.id.title);
//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//        }
//        toolbar.setTitle("Pantau DPR");
    }

    protected abstract int getLayoutResource();

}
