package com.mobisquid.mobicash.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.Login;
import com.mobisquid.mobicash.model.LoginMailObj;
import com.mobisquid.mobicash.model.Transactions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by mobicash on 3/1/17.
 */

public class SendEmailService extends Service {
    Vars vars;
    String location=null;
    String mylocation =null;
    String email,mobile,fullname,sendcancelmsg,myip;
    String country;
    String[] countryCode;
    String[] Allcountryies;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("=====Service oncret====");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         vars = new Vars(this);
        countryCode= this.getResources().getStringArray(R.array.option_array_code);
        Allcountryies = this.getResources().getStringArray(R.array.option_array);
        vars.log("service=========onStart==");
        if (intent == null) {
            return START_STICKY;
        }else if (intent != null) {
            Bundle extrasU = intent.getExtras();
            if (extrasU != null) {
                sendcancelmsg = extrasU.getString("sendcancelmsg");
                email = extrasU.getString("email");
                myip =extrasU.getString("myip");
                mobile =  extrasU.getString("mobile");
                vars.log("service=====email===="+email);
                if(mobile!=null){
                    fullname= extrasU.getString("fullname");
                    RegisterToken(fullname,mobile);
                }
                if(email!=null){
                    getIPAddress();
                }else if(sendcancelmsg!=null){
                    sendCancelMessage(sendcancelmsg);
                }else if(myip!=null){
                    countrycode(myip);
                }

            }
        }

        final String intentAction = intent.getAction();
        if ("action_log_out".equals(intentAction)) {
            vars.log("=======service=====email");

            //Toast.makeText(getApplicationContext(),"logout="+Utils.isAppIsInBackground(this),Toast.LENGTH_LONG).show();
        }

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("=====Service down====");
    }
    public void countrycode(String ip){

        String params[] ={};
        String value[] ={};
        String url ="http://api.ipinfodb.com/v3/ip-city/?key=baf504d8f523141f935087719d5bc4a79c31636309b8b8e02ca3d13c5f15cad8&ip="+ip+"&format=json";
        ConnectionClass.ConnectionClass(this, url, params, value, "loc", new ConnectionClass.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                vars.log("JSON======"+result);
                if(result!=null){
                    try{
                        LocationDetails loc = new Gson().fromJson(result,LocationDetails.class);
                        if(loc.getCountryName().length()>2 && loc.getCityName().length()>2){
                            country = loc.getCountryName();
                            vars.log("Got one LOC========="+ country);
                            String code = countryCode[Arrays.asList(Allcountryies).indexOf(country)];
                            Log.e("CODE==","YOUR CODE+++++"+code);
                            if(vars.country_code==null) {
                                vars.edit.putString("country_code", code);
                                vars.edit.putString("financial_server", Utils.FincialServer(code));
                                vars.edit.apply();
                            }


                            stopService(new Intent(SendEmailService.this, SendEmailService.class));

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else{
                    location= null;
                }

            }
        });

    }
    public String location(String ip){
        location =null;
        String params[] ={};
        String value[] ={};
        String url ="http://api.ipinfodb.com/v3/ip-city/?key=baf504d8f523141f935087719d5bc4a79c31636309b8b8e02ca3d13c5f15cad8&ip="+ip+"&format=json";
        ConnectionClass.ConnectionClass(this, url, params, value, "loc", new ConnectionClass.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                vars.log("JSON======"+result);
                if(result!=null){
                    try{
                        LocationDetails loc = new Gson().fromJson(result,LocationDetails.class);
                        if(loc.getCountryName().length()>2 && loc.getCityName().length()>2){
                            location = loc.getCountryName()+","+loc.getCityName();
                            vars.log("DEVICE LOC===="+location);
                            vars.log("DEVICE==="+Utils.getDeviceName());
                            Sendmail(vars.fullname,Utils.getDeviceName(),location,email);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else{
                    location= null;
                }

            }
        });
        return location;
    }
    public String getIPAddress() {
        ConnectionClass.FreeString(this, "http://v4.ipv6-test.com/api/myip.php", "location", new ConnectionClass.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                vars.log("Ip==="+result);
                if(validIP(result)){
                    mylocation =location(result);

                }else {
                    mylocation =null;
                }
            }
        });
        return mylocation;
    }

    public static boolean validIP (String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    public class LocationDetails{
        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        String latitude;
        String countryName;
        String cityName;
        String longitude;
    }
    private void Sendmail(String name,String device,String location,String email){

        String url =Globals.SOCILA_SERV+"entities.chat/sendmail";
        try {
            JSONObject json = new JSONObject();
            json.put("devicename",device);
            json.put("email",email);
            json.put("location",location);
            json.put("name",name);

            ConnectionClass.JsonString(Request.Method.POST, this, url, json, "Emailsend", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    vars.log("EMAIL RESULTS========"+result);
                    stopService(new Intent(SendEmailService.this, SendEmailService.class));
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void RegisterToken(String fullnmae, final String mobile) {

        String url = "https://support.mobisquid.com/webresources/entities.users";
        try {
            JSONObject json = new JSONObject();
            json.put("fullname", fullnmae);
            json.put("token", vars.token);
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
                    updateUser(vars.token,vars.chk,mobile,"username");
                   // stopService(new Intent(SendEmailService.this, SendEmailService.class));
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
                    vars.log("Firebase reg===========" + result);
                    stopService(new Intent(SendEmailService.this, SendEmailService.class));
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void sendCancelMessage(String gsonstring){
        // Alerter.showdialog(getActivity());
        Transactions trans = new Gson().fromJson(gsonstring,Transactions.class);
        String url ="https://support.mobisquid.com/webresources/entities.transactions/edittrans";
        try{
            JSONObject json = new JSONObject(new Gson().toJson(trans));
            vars.log("JSON==="+json.toString());
            ConnectionClass.JsonString(Request.Method.PUT, this, url, json, "PAYMENR", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    // Alerter.stopdialog();
                    JSONObject reader = null;
                    try {
                        reader = new JSONObject(result);
                        if(reader.getString("error").equalsIgnoreCase("success")){
                            vars.log("service========cancelled============"+result);

                        }else{
                            //Alerter.Error(getActivity(),"ERROR OCCURRED",reader.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    stopService(new Intent(SendEmailService.this, SendEmailService.class));
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
