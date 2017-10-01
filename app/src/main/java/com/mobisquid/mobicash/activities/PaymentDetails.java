package com.mobisquid.mobicash.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.fragments.InitializePayment;
import com.mobisquid.mobicash.fragments.ProvePayment;
import com.mobisquid.mobicash.fragments.UnpaidRequests;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.NotificationUtils;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class PaymentDetails extends AppCompatActivity {

    Bundle extras;
    Vars vars;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(this);
        extras = getIntent().getExtras();

        setContentView(R.layout.activity_payment_details);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        if(extras!=null){
            vars.log("extras======"+extras.toString());
            if(extras.getString("scan")!=null && extras.getString("scan").equalsIgnoreCase("scan")){
                if (savedInstanceState == null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Slide slideTransition = new Slide(Gravity.LEFT);
                        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                        InitializePayment firstfragment = new InitializePayment();
                        Bundle args = new Bundle();
                        args.putString("scan","scan" );
                        firstfragment.setArguments(args);
                        firstfragment.setReenterTransition(slideTransition);
                        firstfragment.setExitTransition(slideTransition);
                        firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.activity_payment_details, firstfragment, "MAIN")
                                // .addToBackStack(null)
                                .commit();
                    } else {
                        System.out.println("===========t============");
                        InitializePayment firstfragment = new InitializePayment();
                        Bundle args = new Bundle();
                        args.putString("scan","scan" );
                        firstfragment.setArguments(args);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.activity_payment_details, firstfragment, "MAIN").commit();
                    }
                }else {
                    vars.log("===Not null===");
                }


            }else if(extras.getString("unpaid")!=null && extras.getString("unpaid").equalsIgnoreCase("unpaid")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide slideTransition = new Slide(Gravity.LEFT);
                    slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                    UnpaidRequests firstfragment = new UnpaidRequests();
                    firstfragment.setArguments(extras);
                    firstfragment.setReenterTransition(slideTransition);
                    firstfragment.setExitTransition(slideTransition);
                    firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.activity_payment_details, firstfragment, "MAIN")
                            // .addToBackStack(null)
                            .commit();
                } else {
                    System.out.println("===========t============");
                    UnpaidRequests firstfragment = new UnpaidRequests();
                    firstfragment.setArguments(extras);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.activity_payment_details, firstfragment, "MAIN").commit();
                }
            }

            else{
                if (savedInstanceState == null) {
                    Utils.Vibarate(this);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Slide slideTransition = new Slide(Gravity.LEFT);
                        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                        ProvePayment firstfragment = new ProvePayment();
                        firstfragment.setArguments(extras);
                        firstfragment.setReenterTransition(slideTransition);
                        firstfragment.setExitTransition(slideTransition);
                        firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.activity_payment_details, firstfragment, "MAIN")
                                // .addToBackStack(null)
                                .commit();
                    } else {
                        System.out.println("===========t============");
                        ProvePayment firstfragment = new ProvePayment();
                        firstfragment.setArguments(extras);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.activity_payment_details, firstfragment, "MAIN").commit();
                    }
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Slide slideTransition = new Slide(Gravity.LEFT);
                        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                        ProvePayment firstfragment = new ProvePayment();
                        firstfragment.setArguments(extras);
                        firstfragment.setReenterTransition(slideTransition);
                        firstfragment.setExitTransition(slideTransition);
                        firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.activity_payment_details, firstfragment, "MAIN")
                                // .addToBackStack(null)
                                .commit();
                    } else {
                        System.out.println("===========t============");
                        ProvePayment firstfragment = new ProvePayment();
                        firstfragment.setArguments(extras);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.activity_payment_details, firstfragment, "MAIN").commit();
                    }
                }

            }

        }
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Globals.PUSH_NOTIFICATION)) {
                    Utils.Vibarate(PaymentDetails.this);
                    System.out.println("===========t============"+Globals.PUSH_NOTIFICATION);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Slide slideTransition = new Slide(Gravity.LEFT);
                        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                        ProvePayment firstfragment = new ProvePayment();
                        firstfragment.setArguments(intent.getExtras());
                        firstfragment.setReenterTransition(slideTransition);
                        firstfragment.setExitTransition(slideTransition);
                        firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.activity_payment_details, firstfragment, "MAIN")
                                // .addToBackStack(null)
                                .commit();
                    } else {
                        System.out.println("===========t============");
                        ProvePayment firstfragment = new ProvePayment();
                        firstfragment.setArguments(intent.getExtras());
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.activity_payment_details, firstfragment, "MAIN").commit();
                    }

                }

            }
        };
    }
    @Override
    protected void onPause() {
        Globals.WHICHUSER="";
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_payment_details);
        if (fragment instanceof InitializePayment){
            fragment.onActivityResult(requestCode,resultCode, data);
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Globals.PUSH_NOTIFICATION));
       vars.log("====On resume===");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.airtime_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_payment_details);
        if (fragment instanceof ProvePayment){
            new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Please confirm!!")
                    .setContentText("Would you like to cancel this payment? This will not be reverted")
                    .setConfirmText("Yes,cancel")
                    .setCancelText("No")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            ProvePayment.sendMessage(PaymentDetails.this);
                            super.onBackPressed();
                        }
                    }).show();
        }else {
            super.onBackPressed();
        }*/
    }
}
