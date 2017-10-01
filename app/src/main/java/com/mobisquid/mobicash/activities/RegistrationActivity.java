package com.mobisquid.mobicash.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.fragments.First_Fragment;
import com.mobisquid.mobicash.fragments.Login_Fragment;
import com.mobisquid.mobicash.fragments.AppFragment;
import com.mobisquid.mobicash.fragments.TourFragment;
import com.mobisquid.mobicash.mqtt.GcmIntentService;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.SendEmailService;
import com.mobisquid.mobicash.utils.Vars;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistrationActivity extends AppCompatActivity {
    Toolbar toolbar;
    Bundle extras;
    Vars vars;
    static int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE=3;
    TelephonyManager telephonyManager;
    String token;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        vars = new Vars(this);
        setContentView(R.layout.activity_registration);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("Mobisquid");
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= 23){
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            } else {
                getDeviceImei();
            }

        }
        if(extras!=null && extras.getString("login")!=null){
            if (savedInstanceState == null) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide slideTransition = new Slide(Gravity.LEFT);
                    slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                    Login_Fragment firstfragment = new Login_Fragment();
                    firstfragment.setReenterTransition(slideTransition);
                    firstfragment.setExitTransition(slideTransition);
                    firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, firstfragment, "MAIN")
                            .commit();
                } else {
                    System.out.println("===========t============");
                    Login_Fragment firstfragment = new Login_Fragment();
                    firstfragment.setArguments(getIntent().getExtras());
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.fragment_container, firstfragment, "MAIN").commit();
                }
            }

        }else if(extras!=null && extras.getString("financial")!=null){
            if (savedInstanceState == null) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide slideTransition = new Slide(Gravity.LEFT);
                    slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                    TourFragment firstfragment = new TourFragment();
                    firstfragment.setReenterTransition(slideTransition);
                    firstfragment.setArguments(extras);
                    firstfragment.setExitTransition(slideTransition);
                    firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, firstfragment, "MAIN")
                            .commit();
                } else {
                    System.out.println("===========t============");
                    TourFragment firstfragment = new TourFragment();
                    firstfragment.setArguments(extras);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, firstfragment, "MAIN").commit();
                }
            }
        } else{
            if (savedInstanceState == null) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide slideTransition = new Slide(Gravity.LEFT);
                    slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                    First_Fragment firstfragment = new First_Fragment();
                    firstfragment.setReenterTransition(slideTransition);
                    firstfragment.setExitTransition(slideTransition);
                    firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, firstfragment, "MAIN")
                            .commit();
                } else {
                    System.out.println("===========t============");
                    First_Fragment firstfragment = new First_Fragment();
                    firstfragment.setArguments(getIntent().getExtras());
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.fragment_container, firstfragment, "MAIN").commit();
                }
            }
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                vars.log("========onReceive========");
                // checking for type intent filter
                if (intent.getAction().equals(Globals.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    token = intent.getStringExtra("token");
                    vars.edit.putString("token",token);
                    vars.edit.apply();
                    //Toast.makeText(RegistrationActivity.this, "GCM registration token: " + token, Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Globals.SENT_TOKEN_TO_SERVER)) {

                }
            }
        };
        if (token == null) {
            vars.log("======checkPlayServices========");
            registerGCM();
        }


    }
    private void registerGCM() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }
    @Override
    public void onBackPressed() {
        if(Globals.whichuser.equalsIgnoreCase("welcome")){
            super.onBackPressed();
        }else {
            super.onBackPressed();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getDeviceImei();
        }else{
            new SweetAlertDialog(RegistrationActivity.this,SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Please note!!")
                    .setContentText("Financial services won't work with out getting your device id. Please allow permission to get your device id")
                    .setCancelText("Don't allow")
                    .setConfirmText("Yes,allow")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                                            MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                                } else {
                                    getDeviceImei();
                                }
                            }
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            onBackPressed();
                        }
                    }).show();
        }
    }
    private String getDeviceImei() {

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

    }
    @Override
    protected void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

    }
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Globals.REGISTRATION_COMPLETE));

        if (vars.social == null && vars.active==null) {
        }else{
            if(extras!=null && extras.getString("login")!=null){

            }else if(extras!=null && extras.getString("financial")!=null){

            } else {
                Intent main = new Intent(this, MainActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(main);
                finish();
            }
        }
        if(vars.country_code==null){
            //get counrtrtrt
            getIPAddress();
        }

    }
    public void getIPAddress() {
        ConnectionClass.FreeString(this, "http://v4.ipv6-test.com/api/myip.php", "location", new ConnectionClass.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                vars.log("Ip==="+result);
                if(SendEmailService.validIP(result)){
                    Intent sendmail = new Intent(RegistrationActivity.this, SendEmailService.class);
                    sendmail.putExtra("myip",result);
                    startService(sendmail);


                }else {

                }
            }
        });

    }
}
