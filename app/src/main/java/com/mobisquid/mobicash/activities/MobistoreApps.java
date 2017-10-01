package com.mobisquid.mobicash.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.IntentIntegrator;
import com.mobisquid.mobicash.utils.IntentResult;
import com.mobisquid.mobicash.utils.Vars;

public class MobistoreApps extends AppCompatActivity {


    Vars vars;
    Toolbar toolbar;
    Alerter alerter;
    String amount;
    ActionBar actionBar;

    RelativeLayout mylife, schoolfees, airtime, qrtimes, checkbalance, television, e_tax, electricity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(this);
        setContentView(R.layout.mobiservices);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        alerter = new Alerter(this);
        log("MY NUMBER IS===== " + vars.mobile);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        //	mylife = (RelativeLayout)findViewById(R.id.my_life);
        schoolfees = (RelativeLayout) findViewById(R.id.school_fees);
        airtime = (RelativeLayout) findViewById(R.id.air_t);
        //qrtimes = (RelativeLayout)findViewById(R.id.qrtimes);
        //checkbalance = (RelativeLayout)findViewById(R.id.checkbalance);
        television = (RelativeLayout) findViewById(R.id.television);
        e_tax = (RelativeLayout) findViewById(R.id.e_tax);
        electricity = (RelativeLayout) findViewById(R.id.electricity_po);

        if (vars.country_code == null || !vars.country_code.equalsIgnoreCase("250")) {
            e_tax.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            electricity.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            schoolfees.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            e_tax.setVisibility(View.GONE);
            electricity.setVisibility(View.GONE);
            schoolfees.setVisibility(View.GONE);

        }

	/*	if(vars.country_code.equalsIgnoreCase("256")){
            e_tax.setBackground(getResources().getDrawable(R.drawable.linear_layout_bg_tv));
		}
		if(vars.country_code.equalsIgnoreCase("250")){
			schoolfees.setBackground(getResources().getDrawable(R.drawable.linear_layout_bg_tv));
		}
*/


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.help_menu, menu);

        return true;


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
                finish();
                return true;

            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        super.onPause();


    }


    @Override
    protected void onResume() {

        super.onResume();


    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        log("scanningResult===============" + scanningResult.toString());
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();

            String scanFormat = scanningResult.getFormatName();


            //Long amt = Long.valueOf(scanContent.toString().trim()).longValue();
            //log("amount==="+amt);

            if (scanContent != null) {
                //get application content to the next activity


                try {
                    Long amt = Long.valueOf(scanContent.toString().trim()).longValue();
                    log("amount===" + amt);

                    Bundle basket = new Bundle();
                    basket.putString("amount", scanContent);
                    basket.putString("keytwo", scanFormat);
                    amount = scanContent;

                    //start the new activity
					/*Intent us = new Intent(MobistoreApps.this,Scan_Airtime_Activity.class);
					us.putExtras(basket);
					startActivity(us);
					finish();*/

                } catch (NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                    alerter.alerterSuccessSimple(MobistoreApps.this, "Scan Error", "Scanned content not number");
                }


            } else {
                Intent back = new Intent(this, MobistoreApps.class);
                startActivity(back);

            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
            Intent us = new Intent(this, MobistoreApps.class);
            startActivity(us);
        }
    }

    public void airtime(View v) {
        if (vars.active == null) {
            alerter.login("Financial services", "You are currently logged out, please login or register");
        } else {
            Intent notice = new Intent(this, AirtimeBuy.class);
            notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, v.findViewById(R.id.aitt), "profile");
            ActivityCompat.startActivity(this, notice, options.toBundle());
            //finish();
        }
    }

    public void television(View v) {

        Intent notice = new Intent(this, TVMenuActivity.class);
        notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, v.findViewById(R.id.tv), "profile");
        ActivityCompat.startActivity(this, notice, options.toBundle());


    }

    public void electricity(View v) {
        if (vars.country_code == null || !vars.country_code.equalsIgnoreCase("250")) {
            Toast.makeText(this, "Coming soon", Toast.LENGTH_LONG).show();
        } else {
            if (vars.active == null) {
                alerter.login("Financial services", "You are currently logged out, please login or register");
            } else {
                if (vars.country_code.equalsIgnoreCase("27")) {
                    Toast.makeText(this, "Service not available for this country", Toast.LENGTH_SHORT).show();
                } else {
                    Intent notice = new Intent(this, Umeme_Pay.class);
                    notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, v.findViewById(R.id.electricity), "profile");
                    ActivityCompat.startActivity(this, notice, options.toBundle());

                }
            }
        }
    }

    public void etax(View v) {
        if (vars.country_code == null || !vars.country_code.equalsIgnoreCase("250")) {
            Toast.makeText(this, "Coming soon", Toast.LENGTH_LONG).show();
        } else {
            Intent notice = new Intent(this, E_GovermentActivity.class);
            notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, v.findViewById(R.id.etax), "profile");
            ActivityCompat.startActivity(this, notice, options.toBundle());
        }
    }

    public void school(View v) {
        if (vars.country_code == null || !vars.country_code.equalsIgnoreCase("250")) {
            Toast.makeText(this, "Service not available for this country", Toast.LENGTH_SHORT).show();
        } else {
            if (vars.active == null) {
                alerter.login("Financial services", "You are currently logged out, please login or register");
            } else {
                if (vars.country_code.equalsIgnoreCase("27")) {
                    Toast.makeText(this, "Service not available for this country", Toast.LENGTH_SHORT).show();
                } else {
                    Intent notice = new Intent(this, SchoolActivity.class);
                    notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, v.findViewById(R.id.sch), "profile");
                    ActivityCompat.startActivity(this, notice, options.toBundle());

                }
            }
        }
    }

    public void log(String string) {
        System.out.println(string);

    }

}
