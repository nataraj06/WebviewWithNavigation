package com.android.webviewnavigation;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CustomWebView mWebView;
    private ImageButton btnPrev, btnNext, btnRefresh, btnClose;
    private LinearLayout footerLayout;
    private Animation bottomUp, bottomDown;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (CustomWebView) findViewById(R.id.webView_container);

        bottomUp = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
        bottomDown = AnimationUtils.loadAnimation(this, R.anim.bottom_down);

        btnPrev = (ImageButton) findViewById(R.id.prev_button);
        btnPrev.setOnClickListener(this);

        btnNext = (ImageButton) findViewById(R.id.next_button);
        btnNext.setOnClickListener(this);

        btnRefresh = (ImageButton) findViewById(R.id.refresh_button);
        btnRefresh.setOnClickListener(this);

        btnClose = (ImageButton) findViewById(R.id.close_button);
        btnClose.setOnClickListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.webViewProgressBar);

        footerLayout = (LinearLayout) findViewById(R.id.footer_layout);
        checkForNavigation();

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(mWebView, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
                checkForCloseOrRefresh(false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(mWebView, url);
                mProgressBar.setVisibility(View.GONE);
                checkForCloseOrRefresh(true);
                checkForNavigation();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
        mWebView.loadUrl("http://www.google.com");


        //TODO mWebview.setOnScrollChangeListener is added from API level 23
       /* mWebView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int top, int i2, int previousTop) {
                if (top > previousTop && footerLayout.getVisibility() == View.GONE) {
                    footerLayout.startAnimation(bottomUp);
                    footerLayout.setVisibility(View.VISIBLE);
                } else if (top <= previousTop && footerLayout.getVisibility() == View.VISIBLE) {
                    footerLayout.startAnimation(bottomDown);
                    footerLayout.setVisibility(View.GONE);
                }
            }
        });*/


        //TODO Supporting scroll listener for the webview in lower end devices
        mWebView.setOnScrollChangedCallback(new CustomWebView.OnScrollChangedCallback() {
            public void onScroll(int top, int previousTop) {
                if (top < previousTop && footerLayout.getVisibility() == View.GONE) {
                    footerLayout.startAnimation(bottomUp);
                    footerLayout.setVisibility(View.VISIBLE);
                } else if (top >= previousTop && footerLayout.getVisibility() == View.VISIBLE) {
                    footerLayout.startAnimation(bottomDown);
                    footerLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prev_button:
                mWebView.goBack();
                break;
            case R.id.next_button:
                mWebView.goForward();
                break;
            case R.id.close_button:
                mWebView.stopLoading();
                break;
            case R.id.refresh_button:
                mWebView.reload();
                break;
        }
    }

    private void checkForCloseOrRefresh(boolean isPagedLoaded) {
        btnRefresh.setVisibility(isPagedLoaded ? View.VISIBLE : View.GONE);
        btnClose.setVisibility(isPagedLoaded ? View.GONE : View.VISIBLE);

    }

    private void checkForNavigation() {
        btnPrev.setEnabled(mWebView.canGoBack());
        btnPrev.setColorFilter(ContextCompat.getColor(this, mWebView.canGoBack() ? R.color.btn_black : R.color.btn_grey), PorterDuff.Mode.SRC_IN);

        btnNext.setEnabled(mWebView.canGoForward());
        btnNext.setColorFilter(ContextCompat.getColor(this, mWebView.canGoForward() ? R.color.btn_black : R.color.btn_grey), PorterDuff.Mode.SRC_IN);
    }
}
