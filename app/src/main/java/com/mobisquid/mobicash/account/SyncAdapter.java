package com.mobisquid.mobicash.account;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by ajay on 28/9/15.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    static final String TAG = SyncAdapter.class.getSimpleName();

    private Context mContext;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        Log.i(TAG,"Sync Adapter created.");
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG,"Sync Adapter called.");
    }
}
