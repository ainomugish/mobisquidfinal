package com.mobisquid.mobicash.fragments;


import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.CheckBalance;
import com.mobisquid.mobicash.activities.TranslogActivity;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Vars;


public class BankingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View rootview;
    LinearLayout balance,history;
       Vars vars;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView imageView;
    ActionBar actionBar;
    Alerter alerter;


    public BankingFragment() {
        // Required empty public constructor
    }

    public static BankingFragment newInstance(String param1, String param2) {
        BankingFragment fragment = new BankingFragment();
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
        setHasOptionsMenu(true);
        android.support.v7.app.ActionBar actionBar =((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
            rootview = inflater.inflate(R.layout.bankingfragment, container, false);

        balance =(LinearLayout) rootview.findViewById(R.id.balance);
        history =(LinearLayout) rootview.findViewById(R.id.history);

        if(vars.active==null) {

            balance.setBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
            history.setBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
        }

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vars.active==null) {
                    alerter.login("Financial services", "You are currently logged out, please login or register");
                }else {
                    Intent notice = new Intent(getActivity(), TranslogActivity.class);
                    notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), getActivity().findViewById(R.id.logs), "profile");
                    ActivityCompat.startActivity(getActivity(), notice, options.toBundle());

                }
            }
        });
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vars.active==null) {
                    alerter.login("Financial services", "You are currently logged out, please login or register");
                }else {
                    Intent notice = new Intent(getActivity(), CheckBalance.class);
                    notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), getActivity().findViewById(R.id.balanceimg), "profile");
                    ActivityCompat.startActivity(getActivity(), notice, options.toBundle());


                }
            }
        });





        return rootview;
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
        toolbar.setTitle("Banking");
        Globals.CLOSE = false;
        super.onResume();
    }
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }



}
