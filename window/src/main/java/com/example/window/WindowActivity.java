package com.example.window;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 包含一个window
 */
public class WindowActivity extends AppCompatActivity {
    private static final String TAG = "window";
    private WindowManager wm;
    private View mWindowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(intent);
        }

       mWindowLayout= LayoutInflater.from(this).inflate(R.layout.window,null);
        Log.v(TAG, "开始");

        Intent intent = new Intent();
        intent.setClassName("com.example.activity","com.example.activity.MyService");
        intent.setAction("com.example.server.aidl");
        //window
        this.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(TAG, "onServiceConnected: service " + service);
                IMyAidlInterface iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
                try {
                    iMyAidlInterface.monitorActivityShow((IActivtyShowCallback) new ActivityShowCallback());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }

    public class ActivityShowCallback extends IActivtyShowCallback.Stub {
        @Override
        public void addssg(IBinder ssg) throws RemoteException {
            //icon.hide
            Log.v(TAG, "window 拿到ssg后进行同步和icon隐藏");
            ISurfaceSyncGroup.Stub.asInterface(ssg);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        lp.format = PixelFormat.RGBA_8888;
        lp.width = 200;
        lp.height =200;
        lp.gravity= Gravity.START|Gravity.TOP;

        wm.addView(mWindowLayout,lp);
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
        wm.removeView(mWindowLayout);
    }
}