package com.mobisquid.mobicash.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.adapters.RequestAdapter;
import com.mobisquid.mobicash.model.Transactions;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnpaidRequests extends Fragment implements RequestAdapter.OnItemClickListener {
    RecyclerView recentrecl;
    View rootview;
    CardView nothingcard;
    RequestAdapter recentAdapter;
    Vars vars;
    List<Transactions> listmesag;
    static String URL_FEED="https://support.mobisquid.com/webresources/entities.transactions/requests";
    public UnpaidRequests() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(getActivity());
        listmesag = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_unpaid_requests, container, false);
        recentrecl = (RecyclerView) rootview.findViewById(R.id.recycler_recent);
        nothingcard =(CardView) rootview.findViewById(R.id.nothingcard);
        recentrecl.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recentrecl.setLayoutManager(llm);
        recentrecl.setItemAnimator(new DefaultItemAnimator());
        recentrecl.setVisibility(View.GONE);
        recentAdapter = new RequestAdapter(getActivity(),listmesag);
        recentrecl.setAdapter(recentAdapter);
        recentAdapter.setOnItemClickListener(this);
        rootview.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return rootview;
    }
    @Override
    public void onResume() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Requests");
        if(getActivity()!=null){
            if(recentAdapter!=null){
                recentAdapter.notifyDataSetChanged();
            }
        }
        checkrequests();
        super.onResume();
    }
    private void checkrequests(){
            try{
                Alerter.showdialog(getActivity());
                JSONObject json = new JSONObject();
                json.put("receiverMobile",vars.mobile);

                vars.log("===="+json.toString());
                ConnectionClass.JsonString(Request.Method.POST,getActivity(), URL_FEED, json,
                        "POST", new ConnectionClass.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Alerter.stopdialog();
                                vars.log("STRING REQUEST:...... " + result);
                                if (result!=null){

                                    nothingcard.setVisibility(View.GONE);
                                    try {
                                        JSONArray detailsArray = new JSONArray(result);
                                        if (detailsArray instanceof JSONArray) {
                                            nothingcard.setVisibility(View.GONE);
                                            recentrecl.setVisibility(View.VISIBLE);
                                            for (int i = 0; i < detailsArray.length(); i++) {
                                                Transactions req = new Gson().fromJson(detailsArray.getString(i),Transactions.class);
                                                listmesag.add(req);
                                            }
                                        }
                                        vars.log("size========"+listmesag.size());
                                        if(listmesag.size()==0) {
                                            Toast.makeText(getActivity(), "No Requests", Toast.LENGTH_LONG).show();
                                            nothingcard.setVisibility(View.VISIBLE);
                                            recentrecl.setVisibility(View.GONE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    recentAdapter.notifyDataSetChanged();

                                }else{
                                    nothingcard.setVisibility(View.VISIBLE);
                                    recentrecl.setVisibility(View.GONE);
                                }

                            }
                        });

            } catch (JSONException e) {
                e.printStackTrace();
            }


    }

    @Override
    public void onItemClick(View itemView, int position) {
        Transactions trans = recentAdapter.getItem(position);
        Bundle extra = new Bundle();
        extra.putString("message",new Gson().toJson(trans));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slideTransition = new Slide(Gravity.LEFT);
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
            ProvePayment firstfragment = new ProvePayment();
            firstfragment.setArguments(extra);
            firstfragment.setReenterTransition(slideTransition);
            firstfragment.setExitTransition(slideTransition);
            firstfragment.setSharedElementEnterTransition(new ChangeBounds());

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_payment_details, firstfragment, "MAIN")
                    // .addToBackStack(null)
                    .commit();
        } else {
            System.out.println("===========t============");
            ProvePayment firstfragment = new ProvePayment();
            firstfragment.setArguments(extra);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.activity_payment_details, firstfragment, "MAIN").commit();
        }

    }
}
