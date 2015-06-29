package id.gits.wplib.apis;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import id.gits.wplib.utils.WPKey;

/**
 * Created by ibun on 13/02/2015.
 */
public class NewImageUpload extends BaseWpApi {
    private static NewImageUpload api;

    public static NewImageUpload newInstance() {
        if (api == null)
            api = new NewImageUpload();
        return api;
    }

    public void callApi(String username, String password, HashMap<String, Object> content, WpCallback<String> callback) {
        Type myType = new TypeToken<String>() {
        }.getType();
        callWPApi(callback, myType, WPKey.METHOD.NEW_POST, 1, username, password, content);
    }

}
