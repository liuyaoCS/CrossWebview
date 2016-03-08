package com.menglvren.crosswebview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/3/8 0008.
 */
public class MyWebView extends WebView {
    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void enableCrossDomain()
    {
        try{
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field.setAccessible(true);
            Object webviewcore=field.get(this);
            Method method=webviewcore.getClass().getDeclaredMethod("nativeRegisterURLSchemeAsLocal", String.class);
            method.setAccessible(true);
            method.invoke(webviewcore, "http");
            method.invoke(webviewcore, "https");
        }
        catch(Exception   e){
            Log.e("ly", "enable crossdomain error");
            e.printStackTrace();
        }
    }
}
