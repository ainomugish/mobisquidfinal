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

public class Fragment_School_Payment extends Fragment {
    Vars vars;
    View rootview;
    EditText edit_reg_num,edit_amount,edit_paid,edit_pin;
    TextInputLayout inp_reg,inp_amount,inp_paid,inp_pin;
    TextView studentName,regNumber,schoolName;
    String schoolcode;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rootview = inflater.inflate(R.layout.fragment_school_pay, container, false);

        studentName =(TextView) rootview.findViewById(R.id.studentName);
        regNumber =(TextView) rootview.findViewById(R.id.regNumber);
        schoolName =(TextView) rootview.findViewById(R.id.schoolName);
        edit_reg_num = (EditText) rootview.findViewById(R.id.edit_reg_num);
        edit_amount = (EditText) rootview.findViewById(R.id.edit_amount);
        edit_paid = (EditText) rootview.findViewById(R.id.edit_paid);
        edit_pin = (EditText) rootview.findViewById(R.id.edit_pin);

        inp_reg =(TextInputLayout) rootview.findViewById(R.id.inp_reg);
        inp_amount =(TextInputLayout) rootview.findViewById(R.id.inp_amount);
        inp_paid =(TextInputLayout) rootview.findViewById(R.id.inp_paid);
        inp_pin =(TextInputLayout) rootview.findViewById(R.id.inp_pin);

        Utils.hidekBoard(rootview.findViewById(R.id.activity_school),getActivity());
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
        if(getArguments()!=null) {
            String result = getArguments().getString("results");
            JSONObject reader = null;
            JSONObject details = null;
            try {
                reader = new JSONObject(result);
                String resultant = reader.getString("result");

                if (resultant.equalsIgnoreCase("Success")) {
                    studentName.setText(reader.getString("studentName"));
                    regNumber.setText(reader.getString("regNumber"));
                    schoolName.setText(reader.getString("schoolName"));
                    schoolcode =reader.getString("schoolCode");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return rootview;
    }

    private void SchoolPay(){
        if (edit_amount.getText().toString().equalsIgnoreCase("")) {
            inp_amount.setError("Amount to pay required");
        } else if (edit_paid.getText().toString().equalsIgnoreCase("")) {
            inp_paid.setError("Amount already paid required");
        } else if (edit_pin.getText().toString().length() < 3) {
            inp_pin.setError("Invalid pin");

        } else {
            inp_amount.setErrorEnabled(false);
            inp_pin.setErrorEnabled(false);
            inp_paid.setErrorEnabled(false);

            new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Confirm")
                    .setCancelText("Cancel")
                    .setContentText("You are about to pay "+edit_amount.getText().toString()
                            +" for "+schoolName.getText().toString()+" Reg.no "+regNumber.getText().toString())
                    .setConfirmText("OK")
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
                            String url = vars.financial_server + "schoolFeesPaySchoolFees.php";
                            String[] params = {"regNumber", "studentName", "schoolName", "schoolCode", "amountToPay"
                                    , "amountPaid", "payername", "customerphone", "clientNumber", "pin"};
                            String[] values = {regNumber.getText().toString(), studentName.getText().toString(),
                                    schoolName.getText().toString(), schoolcode, edit_amount.getText().toString().trim(),
                                    edit_paid.getText().toString().trim(), vars.fullname, vars.mobile, vars.mobile,
                                    edit_pin.getText().toString().trim()};
                            ConnectionClass.ConnectionClass(getActivity(), url, params, values, "Schoolpay", new ConnectionClass.VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    Alerter.stopdialog();
                                    TransactionObj trans = new Gson().fromJson(result, TransactionObj.class);
                                    if (trans.getResult().equalsIgnoreCase("Success")) {
                                        Alerter.Successpayment(getActivity(), edit_amount.getText().toString().trim(),
                                                result,"SCHOOL FEES PAYMENT",R.id.school_container);
                                    } else {
                                        Alerter.Error(getActivity(), "ERROR", trans.getMessage());
                                    }

                                }
                            });

                        }
                    }).show();

        }
    }
    @Override
    public void onResume(){
        super.onResume();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("School fees payment");


    }

}
