package id.gits.dprkita.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import id.gits.dprkita.dao.anggota.Anggota;
import id.gits.wplib.apis.LoginApi;

/**
 * Created by yatnosudar on 2/3/15.
 */
public class SharePreferences {

    public static void saveString(Context activity, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveInt(Context activity, String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    public static String getString(Context activity, String key) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(activity);
            String latihan = sharedPreferences.getString(key, null);
            return latihan;
        } catch (Exception e) {
            return null;
        }

    }

    public static int getInt(Context activity, String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(activity);
        int latihan = sharedPreferences.getInt(key, 0);
        return latihan;
    }

    public static LoginApi.ApiDao sessionLogin(Context context) {
        String json = SharePreferences.getString(context, Constant.TAG.LOGIN_SESSION);
        LoginApi.ApiDao session = new Gson().fromJson(json, LoginApi.ApiDao.class);
        return session;
    }

    public static Anggota profileAnggotaDPR(Context context) {
        String json = SharePreferences.getString(context, Constant.TAG.DETAILANGGOTA);
        Anggota session = new Gson().fromJson(json, Anggota.class);
        return session;
    }

    public static void sessionLogout(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constant.TAG.LOGIN_SESSION);
        editor.remove(Constant.TAG.WAS_SKIP);
        editor.remove(Constant.TAG.USERNAME);
        editor.remove(Constant.TAG.PASSWORD);
        editor.remove(Constant.TAG.ID_DAPIL);
        editor.remove(Constant.TAG.ID_PEMILUAPI);
        editor.remove(Constant.TAG.NO_HP);
        editor.remove(Constant.TAG.DETAILANGGOTA);
        editor.commit();
    }

}
