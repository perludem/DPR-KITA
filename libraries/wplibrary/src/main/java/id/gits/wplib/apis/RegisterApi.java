package id.gits.wplib.apis;


import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by ibun on 13/02/2015.
 */
public class RegisterApi extends BaseWpApi {
    private static RegisterApi api;

    public static RegisterApi newInstance() {
        if (api == null)
            api = new RegisterApi();
        return api;
    }

    public void callApi(String username, String password, String email, WpCallback<Integer> callback) {
        Type myType = new TypeToken<Integer>() {
        }.getType();
        //TODO
        callWPApi(callback, myType, "extapi.callWpMethod", "<REPLACE_WITH_ADMIN_EMAIL>", "<REPLACE_WITH_ADMIN_PASSWORD>", "wp_create_user",
                new String[]{username, password, email});
    }
}
