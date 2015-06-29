package id.gits.wplib.apis;


import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by ibun on 13/02/2015.
 */
public class ForgotPasswordUrlApi extends BaseWpApi {
    private static ForgotPasswordUrlApi api;

    public static ForgotPasswordUrlApi newInstance() {
        if (api == null)
            api = new ForgotPasswordUrlApi();
        return api;
    }

    public void callApi(WpCallback<String> callback) {
        Type myType = new TypeToken<Integer>() {
        }.getType();
        callWPApi(callback, myType, "extapi.callWpMethod", "<REPLACE_WITH_ADMIN_USERNAME>", "<REPLACE_WITH_ADMIN_PASSWORD>", "wp_lostpassword_url",
                new String[]{""});
    }
}
