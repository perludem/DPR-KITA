package id.gits.wplib.apis;


import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ibun on 13/02/2015.
 */
public class CallUserMetaApi extends BaseWpApi {
    private static CallUserMetaApi api;

    public static CallUserMetaApi newInstance() {
        if (api == null)
            api = new CallUserMetaApi();
        return api;
    }

    public void callApi(int user_id, String username, String password, String meta_key, WpCallback<String> callback) {
        Type myType = new TypeToken<String>() {
        }.getType();
        List<Object> cari = new ArrayList<>();
        cari.add(user_id);
        cari.add(meta_key);
        cari.add(true);
        callWPApi(callback, myType, "extapi.callWpMethod", username, password, "get_user_meta", cari);
    }
}
