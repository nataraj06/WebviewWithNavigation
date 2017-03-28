package com.android.webviewnavigation;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class CustomWebView extends WebView {
    private OnScrollChangedCallback mOnScrollChangedCallback;

    public CustomWebView(final Context context) {
        super(context);
    }

    public CustomWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(final int left, final int top, final int previousLeft, final int previousTop) {
        super.onScrollChanged(left, top, previousLeft, previousTop);
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(top, previousTop);
        }
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    public static interface OnScrollChangedCallback {
        public void onScroll(int top, int previousTop);
    }
}