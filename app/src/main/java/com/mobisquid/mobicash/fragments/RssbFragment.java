package com.mobisquid.mobicash.fragments;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class RssbFragment extends Fragment {
    View rootview;
    EditText edit_pin,edit_amount;
    TextInputLayout inp_bill_pin,inp_amount;
    Vars vars;
    Alerter alerter;
    TextView creation_date,description,bill_number,amount,service_id,rra_account_name,rra_account_number;
    boolean okaynow =false;

    public RssbFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        alerter= new Alerter(getActivity());
        rootview = inflater.inflate(R.layout.fragment_rssb, container, false);
        inp_amount=(TextInputLayout) rootview.findViewById(R.id.inp_amount);
        inp_bill_pin=(TextInputLayout) rootview.findViewById(R.id.inp_bill_pin);
        edit_pin=(EditText) rootview.findViewById(R.id.edit_pin);
        edit_amount=(EditText) rootview.findViewById(R.id.edit_amount);

        rra_account_number =(TextView) rootview.findViewById(R.id.rra_account_number);
        creation_date =(TextView) rootview.findViewById(R.id.creation_date);
        description =(TextView) rootview.findViewById(R.id.description);
        bill_number =(TextView) rootview.findViewById(R.id.bill_number);
        amount =(TextView) rootview.findViewById(R.id.amount);
        service_id =(TextView) rootview.findViewById(R.id.service_id);
        rra_account_name =(TextView) rootview.findViewById(R.id.rra_account_name);

        rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        rootview.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit_amount.getText().toString().isEmpty()){
                  inp_amount.setError("Put some amount");
                }
                else if(edit_pin.getText().toString().length()<3){
                    inp_bill_pin.setError("Invalid pin");
                    Toast.makeText(getActivity(),"Invalid pin number",Toast.LENGTH_SHORT).show();
                }else {
                    new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Confirm")
                            .setContentText("You are about to make a payment of "+edit_amount.getText().toString()+"."
                                    +"For "+rra_account_name.getText()+" House hold id "+bill_number.getText())
                            .setConfirmText("OK")
                            .setCancelText("Cancel")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Alerter.showdialog(getActivity());
                                    String url =vars.financial_server+"rssbPay.php";
                                    String[] params={"clientNumber","pin","payerName","amount","payerPhone","householdNID"};
                                    String[] values={vars.mobile,edit_pin.getText().toString().trim(),
                                            vars.fullname,edit_amount.getText().toString().trim()
                                            ,vars.mobile,bill_number.getText().toString()};
                                    ConnectionClass.ConnectionClass(getActivity(), url, params, values, "RSSBPAY", new ConnectionClass.VolleyCallback() {
                                        @Override
                                        public void onSuccess(String result) {
                                            Alerter.stopdialog();
                                            TransactionObj trans = new Gson().fromJson(result, TransactionObj.class);
                                            if(trans.getResult().equalsIgnoreCase("Success")){
                                                Alerter.Successpayment(getActivity(), edit_amount.getText().toString().trim(),
                                                        result,"RSSB PAYMENT",R.id.government_container);
                                            }else {
                                                alerter.alerterSuccessSimple(getActivity(),"ERROR",trans.getMessage());
                                            }



                                        }
                                    });
                                }
                            }).show();
                }

            }
        });
        if(getArguments()!=null){
            String result = getArguments().getString("results");
            JSONObject reader = null;
            JSONObject details = null;
            try {
                reader = new JSONObject(result);
                String resultant = reader.getString("result");
                //  String error = reader.getString("error");
                if (resultant.equalsIgnoreCase("Success")) {
                    details = reader.getJSONObject("details");

                    rra_account_number.setText(details.getString("alreadyPaid"));
                    creation_date.setText(details.getString("invoice"));
                    description.setText(details.getString("numberOfMembers"));
                    bill_number.setText(details.getString("householdNID"));
                    amount.setText(details.getString("totalPremium"));
                    service_id.setText(details.getString("eligibility"));
                    rra_account_name .setText(details.getString("name"));

                }else {
                    alerter.alerterSuccessSimple(getActivity(),"Error",reader.getString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rootview;
    }
    @Override
    public void onResume() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("RSSB PAY");
        super.onResume();
    }

}
