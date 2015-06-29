package id.gits.wplib.apis;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import id.gits.wplib.utils.WPKey;

/**
 * Created by dodulz on 13/02/2015.
 */
public class NewCommentApi extends BaseWpApi {
    private static NewCommentApi api;

    public static NewCommentApi newInstance() {
        if (api == null)
            api = new NewCommentApi();
        return api;
    }

    public void callApi(String username, String password, int post_id, HashMap<String, Object> content, WpCallback<Integer> callback) {
        Type myType = new TypeToken<Integer>() {
        }.getType();
        callWPApi(callback, myType, WPKey.METHOD.NEWCOMMENT, 1, username, password, post_id, content);
    }

}
