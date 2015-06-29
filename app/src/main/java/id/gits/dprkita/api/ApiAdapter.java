package id.gits.dprkita.api;

import android.app.Activity;

import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.Helper;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by yatnosudar on 1/28/15.
 */


public class ApiAdapter {
    private static ApiInterface adapter = null;
    private static ApiInterface adapterJetpack = null;

    public static ApiInterface callAPI() {
        if (adapter == null) {

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constant.URL_API.BASE_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            adapter = restAdapter.create(ApiInterface.class);
        }

        return adapter;
    }

    public static ApiInterface callAPIJetPack() {
        if (adapterJetpack == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constant.URL_API.BASE_URL_JETPACK)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            adapterJetpack = restAdapter.create(ApiInterface.class);
        }
        return adapterJetpack;
    }

    public static ApiInterface callAPI(final Activity activity) {
        if (adapter == null) {

            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestInterceptor.RequestFacade request) {
                            request.addHeader("Accept", "application/json;versions=1");
                            if (Helper.isInternetAvailable()) {
                                int maxAge = 60; // read from cache for 1 minute
                                request.addHeader("Cache-Control", "public, max-age=" + maxAge);
                            } else {
                                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                                request.addHeader("Cache-Control",
                                        "public, only-if-cached, max-stale=" + maxStale);
                            }
                        }
                    });
            RestAdapter restAdapter = builder
                    .setEndpoint(Constant.URL_API.BASE_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            adapter = restAdapter.create(ApiInterface.class);
        }
        return adapter;
    }
}
