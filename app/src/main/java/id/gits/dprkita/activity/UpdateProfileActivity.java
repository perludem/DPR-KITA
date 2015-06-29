package id.gits.dprkita.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.SharePreferences;
import id.gits.dprkita.utils.view.CircleTransform;
import id.gits.wplib.apis.CallUserMetaApi;
import id.gits.wplib.apis.LoginApi;
import id.gits.wplib.apis.UpdateUserApi;
import id.gits.wplib.apis.UpdateUserMetaApi;
import id.gits.wplib.apis.WpCallback;
import id.gits.wplib.utils.WPKey;

/**
 * Created by yatnosudar on 4/21/15.
 */
public class UpdateProfileActivity extends BaseActivity {


    @InjectView(R.id.fullname)
    EditText fullname;
    @InjectView(R.id.email)
    EditText email;
    @InjectView(R.id.inp_hp)
    EditText inp_hp;

    @InjectView(R.id.id_image_profile)
    ImageView id_image_profile;

    @InjectView(R.id.btn_register)
    Button save;

    @InjectView(R.id.change_password)
    TextView change_password;

    String hexString;
    private String username,password;
    LoginApi.ApiDao sessionLogin;
    boolean status_save = false;

    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loading = new ProgressDialog(this);
        loading.setMessage("Proses menyimpan");
        sessionLogin = SharePreferences.sessionLogin(this);
        try {
            hexString = Helper.MD5(sessionLogin.getEmail());
        } catch (NoSuchAlgorithmException e) {
            hexString = null;
        }
        if (hexString!=null){
            Picasso.with(this).load(Constant.URL_API.BASE_IMAGE_GRAVATAR + hexString).transform(new CircleTransform()).resize(250, 250).centerInside().placeholder(getResources().getDrawable(R.drawable.ic_logo)).into(id_image_profile);
        }
        fullname.setText(sessionLogin.getNickname());
        email.setText(sessionLogin.getEmail());

        username = SharePreferences.getString(this, Constant.TAG.USERNAME);
        password = SharePreferences.getString(this, Constant.TAG.PASSWORD);
        CallUserMetaApi.newInstance().callApi(
                Integer.parseInt(sessionLogin.getUser_id()),
                username,password,Constant.TAG.NO_HP,new WpCallback<String>() {
            @Override
            public void success(final String s, String json) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inp_hp.setText(s.replace("\"","").toString());
                    }
                });
            }

            @Override
            public void failure(final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inp_hp.setText(error);
                    }
                });

            }
        });
        if (status_save==false){
            fullname.setEnabled(status_save);
            email.setEnabled(status_save);
            inp_hp.setEnabled(status_save);
            save.setText("PERBARUI PROFIL");
        }else{
            fullname.setEnabled(status_save);
            email.setEnabled(status_save);
            inp_hp.setEnabled(status_save);
            save.setText("SIMPAN");
        }

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePasswordActivity.startActivity(UpdateProfileActivity.this);
            }
        });
    }

    public static void startActivity(Activity ctx) {
        Intent iz = new Intent(ctx, UpdateProfileActivity.class);
        ctx.startActivity(iz);
    }
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_update_profile;
    }

    public void updateProfile(View view){
        if (status_save==true) {
            HashMap<String, String> data = new HashMap<>();
            data.put(WPKey.KEY.NICKNAME, fullname.getText().toString());
            loading.show();
            UpdateUserApi.newInstance().callApi(username, password, data, new WpCallback<Boolean>() {
                @Override
                public void success(Boolean aBoolean, String json) {
                    Log.d("success update profil", json);

                    UpdateUserMetaApi.newInstance().callApi(Integer.parseInt(sessionLogin.getUser_id()), Constant.TAG.NO_HP, inp_hp.getText().toString(), new WpCallback<Boolean>() {
                        @Override
                        public void success(Boolean aBoolean, final String json) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("success update phone", json);
                                    loading.dismiss();
                                    status_save = false;
                                    fullname.setEnabled(status_save);
                                    email.setEnabled(status_save);
                                    inp_hp.setEnabled(status_save);
                                    Toast.makeText(UpdateProfileActivity.this, "Sukses menyimpan", Toast.LENGTH_LONG).show();
                                    save.setText("PERBARUI PROFIL");

                                }
                            });

                        }

                        @Override
                        public void failure(final String error) {
                            Log.e("error update phone", error);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UpdateProfileActivity.this, error, Toast.LENGTH_LONG).show();
                                    loading.dismiss();
                                }
                            });

                        }
                    });
                }

                @Override
                public void failure(final String error) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("error update profil", error);
                            Toast.makeText(UpdateProfileActivity.this, error, Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    });
                }
            });

            LoginApi.newInstance().callApi(username, password, new WpCallback<LoginApi.ApiDao>() {
                @Override
                public void success(LoginApi.ApiDao apiDao, final String json) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharePreferences.saveString(UpdateProfileActivity.this, Constant.TAG.LOGIN_SESSION, json);
                        }
                    });

                }

                @Override
                public void failure(String error) {

                }
            });

        }else{
            status_save = true;
            fullname.setEnabled(status_save);
            email.setEnabled(status_save);
            inp_hp.setEnabled(status_save);
            save.setText("SIMPAN PROFIL");

        }
    }

}
