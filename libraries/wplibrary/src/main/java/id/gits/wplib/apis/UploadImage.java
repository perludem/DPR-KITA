package id.gits.wplib.apis;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import id.gits.wplib.utils.WPKey;

/**
 * Created by ibun on 13/02/2015.
 */
public class UploadImage extends BaseWpApi {
    private static UploadImage api;

    public static UploadImage newInstance() {
        if (api == null)
            api = new UploadImage();
        return api;
    }

    public void callApi(String username, String password, HashMap<String, Object> content, WpCallback<HashMap<String, Object>> callback) {
        Type myType = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        callWPApi(callback, myType, WPKey.METHOD.UPLOAD_IMAGE, 1, username, password, content);
    }

}
