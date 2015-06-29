package id.gits.dprkita.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.IconTextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import id.gits.dprkita.R;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.SharePreferences;
import id.gits.wplib.apis.LoginApi;
import id.gits.wplib.apis.UpdatePasswordApi;
import id.gits.wplib.apis.WpCallback;

public class UpdatePasswordActivity extends BaseActivity {


    @InjectView(R.id.old_password)
    EditText old_password;
    @InjectView(R.id.new_password)
    EditText new_password;
    @InjectView(R.id.confirm_password)
    EditText confirm_password;

    @InjectView(R.id.btn_visibleoldpass)
    IconTextView btn_visibleoldpass;
    @InjectView(R.id.btn_visiblenewpass)
    IconTextView btn_visiblenewpass;
    @InjectView(R.id.btn_visibleconfirmpass)
    IconTextView btn_visibleconfirmpass;

    @InjectView(R.id.btn_update_password)
    Button btn_update_password;

    String username,password;
    LoginApi.ApiDao sessionLogin;

    ProgressDialog progressDialog;
    public boolean isPassVisible1 = false,isPassVisible2= false,isPassVisible3= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses menyimpan");
        username = SharePreferences.getString(this, Constant.TAG.USERNAME);
        password = SharePreferences.getString(this, Constant.TAG.PASSWORD);
        sessionLogin = SharePreferences.sessionLogin(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ganti Password");
        btn_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

    }

    @OnClick(R.id.btn_visiblenewpass)
    public void buttonVisibleNewPassword() {
        if (isPassVisible2) {
            new_password.setTransformationMethod(new PasswordTransformationMethod());
            isPassVisible2 = false;
            btn_visiblenewpass.setText("{fa_eye}");
        } else {
            new_password.setTransformationMethod(null);
            isPassVisible2 = true;
            btn_visiblenewpass.setText("{fa_eye-slash}");
        }
    }
    @OnClick(R.id.btn_visibleconfirmpass)
    public void buttonVisibleConfirmPassword() {
        if (isPassVisible2) {
            confirm_password.setTransformationMethod(new PasswordTransformationMethod());
            isPassVisible2 = false;
            btn_visibleconfirmpass.setText("{fa_eye}");
        } else {
            confirm_password.setTransformationMethod(null);
            isPassVisible2 = true;
            btn_visibleconfirmpass.setText("{fa_eye-slash}");
        }
    }
    @OnClick(R.id.btn_visibleoldpass)
    public void buttonVisibleOldPassword() {
        if (isPassVisible3) {
            old_password.setTransformationMethod(new PasswordTransformationMethod());
            isPassVisible3 = false;
            btn_visibleoldpass.setText("{fa_eye}");
        } else {
            old_password.setTransformationMethod(null);
            isPassVisible3 = true;
            btn_visibleoldpass.setText("{fa_eye-slash}");
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_update_password;
    }

    public static void startActivity(Activity ctx) {
        Intent iz = new Intent(ctx, UpdatePasswordActivity.class);
        ctx.startActivity(iz);
    }
    public void updatePassword(){
        if (old_password.getText().toString().equals(password)){
            if (new_password.getText().toString().equals(confirm_password.getText().toString())&&!new_password.getText().toString().isEmpty()&&!confirm_password.getText().toString().isEmpty()){
                UpdatePasswordApi.newInstance().callApi(sessionLogin.getUser_id(),username,password,new_password.getText().toString(),new WpCallback<Void>() {
                    @Override
                    public void success(Void aVoid, String json) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(UpdatePasswordActivity.this,"Sukses",Toast.LENGTH_LONG).show();
                                SharePreferences.saveString(UpdatePasswordActivity.this, Constant.TAG.PASSWORD, new_password.getText().toString());
                                finish();
                            }
                        });
                    }

                    @Override
                    public void failure(String error) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdatePasswordActivity.this,error,Toast.LENGTH_LONG).show();

                    }
                });
            }else{
                Toast.makeText(UpdatePasswordActivity.this,"Password baru dan konfirmasi password tidak sama / masih kosong",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(UpdatePasswordActivity.this,"Password lama tidak sama",Toast.LENGTH_LONG).show();
        }
    }


}
