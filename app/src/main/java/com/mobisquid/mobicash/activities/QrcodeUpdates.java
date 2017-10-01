package com.mobisquid.mobicash.activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class QrcodeUpdates extends AppCompatActivity {
    Vars vars;
   // String qrcode;
    String encodedqrcode="";
    EditText pin;
    TextView qrc;
    TextInputLayout inp_pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(this);
        setContentView(R.layout.activity_qrcode_updates);
        Utils.hidekBoard(findViewById(R.id.main_layout),this);
        Utils.hidekBoard(findViewById(R.id.activity_qrcode_updates),this);

        inp_pin =(TextInputLayout) findViewById(R.id.inp_pin);
        qrc =(TextView) findViewById(R.id.qrc);
        pin = (EditText) findViewById(R.id.pin);
        if(vars.country_code.equalsIgnoreCase("27")){
            encodedqrcode=vars.country_code+"00"+"-"+ Utils.randomNumber(4)+"-"+Utils.randomNumber(4)+"-"+Utils.randomNumber(4);
        }else {
            encodedqrcode=vars.country_code+"0"+"-"+ Utils.randomNumber(4)+"-"+Utils.randomNumber(4)+"-"+Utils.randomNumber(4);
        }
        qrc.setText(encodedqrcode);
    }

    public void submit(View view) {
        if(pin.getText().toString().length()<3){
            inp_pin.setError("Invalid pin");
            Toast.makeText(this,"Invalid pin",Toast.LENGTH_SHORT).show();
        }else if(encodedqrcode.equalsIgnoreCase("")){
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
        }
        else{
            inp_pin.setErrorEnabled(false);
        Alerter.showdialog(this);
        vars.log("encodedqrcode==="+encodedqrcode);
        String url = vars.financial_server+"clientChangeQrcode.php";
        String[] params = {"qrcode","client","pin"};
        String[] values = {encodedqrcode,vars.mobile,pin.getText().toString().trim()};
        ConnectionClass.ConnectionClass(this, url, params, values, "QRCODE", new ConnectionClass.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Alerter.stopdialog();
                final TransactionObj gsonMessage = new Gson().fromJson(result, TransactionObj.class);
                if (gsonMessage.getResult().equalsIgnoreCase("Success")) {

                    vars.edit.putString("qrcode", gsonMessage.getExtra2());
                    vars.edit.apply();
                    Intent mainpage = new Intent(QrcodeUpdates.this, MainActivity.class);
                    startActivity(mainpage);
                    finish();

                }else{
                    new SweetAlertDialog(QrcodeUpdates.this, SweetAlertDialog.ERROR_TYPE)
                            .setConfirmText("OK")
                            .setTitleText("Error occured")
                            .setContentText(gsonMessage.getMessage())
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            }).show();
                }

            }
        });
    }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getCode ();
    }
 /*   private void getCode (){
        try{
            Alerter.showdialog(this);
            String url ="http://54.69.78.54:8080/mobicQrCodeApi/webresources/userStrings/getUserString";
            JSONObject json = new JSONObject();
            json.put("apiKey", "andrew@moob");
            json.put("userString", qrcode);
            ConnectionClass.JsonString(Request.Method.POST,QrcodeUpdates.this, url, json, "LOGIN", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Alerter.stopdialog();
                    try {
                        JSONObject reader = new JSONObject(result);
                        if(reader.getBoolean("result")){
                            encodedqrcode= reader.getString("encyryptedString");
                            vars.log("encodedqrcode======sucess"+encodedqrcode);

                        }else{

                            Alerter.Error(QrcodeUpdates.this,"Error", reader.getString("message"));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/
}
