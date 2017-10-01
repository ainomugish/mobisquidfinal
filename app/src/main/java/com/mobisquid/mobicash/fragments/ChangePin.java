package com.mobisquid.mobicash.fragments;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Vars;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePin extends Fragment {
    View rootview;
    EditText editText_retrynewpin,editText_newpin,editText_oldpin;
    TextInputLayout inp_retry,inp_old,inp_pin;
    Vars vars;
    String url;


    public ChangePin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vars = new Vars(getActivity());
        rootview = inflater.inflate(R.layout.fragment_change_pin, container, false);
        inp_retry = (TextInputLayout) rootview.findViewById(R.id.inp_retry);
        inp_old= (TextInputLayout) rootview.findViewById(R.id.inp_old);
        inp_pin= (TextInputLayout) rootview.findViewById(R.id.inp_pin);
        editText_retrynewpin = (EditText) rootview.findViewById(R.id.editText_retrynewpin);
        editText_newpin = (EditText) rootview.findViewById(R.id.editText_newpin);
        editText_oldpin = (EditText) rootview.findViewById(R.id.editText_oldpin);
        rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              getActivity().onBackPressed();
            }
        });
        rootview.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText_oldpin.getText().length()<4){
                    inp_old.setError("Pin invalid");

                }
                else if(editText_newpin.getText().length()<4){
                    inp_pin.setError("Pin invalid");
                    inp_old.setErrorEnabled(false);
                }
                else if(!editText_newpin.getText().toString().equals(editText_retrynewpin.getText().toString())) {
                    inp_pin.setError("Pin invalid don't match");
                    inp_retry.setError("Pin invalid don't match");
                }else{
                    inp_retry.setErrorEnabled(false);
                    inp_pin.setErrorEnabled(false);
                    inp_old.setErrorEnabled(false);
                    new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Confirm")
                            .setContentText("Are you sure you want to change your pin?")
                            .setConfirmText("Yes,Change")
                            .setCancelText("No")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    getActivity().onBackPressed();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    change();
                                }
                            }).show();

                }

            }
        });
        return rootview;
    }
    private void change(){
        Alerter.showdialog(getActivity());
        url = vars.financial_server + "ClientChangePin.php";
        String[] params={"OldPin","clientNumber","NewPin"};
        String[] values={editText_oldpin.getText().toString().trim(),vars.mobile,editText_newpin.getText().toString().trim()};

        ConnectionClass.ConnectionClass(getActivity(), url, params, values, "CHANGEP", new ConnectionClass.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Alerter.stopdialog();
                TransactionObj trans = new Gson().fromJson(result,TransactionObj.class);

                if(trans.getResult().equalsIgnoreCase("Success")){
                    new SweetAlertDialog(getActivity(),SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText("Your pin has been successfully changed")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    getActivity().onBackPressed();
                                }
                            }).show();

                }else{

                    Alerter.Error(getActivity(),"error", trans.getError());
                }

            }
        });
    }
    @Override
    public void onResume() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Settings Change pin");

        super.onResume();
    }
}
