package com.mobisquid.mobicash.mqtt;

import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;


public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    private static final String TAG = MyInstanceIDListenerService.class.getSimpleName();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);

        Intent intent = new Intent(this, GcmIntentService.class);
        startService(intent);
    }
    private void sendRegistrationToServer(String token) {
        Vars vars = new Vars(this);
        vars.edit.putString("token",token);
        vars.edit.apply();
        RegisterToken(vars.mobile,token);
        updateUser(token, vars.chk, vars.mobile, vars.username);

    }
    private void RegisterToken(String mobile,String token) {

        String url = "https://support.mobisquid.com/webresources/entities.users/edituser";
        try {
            JSONObject json = new JSONObject();
            json.put("token", token);
            json.put("app", "Mobisquid");
            if(mobile!=null && String.valueOf(mobile.charAt(0)).equalsIgnoreCase("+")){
                json.put("mobile", mobile.substring(1));

            }else if(mobile!=null && !String.valueOf(mobile.charAt(0)).equalsIgnoreCase("+")){
                json.put("mobile", mobile);
            }

            ConnectionClass.JsonString(Request.Method.PUT, this, url, json, "FIREBASE", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.e("Myinstance ","Firebase reg===========" + result);


                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
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
                    Log.i("user","Firebase reg===========" + result);

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}