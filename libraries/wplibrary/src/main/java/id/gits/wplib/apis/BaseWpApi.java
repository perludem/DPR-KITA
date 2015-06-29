package id.gits.wplib.apis;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;
import id.gits.wplib.utils.WPKey;

/**
 * Created by ibun on 13/02/2015.
 */
public class BaseWpApi<T> implements de.timroes.axmlrpc.XMLRPCCallback {
    protected static XMLRPCClient mClient = null;
    protected static Gson gson = new Gson();
    private WpCallback<T> mWpCallback;
    private Type mType;

    public BaseWpApi() {
        try {
            mClient = new XMLRPCClient(new URL(WPKey.URL.XMLRPC_URL));
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    protected void callWPApi(WpCallback<T> wpCallback, Type type, String methodName, Object... param) {
        mType = type;
        mWpCallback = wpCallback;
        long id = mClient.callAsync(this, methodName, param);
    }

    @Override
    public void onResponse(long id, Object result) {
        String json = gson.toJson(result);
        try {
            T res = gson.fromJson(gson.toJson(result), mType);
            mWpCallback.success(res, json);
        } catch (Exception e) {
            mWpCallback.success(null, json);
        }
    }

    @Override
    public void onError(long id, XMLRPCException error) {
        mWpCallback.failure(error.getCause().getMessage());
    }

    @Override
    public void onServerError(long id, XMLRPCServerException error) {
        mWpCallback.failure(error.getCause().getMessage());
    }

    interface WpBaseCallback {
        public void onSuccess(String json);

        public void onError(String message);
    }
}
