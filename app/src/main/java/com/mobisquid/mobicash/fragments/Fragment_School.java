package com.mobisquid.mobicash.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;

public class Fragment_School extends Fragment {
    EditText edit_reference;
    Vars vars;
    View rootview;
    TextInputLayout inp_ref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rootview = inflater.inflate(R.layout.fragment_school, container, false);
        inp_ref = (TextInputLayout) rootview.findViewById(R.id.inp_reg);
        edit_reference = (EditText) rootview.findViewById(R.id.edit_reg_num);

        Utils.hidekBoard(rootview.findViewById(R.id.drawer_layout), getActivity());
        rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        rootview.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SchoolPay();
            }
        });
        return rootview;
    }

    private void SchoolPay() {
        if (edit_reference.getText().toString().equalsIgnoreCase("")) {
            inp_ref.setError("Registration number required");
            Toast.makeText(getActivity(), "Registration number required", Toast.LENGTH_LONG).show();
        } else {
            inp_ref.setErrorEnabled(false);

            Alerter.showdialog(getActivity());
            String url = vars.financial_server + "schoolFeesGetStudentInfo.php";
            String[] params = {"reg_number"};
            String[] values = {edit_reference.getText().toString().trim()};
            ConnectionClass.ConnectionClass(getActivity(), url, params, values, "SCHOOL", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Alerter.stopdialog();
                    JSONObject reader = null;
                    JSONObject details = null;
                    try {
                        reader = new JSONObject(result);
                        String resultant = reader.getString("result");
                        //  String error = reader.getString("error");
                        if (resultant.equalsIgnoreCase("Success")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Slide slideTransition = new Slide(Gravity.BOTTOM);
                                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                                Fragment_School_Payment firstfragment = new Fragment_School_Payment();
                                Bundle extras = new Bundle();
                                extras.putString("results", result);
                                firstfragment.setArguments(extras);
                                firstfragment.setReenterTransition(slideTransition);
                                firstfragment.setExitTransition(slideTransition);
                                firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.school_container, firstfragment, "MAIN")
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                System.out.println("===========t============");
                                Fragment_School_Payment firstfragment = new Fragment_School_Payment();
                                Bundle extras = new Bundle();
                                extras.putString("results", result);
                                firstfragment.setArguments(extras);
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.school_container, firstfragment, "MAIN")
                                        .addToBackStack(null)
                                        .commit();
                            }

                        } else {
                            Alerter.Error(getActivity(), "Error", reader.getString("message"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Registration number");


    }

}
