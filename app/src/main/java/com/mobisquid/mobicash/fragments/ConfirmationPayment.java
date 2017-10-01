package com.mobisquid.mobicash.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.dbstuff.Transactiondb;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.model.Transactions;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.NotificationUtils;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ConfirmationPayment extends Fragment {
    Transactions tranSend;
    TransactionObj mobicoretrans;
    View rootView;
  //  String myemail;
    TextView amount,transid,name,mobile;
    int transactionid;
    Vars vars;
    boolean sent = true;

    public ConfirmationPayment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tranSend = new Gson().fromJson(getArguments().getString("mess"),Transactions.class);
            transactionid = tranSend.getTransid();
           // myemail = getArguments().getString("email");
            mobicoretrans = new Gson().fromJson(getArguments().getString("result"),TransactionObj.class);
            vars.log("res==="+getArguments().getString("result"));
            vars.log("trans=="+getArguments().getString("mess"));
            vars.log("transid======"+transactionid);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_confirmation_payment, container, false);
        amount = (TextView) rootView.findViewById(R.id.amount);
        transid = (TextView) rootView.findViewById(R.id.transid);
        name = (TextView) rootView.findViewById(R.id.name);
        mobile = (TextView) rootView.findViewById(R.id.mobile);

        if (getArguments() != null) {
            amount.setText(tranSend.getAmount());
            name.setText(tranSend.getSenderName());
            transid.setText(mobicoretrans.getTransactionid());
            mobile.setText(tranSend.getSenderMobile());
        }
        rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Payment Successful");
        if(sent){
            sendMessage();
        }
        NotificationUtils.clearNotifications();
        if(!Transactiondb.listAll(Transactiondb.class).isEmpty()){
            Transactiondb.deleteAll(Transactiondb.class);
        }
    }

    private void sendMessage(){
        Alerter.showdialog(getActivity());
        tranSend.setReceiverMobile(mobile.getText().toString().trim());
        tranSend.setSenderMobile(vars.mobile);
        tranSend.setType("paid");
        tranSend.setStatus("paid");
        tranSend.setSenderName(vars.fullname);


        String url ="https://support.mobisquid.com/webresources/entities.transactions/edittrans";
        try{
            JSONObject json = new JSONObject(new Gson().toJson(tranSend));
            vars.log("JSON==="+json.toString());
            ConnectionClass.JsonString(Request.Method.PUT, getActivity(), url, json, "PAYMENR", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Alerter.stopdialog();
                    JSONObject reader = null;
                    try {
                        reader = new JSONObject(result);
                        if(reader.getString("error").equalsIgnoreCase("success")){
                            sent =false;
                            Utils.sendMessage(getActivity(),tranSend, transactionid);

                        }else{

                             new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Error")
                                    .setContentText(reader.getString("message"))
                                     .setCancelText("cancel")
                                     .setConfirmText("Try again")
                                     .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                         @Override
                                         public void onClick(SweetAlertDialog sweetAlertDialog) {
                                             sweetAlertDialog.dismiss();
                                             Utils.sendMessage(getActivity(),tranSend, transactionid);
                                         }
                                     })
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismiss();
                                            sendMessage();
                                            Utils.sendMessage(getActivity(),tranSend, transactionid);
                                        }
                                    }).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
