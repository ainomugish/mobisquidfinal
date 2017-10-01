package com.mobisquid.mobicash.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
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
import android.widget.Toast;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.fragments.EtaxPayment;
import com.mobisquid.mobicash.fragments.Fragment_gov_menu;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

/**
 * Created by mobicash on 1/5/16.
 */
public class E_GovermentActivity extends AppCompatActivity implements Fragment_gov_menu.OnFragmentInteractionListener {
    Alerter alerter;
    Vars vars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(this);
        alerter = new Alerter(this);
        setContentView(R.layout.e_goverment_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Government Services");
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
                Fragment_gov_menu firstfragment = new Fragment_gov_menu();
                firstfragment.setArguments(getIntent().getExtras());
                firstfragment.setReenterTransition(slideTransition);
                firstfragment.setExitTransition(slideTransition);
                firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.government_container, firstfragment, "MAIN")
                        .commit();
            } else {
                System.out.println("===========t============");
                Fragment_gov_menu firstfragment = new Fragment_gov_menu();
                firstfragment.setArguments(getIntent().getExtras());
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.government_container, firstfragment, "MAIN").commit();
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

    public void etax(View view) {
        if(vars.active==null) {
            alerter.login("Financial services", "You are currently logged out, please login or register");
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Slide slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                EtaxPayment firstfragment = new EtaxPayment();
                firstfragment.setArguments(getIntent().getExtras());
                firstfragment.setReenterTransition(slideTransition);
                firstfragment.setExitTransition(slideTransition);
                firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.government_container, firstfragment, "MAIN")
                        .addToBackStack(null)
                        .commit();
            } else {
                System.out.println("===========t============");
                EtaxPayment firstfragment = new EtaxPayment();
                firstfragment.setArguments(getIntent().getExtras());
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.government_container, firstfragment, "MAIN")
                        .addToBackStack(null)
                        .commit();
            }
           // finish();
        }
    }

    public void IRREMBO(View view) {
        if(vars.active==null) {
            alerter.login("Financial services", "You are currently logged out, please login or register");
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Slide slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                EtaxPayment firstfragment = new EtaxPayment();
                Bundle extar = new Bundle();
                extar.putString("irrebo","irrebo");
                firstfragment.setArguments(extar);
                firstfragment.setReenterTransition(slideTransition);
                firstfragment.setExitTransition(slideTransition);
                firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.government_container, firstfragment, "MAIN")
                        .addToBackStack(null)
                        .commit();
            } else {
                System.out.println("===========t============");
                EtaxPayment firstfragment = new EtaxPayment();
                Bundle extar = new Bundle();
                extar.putString("irrebo","irrebo");
                firstfragment.setArguments(extar);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.government_container, firstfragment, "MAIN")
                        .addToBackStack(null)
                        .commit();
            }

        }
        /*if(vars.active==null) {
            alerter.login("Financial services", "You are currently logged out, please login or register");
        }else {
            if(vars.country_code.equalsIgnoreCase("250")) {
                Intent notice = new Intent(this,GovementPayment.class);
                notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                notice.putExtra("action", "IRREMBO");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        view.findViewById(R.id.irrembo), "");
                ActivityCompat.startActivity(this, notice, options.toBundle());

            }else{
                Toast.makeText(this,"Not available for this country",Toast.LENGTH_LONG).show();
            }
            // finish();
        }*/

    }

    public void Rssb(View view) {
        if(vars.active==null) {
            alerter.login("Financial services", "You are currently logged out, please login or register");
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Slide slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                EtaxPayment firstfragment = new EtaxPayment();
                Bundle extar = new Bundle();
                extar.putString("rssb","rssb");
                firstfragment.setArguments(extar);
                firstfragment.setReenterTransition(slideTransition);
                firstfragment.setExitTransition(slideTransition);
                firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.government_container, firstfragment, "MAIN")
                        .addToBackStack(null)
                        .commit();
            } else {
                System.out.println("===========t============");
                EtaxPayment firstfragment = new EtaxPayment();
                Bundle extar = new Bundle();
                extar.putString("rssb","rssb");
                firstfragment.setArguments(extar);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.government_container, firstfragment, "MAIN")
                        .addToBackStack(null)
                        .commit();
            }
            // finish();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}