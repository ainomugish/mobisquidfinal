package com.mobisquid.mobicash.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.ChangeBounds;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.adapters.TranslogAdapter;
import com.mobisquid.mobicash.fragments.History_input_Fragment;
import com.mobisquid.mobicash.model.TransObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TranslogActivity extends AppCompatActivity {
    Vars vars;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(this);

        setContentView(R.layout.activity_translog);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                History_input_Fragment firstfragment = new History_input_Fragment();
                firstfragment.setArguments(getIntent().getExtras());
                firstfragment.setReenterTransition(slideTransition);
                firstfragment.setExitTransition(slideTransition);
                firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.trans_container, firstfragment, "MAIN")
                        .commit();
            } else {
                System.out.println("===========t============");
                History_input_Fragment firstfragment = new History_input_Fragment();
                firstfragment.setArguments(getIntent().getExtras());
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.trans_container, firstfragment, "MAIN").commit();
            }
        }





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //	getMenuInflater().inflate(R.menu.help_menu, menu);

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
}