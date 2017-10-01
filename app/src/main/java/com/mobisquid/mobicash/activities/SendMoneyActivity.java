package com.mobisquid.mobicash.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.fragments.ConfirmTrasferActivity;
import com.mobisquid.mobicash.fragments.SendMoney_Fragment;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SendMoneyActivity extends AppCompatActivity {

    Vars vars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        vars = new Vars(this);
        setContentView(R.layout.sendmoney_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Send Money");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        if (savedInstanceState == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Slide slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));

                SendMoney_Fragment firstfragment = new SendMoney_Fragment();
                firstfragment.setArguments(getIntent().getExtras());
                firstfragment.setReenterTransition(slideTransition);
                firstfragment.setExitTransition(slideTransition);
                firstfragment.setSharedElementEnterTransition(new ChangeBounds());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.money_container, firstfragment, "MAIN")
                        .commit();
            } else {
                System.out.println("===========t============");
                SendMoney_Fragment firstfragment = new SendMoney_Fragment();
                firstfragment.setArguments(getIntent().getExtras());
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.money_container, firstfragment, "MAIN").commit();
            }
        }


    }


    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.home, menu);

        return true;


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.home:
                Utils.home(this);
                return true;

            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void cancel_button_register(View view) {
        onBackPressed();
    }


}
