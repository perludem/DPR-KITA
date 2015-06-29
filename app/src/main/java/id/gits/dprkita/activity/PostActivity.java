package id.gits.dprkita.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import id.gits.dprkita.R;
import id.gits.dprkita.dao.anggota.Anggota;
import id.gits.dprkita.fragment.KomisiFragment;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.SharePreferences;
import id.gits.dprkita.utils.json.KomisiJSON;
import id.gits.dprkita.utils.view.MyProgressView;
import id.gits.wplib.apis.LoginApi;
import id.gits.wplib.apis.NewPostApi;
import id.gits.wplib.apis.UploadImage;
import id.gits.wplib.apis.WpCallback;
import id.gits.wplib.utils.WPKey;


public class PostActivity extends BaseActivity {


    private static final String ID_ANGGOTA = "ID";
    private static final String NAMA_ANGGOTA = "NAMA";
    private static final String POST_TYPE = "POST_TYPE";
    private static final String TITLE = "TITLE";
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 300;
    private static String furi;
    private static File mediaFile;
    private static Uri fileUri;
    @InjectView(R.id.ev_post)
    EditText ev_post;
    @InjectView(R.id.view_progress)
    MyProgressView progressView;
    @InjectView(R.id.img_attach)
    ImageView img_attach;
    @InjectView(R.id.pilih_komisi)
    LinearLayout pilih_komisi;
    @InjectView(R.id.tv_komisi)
    TextView tv_komisi;
    @InjectView(R.id.img_arrow)
    ImageButton img_arrow;
    @InjectView(R.id.img_arrow_rl)
    ImageButton img_arrow_rl;
    @InjectView(R.id.lin_rl)
    LinearLayout lin_rl;
    @InjectView(R.id.select_rl)
    TextView select_rl;
    WpCallback<HashMap<String, Object>> resultImageListener = new WpCallback<HashMap<String, Object>>() {
        @Override
        public void success(HashMap<String, Object> stringObjectHashMap, String json) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressView.stopAndError("Success Upload Image", false);
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }

