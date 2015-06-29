package id.gits.wplib.apis;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by ibun on 13/02/2015.
 */
public class UpdateUserMetaApi extends BaseWpApi {
    private static UpdateUserMetaApi api;

    public static UpdateUserMetaApi newInstance() {
        if (api == null)
            api = new UpdateUserMetaApi();
        return api;
    }

    public void callApi(int userId, String metaKey, String metaValue, WpCallback<Boolean> callback) {
        Type myType = new TypeToken<Integer>() {
        }.getType();
        callWPApi(callback, myType, "extapi.callWpMethod", "usercreator", "usercreator", "update_user_meta",
                new String[]{userId + "", metaKey, metaValue});
    }
}
