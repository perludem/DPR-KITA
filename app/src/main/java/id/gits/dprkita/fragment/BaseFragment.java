package id.gits.dprkita.fragment;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.google.gson.Gson;

import id.gits.dprkita.R;
import id.gits.dprkita.activity.LoginActivity;
import id.gits.dprkita.activity.UpdateProfileActivity;
import id.gits.dprkita.dao.anggota.Anggota;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.SharePreferences;
import id.gits.wplib.apis.LoginApi;

/**
 * Created by yatnosudar on 1/30/15.
 */
public abstract class BaseFragment extends Fragment {
    public View view;
    public Toolbar mToolbar;
    public ActionBar ab;
    private LoginApi.ApiDao session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (mToolbar != null) {
            ((ActionBarActivity) getActivity()).setSupportActionBar(mToolbar);
        }
        ab = Helper.getActionBarFragment(getActivity());
        getFragmentManager().addOnBackStackChangedListener(getListener());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this instanceof MainDashboardFragment) {
            ab.setDisplayUseLogoEnabled(true);
            ab.setLogo(R.drawable.ic_launcher);
            ab.setDisplayHomeAsUpEnabled(false);
            ((ActionBarActivity) getActivity()).getSupportActionBar().invalidateOptionsMenu();
        } else {
            ab.setDisplayUseLogoEnabled(false);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    //    public void parsing() {
//        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        if (mToolbar != null) {
//            ((ActionBarActivity) getActivity()).setSupportActionBar(mToolbar);
//        }
//        ab = Helper.getActionBarFragment(getActivity());
//        getFragmentManager().addOnBackStackChangedListener(getListener());
//    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getFragmentManager();
                if (manager != null) {
                    int backStackEntryCount = manager.getBackStackEntryCount();
//                    if (backStackEntryCount == 0) {
//                        getActivity().finish();
//                        return;
//                    }
                    try {
                        Fragment fragment = manager.getFragments()
                                .get(backStackEntryCount);
                        fragment.onResume();
                    } catch (Exception e) {
                    }

                }
            }
        };

        return result;
    }

    public void withoutToolbar() {
        ab = Helper.getActionBarFragment(getActivity());
    }

    protected abstract int getLayoutResource();

    public void addBackButton() {
        ab.setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

    public void enableScroll(ObservableScrollView scrollView) {
        scrollView.setOnTouchListener(null);
    }

    public void disableScroll(ObservableScrollView scrollView) {
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (this instanceof MainDashboardFragment) {
        } else {
            menu.findItem(R.id.action_profile).setVisible(false);
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_login).setVisible(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (getActivity()!=null) {
            if (this instanceof MainDashboardFragment) {
                inflater.inflate(R.menu.menu_main_home_dpr, menu);
                session = SharePreferences.sessionLogin(getActivity());
                if (session == null) {
                    menu.findItem(R.id.action_profile).setVisible(false);
                } else {
                    if (Helper.isAnggotaDPR(session.getRoles().get(0))) {
                        menu.findItem(R.id.action_profile).setVisible(true);
                    } else
                        menu.findItem(R.id.action_profile).setVisible(true);
                }
//            menu.findItem(R.id.action_search).setIcon(new IconDrawable(getActivity(),
//                    Iconify.IconValue.fa_search).colorRes(android.R.color.white).actionBarSize());
                // Get the SearchView and set the searchable configuration
                SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
                SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
                // Assumes current activity is the searchable activity
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                searchView.setIconifiedByDefault(true); // Do iconify the widget; expand it by default

                if (SharePreferences.sessionLogin(getActivity()) == null) {
                    menu.findItem(R.id.action_logout).setVisible(false);
                } else {
                    menu.findItem(R.id.action_login).setVisible(false);
                }
            } else {
                menu.findItem(R.id.action_profile).setVisible(false);
                menu.findItem(R.id.action_search).setVisible(false);
                menu.findItem(R.id.action_logout).setVisible(false);
                menu.findItem(R.id.action_login).setVisible(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                LoginActivity.startActivity(getActivity(), 1);
                return true;
            case R.id.action_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getActivity().getString(R.string.alert_logout));
                builder.setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharePreferences.sessionLogout(getActivity());
                        LoginActivity.startActivity(getActivity(), 1);
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("Batal",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            case R.id.action_profile:
                    if (Helper.isAnggotaDPR(session.getRoles().get(0))) {
                        Anggota anggota = new Gson().fromJson(SharePreferences.getString(getActivity(), Constant.TAG.DETAILANGGOTA), Anggota.class);
                        Helper.replaceFragment(getActivity(), ProfileFragment.newInstance(anggota.getId(), anggota.getNama()), R.id.container, getFragmentManager());
                    } else{
                        UpdateProfileActivity.startActivity(getActivity());
                    }
        }
        return super.onOptionsItemSelected(item);

    }
}
