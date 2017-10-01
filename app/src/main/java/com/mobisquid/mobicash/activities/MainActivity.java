package com.mobisquid.mobicash.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.mobisquid.mobicash.dbstuff.Apps;
import com.mobisquid.mobicash.model.NewApp;
import com.mobisquid.mobicash.utils.AutoLogOutService;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;
import com.splunk.mint.Mint;

public class MainActivity extends AppCompatActivity {
   Vars vars ;
    Bundle extras;
    String newapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(this);
        //vars.edit.putString("mobile","27843761111");
        //vars.edit.putString("country_code","277");
        //vars.edit.apply();
        extras = getIntent().getExtras();
        Mint.initAndStartSession(this.getApplication(), "f9c0336d");
        if(Apps.listAll(Apps.class).isEmpty()){
            Apps app = new Apps(true,true,true,false,true,false,true);
            app.save();
        }

        if (vars.social == null && vars.active==null) {
            Intent chk = new Intent(this, RegistrationActivity.class);
            startActivity(chk);
            finish();
        }else{

            Intent main = new Intent(this,MainScreen.class);
            //main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            if(extras!=null) {
                main.putExtras(extras);
            }
           // Intent startServiceIntent = new Intent(this, AutoLogOutService.class);
           // startService(startServiceIntent);
           startActivity(main);
            finish();
        }


       /* Intent main = new Intent(this,MainScreen.class);
        if(extras!=null){

            newapp = extras.getString("newapp");
            vars.log("Mainactivity extras not null==============="+newapp);
            NewApp save = new Gson().fromJson(newapp,NewApp.class);
            if(save.getType().equalsIgnoreCase("yes")){
                vars.edit.putString("active", "activenow");
            }
            vars.edit.putString("userId", save.getUserid());
            vars.edit.putString("social", "social");
            vars.edit.putString("country_code", save.getCode());
            vars.edit.putString("imei",save.getImei());
            vars.edit.putString("profile_number", save.getMobile());
            vars.edit.putString("mobile", save.getMobile());
            vars.edit.putString("fullname", save.getName());
            vars.edit.putString("financial_server", Utils.FincialServer(save.getCode()));
            vars.edit.putString("qrcode",save.getQrcode());
            vars.edit.apply();

          //  main.putExtra("newapp",newapp);

        }
        Intent startServiceIntent = new Intent(this, AutoLogOutService.class);
        startService(startServiceIntent);
        startActivity(main);
        finish();*/


    }
    }
