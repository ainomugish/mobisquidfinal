package com.mobisquid.mobicash.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.ChatMain;
import com.mobisquid.mobicash.activities.Login;
import com.mobisquid.mobicash.activities.MobistoreApps;
import com.mobisquid.mobicash.activities.SendMoneyActivity;
import com.mobisquid.mobicash.activities.SettingActivity;
import com.mobisquid.mobicash.adapters.GridMenuViewAdapter;
import com.mobisquid.mobicash.dbstuff.Socialdb;
import com.mobisquid.mobicash.model.ItemObjectGrid;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.IntentIntegrator_police;
import com.mobisquid.mobicash.utils.Vars;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MenuFragment extends Fragment implements GridMenuViewAdapter.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Vars vars;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<ItemObjectGrid> rowListItem;
    View rootview;
    TextView count;
    Alerter alerter;
    GridMenuViewAdapter rcAdapter;

    private OnFragmentInteractionListener mListener;

    public MenuFragment() {
        // Required empty public constructor
    }


    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(getActivity());
        alerter = new Alerter(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_menu, container, false);
        count =(TextView) rootview.findViewById(R.id.count);
        rowListItem = getAllItemList();

        RecyclerView rView = (RecyclerView) rootview.findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        rcAdapter = new GridMenuViewAdapter(getActivity(), rowListItem);
        rcAdapter.setOnItemClickListener(this);
        rView.setAdapter(rcAdapter);


        return rootview;
    }
    private void reload(){
        rowListItem = getAllItemList();

        RecyclerView rView = (RecyclerView) rootview.findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        rcAdapter = new GridMenuViewAdapter(getActivity(), rowListItem);
        rcAdapter.setOnItemClickListener(this);
        rView.setAdapter(rcAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(View view, ItemObjectGrid item) {
        System.out.println("item====="+item.getName());
        if(item.getName().equalsIgnoreCase("BuyServices")){

             Intent notice = new Intent(getActivity(),MobistoreApps.class);
            notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view.findViewById(R.id.country_photo), "profile");
            ActivityCompat.startActivity(getActivity(), notice, options.toBundle());

        }else if(item.getName().equalsIgnoreCase("Banking")){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Slide slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                BankingFragment firstfragment = new BankingFragment();
                firstfragment.setReenterTransition(slideTransition);
                firstfragment.setExitTransition(slideTransition);
                firstfragment.setSharedElementEnterTransition(new ChangeBounds());
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main_screen, firstfragment,"MAIN")
                        .addToBackStack(null)
                        .commit();
            }else {
                System.out.println("===========t============");
                BankingFragment firstfragment = new BankingFragment();
                firstfragment.setArguments(getActivity().getIntent().getExtras());
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.content_main_screen, firstfragment,"MAIN").commit();
            }

        }else if(item.getName().equalsIgnoreCase("SendMoney")){
            if(vars.active==null) {
                alerter.login("Financial services", "You are currently logged out, please login or register");
            }else {
                Intent notice = new Intent(getActivity(), SendMoneyActivity.class);
                notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view.findViewById(R.id.country_photo), "profile");
                ActivityCompat.startActivity(getActivity(), notice, options.toBundle());
            }

        }else if(item.getName().equalsIgnoreCase("Payments")){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Slide slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                PaymentFragment firstfragment = new PaymentFragment();
                firstfragment.setReenterTransition(slideTransition);
                firstfragment.setExitTransition(slideTransition);
                firstfragment.setSharedElementEnterTransition(new ChangeBounds());
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main_screen, firstfragment,"MAIN")
                        .addToBackStack(null)
                        .commit();
            }else {
                System.out.println("===========t============");
                PaymentFragment firstfragment = new PaymentFragment();
                firstfragment.setArguments(getActivity().getIntent().getExtras());
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.content_main_screen, firstfragment,"MAIN").commit();
            }


        }
        else if(item.getName().equalsIgnoreCase("Settings")){

                Intent notice = new Intent(getActivity(),SettingActivity.class);
                notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view.findViewById(R.id.country_photo), "profile");
                ActivityCompat.startActivity(getActivity(), notice, options.toBundle());

        }else if(item.getName().equalsIgnoreCase("Chat")){
            Intent notice = new Intent(getActivity(),ChatMain.class);
            notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view.findViewById(R.id.country_photo), "card");
            ActivityCompat.startActivity(getActivity(), notice, options.toBundle());
        }

    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private List<ItemObjectGrid> getAllItemList(){

        List<ItemObjectGrid> allItems = new ArrayList<>();
        if(vars.isfinanance){
           // allItems.add(new ItemObjectGrid("BuyServices", R.mipmap.ic_services));
            allItems.add(new ItemObjectGrid("Payments", R.mipmap.ic_payment));
            allItems.add(new ItemObjectGrid("Banking", R.mipmap.ic_bank));
            allItems.add(new ItemObjectGrid("SendMoney", R.mipmap.ic_sendmoney));
          //  allItems.add(new ItemObjectGrid("Settings", R.mipmap.ic_settings));
        }else {
            if (vars.chaton) {
                allItems.add(new ItemObjectGrid("Chat", R.mipmap.ic_chat));
            }
            //allItems.add(new ItemObjectGrid("CrowdPolice", R.drawable.ic_police));
           // allItems.add(new ItemObjectGrid("Settings", R.mipmap.ic_settings));
        }
        return allItems;
    }
    @Override
    public void onPause() {
        Globals.whichfag="";
        super.onPause();

    }
    @Override
    public void onResume() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        Globals.whichfag="main";
        if(vars.isfinanance){
            toolbar.setTitle("Finance");
        }else {
            toolbar.setTitle("Social");
        }

        Globals.CLOSE = true;
        if(getActivity()!=null){
            reload();
        }
        super.onResume();
    }
}
