package com.mobisquid.mobicash.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.TVMenuActivity;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.IntentIntegrator_tv_watch;
import com.mobisquid.mobicash.utils.Vars;

public class TvMenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout watchafricatv,dstv,canel,startimes;
    Vars vars;
    Gson gson;
    AlertDialog alertDialog;
    Alerter alerter;
    View rootview;

    private OnFragmentInteractionListener mListener;

    public TvMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TvMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TvMenuFragment newInstance(String param1, String param2) {
        TvMenuFragment fragment = new TvMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        vars = new Vars(getActivity());
        vars.log("country========"+vars.country_code);
        gson = new Gson();
        alerter = new Alerter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_tv_menu, container, false);
        watchafricatv = (LinearLayout) rootview.findViewById(R.id.watchafricatv);
        dstv = (LinearLayout) rootview.findViewById(R.id.dstv);
        canel = (LinearLayout) rootview.findViewById(R.id.canelpay);
        // gotv = (LinearLayout) findViewById(R.id.gotv);
        startimes = (LinearLayout) rootview.findViewById(R.id.startime);

        if(vars.country_code==null || vars.country_code.equalsIgnoreCase("27")) {
            dstv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            canel.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            // gotv.setBackground(getResources().getDrawable(R.drawable.linear_layout_bg_tv));
            startimes.setBackgroundColor(getResources().getColor(R.color.colorAccent));

            dstv.setVisibility(View.GONE);
            canel.setVisibility(View.GONE);
            // gotv.setBackground(getResources().getDrawable(R.drawable.linear_layout_bg_tv));
            startimes.setVisibility(View.GONE);
        }
        rootview.findViewById(R.id.watchafricatv).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                IntentIntegrator_tv_watch scanIntegrator = new IntentIntegrator_tv_watch(getActivity());
                scanIntegrator.shareText();
            }
        });
        rootview.findViewById(R.id.dstv).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(vars.active==null) {
                    alerter.login("Financial services", "You are currently logged out, please login or register");
                }else {
                    if (vars.country_code.equalsIgnoreCase("250")) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Slide slideTransition = new Slide(Gravity.LEFT);
                            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                            PayTVActivityfragment firstfragment = new PayTVActivityfragment();
                            Bundle extras = new Bundle();
                            firstfragment.setArguments(extras);
                            firstfragment.setReenterTransition(slideTransition);
                            firstfragment.setExitTransition(slideTransition);
                            firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.tvmenu_container, firstfragment, "MAIN")
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            System.out.println("===========t============");
                            PayTVActivityfragment firstfragment = new PayTVActivityfragment();
                            Bundle extras = new Bundle();
                            firstfragment.setArguments(extras);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.tvmenu_container, firstfragment, "MAIN")
                                    .addToBackStack(null)
                                    .commit();
                        }


                    }else{
                        Toast.makeText(getActivity(),"Not yet available for this country",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        rootview.findViewById(R.id.canelpay).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(vars.active==null) {
                    alerter.login("Financial services", "You are currently logged out, please login or register");
                }else {
                    if (vars.country_code.equalsIgnoreCase("250")) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Slide slideTransition = new Slide(Gravity.LEFT);
                            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                            PayTVActivityfragment firstfragment = new PayTVActivityfragment();
                            Bundle extras = new Bundle();
                            extras.putString("canel", "canel");
                            firstfragment.setArguments(extras);
                            firstfragment.setReenterTransition(slideTransition);
                            firstfragment.setExitTransition(slideTransition);
                            firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.tvmenu_container, firstfragment, "MAIN")
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            System.out.println("===========t============");
                            PayTVActivityfragment firstfragment = new PayTVActivityfragment();
                            Bundle extras = new Bundle();
                            extras.putString("canel", "canel");
                            firstfragment.setArguments(extras);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.tvmenu_container, firstfragment, "MAIN")
                                    .addToBackStack(null)
                                    .commit();
                        }


                    }else{
                        Toast.makeText(getActivity(),"Not yet available for this country",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        rootview.findViewById(R.id.startime).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(vars.active==null) {
                    alerter.login("Financial services", "You are currently logged out, please login or register");
                }else {
                    if (vars.country_code.equalsIgnoreCase("250")) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Slide slideTransition = new Slide(Gravity.LEFT);
                            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                            PayTVActivityfragment firstfragment = new PayTVActivityfragment();
                            Bundle extras = new Bundle();
                            extras.putString("startime","startime");
                            firstfragment.setArguments(extras);
                            firstfragment.setReenterTransition(slideTransition);
                            firstfragment.setExitTransition(slideTransition);
                            firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.tvmenu_container, firstfragment, "MAIN")
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            System.out.println("===========t============");
                            PayTVActivityfragment firstfragment = new PayTVActivityfragment();
                            Bundle extras = new Bundle();
                            extras.putString("startime","startime");
                            firstfragment.setArguments(extras);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.tvmenu_container, firstfragment, "MAIN")
                                    .addToBackStack(null)
                                    .commit();
                        }


                    }else{
                        Toast.makeText(getActivity(),"Not yet available for this country",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return rootview;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onResume(){
       super.onResume();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("TV menu");


    }
}
