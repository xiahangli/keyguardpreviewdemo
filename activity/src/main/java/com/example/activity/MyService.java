package com.example.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.window.IActivtyShowCallback;
import com.example.window.IMyAidlInterface;
import com.example.window.ISurfaceSyncGroup;

public class MyService extends Service {
    private static final String TAG = "window";
    public MyService() {
        Log.i(TAG, "MyService: 1111");
    }

    private Handler handler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "模拟1秒后Activity界面初始化完成准备第一帧绘制");
                try {
                    mCallback.addssg(iSurfaceSyncGroup.asBinder());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 1000);
        return new MyInterface();
    }


    private ISurfaceSyncGroup iSurfaceSyncGroup = new ISurfaceSyncGroup.Stub() {//模拟activity view拿到的viewrootimpl.getssg
        @Override
        public boolean addToSync(ISurfaceSyncGroup surfaceSyncGroup, boolean parentSyncGroupMerge) throws RemoteException {
            return false;
        }

        @Override
        public boolean onAddedToSyncGroup(IBinder parentSyncGroupToken, boolean parentSyncGroupMerge) throws RemoteException {
            return false;
        }
    };

    public class MyInterface extends  IMyAidlInterface.Stub {
        @Override
        public void monitorActivityShow(IActivtyShowCallback callback) throws RemoteException {
            mCallback = callback;
        }

    }

    private IActivtyShowCallback mCallback;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}