package id.gits.wplib.apis;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import id.gits.wplib.utils.WPKey;

/**
 * Created by ibun on 13/02/2015.
 */
public class UpdateUserApi extends BaseWpApi {
    private static UpdateUserApi api;

    public static UpdateUserApi newInstance() {
        if (api == null)
            api = new UpdateUserApi();
        return api;
    }

    public void callApi(String username, String password, HashMap<String, String> content, WpCallback<Boolean> callback) {
        Type myType = new TypeToken<Boolean>() {
        }.getType();
        callWPApi(callback, myType, WPKey.METHOD.UPDATE_PROFILE, 1, username, password, content);
    }

}
