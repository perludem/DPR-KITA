package id.gits.wplib.apis;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by ibun on 13/02/2015.
 */
public class UpdatePasswordApi extends BaseWpApi {
    private static UpdatePasswordApi api;

    public static UpdatePasswordApi newInstance() {
        if (api == null)
            api = new UpdatePasswordApi();
        return api;
    }

    public void callApi(String userId, String username, String password,String newpassword, WpCallback<Void> callback) {
        Type myType = new TypeToken<Integer>() {
        }.getType();
        callWPApi(callback, myType, "extapi.callWpMethod", username, password, "wp_set_password",
                new String[]{newpassword, userId});
    }
}
