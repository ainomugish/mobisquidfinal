package com.mobisquid.mobicash.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.EshoppingActivity;
import com.mobisquid.mobicash.activities.Login;
import com.mobisquid.mobicash.activities.MainActivity;
import com.mobisquid.mobicash.activities.MobistoreApps;
import com.mobisquid.mobicash.dbstuff.Apps;
import com.mobisquid.mobicash.dbstuff.Socialdb;
import com.mobisquid.mobicash.model.SupportObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.IntentIntegrator_police;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

/**
 * A simple {@link Fragment} subclass.
 */
public class TourFragment extends Fragment {
    View rootview;
    Vars vars;
    Apps apps;


    public TourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        vars = new Vars(getActivity());

        rootview = inflater.inflate(R.layout.fragment_tour, container, false);
        rootview.findViewById(R.id.crowdpol).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Socialdb.listAll(Socialdb.class).isEmpty()){
                    Alerter.Sociallogin(getActivity(),"Social service", "You are currently logged out, please login or register");
                }else {
                    IntentIntegrator_police scanIntegrator = new IntentIntegrator_police(getActivity());
                    scanIntegrator.shareText();
                }
            }
        });
        rootview.findViewById(R.id.services).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vars.active!=null){
                    Intent notice = new Intent(getActivity(), MobistoreApps.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), rootview.findViewById(R.id.imgservices), "services");
                    ActivityCompat.startActivity(getActivity(), notice, options.toBundle());
                }else {
                    Intent notice = new Intent(getActivity(), Login.class);
                    notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), rootview.findViewById(R.id.imgfnc), "login");
                    ActivityCompat.startActivity(getActivity(), notice, options.toBundle());
                }


            }
        });
        rootview.findViewById(R.id.eshopping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notice = new Intent(getActivity(), com.mobisquid.mobicash.ecommerce.MainActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), rootview.findViewById(R.id.image), "eshopping");
                ActivityCompat.startActivity(getActivity(), notice, options.toBundle());

            }
        });
        rootview.findViewById(R.id.social).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vars.social!=null){
                    Utils.isFinance(getActivity(),false);
                    Intent main = new Intent(getActivity(),MainActivity.class);
                    startActivity(main);
                    getActivity().finish();

                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Slide slideTransition = new Slide(Gravity.LEFT);
                        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                        Login_Fragment firstfragment = new Login_Fragment();
                        firstfragment.setReenterTransition(slideTransition);
                        firstfragment.setExitTransition(slideTransition);
                        firstfragment.setSharedElementEnterTransition(new ChangeBounds());
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, firstfragment, "MAIN")
                                .addToBackStack(null)
                                .commit();
                    } else {
                        System.out.println("===========t============");
                        Login_Fragment firstfragment = new Login_Fragment();
                        firstfragment.setArguments(getActivity().getIntent().getExtras());
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.addToBackStack(null);
                        ft.replace(R.id.fragment_container, firstfragment, "MAIN").commit();
                    }
                }
            }
        });
        rootview.findViewById(R.id.finance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vars.active!=null){
                    Utils.isFinance(getActivity(),true);
                    Intent main = new Intent(getActivity(),MainActivity.class);
                    startActivity(main);
                    getActivity().finish();
                }else {
                    Intent notice = new Intent(getActivity(), Login.class);
                    notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), rootview.findViewById(R.id.imgfnc), "login");
                    ActivityCompat.startActivity(getActivity(), notice, options.toBundle());
                    getActivity().finish();
                }
            }
        });
        getActivity().findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAll() ;
            }
        });
        return rootview;
    }
private void showAll(){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        Slide slideTransition = new Slide(Gravity.BOTTOM);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        AppFragment firstfragment = new AppFragment();
        firstfragment.setReenterTransition(slideTransition);
        firstfragment.setExitTransition(slideTransition);
        firstfragment.setSharedElementEnterTransition(new ChangeBounds());
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, firstfragment,"MAIN")
                .addToBackStack(null)
                .commit();
    }else {
        System.out.println("===========t============");
        AppFragment firstfragment = new AppFragment();
        firstfragment.setArguments(getActivity().getIntent().getExtras());
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.fragment_container, firstfragment,"MAIN").commit();
    }
}
    @Override
    public void onPause() {
        super.onPause();
        Globals.whichuser="";
    }

    @Override
    public void onResume() {
        super.onResume();
        Globals.whichuser="tour";
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Mobisquid Tour");
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        if(!Apps.listAll(Apps.class).isEmpty()){

            apps = Apps.findById(Apps.class,1);

            Visibility(rootview.findViewById(R.id.card_social),apps.isSocial());
            Visibility(rootview.findViewById(R.id.card_finance),apps.isFinance());
            Visibility(rootview.findViewById(R.id.card_services),apps.isServices());
            Visibility(rootview.findViewById(R.id.card_business),apps.isBusiness());
            Visibility(rootview.findViewById(R.id.card_eshop),apps.isEshop());
            Visibility(rootview.findViewById(R.id.card_beacon),apps.isBeacon());
            Visibility(rootview.findViewById(R.id.card_crowpolice),apps.isCrowdpolice());

        }
    }
    private void Visibility(View view, boolean isVisible){
        if(isVisible){
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.help_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                Intent home = new Intent(getActivity(), MainActivity.class);
                startActivity(home);
                getActivity().finish();

                return false;
            case R.id.menu:
                showAll();
                return true;
            case R.id.needhelp:
                Bundle extras = new Bundle();
                if(vars.mobile!=null){
                    SupportObj user = new SupportObj();
                    user.setFullname(vars.fullname);
                    user.setMobile(vars.mobile);
                    if(vars.username!=null){
                        user.setUserid(vars.chk);
                        user.setUsername(vars.username);
                    }
                   extras.putString("user",new Gson().toJson(user));

                }else{
                    extras = null;
                }
                Utils.Support(getActivity(),extras,getActivity().findViewById(R.id.toolbar));
                return true;

            default:
                break;
        }

        return false;
    }

}
