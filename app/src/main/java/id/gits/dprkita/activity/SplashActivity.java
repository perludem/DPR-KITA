package id.gits.dprkita.activity;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import id.gits.dprkita.R;
import id.gits.dprkita.api.ApiAdapter;
import id.gits.dprkita.dao.BaseDao;
import id.gits.dprkita.dao.dapil.Dapil;
import id.gits.dprkita.dao.dapil.Data;
import id.gits.dprkita.dao.dapil.Wilayah;
import id.gits.dprkita.dao.komisi.DataKomisi;
import id.gits.dprkita.dao.provinsi.Provinsi;
import id.gits.dprkita.db.DapilDB;
import id.gits.dprkita.db.ProvinsiDB;
import id.gits.dprkita.db.WilayahDB;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.GPSTracker;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.MyLocation;
import id.gits.dprkita.utils.SharePreferences;
import id.gits.dprkita.utils.json.DapilJSON;
import id.gits.dprkita.utils.json.KomisiJSON;
import id.gits.wplib.apis.LoginApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by yatnosudar on 2/3/15.
 */
public class SplashActivity extends ActionBarActivity {

    private static final int DAPIL = 0;
    private static final int PROVINSI = 1;
    public int count = 0;
    List<Provinsi> provinsi;
    Callback<BaseDao<id.gits.dprkita.dao.provinsi.Data>> provinsiCallback = new Callback<BaseDao<id.gits.dprkita.dao.provinsi.Data>>() {
        @Override
        public void success(BaseDao<id.gits.dprkita.dao.provinsi.Data> dataBaseDao, Response response) {
            provinsi = dataBaseDao.getData().getResults().getProvinsi();
            new ProcessInsertDapil().execute(PROVINSI);
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(SplashActivity.this, "PROVINSI : " + error.getMessage(), Toast.LENGTH_LONG);
        }
    };
    MyLocation myLocation = new MyLocation();
    String username;
    String password;
    Timer timer;
    boolean isTimerFinished = false;
    //    @Override
//    protected void onResume() {
//        super.onResume();
//
//        username = SharePreferences.getString(SplashActivity.this, Constant.TAG.USERNAME);
//        password = SharePreferences.getString(SplashActivity.this, Constant.TAG.PASSWORD);
//        if (username != null || password != null) {
//            MainActivity.startActivity(SplashActivity.this);
//            finish();
//        }
//    }
//    boolean isLocationFound = false;
    public MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {

        @Override
        public void gotLocation(Location location) {

            HashMap<String, String> track = new HashMap<>();
            if (location != null) {
                String strloc = location.getLatitude() + ","
                        + location.getLongitude();

                track.put("lat", location.getLatitude() + "");
                track.put("lng", location.getLatitude() + "");
                SharePreferences.saveString(SplashActivity.this, Constant.TAG.LONGITUDE, location.getLatitude() + "");
                SharePreferences.saveString(SplashActivity.this, Constant.TAG.LATITUDE, location.getLongitude() + "");
            } else {
                track.put("lat", "-6.2297465");
                track.put("lng", "106.829518");
                SharePreferences.saveString(SplashActivity.this, Constant.TAG.LONGITUDE, "-6.2297465");
                SharePreferences.saveString(SplashActivity.this, Constant.TAG.LATITUDE, "106.829518");
            }
            track.put("Splash Screen", "Open");
            Helper.addActionMixpanel(SplashActivity.this, "SplashScreen", track);

            downloadData();

            if (isTimerFinished)
                nextActivity();
        }
    };
    List<Dapil> dapil;
    Callback<BaseDao<id.gits.dprkita.dao.dapil.Data>> dapiCallback = new Callback<BaseDao<id.gits.dprkita.dao.dapil.Data>>() {
        @Override
        public void success(BaseDao<id.gits.dprkita.dao.dapil.Data> dataBaseDao, Response response) {
            dapil = dataBaseDao.getData().getResults().getDapil();
            new ProcessInsertDapil().execute(DAPIL);
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(SplashActivity.this, "DAPIL : " + error.getMessage(), Toast.LENGTH_LONG);
        }
    };
    Callback<BaseDao<Data>> listenerDapil = new Callback<BaseDao<Data>>() {
        @Override
        public void success(final BaseDao<Data> dataBaseDao, Response response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dapil = dataBaseDao.getData().getResults().getDapil();
                    new ProcessInsertDapil().execute(DAPIL);
                }
            });
        }

        @Override
        public void failure(RetrofitError error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    id.gits.dprkita.dao.dapil.BaseDaoDapil d = new Gson().fromJson(DapilJSON.DAPIL_V2, id.gits.dprkita.dao.dapil.BaseDaoDapil.class);
                    dapil = d.getData().getResults().getDapil();
                    new ProcessInsertDapil().execute(DAPIL);
                }
            });
        }
    };
    Callback<BaseDao<DataKomisi>> komisiCallback = new Callback<BaseDao<DataKomisi>>() {
        @Override
        public void success(BaseDao<DataKomisi> resultsKomisiBaseDao, Response response) {
            String data = new Gson().toJson(resultsKomisiBaseDao.getData().getResults().getKomisi());
            Log.d("data komisi", data);
            SharePreferences.saveString(SplashActivity.this, Constant.TAG.WAS_SAVE_KOMISI, data);
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(SplashActivity.this, "Komisi : " + error.getMessage(), Toast.LENGTH_LONG);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                process();
                return null;
            }
        }.execute();
    }

    private void process(){
        int dap = SharePreferences.getInt(SplashActivity.this, Constant.TAG.WAS_SAVE_DAPIL);
        if (dap != Constant.TAG.SUCCESS_V2) {
            SharePreferences.sessionLogout(this);
            DapilDB.deleteAll(DapilDB.class);
            ProvinsiDB.deleteAll(ProvinsiDB.class);;
        }


        username = SharePreferences.getString(SplashActivity.this, Constant.TAG.USERNAME);
        password = SharePreferences.getString(SplashActivity.this, Constant.TAG.PASSWORD);

        // create class object
        GPSTracker gps = new GPSTracker(SplashActivity.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            Location location = gps.getLocation();
            HashMap<String, String> track = new HashMap<>();
            if (location != null) {
                track.put("lat", gps.getLatitude() + "");
                track.put("lng", gps.getLongitude() + "");
                SharePreferences.saveString(SplashActivity.this, Constant.TAG.LONGITUDE, gps.getLatitude() + "");
                SharePreferences.saveString(SplashActivity.this, Constant.TAG.LATITUDE, gps.getLongitude() + "");
            } else {
                String lng = SharePreferences.getString(SplashActivity.this,Constant.TAG.LONGITUDE);
                String lat = SharePreferences.getString(SplashActivity.this,Constant.TAG.LATITUDE);
                if (lat==null&&lng==null) {
                    SharePreferences.saveString(SplashActivity.this, Constant.TAG.LATITUDE, "-6.2297465");
                    SharePreferences.saveString(SplashActivity.this, Constant.TAG.LONGITUDE, "106.829518");
                    track.put("lat", "-6.2297465");
                    track.put("lng", "106.829518");
                }else{
                    track.put("lat", lng);
                    track.put("lng", lat);
                }

            }
            track.put("Splash Screen", "Open");
            Helper.addActionMixpanel(SplashActivity.this, "SplashScreen", track);
            downloadData();
            nextActivity();
        } else {
            String lng = SharePreferences.getString(SplashActivity.this,Constant.TAG.LONGITUDE);
            String lat = SharePreferences.getString(SplashActivity.this,Constant.TAG.LATITUDE);
            if (lat==null&&lng==null) {
                SharePreferences.saveString(SplashActivity.this, Constant.TAG.LATITUDE, "-6.2297465");
                SharePreferences.saveString(SplashActivity.this, Constant.TAG.LONGITUDE, "106.829518");
            }
            downloadData();
            nextActivity();
        }


        int open_first_time = SharePreferences.getInt(SplashActivity.this, "OPEN_FIRST_TIME");
        if (open_first_time == 0) {
            LoginApi.ApiDao login = SharePreferences.sessionLogin(this);
            if (login != null) {
                MixpanelAPI mixpanelAPI = Helper.mixpanel(this);
                mixpanelAPI.getPeople().identify(login.getUser_id());
                mixpanelAPI.getPeople().set("$name", login.getUsername());
                mixpanelAPI.getPeople().set("$email", login.getEmail1());
                mixpanelAPI.getPeople().set("role", login.getRoles().get(0));
                mixpanelAPI.getPeople().initPushHandling(Constant.KEY.GOOGLE_SENDER_ID);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(login));
                    mixpanelAPI.getPeople().set(jsonObject);
                    mixpanelAPI.registerSuperProperties(jsonObject);
                } catch (JSONException e) {
                }
                SharePreferences.saveInt(SplashActivity.this, "OPEN_FIRST_TIME", 1);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        timer.cancel();
    }

    public void downloadData() {
        int dap = SharePreferences.getInt(SplashActivity.this, Constant.TAG.WAS_SAVE_DAPIL);
        final int pro = SharePreferences.getInt(SplashActivity.this, Constant.TAG.WAS_SAVE_PROVINSI);
        String komisi = SharePreferences.getString(SplashActivity.this, Constant.TAG.WAS_SAVE_KOMISI);

        if (komisi == null || komisi.equals("null")) {
            id.gits.dprkita.dao.komisi.BaseDaoDapil dp = new Gson().fromJson(KomisiJSON.KOMISI, id.gits.dprkita.dao.komisi.BaseDaoDapil.class);
            String data = new Gson().toJson(dp.getData().getResults().getKomisi());
            Log.d("data komisi", data);
            SharePreferences.saveString(SplashActivity.this, Constant.TAG.WAS_SAVE_KOMISI, data);

            //ApiAdapter.callAPI().listkomisi(Constant.KEY.PEMILUAPI, komisiCallback);
        }
        if (dap != Constant.TAG.SUCCESS_V2 && pro != Constant.TAG.SUCCESS_V2 || dap == Constant.TAG.SUCCESS && pro == Constant.TAG.SUCCESS) {
            ApiAdapter.callAPI().listDapil_v2(Constant.KEY.PEMILUAPI, 77, listenerDapil);

            id.gits.dprkita.dao.provinsi.BaseDaoDapil e = new Gson().fromJson(DapilJSON.PROVINSI, id.gits.dprkita.dao.provinsi.BaseDaoDapil.class);
            provinsi = e.getData().getResults().getProvinsi();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new ProcessInsertDapil().execute(PROVINSI);
                }
            });


            /*ApiAdapter.callAPI().listDapil(Constant.KEY.PEMILUAPI, "DPR", dapiCallback);
            ApiAdapter.callAPI().listProvinsi(Constant.KEY.PEMILUAPI, provinsiCallback);*/

        }
    }

    private void nextActivity() {
        int skip = SharePreferences.getInt(SplashActivity.this, Constant.TAG.WAS_SKIP);
        if ((username == null || password == null) && skip != Constant.TAG.SKIP) {
            LoginActivity.startActivity(SplashActivity.this, 1);
            finish();
        } else {
            MainActivity.startActivity(SplashActivity.this);
            finish();
        }
    }

    private void findCurrentLocation() {
        myLocation.getLocation(this, locationResult);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Helper.mixpanel(SplashActivity.this).getPeople().getSurveyIfAvailable();
        Helper.mixpanel(SplashActivity.this).getPeople().showNotificationIfAvailable(SplashActivity.this);
    }

    class ProcessInsertDapil extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            if (params[0] == PROVINSI) {
                List<ProvinsiDB> provinsiDBs = new ArrayList<>();
                for (Provinsi prov : provinsi) {
                    ProvinsiDB provinsiDB = new ProvinsiDB();
                    provinsiDB.setIdProvinsi(prov.getId());
                    provinsiDB.setNama(prov.getNama());
                    provinsiDB.setNama_lengkap(prov.getNama_lengkap());
                    provinsiDB.setNama_inggris(prov.getNama_inggris());
                    provinsiDB.setJumlah_kursi(prov.getJumlah_kursi());
                    provinsiDB.setJumlah_penduduk(prov.getJumlah_penduduk());
                    provinsiDB.setPro_id(prov.getPro_id());
                    provinsiDBs.add(provinsiDB);
                }
                ProvinsiDB.saveInTx(provinsiDBs);
                SharePreferences.saveInt(SplashActivity.this, Constant.TAG.WAS_SAVE_PROVINSI, Constant.TAG.SUCCESS_V2);
                count++;
            } else {
                List<DapilDB> dapilDBs = new ArrayList<>();
                for (Dapil dap : dapil) {
                    DapilDB dapilDB = new DapilDB();
                    dapilDB.setIdDapil(dap.getKode());
                    dapilDB.setNama(dap.getNama());
                    dapilDB.setJumlah_kursi(dap.getJumlah_kursi());
                    dapilDB.setJumlah_penduduk(dap.getJumlah_penduduk());
                    dapilDB.setNama_provinsi(dap.getProvinsi().getNama());
                    dapilDB.setProvinsi(dap.getProvinsi());
//                    dapilDB.setSubdapil(dap.getSubdapil());
                    dapilDB.save();


                    for (Wilayah wilayah : dap.getWilayah()) {
                        WilayahDB wilayahDB = new WilayahDB();
                        wilayahDB.setDapil(dapilDB);
                        wilayahDB.setNama(wilayah.getNama());
                        wilayahDB.save();
                    }
                    dapilDBs.add(dapilDB);
                }
//                DapilDB.saveInTx(dapilDBs);
                SharePreferences.saveInt(SplashActivity.this, Constant.TAG.WAS_SAVE_DAPIL, Constant.TAG.SUCCESS_V2);
                count++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           /* if (count > 1 && isTimerFinished) {
                nextActivity();
            }*/
        }
    }
}
