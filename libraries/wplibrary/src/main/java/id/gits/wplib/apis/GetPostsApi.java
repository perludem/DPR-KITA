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
public class GetPostsApi extends BaseWpApi {

    private static GetPostsApi api;

    public static GetPostsApi newInstance() {
        if (api == null)
            api = new GetPostsApi();
        return api;
    }

    //TODO set parameter
    public void callApi(String username, String password, HashMap<String, Object> content, WpCallback<List<PostDao>> callback) {
        Type myType = new TypeToken<List<PostDao>>() {
        }.getType();
        callWPApi(callback, myType, WPKey.METHOD.GET_POST, 1, username, password, content);
    }


}
