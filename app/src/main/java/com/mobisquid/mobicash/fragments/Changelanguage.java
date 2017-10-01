package com.mobisquid.mobicash.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.Laguages;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Changelanguage extends Fragment {
    View rootview;
    Spinner spn_language;
    ArrayAdapter<String> lagadap;
    String selected_lag;
    static final String TAG = Changelanguage.class.getSimpleName();
    Vars vars;

    public Changelanguage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vars = new Vars(getActivity());
        rootview = inflater.inflate(R.layout.fragment_changelanguage, container, false);
        spn_language = (Spinner) rootview.findViewById(R.id.language);
        lagadap = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Laguages.MUSTER);
        spn_language.setAdapter(lagadap);
        spn_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_lag = lagadap.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        rootview.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vars.language.equalsIgnoreCase(selected_lag)) {
                    Toast.makeText(getActivity(), selected_lag + " already your default language", Toast.LENGTH_LONG).show();
                } else {
                    String url = Globals.SOCILA_SERV+"entities.chat/edituser";
                    Alerter.showdialog(getActivity());
                    try {
                        JSONObject json = new JSONObject();
                        json.put("username", vars.username);
                        json.put("userid", vars.chk);
                        json.put("mobile", vars.mobile);
                        json.put("language", selected_lag);
                        ConnectionClass.JsonString(Request.Method.PUT, getActivity(), url, json, "CHANGE LAG", new ConnectionClass.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Log.i(TAG, "lang feed===========" + result);
                                Alerter.stopdialog();
                                try {
                                    JSONObject reader = new JSONObject(result);
                                    if (reader.getString("error").equalsIgnoreCase("no_error")) {
                                        Toast.makeText(getActivity(), selected_lag + " updated successfully", Toast.LENGTH_LONG).show();
                                        vars.edit.putString("language", selected_lag);
                                        vars.edit.apply();
                                        getActivity().finish();
                                    } else {
                                        Toast.makeText(getActivity(), "Something went wrong try again..", Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        return rootview;
    }

}
