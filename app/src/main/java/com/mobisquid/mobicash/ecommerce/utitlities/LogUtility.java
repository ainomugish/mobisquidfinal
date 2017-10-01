package com.mobisquid.mobicash.ecommerce.utitlities;

import android.util.Log;

/**
 * Created by Administrator on 2/23/2017.
 */

public final class LogUtility {

    private static final boolean SHOULD_SHOW_LOGS = true;
    private static final String TAG = "eccc";

    public static final void errorLog(String message) {
        if (SHOULD_SHOW_LOGS)
            Log.e(TAG, message);
    }



    public static final void debugLog(String message) {
        if (SHOULD_SHOW_LOGS)
            Log.d(TAG, message);
    }



    public static final void horribleSituationLog(String message) {
        if (SHOULD_SHOW_LOGS)
            Log.wtf(TAG, message);
    }


}
