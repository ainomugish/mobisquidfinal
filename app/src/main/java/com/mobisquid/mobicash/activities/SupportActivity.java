package com.mobisquid.mobicash.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.internal.view.SupportMenu;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.fragments.Fragment_Userdetails;
import com.mobisquid.mobicash.fragments.Supportmenu;
import com.mobisquid.mobicash.utils.Utils;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SupportActivity extends AppCompatActivity implements Supportmenu.OnFragmentInteractionListener {
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        setContentView(R.layout.activity_support);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();


        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        if (savedInstanceState == null) {
            Utils.changeFragment(this,new Supportmenu()
                    ,getSupportFragmentManager().beginTransaction()
                    ,extras,
                    R.id.support_container
                    ,false
                    ,false);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.airtime_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.support_container);
        if (fragment instanceof Fragment_Userdetails){
            new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Confirm")
                    .setContentText("Are you sure you want to cancel this? ")
                    .setConfirmText("Yes,cancel")
                    .setCancelText("NO")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            finish();
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    }).show();
        }else {
            super.onBackPressed();
        }
    }
}
