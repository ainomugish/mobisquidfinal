package com.mobisquid.mobicash.utils;

/**
 * Created by mobicash on 5/10/16.
 */
import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "mobisquid_notifiyer";

    // All Shared Preferences Keys

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void addNotification(String thread,String notification) {

        // get old notifications
        String oldNotifications = getNotifications(thread);

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(thread, oldNotifications);
        editor.commit();
    }

    public String getNotifications(String thread) {
        return pref.getString(thread, null);
    }

    public void clear(String thread) {
        editor.remove(thread);
        editor.apply();
    }
}