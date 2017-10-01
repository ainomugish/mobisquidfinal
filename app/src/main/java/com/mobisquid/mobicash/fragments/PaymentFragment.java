package com.mobisquid.mobicash.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.IntentIntegrator;
import com.mobisquid.mobicash.utils.IntentResult;
import com.mobisquid.mobicash.utils.Vars;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {
    View rootview;
    Vars vars;
    Alerter alerter;

    public PaymentFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ActionBar actionBar =((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
       vars = new Vars(getActivity());
        alerter = new Alerter(getActivity());
        rootview = inflater.inflate(R.layout.fragment_payment, container, false);
       /* rootview.findViewById(R.id.mobishope).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vars.active==null) {
                    alerter.login("Financial services", "You are currently logged out, please login or register");
                }else {
                    IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
                    scanIntegrator.initiateScan();
                }
            }
        });*/
        rootview.findViewById(R.id.merch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vars.active==null) {
                    alerter.login("Financial services", "You are currently logged out, please login or register");
                }else {
                    vars.log("qrcode===="+vars.qrcode);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Slide slideTransition = new Slide(Gravity.LEFT);
                        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                        Myqrcode firstfragment = new Myqrcode();
                        firstfragment.setReenterTransition(slideTransition);
                        firstfragment.setExitTransition(slideTransition);
                        firstfragment.setSharedElementEnterTransition(new ChangeBounds());
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_main_screen, firstfragment, "MAIN")
                                .addToBackStack(null)
                                .commit();
                    } else {
                        System.out.println("===========t============");
                        Myqrcode firstfragment = new Myqrcode();
                        firstfragment.setArguments(getActivity().getIntent().getExtras());
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.addToBackStack(null);
                        ft.replace(R.id.content_main_screen, firstfragment, "MAIN").commit();
                    }
                }
            }
        });
        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null) {

            String scanContent = scanningResult.getContents();
            vars.log("scanContent=="+scanContent);


            if (scanContent!= null){
                try {
                    byte[] mydata = Base64.decode(scanContent,Base64.DEFAULT);
                    String mydeco_string = new String(mydata,"UTF-8");
                    String[] split = mydeco_string.split("#");
                    vars.log("id_cart====="+split[0]);
                    vars.log("productId====="+split[1]);
                    vars.log("merchantid====="+split[2]);

                    vars.log("price====="+split[3]);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{
                getActivity().onBackPressed();
                vars.log("value==back=");
            }
        }
        else{
            Toast toast = Toast.makeText(getActivity(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
            getActivity().finish();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onResume() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Payment");
        Globals.CLOSE = false;
        super.onResume();
    }
}
