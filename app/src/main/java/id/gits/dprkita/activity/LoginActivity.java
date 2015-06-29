package id.gits.dprkita.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import id.gits.dprkita.R;
import id.gits.dprkita.api.ApiAdapter;
import id.gits.dprkita.dao.BaseDao;
import id.gits.dprkita.dao.anggota.DataAnggota;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.SharePreferences;
import id.gits.wplib.apis.CallUserMetaApi;
import id.gits.wplib.apis.ForgotPasswordUrlApi;
import id.gits.wplib.apis.LoginApi;
import id.gits.wplib.apis.WpCallback;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by yatnosudar on 2/14/15.
 */
public class LoginActivity extends ActionBarActivity {

    private static final String FROM_SPLASH = "from_splash";
    @InjectView(R.id.username)
    EditText username;
    @InjectView(R.id.password)
    EditText password;
    @InjectView(R.id.login_form)
    ScrollView login_form;
    @InjectView(R.id.login_progress)
    ProgressBar progressBar;
    @InjectView(R.id.tv_message)
    TextView tv_message;
    @InjectView(R.id.tv_lanjut)
    TextView tv_lanjut;
    MixpanelAPI mixpanelAPI;
    private int splash = 0;
    private WpCallback<String> forgotPasswordUrlListener = new WpCallback<String>() {
        @Override
        public void success(String s, String json) {
            //buka webview
            json = json.replace("\"", "");
            json = json.replace("\\u003d", "=");
            json = json.replace("\\u0026", "&");
            WebActivity.startThisActivity(LoginActivity.this, "Lupa Password", json, true);
            showProgress(false);

            HashMap<String, String> props = new HashMap<>();
            props.put("username", username.getText().toString());
            props.put("status", Constant.TAG.SUCCESS_STRING);
            Helper.addActionMixpanel(LoginActivity.this, getResources().getString(R.string.mix_forgot), props);
        }

        @Override
        public void failure(String error) {
            showProgress(false);
        }
    };
    private WpCallback<LoginApi.ApiDao> loginListener = new WpCallback<LoginApi.ApiDao>() {
        @Override
        public void success(LoginApi.ApiDao apiDao, String json) {

            SharePreferences.saveString(LoginActivity.this, Constant.TAG.USERNAME, username.getText().toString());
            SharePreferences.saveString(LoginActivity.this, Constant.TAG.PASSWORD, password.getText().toString());
            SharePreferences.saveString(LoginActivity.this, Constant.TAG.LOGIN_SESSION, json);

            saveDAPIL(apiDao.getUser_id());


            HashMap<String, String> props = new HashMap<>();
            props.put("username", username.getText().toString());
            props.put("status", Constant.TAG.SUCCESS_STRING);
            Helper.addActionMixpanel(LoginActivity.this, getResources().getString(R.string.mix_login), props);

            MixpanelAPI mixpanelAPI = Helper.mixpanel(LoginActivity.this);
            mixpanelAPI.identify(apiDao.getUser_id());
            mixpanelAPI.getPeople().identify(apiDao.getUser_id());
            mixpanelAPI.getPeople().set("$name", username.getText().toString());
            mixpanelAPI.getPeople().set("$email", apiDao.getEmail1());
            mixpanelAPI.getPeople().set("role", apiDao.getRoles().get(0).toString());
            try {
                JSONObject jsonObject = new JSONObject(json);
                mixpanelAPI.getPeople().set(jsonObject);
                mixpanelAPI.registerSuperProperties(jsonObject);
            } catch (JSONException e) {
            }
            SharePreferences.saveInt(LoginActivity.this, "OPEN_FIRST_TIME", 1);
            mixpanelAPI.getPeople().initPushHandling(Constant.KEY.GOOGLE_SENDER_ID);


        }


        @Override
        public void failure(String error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProgress(false);
                    tv_message.setVisibility(View.VISIBLE);
                    login_form.setVisibility(View.VISIBLE);
                }
            });

            HashMap<String, String> props = new HashMap<>();
            props.put("username", username.getText().toString());
            props.put("status", Constant.TAG.FAILURE_STRING);
            Helper.addActionMixpanel(LoginActivity.this, getResources().getString(R.string.mix_login), props);
        }
    };

    public static void startActivity(Context ctx, int from) {
        Intent i = new Intent(ctx, LoginActivity.class);
        i.putExtra(FROM_SPLASH, from);
        ctx.startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Helper.mixpanel(LoginActivity.this).getPeople().getSurveyIfAvailable();
        Helper.mixpanel(LoginActivity.this).getPeople().showNotificationIfAvailable(LoginActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        Helper.addActionMixpanel(this, getResources().getString(R.string.mix_login_screen), null);
        splash = getIntent().getIntExtra(FROM_SPLASH, 0);

        tv_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreferences.saveInt(LoginActivity.this, Constant.TAG.WAS_SKIP, Constant.TAG.SKIP);
                if (splash == 1) {
                    MainActivity.startActivity(LoginActivity.this);
                    finish();
                } else {
                    finish();
                }
            }
        });
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                    buttonLogin();
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.btn_forgotpassword)
    public void buttonForgotPassword() {
        showProgress(true);
        ForgotPasswordUrlApi.newInstance().callApi(forgotPasswordUrlListener);
        Helper.addActionMixpanel(this, getResources().getString(R.string.mix_forgot_screen), null);
    }

    @OnClick(R.id.btn_register)
    public void buttonRegister() {
        RegisterActivity.startActivity(this);
        Helper.addActionMixpanel(this, getResources().getString(R.string.mix_register_screen), null);
    }

    @OnClick(R.id.btn_login)
    public void buttonLogin() {
        if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
//            String regex = "^([\\w\\-\\.]+)@((\\[([0-9]{1,3}\\.){3}[0-9]{1,3}\\])|(([\\w\\-]+\\.)+)([a-zA-Z]{2,4}))$";
//            Pattern p = Pattern.compile(regex);
//            Matcher m = p.matcher(username.getText());
//            if (m.find()) {
            showProgress(true);
            LoginApi.newInstance().callApi(username.getText().toString(), password.getText().toString(), loginListener);
            HashMap<String, String> props = new HashMap<>();
            props.put("username", username.getText().toString());
            Helper.addActionMixpanel(this, getResources().getString(R.string.mix_login), props);
//            } else {
//                showError("Format email salah");
//            }
        } else {
            showError("Username dan kata sandi harus di isi");
        }
    }

    private void showError(String msg) {
        tv_message.setVisibility(View.VISIBLE);
        tv_message.setText(msg);
    }

    public void saveDAPIL(final String id_user) {
        CallUserMetaApi.newInstance().callApi(Integer.parseInt(id_user), username.getText().toString(), password.getText().toString(), "no_hp", new WpCallback<String>() {
            @Override
            public void success(String s, String json) {
                Log.d("no_hp", s);
                SharePreferences.saveString(LoginActivity.this, Constant.TAG.NO_HP, s);
                CallUserMetaApi.newInstance().callApi(Integer.parseInt(id_user), username.getText().toString(), password.getText().toString(), "id_pemiluapi", new WpCallback<String>() {
                    @Override
                    public void success(String s, String json) {
                        SharePreferences.saveString(LoginActivity.this, Constant.TAG.ID_PEMILUAPI, s);
                        if (s.isEmpty()) {
                            if (splash == 1) {
                                MainActivity.startActivity(LoginActivity.this);
                                finish();
                            } else
                                finish();
                        } else {
                            saveDetail(s);
                        }

                        CallUserMetaApi.newInstance().callApi(Integer.parseInt(id_user), username.getText().toString(), password.getText().toString(), "id_dapil", new WpCallback<String>() {
                            @Override
                            public void success(String s, String json) {
                                Log.d("no_hp", s);
                                SharePreferences.saveString(LoginActivity.this, Constant.TAG.ID_DAPIL, s);
                            }

                            @Override
                            public void failure(String error) {
                                Log.d("error", error);
                            }
                        });
                    }

                    @Override
                    public void failure(String error) {
                        Log.d("error", error);
                    }
                });

            }

            @Override
            public void failure(String error) {
                Log.d("error", error);
            }
        });
    }

    public void saveDetail(String id_anggota) {
        ApiAdapter.callAPI().detailAnggota(id_anggota, Constant.KEY.PEMILUAPI, new Callback<BaseDao<DataAnggota>>() {
            @Override
            public void success(BaseDao<DataAnggota> dataAnggotaBaseDao, Response response) {
                showProgress(false);
                String json = new Gson().toJson(dataAnggotaBaseDao.getData().getResults().getAnggota().get(0));
                SharePreferences.saveString(LoginActivity.this, Constant.TAG.DETAILANGGOTA, json);
                if (splash == 1) {
                    MainActivity.startActivity(LoginActivity.this);
                    finish();
                } else
                    finish();
            }

            @Override
            public void failure(RetrofitError error) {
                showProgress(false);
                Log.d("Login", error.getMessage());
                if (splash == 1) {
                    MainActivity.startActivity(LoginActivity.this);
                    finish();
                } else
                    finish();
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (show) {
                    login_form.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    login_form.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 99 && resultCode == RESULT_OK) {
            if (splash == 1) {
                MainActivity.startActivity(LoginActivity.this);
                finish();
            } else
                finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Helper.mixpanel(LoginActivity.this).flush();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}