package com.mobisquid.mobicash.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.TermsAndConditions;
import com.mobisquid.mobicash.utils.Vars;

/**
 * A simple {@link Fragment} subclass.
 */
public class First_Fragment extends Fragment {
    LinearLayout linearLayout1,support_img,login_img,join_img;
    View rootview;
    Vars vars;
    ViewTreeObserver observer;
    ViewGroup.LayoutParams params,paramsl,paramsj;
    public First_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        rootview = inflater.inflate(R.layout.fragment_first_, container, false);

        rootview.findViewById(R.id.frqquestion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vars.log("===========asked=============");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide slideTransition = new Slide(Gravity.LEFT);
                    slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                    Frequentlyasked firstfragment = new Frequentlyasked();
                    firstfragment.setReenterTransition(slideTransition);
                    firstfragment.setExitTransition(slideTransition);
                    firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                } else {
                    System.out.println("===========t============");
                    Frequentlyasked firstfragment = new Frequentlyasked();
                    firstfragment.setArguments(getArguments());
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                }

            }
        });

        rootview.findViewById(R.id.tour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vars.log("==========login==============");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide slideTransition = new Slide(Gravity.LEFT);
                    slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                    TourFragment firstfragment = new TourFragment();
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
                    firstfragment.setArguments(getArguments());
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

       /* linearLayout1 =(LinearLayout)  rootview.findViewById(R.id.support);
        support_img =(LinearLayout)  rootview.findViewById(R.id.support_img);
        params = support_img.getLayoutParams();
        join_img =(LinearLayout)  rootview.findViewById(R.id.join_img);
        paramsj = join_img.getLayoutParams();
        login_img =(LinearLayout)  rootview.findViewById(R.id.login_img);
        paramsl = login_img.getLayoutParams();

        observer = linearLayout1.getViewTreeObserver();
        rootview.findViewById(R.id.join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vars.log("===========join=============");
                Intent reg = new Intent(getActivity(), TermsAndConditions.class);
                startActivity(reg);
                getActivity().finish();

            }
        });
        rootview.findViewById(R.id.supporting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vars.log("==========support==============");
            }
        });
        rootview.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vars.log("==========login==============");
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
                    firstfragment.setArguments(getArguments());
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                }
            }
        });*/
        return rootview;
    }
   /* protected void init() {
        params.height =linearLayout1.getHeight();
        paramsl.height =linearLayout1.getHeight();
        paramsj.height =linearLayout1.getHeight();
    }*/

    @Override
    public void onResume() {
        super.onResume();
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
       /* observer = linearLayout1.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                init();
                linearLayout1.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
            }
        });*/
    }
}
