package id.gits.dprkita.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import id.gits.dprkita.R;


public class WebActivity extends BaseActivity {
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_CONTENT = "content";
    public static final String EXTRA_TITLE = "title";
    private WebView webView;
    private ProgressBar pb;

    public static void startThisActivity(Context ctx, String title, String data, boolean isUrl) {
        Intent intent = new Intent(ctx, WebActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        if (isUrl) {
            intent.putExtra(EXTRA_URL, data);
        } else
            intent.putExtra(EXTRA_CONTENT, data);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (title != null) {
            setTitle(title);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.webview);
        pb = (ProgressBar) findViewById(R.id.progressbar);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());

        String url = getIntent().getStringExtra(EXTRA_URL);
        String content = getIntent().getStringExtra(EXTRA_CONTENT);
        if (url == null) {
            webView.loadDataWithBaseURL("http://gits.co.id", content, "text/html", "UTF-8", "");
        } else {
            webView.loadUrl(url);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_web;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if (uri.getQueryParameter("checkemail") != null && uri.getQueryParameter("checkemail").equals("confirm")) {
                Toast.makeText(WebActivity.this, "Silakan cek email Anda", Toast.LENGTH_LONG).show();
            }
            finish();
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            if (progress < 100 && pb.getVisibility() == ProgressBar.GONE) {
                pb.setVisibility(ProgressBar.VISIBLE);
            }
            pb.setProgress(progress);
            if (progress == 100) {
                pb.setVisibility(ProgressBar.GONE);
            }
        }
    }

}
