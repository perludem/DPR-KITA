package id.gits.dprkita.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import id.gits.dprkita.R;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.SharePreferences;
import id.gits.wplib.apis.LoginApi;
import id.gits.wplib.apis.RegisterApi;
import id.gits.wplib.apis.UpdateUserApi;
import id.gits.wplib.apis.UpdateUserMetaApi;
import id.gits.wplib.apis.WpCallback;
import id.gits.wplib.utils.WPKey;

/**
 * Created by yatnosudar on 2/14/15.
 */
public class RegisterActivity extends ActionBarActivity {
    @InjectView(R.id.fullname)
    EditText fullname;
    @InjectView(R.id.username)
    EditText username;
    @InjectView(R.id.password)
    EditText password;
    @InjectView(R.id.inp_hp)
    EditText inpHp;
    @InjectView(R.id.email)
    EditText inpEmail;
    @InjectView(R.id.login_form)
    ScrollView login_form;
    @InjectView(R.id.login_progress)
    ProgressBar progressBar;
    @InjectView(R.id.tv_message)
    TextView tv_message;
    @InjectView(R.id.btn_visiblepass)
    TextView btnVisiblePass;
    //android:id="@+id/email_login_form"
    @InjectView(R.id.checkBox)
    CheckBox checkBox;
    boolean isPassVisible = false;
    WpCallback<Boolean> updateProfileListener = new WpCallback<Boolean>() {
        @Override
        public void success(Boolean aBoolean, String json) {
            Log.d("REGISTRASI", aBoolean + " SUCCESS");
        }

        @Override
        public void failure(String error) {
            Log.d("REGISTRASI", error);
        }
    };
    private WpCallback<LoginApi.ApiDao> loginListener = new WpCallback<LoginApi.ApiDao>() {
        @Override
        public void success(LoginApi.ApiDao apiDao, String json) {
            showProgress(false);
            SharePreferences.saveString(RegisterActivity.this, Constant.TAG.USERNAME, username.getText().toString());
            SharePreferences.saveString(RegisterActivity.this, Constant.TAG.PASSWORD, password.getText().toString());
            SharePreferences.saveString(RegisterActivity.this, Constant.TAG.LOGIN_SESSION, json);
            setResult(RESULT_OK);
            finish();

            MixpanelAPI mixpanelAPI = Helper.mixpanel(RegisterActivity.this);
            mixpanelAPI.getPeople().identify(apiDao.getUser_id());
            mixpanelAPI.getPeople().set("$name", username.getText().toString());
            mixpanelAPI.getPeople().set("$email", apiDao.getEmail1());
            mixpanelAPI.getPeople().set("no_hp", inpHp.getText().toString());
            mixpanelAPI.getPeople().set("role", apiDao.getRoles().get(0).toString());
            try {
                JSONObject jsonObject = new JSONObject(json);
                mixpanelAPI.getPeople().set(jsonObject);
                mixpanelAPI.registerSuperProperties(jsonObject);
            } catch (JSONException e) {
            }
            SharePreferences.saveInt(RegisterActivity.this, "OPEN_FIRST_TIME", 1);
            showProgress(false);
        }

        @Override
        public void failure(String error) {
            showProgress(false);
            showError(error);
        }
    };
    private WpCallback<Boolean> updateUserMetaCallback = new WpCallback<Boolean>() {
        @Override
        public void success(Boolean res, String json) {
            LoginApi.newInstance().callApi(inpEmail.getText().toString(), password.getText().toString(), loginListener);
        }

        @Override
        public void failure(String error) {
            showProgress(false);
            showError(error);
        }
    };
    private WpCallback<Integer> registerListener = new WpCallback<Integer>() {
        @Override
        public void success(Integer res, String json) {
            if (res != null && res > 0) {
                UpdateUserMetaApi.newInstance().callApi(res, "no_hp", inpHp.getText().toString(), updateUserMetaCallback);
                HashMap<String, String> content = new HashMap<>();
                content.put(WPKey.KEY.NICKNAME, fullname.getText().toString());
                UpdateUserApi.newInstance().callApi(inpEmail.getText().toString(), password.getText().toString(), content, updateProfileListener);
            } else {
                showProgress(false);
                showError("Username atau email sudah terdaftar");
            }
        }

        @Override
        public void failure(String error) {
            showProgress(false);
            showError(error);
        }
    };

    public static void startActivity(Context ctx) {
        Intent i = new Intent(ctx, RegisterActivity.class);
        ((Activity) ctx).startActivityForResult(i, 99);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        inpHp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

    @OnClick(R.id.btn_register)
    public void buttonRegister() {
        if (checkBox.isChecked()) {
            String regex = "^([\\w\\-\\.]+)@((\\[([0-9]{1,3}\\.){3}[0-9]{1,3}\\])|(([\\w\\-]+\\.)+)([a-zA-Z]{2,4}))$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(inpEmail.getText());

            if (!inpEmail.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !fullname.getText().toString().isEmpty()) {
                if (m.find()) {
                    showProgress(true);
                    RegisterApi.newInstance().callApi(inpEmail.getText().toString(), password.getText().toString(), inpEmail.getText().toString(), registerListener);
                } else {
                    showError("Format email salah");
                }
            } else {
                showError("Nama, kata sandi, dan email harus diisi");
            }
        }
    }

    @OnClick(R.id.checkBox)
    public void check() {
        checkBox.isChecked();
    }

    @OnClick(R.id.btn_login)
    public void buttonLogin() {

        finish();
    }

    @OnClick(R.id.btn_visiblepass)
    public void buttonVisiblePassword() {
        if (isPassVisible) {
            password.setTransformationMethod(new PasswordTransformationMethod());
            isPassVisible = false;
            btnVisiblePass.setText("{fa_eye}");
        } else {
            password.setTransformationMethod(null);
            isPassVisible = true;
            btnVisiblePass.setText("{fa_eye-slash}");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Helper.mixpanel(RegisterActivity.this).getPeople().getSurveyIfAvailable();
        Helper.mixpanel(RegisterActivity.this).getPeople().showNotificationIfAvailable(RegisterActivity.this);
    }

    @OnClick(R.id.tos)
    public void TOS() {
        TOSActivy.startActivity(RegisterActivity.this);
    }

    private void showError(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText(msg);
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
}