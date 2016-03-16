package com.menglvren.crosswebview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    private EditText et_url;
    private Button search;
    private Button back;
    private MyWebView web;

    private static final String INJECTION_TOKEN = "**injection**";

    private final String home="http://m.weibo.cn";

    private final String ajax="file:///android_asset/ajax.html";
    private final String jsonp="file:///android_asset/jsonp.html";
    private final String form="file:///android_asset/form.html";

    private final String video="http://192.168.66.209/video.html";
    private final String video2="http://192.168.69.25/video-plugin/demoX1.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }
    private void initView(){
        et_url = (EditText) findViewById(R.id.url);
        search= (Button) findViewById(R.id.search);
        back=(Button) findViewById(R.id.back);
        web= (MyWebView) findViewById(R.id.web);


        et_url.setText(home);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to = et_url.getText().toString();
                if (!TextUtils.isEmpty(to)) {
                    web.loadUrl(to);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(web.canGoBack()){
                    web.goBack();
                }
            }
        });

        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDatabaseEnabled(true);
        web.getSettings().setDatabasePath(this.getCacheDir().getAbsolutePath());
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        web.getSettings().setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            web.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        web.enableCrossDomain();
        web.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                WebResourceResponse response = super.shouldInterceptRequest(view, url);
//                if(et_url != null && et_url.contains(INJECTION_TOKEN)) {
//                    String assetPath = et_url.substring(et_url.indexOf(INJECTION_TOKEN) + INJECTION_TOKEN.length(), et_url.length());
//                    try {
//                        response = new WebResourceResponse(
//                                "application/javascript",
//                                "UTF8",
//                                MainActivity.this.getAssets().open(assetPath)
//                        );
//                    } catch (IOException e) {
//                        e.printStackTrace(); // Failed to load asset file
//                    }
//                }
                return response;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                et_url.setText(url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.contains("weibo.com") || url.contains("weibo.cn")) {
                    view.loadUrl("javascript:" + loadRemote());
                }

//                view.loadUrl("javascript:" + ajaxAction());
//                view.loadUrl("javascript:" + jqueryInit());
//                view.loadUrl("javascript:" + jsonpAction());
            }
        });
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }
        });
        web.loadUrl(home);
    }

    private String loadLocal(){
        String js = "var newscript = document.createElement(\"script\");";
        js += "newscript.src=\"file:///android_asset/js/test.js\";";
        js += "document.head.appendChild(newscript);";
        return js;
    }
    private String loadRemote(){

        String js="var newlink = document.createElement(\"link\");";
        js +="newlink.setAttribute(\"rel\",\"stylesheet\");";
        js +="newlink.setAttribute(\"type\",\"text/css\");";
        js +="newlink.setAttribute(\"href\",\"http://123.57.54.83:5000/static/embed_mobile.css\");";
        js += "document.head.appendChild(newlink);";

        js += "var newscript1 = document.createElement(\"script\");";
        js += "newscript1.src=\"http://123.57.54.83:5000/static/jquery.min.js\";";
        js += "document.head.appendChild(newscript1);";

        js += "var newscript = document.createElement(\"script\");";
        js += "newscript.src=\"http://123.57.54.83:5000/static/embed_mobile.js\";";
        js += "document.head.appendChild(newscript);";
        return js;
    }
    private String ajaxAction() {

        String jsStr = "var xmlhttp;" +
                "var et_url=\"http://192.168.66.209/server2.php\";" +
                "function ajax_request(){" +
                    "xmlhttp=new XMLHttpRequest();" +
                    "xmlhttp.onreadystatechange = ajax_call_back;" +
                    "xmlhttp.open(\"GET\",et_url,true);" +
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
    private String jsonpAction() {

        String jsStr = "$.getJSON(\"http://192.168.64.55/jsonp.php?callback=?\",{" +
                                    "foo: \"bar\"," +
                                    "format: \"json\"" +
                                    "},function(data) {" +
                                    "    alert(data.format);" +
                                    "  });  ";

        return jsStr;
    }
    private String jqueryInit(){
        String js = "var newscript = document.createElement(\"script\");";
        js += "newscript.src=\"http://code.jqueryInit.com/jqueryInit-1.5.js\";";
        js += "document.head.appendChild(newscript);";
        return js;
    }

}
