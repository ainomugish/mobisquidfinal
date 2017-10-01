package com.mobisquid.mobicash.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.mobisquid.mobicash.BuildConfig;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.fragments.First_sms_fragment;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Vars;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SmsActivity extends AppCompatActivity implements First_sms_fragment.OnFragmentInteractionListener {
    Bundle extr;
    String financials,versionName ,changepass;
    Vars vars;
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        extr = getIntent().getExtras();
        vars= new Vars(this);
        if(extr!=null){
            financials = extr.getString("financials");
            changepass= extr.getString("changepass");
        }
        setContentView(R.layout.activity_sms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        version = (TextView) findViewById(R.id.version);
        int versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;
        version.setText("V "+versionName+" - "+versionCode);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            First_sms_fragment firstfragment = new First_sms_fragment();
            firstfragment.setArguments(extr);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstfragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction(int position) {

    }

    @Override
    protected void onPause() {

        super.onPause();
    }
    @Override
    public void onBackPressed() {
        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Confirm!!")
                .setContentText("Are you sure you want to quit?")
                .setCancelText("No")
                .setConfirmText("Yes,close")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                     sweetAlertDialog.dismiss();
                        SmsActivity.super.onBackPressed();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.help_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        // Handle action bar actions click
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
