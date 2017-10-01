package com.mobisquid.mobicash.activities;

import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.fragments.Fragment_School;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SchoolActivity extends AppCompatActivity {
    LinearLayout payout, reglay;
    EditText edit_reg_num,edit_amount,edit_paid,edit_pin;
    TextInputLayout inp_reg,inp_amount,inp_paid,inp_pin;
    TextView studentName,regNumber,schoolName;
    String schoolcode;
    Vars vars;
    Alerter alerter;
    JSONObject reader = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(this);
        alerter = new Alerter(this);
        setContentView(R.layout.activity_school);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("School fees payment");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        if (savedInstanceState == null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Slide slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                Fragment_School firstfragment = new Fragment_School();
                firstfragment.setArguments(getIntent().getExtras());
                firstfragment.setReenterTransition(slideTransition);
                firstfragment.setExitTransition(slideTransition);
                firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.school_container, firstfragment, "MAIN")
                        .commit();
            } else {
                System.out.println("===========t============");
                Fragment_School firstfragment = new Fragment_School();
                firstfragment.setArguments(getIntent().getExtras());
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.school_container, firstfragment, "MAIN").commit();
            }
        }






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

}
