package com.nic.agmt.Activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nic.agmt.Constant.AppConstant;
import com.nic.agmt.Interface.WebAppInterface;
import com.nic.agmt.R;
import com.nic.agmt.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

/** Created By Dileep Kumar
 * **/

public class MainActivity extends AppCompatActivity  {

    WebView myWebView;
    WebSettings webSettings;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView url_text;
    int initial=0;
    private ProgressBar pgsBar;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        myWebView = (WebView) findViewById(R.id.webview);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        url_text = findViewById(R.id.url_text);

        pgsBar = (ProgressBar) findViewById(R.id.pBar);

        webSettings = myWebView.getSettings();
        webSettings.setSaveFormData(false);
        //myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
       // myWebView.addJavascriptInterface(new WebAppInterface(this), "Audio");
        myWebView.setWebChromeClient(new WebChromeClient() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //Required functionality here
                //return super.onJsAlert(view, url, message, result);
                int title = view.getDisplay().getDisplayId();
                Utils.showAlert(MainActivity.this,message,"alert",result);
                //result.cancel();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                Utils.showAlert(MainActivity.this,message,"confirm",result);
                //result.confirm();
                return true;
                //return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
               /*Utils.showAlert(MainActivity.this,message);
                result.cancel();
                return true;*/
                return super.onJsAlert(view, url, message, result);
            }


            @Override
            public void onPermissionRequest(PermissionRequest request) {
                //super.onPermissionRequest(request);
/*
                final String[] requestedResources = request.getResources();
                for (String r : requestedResources) {
                    if (r.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                        request.grant(new String[]{PermissionRequest.RESOURCE_AUDIO_CAPTURE});
                        //break;
                    }
                }
*/

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        request.grant(request.getResources());
                    }
                });


            }

            @Override
            public void onPermissionRequestCanceled(PermissionRequest request) {
                super.onPermissionRequestCanceled(request);
            }

            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    url_text.setText(view.getUrl().toString());
                    textColor(view.getUrl().toString());
                    pgsBar.setVisibility(View.VISIBLE);
                    pgsBar.setProgress(progress);
                    progressDialog.show();
                }
                if (progress == 100) {
                    pgsBar.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
            }

            @Override
            public boolean onJsTimeout() {
                return super.onJsTimeout();
            }
        });




        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMyPage(AppConstant.KEY_URL);
            }
        });
        loadMyPage(AppConstant.KEY_URL);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
    public void loadMyPage(String url){
        if(Utils.isOnline(this)) {
            myWebView.requestFocus();
            myWebView.getSettings().setLightTouchEnabled(true);
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.getSettings().setGeolocationEnabled(true);
            myWebView.getSettings().setAllowFileAccess(true);
            myWebView.getSettings().setAllowContentAccess(true);
            myWebView.setSoundEffectsEnabled(true);
            myWebView.clearCache(true);
            myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            myWebView.getSettings().setUseWideViewPort(true);
            if(initial ==1) {
                myWebView.loadUrl(myWebView.getUrl());
            }
            else {
                myWebView.loadUrl(url);
            }
            swipeRefreshLayout.setRefreshing(true);
            myWebView.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(MainActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                    //loadMyPage(AppConstant.KEY_URL);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    swipeRefreshLayout.setRefreshing(false);
                    initial=1;
                    /** This call inject JavaScript into the page which just finished loading.
                    */
                    //myWebView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");

                }


                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return super.shouldOverrideUrlLoading(view, request);
                }

            });
        }
        else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(1);
        val = val * 100d;
        return val.intValue();
    }

    public void textColor(String url){
        final SpannableStringBuilder sb = new SpannableStringBuilder(url);

// Span to set text color to some RGB value
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(0, 128, 0));

// Span to make text bold
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

// Set the text color for first 4 characters
        sb.setSpan(fcs, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

// make them also bold
        sb.setSpan(bss, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        url_text.setText(sb);
    }

}
