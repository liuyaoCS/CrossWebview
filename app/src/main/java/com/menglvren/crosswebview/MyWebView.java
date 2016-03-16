package com.menglvren.crosswebview;

import android.content.Context;
import android.os.Build;
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
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.JELLY_BEAN){
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
                Log.e("ly", "enable cross-domain error");
                e.printStackTrace();
            }
        }else if(Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT){
            try {
                Field webviewclassic_field = WebView.class.getDeclaredField("mProvider");
                webviewclassic_field.setAccessible(true);
                Object webviewclassic = webviewclassic_field.get(this);
                Field webviewcore_field = webviewclassic.getClass().getDeclaredField("mWebViewCore");
                webviewcore_field.setAccessible(true);
                Object mWebViewCore = webviewcore_field.get(webviewclassic);
                Field nativeclass_field = webviewclassic.getClass().getDeclaredField("mNativeClass");
                nativeclass_field.setAccessible(true);
                Object mNativeClass = nativeclass_field.get(webviewclassic);
                Method method = mWebViewCore.getClass().getDeclaredMethod(
                        "nativeRegisterURLSchemeAsLocal",
                        new Class[] { int.class, String.class });
                method.setAccessible(true);
                method.invoke(mWebViewCore, mNativeClass, "http");
                method.invoke(mWebViewCore, mNativeClass, "https");
            } catch (Exception e) {
                Log.e("ly", "enable cross-domain error");
                e.printStackTrace();
            }
        }else{
            Log.d("ly","not 4.* system,please enable cross main by others method,e.g. CORS");
        }

    }

}
