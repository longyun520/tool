package com.zkys.operationtool.util;

import android.view.ViewGroup;
import android.webkit.WebView;

public final class WebViewLifecycleUtils {

    public static void onResume(WebView webView) {
        webView.onResume();
        webView.resumeTimers();
    }

    public static void onPause(WebView webView) {
        webView.onPause();
        webView.pauseTimers();
    }

    public static void onDestroy(WebView webView) {
        ((ViewGroup) webView.getParent()).removeView(webView);
        //清除历史记录
        webView.clearHistory();
        //停止加载
        webView.stopLoading();
        //加载一个空白页
        webView.loadUrl("about:blank");
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        //移除WebView所有的View对象
        webView.removeAllViews();
        //销毁此的WebView的内部状态
        webView.destroy();
    }
}