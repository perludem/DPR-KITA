package id.gits.dprkita;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.List;

import id.gits.wplib.apis.GetPostsApi;
import id.gits.wplib.apis.WpCallback;
import id.gits.wplib.daos.PostDao;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test() {
        GetPostsApi.newInstance().callApi(new WpCallback<List<PostDao>>() {
            @Override
            public void success(List<PostDao> listPostDao, Object response) {
            }

            @Override
            public void failure(String error) {

            }
        });
    }
}