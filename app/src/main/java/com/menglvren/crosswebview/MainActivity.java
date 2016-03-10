package com.menglvren.crosswebview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends Activity {
    private EditText url;
    private Button search;
    private MyWebView web;

    private final String home="https://wap.baidu.com/";
    private final String local="file:///android_asset/test.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }
    private void initView(){
        url= (EditText) findViewById(R.id.url);
        search= (Button) findViewById(R.id.search);
        web= (MyWebView) findViewById(R.id.web);


        url.setText(home);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to = url.getText().toString();
                if (!TextUtils.isEmpty(to)) {
                    web.loadUrl(to);
                }
            }
        });

        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDatabaseEnabled(true);
        web.getSettings().setDatabasePath(this.getCacheDir().getAbsolutePath());
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            web.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        web.enableCrossDomain();
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                view.loadUrl("javascript:" + ajaxAction());
            }
        });
        web.setWebChromeClient(new WebChromeClient());
    }

    private String ajaxAction() {

        String jsStr = "var xmlhttp;" +
                "var url=\"http://192.168.67.164/index.html\";" +
                "function ajax_request(){" +
                    "xmlhttp=new XMLHttpRequest();" +
                    "xmlhttp.onreadystatechange = ajax_call_back;" +
                    "xmlhttp.open(\"GET\",url,true);" +
                    "xmlhttp.send();" +
                "}" +
                "function ajax_call_back(){" +
                    "if (xmlhttp.readyState == 4) {" +
                        "if (xmlhttp.status == 200) {" +
                        "  var str = xmlhttp.responseText;" +
                        "  alert(\"return data:\"+str);" +
                        "}" +
                        "else if(xmlhttp.status == 404){" +
                        "  alert(\"res not exits\");" +
                        "}" +
                        "else {" +
                        "  alert(\"Ajax request err,status:\"+xmlhttp.status);" +
                        "}" +
                    "}" +
                    "else{" +
                        "alert(\"state:\"+xmlhttp.readyState);" +
                    "}" +
                "}"+
                "ajax_request();";


        return jsStr;
    }

}
