package com.mobisquid.mobicash.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.fragments.Registration_one;
import com.mobisquid.mobicash.fragments.Verify_ID;


public class FinactionalRegistration extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    String number,countrycode,location;
    Bundle extras;
    static final String TAG = FinactionalRegistration.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_reagistration);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            //actionBar.setTitle("Financial Registration");
        }
        extras = getIntent().getExtras();
        if(extras!=null){
            number = extras.getString("number");
            countrycode = extras.getString("code");
            location = extras.getString("location");

        }
        Log.i(TAG,"ACCESS CODE===="+countrycode);
        if(countrycode.equalsIgnoreCase("27")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Slide slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                System.out.println("===========frgment===Verify_ID=========");
                Bundle args = new Bundle();
                args.putString("number", number);
                args.putString("code", countrycode);

                Verify_ID firstfragment = new Verify_ID();
                firstfragment.setReenterTransition(slideTransition);
                firstfragment.setExitTransition(slideTransition);
                firstfragment.setArguments(args);
                firstfragment.setSharedElementEnterTransition(new ChangeBounds());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_finactional_registration, firstfragment, "MAIN")
                        .commit();
            } else {
                System.out.println("===========t============");
                Verify_ID firstfragment = new Verify_ID();
                Bundle args = new Bundle();
                args.putString("number", number);
                args.putString("code", countrycode);
                firstfragment.setArguments(args);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.content_finactional_registration, firstfragment, "MAIN").commit();
            }

        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Slide slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                System.out.println("===========frgment============");
                Bundle args = new Bundle();
                args.putString("number", number);
                args.putString("code", countrycode);

                Registration_one firstfragment = new Registration_one();
                firstfragment.setReenterTransition(slideTransition);
                firstfragment.setExitTransition(slideTransition);
                firstfragment.setArguments(args);
                firstfragment.setSharedElementEnterTransition(new ChangeBounds());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_finactional_registration, firstfragment, "MAIN")
                        .commit();
            } else {
                System.out.println("===========t============");
                Registration_one firstfragment = new Registration_one();
                Bundle args = new Bundle();
                args.putString("number", number);
                args.putString("code", countrycode);
                firstfragment.setArguments(args);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.content_finactional_registration, firstfragment, "MAIN").commit();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //   getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
