package com.example.webapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

public class MainActivity extends AppCompatActivity {
    Button b1;
    EditText ed1;
    Intent mServiceIntent;
    private ClipData clipdata;
    private ClipboardManager mgr;
    String url=new String();


    private WebView wv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        b1=(Button)findViewById(R.id.button);
        ed1=(EditText)findViewById(R.id.editText);

        //background service
        YourService mYourService = new YourService();
        mServiceIntent = new Intent(this, mYourService.getClass());
        if (!isMyServiceRunning(mYourService.getClass())) {
            startService(mServiceIntent);
        }
        //clipboard activity
        Intent cl=new Intent(MainActivity.this,ClipboardMonitorService.class);
        //startService(cl);
        ed1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    mgr = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    assert mgr != null;
                    if ((mgr.hasPrimaryClip())) {
                        if ((mgr.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                            final ClipData.Item item = mgr.getPrimaryClip().getItemAt(0);
                            ed1.setText(item.getText().toString());
                            Toast.makeText(getApplicationContext(), "URL Pasted",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        });


        //webView
        wv1=(WebView)findViewById(R.id.webView);
        wv1.setWebViewClient(new MyBrowser());


        b1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                ClipboardActivity();
                String url = ed1.getText().toString();
                wv1.getSettings().setLoadsImagesAutomatically(true);
                wv1.getSettings().setJavaScriptEnabled(true);
                wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                wv1.loadUrl(url);

            }
        });

    }
    public void ClipboardActivity(){

    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }
    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        super.onDestroy();
    }
    //web view
    private static class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}