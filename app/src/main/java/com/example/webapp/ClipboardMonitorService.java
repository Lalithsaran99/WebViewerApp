package com.example.webapp;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;

public class ClipboardMonitorService extends Service {

    private ClipboardManager mClipboardManager;

    @Override
    public void onCreate() {
        super.onCreate();

        mClipboardManager =
                (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        assert mClipboardManager!=null;
        mClipboardManager.addPrimaryClipChangedListener(
                mOnPrimaryClipChangedListener);

        System.out.println("Service started running..");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mClipboardManager != null) {
            mClipboardManager.removePrimaryClipChangedListener(
                    mOnPrimaryClipChangedListener);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private ClipboardManager.OnPrimaryClipChangedListener mOnPrimaryClipChangedListener =
            new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {

                    String charSequence = mClipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
                    System.out.println("Copied Link : ====================" + charSequence);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("copiedLink", charSequence);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            };



}
