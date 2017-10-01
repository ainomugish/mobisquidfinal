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
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class IramboFragment extends Fragment {
    View rootview;
    LinearLayout payment,pin_lay;
    EditText edit_pin;
    TextInputLayout inp_bill_pin;
    Vars vars;
    Alerter alerter;
    TextView creation_date,description,bill_number,amount,service_id,rra_account_name,rra_account_number;
    boolean okaynow =false;
    public IramboFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        alerter= new Alerter(getActivity());
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_irambo, container, false);
        payment = (LinearLayout) rootview.findViewById(R.id.payment);
        Utils.hidekBoard(payment,getActivity());
        pin_lay= (LinearLayout) rootview.findViewById(R.id.pin_lay);

        inp_bill_pin=(TextInputLayout) rootview.findViewById(R.id.inp_bill_pin);

        edit_pin=(EditText) rootview.findViewById(R.id.edit_pin);

        rra_account_number =(TextView) rootview.findViewById(R.id.rra_account_number);
        creation_date =(TextView) rootview.findViewById(R.id.creation_date);
        description =(TextView) rootview.findViewById(R.id.description);
        bill_number =(TextView) rootview.findViewById(R.id.bill_number);
        amount =(TextView) rootview.findViewById(R.id.amount);
        service_id =(TextView) rootview.findViewById(R.id.service_id);
        rra_account_name =(TextView) rootview.findViewById(R.id.rra_account_name);

        if(getArguments()!=null){
            String result = getArguments().getString("results");
            JSONObject reader = null;
            JSONObject details = null;
            try {
                reader = new JSONObject(result);
                String resultant = reader.getString("result");
                String error = reader.getString("error");
                if (resultant.equalsIgnoreCase("Success")) {
                    payment.setVisibility(View.VISIBLE);

                    rra_account_number.setText(reader.getString("rra_account_number"));
                    creation_date.setText(reader.getString("creation_date"));
                    description.setText(reader.getString("description"));
                    bill_number.setText(reader.getString("bill_number"));
                    amount.setText(reader.getString("amount"));
                    service_id.setText(reader.getString("service_id"));
                    rra_account_name .setText(reader.getString("rra_account_name"));

                }else {
                    alerter.alerterSuccessSimple(getActivity(),"Error",error);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        rootview.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit_pin.getText().toString().length()<3){
                    inp_bill_pin.setError("Invalid pin");
                    Toast.makeText(getActivity(),"Invalid pin number",Toast.LENGTH_SHORT).show();
                }else {
                    new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Confirm")
                            .setContentText("You are about to make a payment of "+amount.getText()+"."+"For bill number "+bill_number.getText())
                            .setConfirmText("OK")
                            .setCancelText("Cancel")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    okaynow=false;
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Alerter.showdialog(getActivity());
                                    String url =vars.financial_server+"iremboPay.php";
                                    String[] params={"clientNumber","pin","billNumber","accountNumber","paidAmount",
                                            "paymentStatus","paymentType","payerName","payerPhone"};
                                    String[] values={vars.mobile,edit_pin.getText().toString().trim(),bill_number.getText().toString(),
                                            rra_account_number.getText().toString(),amount.getText().toString(),"approved","cash",vars.fullname,vars.mobile};
                                    ConnectionClass.ConnectionClass(getActivity(), url, params, values, "IREMBOPAY", new ConnectionClass.VolleyCallback() {
                                        @Override
                                        public void onSuccess(String result) {
                                            Alerter.stopdialog();
                                            TransactionObj trans = new Gson().fromJson(result, TransactionObj.class);
                                            if(trans.getResult().equalsIgnoreCase("Success")){
                                                Alerter.Successpayment(getActivity(), amount.getText().toString(),
                                                        result,"IREMBO PAYMENT",R.id.government_container);

                                              /*  new SweetAlertDialog(getActivity(),SweetAlertDialog.SUCCESS_TYPE)
                                                        .setTitleText("Success")
                                                        .setContentText(trans.getMessage())
                                                        .setConfirmText("OK")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                sweetAlertDialog.dismiss();
                                                                getActivity().onBackPressed();
                                                                okaynow=false;

                                                            }
                                                        }).show();*/
                                            }else {
                                                alerter.alerterSuccessSimple(getActivity(),"ERROR",trans.getError());
                                            }



                                        }
                                    });
                                }
                            }).show();
                }
            }
        });
        return rootview;
    }

    @Override
    public void onResume() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("IREMBO PAY");
        super.onResume();
    }
}
