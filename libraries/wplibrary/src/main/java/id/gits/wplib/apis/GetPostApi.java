package id.gits.wplib.apis;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import id.gits.wplib.dao.PostDao;
import id.gits.wplib.utils.WPKey;

/**
 * Created by ibun on 13/02/2015.
 */
public class GetPostApi extends BaseWpApi {
    private static GetPostApi api;

    public static GetPostApi newInstance() {
        if (api == null)
            api = new GetPostApi();
        return api;
    }

    public void callApi(String username, String password, HashMap<String, Object> content, WpCallback<List<PostDao>> callback) {
        Type myType = new TypeToken<List<PostDao>>() {
        }.getType();
        callWPApi(callback, myType, WPKey.METHOD.EXTAPI, 1, username, password, "get_posts", content);
    }

}
