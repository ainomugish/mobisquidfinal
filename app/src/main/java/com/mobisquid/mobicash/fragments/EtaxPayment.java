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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;

public class EtaxPayment extends Fragment {
    EditText edit_reference;
    TextView textnote;
    Vars vars;
    View rootview;
    TextInputLayout inp_ref;
    String rssb,irrebo=null;
    ImageView companyimage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(getActivity());
        if (getArguments() != null) {
            rssb = getArguments().getString("rssb");
            irrebo = getArguments().getString("irrebo");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rootview = inflater.inflate(R.layout.activity_etax_payment, container, false);
        companyimage = (ImageView) rootview.findViewById(R.id.companyimage);
        inp_ref = (TextInputLayout) rootview.findViewById(R.id.inp_ref);
        edit_reference = (EditText) rootview.findViewById(R.id.edit_reference);
        textnote =(TextView) rootview.findViewById(R.id.textnote);
        if(rssb!=null){
            companyimage.setImageResource(R.mipmap.ic_rssb);
            textnote.setText("Rwanda Social Security Board, please provide your household ID");
            edit_reference.setHint("Household ID");
        }else if(irrebo!=null){
            companyimage.setImageResource(R.drawable.irembologo);
            textnote.setText("IREMBO services, please provide your bill number");
            edit_reference.setHint("Bill number");
        }else{
            companyimage.setImageResource(R.drawable.rwandarevnue);
            edit_reference.setHint("Reference number");
        }

        Utils.hidekBoard(rootview.findViewById(R.id.drawer_layout),getActivity());
        rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              getActivity().onBackPressed();
            }
        });
        rootview.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rssb!=null){
                    rssbsend();
                }else if(irrebo!=null) {
                    Irrebo();

                }else{
                    RRAsend();
                }
            }
        });
        return rootview;
    }

    public void RRAsend() {
        if (edit_reference.getText().toString().equalsIgnoreCase("")) {
            inp_ref.setError("Reference number required");
            Toast.makeText(getActivity(), "Reference number required", Toast.LENGTH_LONG).show();
        } else {
            inp_ref.setErrorEnabled(false);
            vars.log("Resfr===" + edit_reference.getText().toString());
            String[] parameters = {"ref_number"};
            String[] values = {edit_reference.getText().toString().trim()};
            Alerter.showdialog(getActivity());
            ConnectionClass.ConnectionClass(getActivity(), vars.financial_server + "getRefDetails.php", parameters, values, "RRA", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Alerter.stopdialog();
                    TransactionObj trans = new Gson().fromJson(result, TransactionObj.class);
                    if (trans.getResult().equalsIgnoreCase("Success")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Slide slideTransition = new Slide(Gravity.BOTTOM);
                            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                            PayTaxes_Fragment firstfragment = new PayTaxes_Fragment();
                            Bundle extras = new Bundle();
                            extras.putString("results",result);
                            firstfragment.setArguments(extras);
                            firstfragment.setReenterTransition(slideTransition);
                            firstfragment.setExitTransition(slideTransition);
                            firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.government_container, firstfragment, "MAIN")
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            System.out.println("===========t============");
                            PayTaxes_Fragment firstfragment = new PayTaxes_Fragment();
                            Bundle extras = new Bundle();
                            extras.putString("results",result);
                            firstfragment.setArguments(extras);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.government_container, firstfragment, "MAIN")
                                    .addToBackStack(null)
                                    .commit();
                        }

                    } else {

                        Alerter.Error(getActivity(), "Error", trans.getError());
                    }

                }
            });

        }
    }
    private void rssbsend(){
        if (edit_reference.getText().toString().equalsIgnoreCase("")) {
            inp_ref.setError("Household ID required");
            Toast.makeText(getActivity(), "Household ID required", Toast.LENGTH_LONG).show();
        } else {
            inp_ref.setErrorEnabled(false);

            Alerter.showdialog(getActivity());
            String url = vars.financial_server + "rssbGetDetails.php";
            String[] params = {"householdNID"};
            String[] values = {edit_reference.getText().toString().trim()};
            ConnectionClass.ConnectionClass(getActivity(), url, params, values, "RSSB", new ConnectionClass.VolleyCallback() {
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
                                RssbFragment firstfragment = new RssbFragment();
                                Bundle extras = new Bundle();
                                extras.putString("results", result);
                                firstfragment.setArguments(extras);
                                firstfragment.setReenterTransition(slideTransition);
                                firstfragment.setExitTransition(slideTransition);
                                firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.government_container, firstfragment, "MAIN")
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                System.out.println("===========t============");
                                RssbFragment firstfragment = new RssbFragment();
                                Bundle extras = new Bundle();
                                extras.putString("results", result);
                                firstfragment.setArguments(extras);
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.government_container, firstfragment, "MAIN")
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
    private void Irrebo(){
        if (edit_reference.getText().toString().equalsIgnoreCase("")) {
            inp_ref.setError("Bill number required");
            Toast.makeText(getActivity(), "Bill number required", Toast.LENGTH_LONG).show();
        } else {
            inp_ref.setErrorEnabled(false);

            Alerter.showdialog(getActivity());
            String url = vars.financial_server + "iremboGetRefDetails.php";
            String[] params = {"bill_number"};
            String[] values = {edit_reference.getText().toString().trim()};
            ConnectionClass.ConnectionClass(getActivity(), url, params, values, "IREMBO", new ConnectionClass.VolleyCallback() {
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
                                IramboFragment firstfragment = new IramboFragment();
                                Bundle extras = new Bundle();
                                extras.putString("results", result);
                                firstfragment.setArguments(extras);
                                firstfragment.setReenterTransition(slideTransition);
                                firstfragment.setExitTransition(slideTransition);
                                firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.government_container, firstfragment, "MAIN")
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                System.out.println("===========t============");
                                IramboFragment firstfragment = new IramboFragment();
                                Bundle extras = new Bundle();
                                extras.putString("results", result);
                                firstfragment.setArguments(extras);
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.government_container, firstfragment, "MAIN")
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
    public void onResume(){
        super.onResume();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        if(rssb!=null){
            toolbar.setTitle("RSSB PAY");
        }else if(irrebo!=null){
            toolbar.setTitle("IREMBO PAY");
        }else{
            toolbar.setTitle("RRA PAY");
        }


    }

}
