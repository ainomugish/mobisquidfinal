package com.mobisquid.mobicash.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.Login;
import com.mobisquid.mobicash.dbstuff.Apps;
import com.mobisquid.mobicash.payment.activities.PaymentActivity;
import com.mobisquid.mobicash.payment.qrcodereader.QRCodeReader;
import com.mobisquid.mobicash.utils.CircleTransform;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Vars;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppFragment extends Fragment {
    Vars vars;
    View rootview;
    SwitchCompat sw_social, sw_finance, sw_services, sw_business, sw_eshop, sw_beacon, sw_crowpolice;
    Apps apps;
    TextView tvPayment, tvScan;


    public AppFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        rootview = inflater.inflate(R.layout.appfragment, container, false);
        sw_social = (SwitchCompat) rootview.findViewById(R.id.sw_social);
        sw_finance = (SwitchCompat) rootview.findViewById(R.id.sw_finance);
        sw_services = (SwitchCompat) rootview.findViewById(R.id.sw_services);
        sw_business = (SwitchCompat) rootview.findViewById(R.id.sw_business);
        sw_eshop = (SwitchCompat) rootview.findViewById(R.id.sw_eshop);
        sw_beacon = (SwitchCompat) rootview.findViewById(R.id.sw_beacon);
        sw_crowpolice = (SwitchCompat) rootview.findViewById(R.id.sw_crowdpolice);
        tvScan = (TextView) rootview.findViewById(R.id.tvScanQRCode);

        tvScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), QRCodeReader.class));
            }
        });
        tvPayment = (TextView) rootview.findViewById(R.id.tvPayment);
        tvPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PaymentActivity.class));
            }
        });

        if (getArguments() != null) {

        }
        if (!Apps.listAll(Apps.class).isEmpty()) {

            apps = Apps.findById(Apps.class, 1);
            sw_social.setChecked(apps.isSocial());
            sw_finance.setChecked(apps.isFinance());
            sw_services.setChecked(apps.isServices());
            sw_business.setChecked(apps.isBusiness());
            sw_eshop.setChecked(apps.isEshop());
            sw_beacon.setChecked(apps.isBeacon());
            sw_crowpolice.setChecked(apps.isCrowdpolice());

        }


        getActivity().findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();

             /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide slideTransition = new Slide(Gravity.BOTTOM);
                    slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                    TourFragment firstfragment = new TourFragment();
                    Bundle extras = new Bundle();
                    firstfragment.setArguments(extras);
                    firstfragment.setReenterTransition(slideTransition);
                    firstfragment.setExitTransition(slideTransition);
                    firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, firstfragment, "MAIN")
                              .addToBackStack(null)
                            .commit();
                } else {
                    System.out.println("===========t============");
                    TourFragment firstfragment = new TourFragment();
                    Bundle extras = new Bundle();
                    extras.putString("myname",getArguments().getString("myname"));
                    extras.putString("userid",getArguments().getString("userid"));
                    firstfragment.setArguments(extras);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                }*/

            }
        });

        sw_social.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vars.log("active====" + isChecked);
                apps.setSocial(isChecked);
                apps.update();

            }
        });
        sw_finance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vars.log("active====" + isChecked);
                apps.setFinance(isChecked);
                apps.update();

            }
        });
        sw_services.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vars.log("active====" + isChecked);
                apps.setServices(isChecked);
                apps.update();

            }
        });
        sw_business.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vars.log("active====" + isChecked);
                apps.setBusiness(isChecked);
                apps.update();

            }
        });
        sw_eshop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vars.log("active====" + isChecked);
                apps.setEshop(isChecked);
                apps.update();

            }
        });
        sw_beacon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vars.log("active====" + isChecked);
                apps.setBeacon(isChecked);
                apps.update();

            }
        });
        sw_crowpolice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vars.log("active====" + isChecked);
                apps.setCrowdpolice(isChecked);
                apps.update();

            }
        });


        return rootview;
    }

    @Override
    public void onPause() {
        super.onPause();
        Globals.whichuser = "";
    }

    @Override
    public void onResume() {
        super.onResume();
        Globals.whichuser = "welcome";
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Mobisquid-Welcome");
    }

}
