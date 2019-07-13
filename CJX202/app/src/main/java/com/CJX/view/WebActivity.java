package com.CJX.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.cjx20.R;

public class WebActivity extends Activity{
        private String url;
        private ProgressBar mLoadingProgress;
        private WebView mWebView;
        private FrameLayout mWebContainer;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            Bundle bundle = this.getIntent().getExtras();
            url = bundle.getString("url");
            super.onCreate(savedInstanceState);

            setContentView(R.layout.web_container);

            mWebContainer = (FrameLayout) findViewById(R.id.web_container);
            mWebView = new WebView(getApplicationContext());
//            mLoadingProgress.setMax(100);
            mWebContainer.addView(mWebView);
            mWebView.getSettings().setSupportZoom(true); //设置可以支持缩放
//            webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
            mWebView.loadUrl(url);

            // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    //设置加载进度条
                    view.setWebChromeClient(new WebChromeClientProgress());
                    return true;
                }

            });
        }
        private class WebChromeClientProgress extends WebChromeClient {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (mLoadingProgress != null) {
                    mLoadingProgress.setProgress(progress);
                    if (progress == 100) mLoadingProgress.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, progress);
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();

            destroyWebView();
        }

        public void destroyWebView() {

            mWebContainer.removeAllViews();

            if(mWebView != null) {
                mWebView.clearHistory();
                mWebView.clearCache(true);
                mWebView.loadUrl("about:blank");
                mWebView.pauseTimers();
                mWebView = null;
            }

        }

}


