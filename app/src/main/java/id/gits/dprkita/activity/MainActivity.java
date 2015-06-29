package id.gits.dprkita.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import id.gits.dprkita.R;
import id.gits.dprkita.dao.komisi.Komisi;
import id.gits.dprkita.db.DapilDB;
import id.gits.dprkita.fragment.KomisiFragment;
import id.gits.dprkita.fragment.MainDashboardFragment;
import id.gits.dprkita.fragment.MainPartaiListFragment;
import id.gits.dprkita.fragment.PartaiListFragment;
import id.gits.dprkita.fragment.ProfileFragment;
import id.gits.dprkita.fragment.SearchResultFragment;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.ProgressListener;

/**
 * Created by yatnosudar on 2/15/15.
 */
public class MainActivity extends BaseActivity implements PartaiListFragment.FragmentListener, ProfileFragment.FragmentListener, ProgressListener {

    private final String MAINDASHBOARD = "maindashboard";

    public static void startActivity(Context activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MainDashboardFragment.newInstance(), MAINDASHBOARD)
                    .commit();
        }
        Helper.mixpanel(MainActivity.this).getPeople().showNotificationIfAvailable(MainActivity.this);
        handleIntent(getIntent());

    }

    @Override
    protected void onResume() {
        super.onResume();
        Helper.mixpanel(MainActivity.this).getPeople().getSurveyIfAvailable();
        Helper.mixpanel(MainActivity.this).getPeople().showNotificationIfAvailable(MainActivity.this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Helper.replaceFragment(this, SearchResultFragment.newInstance(query), R.id.container, getSupportFragmentManager());
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void setColor(String color, String color2) {
        String backStateName = MainPartaiListFragment.class.getName();
        String fragmentTag = backStateName;
        MainPartaiListFragment f = (MainPartaiListFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
        f.setColorBackgroundTab(color, color2);
        toolbar.setBackgroundColor(Color.parseColor(color));
    }

    @Override
    public void changeColorToolbar(String color) {
        toolbar.setBackgroundColor(Color.parseColor(color));
    }

    @Override
    public void resetColorToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.red));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Helper.mixpanel(MainActivity.this).flush();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("filter", requestCode + "" + resultCode);
        if (requestCode == 9) {
            if (resultCode == RESULT_OK) {
                MainDashboardFragment dashboardFragment = (MainDashboardFragment) getSupportFragmentManager().findFragmentByTag(MAINDASHBOARD);
                Komisi komisi = (Komisi) data.getSerializableExtra("komisi");
                String[] changeName = komisi.getNama().split(" ");
                String numberKomisi = changeName[0];
                if (dashboardFragment != null && komisi != null && numberKomisi != null) {
                    dashboardFragment.sendKomisiFromReportFragment(numberKomisi);
                } else {
                    Log.e("status e", "komisi null & fragment null");
                }
            }
        }
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                MainDashboardFragment dashboardFragment = (MainDashboardFragment) getSupportFragmentManager().findFragmentByTag(MAINDASHBOARD);
                DapilDB dapil = (DapilDB) data.getSerializableExtra(Constant.TAG.DATADAPIL);
                if (dashboardFragment != null && dapil != null) {
                    dashboardFragment.sendDapilFromList(dapil);
                } else {
                    Log.e("status e", "komisi null & fragment null");
                }
            }
        }
        if (requestCode == 900) {
            if (resultCode == RESULT_OK) {
                String backStateName = KomisiFragment.class.getName();
                KomisiFragment fragment = (KomisiFragment) getSupportFragmentManager().findFragmentByTag(backStateName);
                if (fragment != null)
                fragment.callApi();
            }
        }
        if (requestCode == 300) {
            if (resultCode == RESULT_OK) {
                MainDashboardFragment dashboardFragment = (MainDashboardFragment) getSupportFragmentManager().findFragmentByTag(MAINDASHBOARD);
                if (dashboardFragment != null) {
                    dashboardFragment.sendPostFromPost(1);
                }
            }
        }
        if (requestCode == 120) {
            if (resultCode == RESULT_OK) {
                int total = data.getIntExtra("isComment", 0);
                MainDashboardFragment dashboardFragment = (MainDashboardFragment) getSupportFragmentManager().findFragmentByTag(MAINDASHBOARD);
                if (dashboardFragment != null) {
                    dashboardFragment.sendPostFromPost(total);
                }
            }
        }
        if (requestCode == 500) {
            if (resultCode == RESULT_OK) {
                MainDashboardFragment dashboardFragment = (MainDashboardFragment) getSupportFragmentManager().findFragmentByTag(MAINDASHBOARD);
                if (dashboardFragment != null) {
                    dashboardFragment.sendPostFromPost(1);
                }
            }
        }
        if (requestCode == 400) {
            if (resultCode == RESULT_OK) {
                ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag(ProfileFragment.class.getName());
                if (profileFragment != null) {
                    profileFragment.sendPostFromPost();
                }
            }
        }

    }


    @Override
    public void onStopProgress() {
        MainDashboardFragment dashboardFragment = (MainDashboardFragment) getSupportFragmentManager().findFragmentByTag(MAINDASHBOARD);
        if (dashboardFragment != null)
            dashboardFragment.stopProgressBar();
    }

    @Override
    public void onStartProgress() {
        MainDashboardFragment dashboardFragment = (MainDashboardFragment) getSupportFragmentManager().findFragmentByTag(MAINDASHBOARD);
        if (dashboardFragment != null)
            dashboardFragment.startProgressBar();
    }
}
