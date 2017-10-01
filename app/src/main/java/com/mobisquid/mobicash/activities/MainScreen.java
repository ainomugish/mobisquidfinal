package com.mobisquid.mobicash.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.dbstuff.Transactiondb;
import com.mobisquid.mobicash.fragments.MenuFragment;
import com.mobisquid.mobicash.mqtt.ServiceDemo;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.NotificationUtils;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainScreen extends AppCompatActivity implements MenuFragment.OnFragmentInteractionListener, ViewPagerEx.OnPageChangeListener {
    Bundle extras;
    Vars vars;
    String newapp;
    private SliderLayout mDemoSlider;
  //  Button upgrade;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    boolean finance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(this);
        extras = getIntent().getExtras();
        setContentView(R.layout.activity_main_screen);
        if(extras!=null){
            vars.log("Mainactivity extras not null===============");
        }
        if(!Transactiondb.listAll(Transactiondb.class).isEmpty()){
            Transactiondb.deleteAll(Transactiondb.class);
            vars.log("DELETED======");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            Utils.changeFragment(this,new MenuFragment(),getSupportFragmentManager().beginTransaction()
                    ,extras,R.id.content_main_screen,false,false);

        }
//Slider things
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Business solution", "http://www.mobicashonline.com/app/themes/mobicash/img/business/sliderBusiness.jpg");
        url_maps.put("Pay Taxes", "https://www.mcash.rw/images/banner2.jpg");
        url_maps.put("Pay TV subscriptions", "https://www.mcash.rw/images/banner4.jpg");
        url_maps.put("Top-up Airtime", "https://www.mcash.rw/images/banner1.jpg");
        url_maps.put("Pay electricity", "https://www.mcash.rw/images/banner3.jpg");
        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

     if(vars.mobile!=null){
         vars.log("=======service start======="+vars.mobile);
         Intent service = new Intent(MainScreen.this, ServiceDemo.class);
         service.putExtra("userName",vars.mobile);
         startService(service);
     }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Globals.PUSH_NOTIFICATION)) {

                    System.out.println("===========mainse==========="+Globals.PUSH_NOTIFICATION);
                    Intent req= new Intent(MainScreen.this,PaymentDetails.class);
                    req.putExtra("message",intent.getStringExtra("message"));
                    startActivity(req);
                }

            }
        };

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        vars.log("================meeeeeeeeeeee============");
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if(fragment!=null){
                fragment.onActivityResult(requestCode, resultCode, data);
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainlogout, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                if(vars.active!=null){
                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Confirm!")
                            .setContentText("Are you sure you want to logout from financials?")
                            .setCancelText("No")
                            .setConfirmText("Yes, logout")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    vars.edit.remove("active");
                                    vars.edit.apply();

                                    Intent mainw = new Intent(MainScreen.this,MainActivity.class);
                                    startActivity(mainw);
                                    finish();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                }else if(vars.active==null && vars.social!=null){
                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Confirm!")
                            .setContentText("Are you sure you want to logout from Social?")
                            .setCancelText("No")
                            .setConfirmText("Yes, logout")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    vars.edit.remove("social");
                                    vars.edit.apply();

                                    Intent mainw = new Intent(MainScreen.this,MainActivity.class);
                                    startActivity(mainw);
                                    finish();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                }
                return true;
            case android.R.id.home:
               onBackPressed();
                return true;
            case R.id.action_settings:
                Intent settings = new Intent(this,SettingActivity.class);
                settings.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                ActivityOptionsCompat options_s = ActivityOptionsCompat.makeSceneTransitionAnimation(this, this.findViewById(R.id.appbar), "profile");
                ActivityCompat.startActivity(this, settings, options_s.toBundle());
                return true;
            case R.id.menu:
                Intent notice = new Intent(MainScreen.this, RegistrationActivity.class);
                notice.putExtra("financial","financial");
                notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainScreen.this
                        , this.findViewById(R.id.appbar), "profile");
                ActivityCompat.startActivity(MainScreen.this, notice, options.toBundle());
                //finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    protected void onPause() {
        Globals.WHICHUSER="";
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();

    }
    protected void onResume() {
        super.onResume();
        Globals.WHICHUSER="pay";
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Globals.PUSH_NOTIFICATION));
        vars.log("====On resume===");

    }
    @Override
    public void onBackPressed() {
        if(Globals.whichfag != null && Globals.whichfag.equalsIgnoreCase("main")) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Confirm!!")
                    .setContentText("Would you like to close the app?")
                    .setConfirmText("Yes, close")
                    .setCancelText("No")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            System.exit(0);
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
