package com.mobisquid.mobicash.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ajay on 28/9/15.
 */
public class SyncService extends Service {
    static final String TAG = SyncService.class.getSimpleName();

    private static final Object sSyncAdapterLock = new Object();
    private static SyncAdapter mSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.i(TAG,"Sync Service created.");
        synchronized (sSyncAdapterLock){
            if(mSyncAdapter == null){
                mSyncAdapter = new SyncAdapter(getApplicationContext(),true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"Sync Service binded.");
        return mSyncAdapter.getSyncAdapterBinder();
    }
}
