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
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.model.Transactions;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Success_Fragment extends Fragment {

    TransactionObj mobicoretrans;
    View rootView;
  //  String myemail;
    TextView amount,transid,transtype,notes;
    Vars vars;
    String amountpaid,transtypemsg;

    public Success_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mobicoretrans = new Gson().fromJson(getArguments().getString("result"),TransactionObj.class);
            amountpaid =getArguments().getString("amount");
            transtypemsg =getArguments().getString("type");
            vars.log("res==="+getArguments().getString("result"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.success_fragment, container, false);
        amount = (TextView) rootView.findViewById(R.id.amount);
        transid = (TextView) rootView.findViewById(R.id.transid);
        transtype = (TextView) rootView.findViewById(R.id.transtype);
        notes = (TextView) rootView.findViewById(R.id.notes);

        if (getArguments() != null) {
            amount.setText(amountpaid);
            transtype.setText(transtypemsg);
            transid.setText(mobicoretrans.getTransactionid());
            notes.setText(mobicoretrans.getMessage());
        }
        rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Payment Successful");

    }


}
