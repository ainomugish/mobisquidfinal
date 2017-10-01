package com.mobisquid.mobicash.mqtt;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.SendEmailService;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;


public class GcmIntentService extends IntentService {
    Vars vars ;
    private static final String TAG = GcmIntentService.class.getSimpleName();

    public GcmIntentService() {
        super(TAG);
    }

    public static final String KEY = "key";
    public static final String TOPIC = "topic";
    public static final String SUBSCRIBE = "subscribe";
    public static final String UNSUBSCRIBE = "unsubscribe";
    public static final String REGISTER = "register";


    @Override
    protected void onHandleIntent(Intent intent) {
        vars = new Vars(this);
        vars.log("========gcm onHandleIntent=======");
        String key = intent.getStringExtra(KEY);
        if(key!=null) {
            switch (key) {
                case SUBSCRIBE:
                    // subscribe to a topic
                    String topic = intent.getStringExtra(TOPIC);
                    subscribeToTopic(topic);
                    break;
                case UNSUBSCRIBE:
                    String topic1 = intent.getStringExtra(TOPIC);
                    unsubscribeFromTopic(topic1);
                    break;
                case REGISTER:
                    vars.log("==Registering===");
                    registerGCM();
                    break;
                default:
                    // if key is not specified, register with GCM
                    vars.log("==Registering==default=");
                    registerGCM();

            }
        }

    }

    /**
     * Registering with GCM and obtaining the gcm registration id
     */
    private void registerGCM() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = null;

        try {
            token = FirebaseInstanceId.getInstance().getToken();
            Log.e(TAG, "GCM Registration Token: " + token);
            // sending the registration id to our server
            if(vars.mobile!=null){
                sendRegistrationToServer(token,vars.mobile);
            }

         //   subscribeToTopic(token);
            sharedPreferences.edit().putBoolean(Globals.SENT_TOKEN_TO_SERVER, true).apply();
        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);

            sharedPreferences.edit().putBoolean(Globals.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Globals.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token,String mobile) {
            if(vars.mobile!=null && vars.fullname!=null) {
                String url = "https://support.mobisquid.com/webresources/entities.users";
                try {
                    JSONObject json = new JSONObject();
                    json.put("fullname", vars.fullname);
                    json.put("token", token);
                    json.put("app", "Mobisquid");

                    if(mobile!=null && String.valueOf(mobile.charAt(0)).equalsIgnoreCase("+")){
                        json.put("mobile", mobile.substring(1));

                    }else if(mobile!=null && !String.valueOf(mobile.charAt(0)).equalsIgnoreCase("+")){
                        json.put("mobile", mobile);
                    }

                    ConnectionClass.JsonString(Request.Method.POST, this, url, json, "FIREBASE", new ConnectionClass.VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            vars.log("Firebase reg===========" + result);
                            updateUser(token, vars.chk, vars.mobile, vars.username);

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

    }
    private void updateUser(String token, String chk, String mobile, String username) {

        String url = Globals.SOCILA_SERV+"entities.chat/edituser";
        try {
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("token", token);
            json.put("userid", chk);
            json.put("mobile", mobile);

            ConnectionClass.JsonString(Request.Method.PUT, this, url, json, "FIREBASETOKEN", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    vars.log("Firebase reg===========" + result);

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    /**
     * Subscribe to a topic
     */
    public void subscribeToTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    public void unsubscribeFromTopic(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }
}