        @Override
        public void failure(final String error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressView.stopAndError(error, false);
                }
            });
        }
    };
    String tipe = "";
    private String komisi;
    private String id_anggota;
    private String nama_anggota;
    private String username, password, post_type, title;
    private LoginApi.ApiDao user;
    private String ruang_lingkup = null;
    private int id_komisi = 0;
    private boolean isHaveImage = false;
    WpCallback<String> resultListener = new WpCallback<String>() {
        @Override
        public void success(String s, String json) {
            if (isHaveImage) {
                uploadImage(Integer.parseInt(json.replace("\"", "").trim()));
            } else {
                setResult(RESULT_OK);
                finish();
            }
        }

        @Override
        public void failure(final String error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(PostActivity.class.getName(), error);
                    Toast.makeText(PostActivity.this, getResources().getString(R.string.errorNetwork), Toast.LENGTH_LONG).show();
                    progressView.setVisibility(View.GONE);
                }
            });
        }
    };

    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "anggota_dpr");
        if (!mediaStorage.exists()) {
            if (!mediaStorage.mkdirs()) {
                Log.e("CAMERA : User fragment", "failed create directory");
                return null;
            }
        }
        String timeStamp = String.valueOf(new Date().getTime());

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorage.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            furi = mediaFile.getPath();
        }
        return mediaFile;
    }

    public static void startActivity(Activity ctx, String title, String post_type, String id, String name, int requestcode) {
        Intent i = new Intent(ctx, PostActivity.class);
        i.putExtra(TITLE, title);
        i.putExtra(POST_TYPE, post_type);
        i.putExtra(ID_ANGGOTA, id);
        i.putExtra(NAMA_ANGGOTA, name);
        ctx.startActivityForResult(i, requestcode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        id_anggota = getIntent().getStringExtra(ID_ANGGOTA);
        nama_anggota = getIntent().getStringExtra(NAMA_ANGGOTA);
        post_type = getIntent().getStringExtra(POST_TYPE);
        title = getIntent().getStringExtra(TITLE);
        parsing();
        username = SharePreferences.getString(this, Constant.TAG.USERNAME);
        password = SharePreferences.getString(this, Constant.TAG.PASSWORD);

        if (username == null || password == null) {
            LoginActivity.startActivity(this, 0);
        }

        tv_komisi.setText("PILIH KOMISI");
        if (post_type.equals(WPKey.CUSTOM_FIELD.ASPIRASI)) {
            pilih_komisi.setVisibility(View.VISIBLE);

            img_arrow.setImageDrawable(Helper.makeOwnIcon(this, R.color.bluegray700, Iconify.IconValue.fa_angle_down));
            img_arrow_rl.setImageDrawable(Helper.makeOwnIcon(this, R.color.bluegray700, Iconify.IconValue.fa_angle_down));
        } else
            pilih_komisi.setVisibility(View.GONE);
        lin_rl.setVisibility(View.GONE);


        if (post_type.equals(WPKey.CUSTOM_FIELD.ASPIRASI)) {
            tipe = "ASPIRASI";
        } else if (post_type.equals(WPKey.CUSTOM_FIELD.BIOCOMMENT)) {
            tipe = "ASPIRASI ANGGOTA";
        } else if (post_type.equals(WPKey.CUSTOM_FIELD.STATUSANGGOTA)) {
            tipe = "PODIUM";
        }


    }

    @OnClick(R.id.list_komisi)
    public void pilihKomisi() {
        ruang_lingkup = null;
        select_rl.setText("");
        Intent i = new Intent(PostActivity.this, ListKomisiActivity.class);
        startActivityForResult(i, 9);
    }

    @OnClick(R.id.list_rl)
    public void pilihRuangLingkup() {
        final String[] dataRuangLingkup = KomisiJSON.listRuangLingkup().get(id_komisi);
        AlertDialog.Builder dialogSelected = new AlertDialog.Builder(PostActivity.this);
        dialogSelected.setTitle("Pilih Ruang Lingkup");
        dialogSelected.setItems(dataRuangLingkup, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ruang_lingkup = dataRuangLingkup[which];
                select_rl.setText(ruang_lingkup);
                dialog.dismiss();
            }
        });
        dialogSelected.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        username = SharePreferences.getString(this, Constant.TAG.USERNAME);
        password = SharePreferences.getString(this, Constant.TAG.PASSWORD);
    }

    public void parsing() {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(title);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_post;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post, menu);
        menu.findItem(R.id.action_picture).setIcon(new IconDrawable(PostActivity.this, Iconify.IconValue.fa_camera_retro).colorRes(R.color.white).actionBarSize());
        menu.findItem(R.id.action_done).setIcon(new IconDrawable(PostActivity.this, Iconify.IconValue.fa_send).colorRes(R.color.white).actionBarSize());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {

            if (komisi == null && post_type.equals(WPKey.CUSTOM_FIELD.ASPIRASI))
                Toast.makeText(PostActivity.this, getResources().getString(R.string.pilih_komisi), Toast.LENGTH_LONG).show();
            if (ruang_lingkup == null && post_type.equals(WPKey.CUSTOM_FIELD.ASPIRASI))
                Toast.makeText(PostActivity.this, getResources().getString(R.string.pilih_ruang_lingkup), Toast.LENGTH_LONG).show();

            if (komisi != null && ruang_lingkup != null) {
                if (ev_post.getText().toString().length() > 10) {
                    pilih_komisi.setEnabled(false);
                    lin_rl.setEnabled(false);
                    submit();
                } else {
                    Toast.makeText(PostActivity.this, getResources().getString(R.string.long_post), Toast.LENGTH_LONG).show();
                }
            }

            if (!post_type.equals(WPKey.CUSTOM_FIELD.ASPIRASI))
                submit();

            return true;
        } else if (id == R.id.action_picture) {
            takeCamera();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void takeCamera() {


        furi = null;
        mediaFile = null;
        fileUri = null;

        //TODO ALert dialog
        String[] arr = new String[]{"Foto dari Kamera", "Pilih dari Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setTitle("Upload gambar")
                .setItems(arr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (Helper.checkCamera(PostActivity.this)) {
                                takeImage();
                            } else {
                                errorCamera();
                            }
                        } else {
                            pickGallery();
                        }
                    }
                });
        builder.create().show();

    }

    public void errorCamera() {
        AlertDialog.Builder cameraError = new AlertDialog.Builder(PostActivity.this);
        cameraError.setMessage("Tidak memiliki perangkat kamera");
        cameraError.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        cameraError.show();
    }

    private void pickGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void takeImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public void setPreview(String uri) {
        img_attach.setVisibility(View.VISIBLE);
        img_attach.setImageURI(Uri.fromFile(new File(uri)));
    }

    public void uploadImage(int id_post) {
        progressView.startProgress();
        HashMap<String, Object> content = new HashMap<>();
        String path = fileUri.getPath();
        String filename = path.substring(path.lastIndexOf("/") + 1);
        content.put("name", filename + ".jpg");
        content.put("type", "image/jpeg");
        content.put("bits", Helper.getBytesFromBitmap(path));
        content.put("overwrite", false);
        content.put("post_id", id_post);
        UploadImage.newInstance().callApi(username, password, content, resultImageListener);
    }

    private void submit() {
        user = SharePreferences.sessionLogin(PostActivity.this);
        if (post_type.equals(WPKey.CUSTOM_FIELD.ASPIRASI)) {
            sendToAspirasi(ruang_lingkup, komisi);
        } else if (post_type.equals(WPKey.CUSTOM_FIELD.BIOCOMMENT)) {
            sendToBioComment();
            sendToAspirasi("-", "-");
        } else if (post_type.equals(WPKey.CUSTOM_FIELD.STATUSANGGOTA)) {
            sendToStatusAnggota();
        }
        pilih_komisi.setEnabled(false);
        lin_rl.setEnabled(false);
        progressView.setVisibility(View.VISIBLE);
    }

    public void sendToStatusAnggota() {
        String comment = ev_post.getText().toString();
        HashMap<String, Object> content = new HashMap<>();
        content.put(WPKey.KEY.POST_CONTENT, comment);
        content.put(WPKey.KEY.POST_TYPE, post_type);
        content.put(WPKey.KEY.POST_STATUS, Constant.TAG.PUBLISH);
        List<HashMap<String, String>> customFields = new ArrayList<>();
        customFields.add(Helper.valueCustomField(WPKey.KEY.USERNAME, user.getDisplay_name()));
        content.put(WPKey.KEY.POST_TITLE, id_anggota);
        customFields.add(Helper.valueCustomField(WPKey.KEY.ID_ANGGOTA_DPR, id_anggota));
        customFields.add(Helper.valueCustomField(WPKey.KEY.NAMA_ANGGOTA, nama_anggota));
        customFields.add(Helper.valueCustomField(WPKey.KEY.DAPIL, SharePreferences.getString(PostActivity.this, Constant.TAG.ID_DAPIL)));
        content.put(WPKey.KEY.CUSTOM_FIELD, customFields);
        NewPostApi.newInstance().callApi(username, password, content, resultListener);

        HashMap<String, String> props = new HashMap<>();
        props.put("nama", nama_anggota);
        props.put("type", tipe);
        props.put("status", comment);
        Helper.addActionMixpanel(PostActivity.this, getResources().getString(R.string.mix_post) + " " + tipe, props);
    }

    public void sendToBioComment() {
        String comment = ev_post.getText().toString();
        HashMap<String, Object> content = new HashMap<>();
        content.put(WPKey.KEY.POST_CONTENT, comment);
        content.put(WPKey.KEY.POST_TYPE, post_type);
        content.put(WPKey.KEY.POST_STATUS, Constant.TAG.PUBLISH);
        List<HashMap<String, String>> customFields = new ArrayList<>();
        customFields.add(Helper.valueCustomField(WPKey.KEY.USERNAME, user.getDisplay_name()));
        content.put(WPKey.KEY.POST_TITLE, id_anggota);
        customFields.add(Helper.valueCustomField(WPKey.KEY.ID_ANGGOTA_DPR, id_anggota));
        customFields.add(Helper.valueCustomField(WPKey.KEY.NAMA_ANGGOTA, nama_anggota));
        content.put(WPKey.KEY.CUSTOM_FIELD, customFields);
        NewPostApi.newInstance().callApi(username, password, content, resultListener);

        HashMap<String, String> props = new HashMap<>();
        props.put("nama", nama_anggota);
        props.put("type", tipe);
        props.put("status", comment);
        Helper.addActionMixpanel(PostActivity.this, getResources().getString(R.string.mix_post) + " " + tipe, props);
    }

    public void sendToAspirasi(String ruang_lingkup, String komisi) {
        HashMap<String, Object> contentAspirasi = new HashMap<>();
        contentAspirasi.put(WPKey.KEY.POST_CONTENT, ev_post.getText().toString());
        contentAspirasi.put(WPKey.KEY.POST_TYPE, WPKey.CUSTOM_FIELD.ASPIRASI);
        contentAspirasi.put(WPKey.KEY.POST_STATUS, Constant.TAG.PUBLISH);
        if (post_type.equals(WPKey.CUSTOM_FIELD.ASPIRASI)) {
            contentAspirasi.put(WPKey.KEY.POST_TITLE, Constant.TAG.ASPIRASI + ":" + komisi + " " + ruang_lingkup);
        } else {
            contentAspirasi.put(WPKey.KEY.POST_TITLE, Constant.TAG.ASPIRASI + ":" + nama_anggota);
        }

        List<HashMap<String, String>> customFieldsAspirasi = new ArrayList<>();
        customFieldsAspirasi.add(Helper.valueCustomField(WPKey.KEY.USERNAME, user.getDisplay_name()));
        Anggota profileAnggotaDPR = SharePreferences.profileAnggotaDPR(PostActivity.this);
        if (profileAnggotaDPR != null) {
            customFieldsAspirasi.add(Helper.valueCustomField(WPKey.KEY.USERNAME, profileAnggotaDPR.getNama()));
        }
        customFieldsAspirasi.add(Helper.valueCustomField("ruang_lingkup", ruang_lingkup));
        customFieldsAspirasi.add(Helper.valueCustomField("komisi", komisi));
        contentAspirasi.put(WPKey.KEY.CUSTOM_FIELD, customFieldsAspirasi);
        NewPostApi.newInstance().callApi(username, password, contentAspirasi, resultListener);

        HashMap<String, String> props = new HashMap<>();
        props.put("nama", nama_anggota);
        props.put("type", tipe);
        props.put("komisi", komisi);
        props.put("ruang_lingkup", ruang_lingkup);
        props.put("status", ev_post.getText().toString());
        Helper.addActionMixpanel(PostActivity.this, getResources().getString(R.string.mix_post) + " " + tipe, props);
    }

    public void croppedResult(Intent result) {
        isHaveImage = true;
        fileUri = Crop.getOutput(result);
        furi = fileUri.getPath();
        setPreview(furi);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri tmpUri = data.getData();
                Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped" + PICK_IMAGE_ACTIVITY_REQUEST_CODE));
                new Crop(tmpUri).output(outputUri).start(PostActivity.this);

            }
        } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped" + CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE));
                new Crop(fileUri).output(outputUri).asSquare().start(PostActivity.this);

            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            if (resultCode == Activity.RESULT_OK)
                croppedResult(data);
        } else if (requestCode == 9) {
            if (resultCode == Activity.RESULT_OK) {
                String[] fraksi = KomisiFragment.komisi.getNama().split(" ");
                komisi = "Komisi " + fraksi[0];
                tv_komisi.setText(komisi);
                id_komisi = KomisiFragment.komisi.getId();
                lin_rl.setVisibility(View.VISIBLE);
                KomisiFragment.komisi = null;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Helper.mixpanel(PostActivity.this).flush();
    }
}
