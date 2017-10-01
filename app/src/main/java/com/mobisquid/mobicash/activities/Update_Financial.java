package com.mobisquid.mobicash.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Update_Financial extends AppCompatActivity {
    Vars vars;
    Bundle extras;
    String name ,dob,myear,mmonth,mday;

    EditText year,month,day,register_id,register_pin;
    TelephonyManager tel;
    ProgressDialog progressDialog;
    Alerter alerter;
    LinearLayout main_layout;
    static final String TAG = Update_Financial.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        vars = new Vars(this);
        alerter = new Alerter(this);
        super.onCreate(savedInstanceState);
        tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        setContentView(R.layout.activity_update__financial);
        main_layout =(LinearLayout) findViewById(R.id.main_layout);
        Utils.hidekBoard(findViewById(R.id.main_layout),this);

        year =(EditText) findViewById(R.id.year);
        month =(EditText) findViewById(R.id.month);
        day =(EditText) findViewById(R.id.day);
        register_id =(EditText) findViewById(R.id.register_idnumber);
        register_pin =(EditText) findViewById(R.id.register_pin);
       // editText_todate.setFocusable(false);
        extras = getIntent().getExtras();
        if(extras!=null){
            name = extras.getString("name");
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
/*
        editText_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getDate(Update_Financial.this, new Utils.StringCallback() {
                    @Override
                    public void onSuccess(String result) {
                        editText_todate.setText(result);
                    }
                });

            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.needhelp:
                if(Utils.checklogin(this)){
                //    Intent setting = new Intent(this, Help.class);
                //    startActivity(setting);
                }else{
                    Toast.makeText(this, "Login or registration required", Toast.LENGTH_LONG).show();
                }

                return true;
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void submit(View view) {
        if(year.getText().toString().isEmpty()||month.getText().toString().isEmpty()||
                day.getText().toString().isEmpty()||year.getText().toString().length()!=4
                || Integer.valueOf(month.getText().toString().trim())>12
                || Integer.valueOf(day.getText().toString().trim())>31){
          Toast.makeText(this,"invalid date of birth",Toast.LENGTH_LONG).show();
        }
        else if(register_id.getText().length()<2){
            register_id.setError("Invalid id number");
        }
        else if(register_pin.getText().length()<4){
            register_pin.setError("Wrong pin provided");

        }else{
            myear = year.getText().toString().trim();

            if(String.valueOf(month.getText().toString().charAt(0)).equalsIgnoreCase("0")){
                mmonth = String.valueOf(month.getText().toString().charAt(1));
            }else{
                mmonth = month.getText().toString().trim();
            }

            if(String.valueOf(day.getText().toString().charAt(0)).equalsIgnoreCase("0")){
                mday = String.valueOf(day.getText().toString().charAt(1));
            }else{
                mday = day.getText().toString().trim();
            }

            dob = myear+"-"+mmonth+"-"+mday;
            vars.log("dob====="+dob);
            alter();
        }
    }

    public void cancel(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void alter() {
        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setCancelText("Cancel")
                .setConfirmText("Update")
                .setTitleText("Please note")
                .setContentText("By clicking update only this device will be allowed to carry out your financial transactions")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        update_imei();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();

    }

    public void update_imei(){
        progressDialog = ProgressDialog.show(this,"Updating device","Please wait...",true,false);
        String[] parameter ={"idNo","pinCode","dob","imei"};
        String[] values ={register_id.getText().toString().trim(),register_pin.getText().toString().trim(),
                dob,tel.getDeviceId()};
        String URL= vars.financial_server+"ClientChangeImei.php";

        ConnectionClass.ConnectionClass(this, URL, parameter, values, "Update", new ConnectionClass.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG,"RESULTS====="+result);
                final TransactionObj gsonMessage = new Gson().fromJson(result, TransactionObj.class);
                if (gsonMessage.getResult().equalsIgnoreCase("Success")) {
                        vars.edit.putString("active", "activenow");
                      //  vars.edit.putString("social", "social");
                    vars.edit.putString("email", gsonMessage.getExtra1());
                    vars.edit.putString("imei",  tel.getDeviceId());
                    vars.edit.putString("fullname", gsonMessage.getDetails());
                    vars.edit.putString("profile_number", gsonMessage.getClient());
                    vars.edit.putString("mobile", gsonMessage.getClient());
                    Utils.isFinance(Update_Financial.this,true);
                    if (gsonMessage.getExtra2() != null) {
                        vars.edit.putString("qrcode", gsonMessage.getExtra2());
                    }
                        vars.edit.apply();

                    progressDialog.dismiss();
                    if (gsonMessage.getExtra2() == null) {
                        Intent mainpage = new Intent(Update_Financial.this, QrcodeUpdates.class);
                        startActivity(mainpage);
                        finish();

                    }else {

                        Utils.home(Update_Financial.this);
                    }

                    //	alerterSuccessSimple("Success", "Enjoy MobiSquid");


                } else {
                    progressDialog.dismiss();
                    alerter.alerterSuccessSimple(Update_Financial.this,"Error", gsonMessage.getError());
                    //}

                }

            }
        });


    }
}
