package com.mobisquid.mobicash.fragments;


import android.content.Intent;
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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendMoney_Fragment extends Fragment {
    View rootview;

    EditText editText_number;
    TextInputLayout inp_mobile;
    RadioGroup which_remitance;
    Vars vars;

    public SendMoney_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vars = new Vars(getActivity());

        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_send_money, container, false);
        which_remitance = (RadioGroup) rootview.findViewById(R.id.which_remitance);
        which_remitance.setVisibility(View.GONE);
        if(vars.country_code.equalsIgnoreCase("27")){
            which_remitance.setVisibility(View.VISIBLE);
        }else{
            which_remitance.setVisibility(View.GONE);
        }

        Utils.hidekBoard(rootview.findViewById(R.id.drawer_layout),getActivity());
        inp_mobile=(TextInputLayout) rootview.findViewById(R.id.inp_mobile);
        editText_number=(EditText) rootview.findViewById(R.id.editText_number);
        rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        rootview.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!vars.Checknumber(getActivity(),vars.country_code,
                        editText_number.getText().toString().trim())){
                    inp_mobile.setError("Invalid number");
                    Toast.makeText(getActivity(),"Invalid number",Toast.LENGTH_SHORT).show();
                }else {
                    checkclient(editText_number.getText().toString().trim());
                }
            }
        });
        return rootview;
    }

    @Override
    public void onResume() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Send Money");
        super.onResume();
    }
    public void checkclient(final String number){
        Alerter.showdialog(getActivity());
        String[] parameters ={"reciever","sender"};
        String[] values ={number,vars.mobile};
        String URL_FEED = vars.financial_server+"CheckMember.php";
        ConnectionClass.ConnectionClass(getActivity(), URL_FEED, parameters, values, "Sendmoney", new ConnectionClass.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Alerter.stopdialog();
                ResultObj resultObj =new Gson().fromJson(result, ResultObj.class);

                if (resultObj.getResult().equalsIgnoreCase("Success")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Slide slideTransition = new Slide(Gravity.BOTTOM);
                        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                        ConfirmTrasferActivity firstfragment = new ConfirmTrasferActivity();
                        Bundle extras = new Bundle();
                        extras.putString("results", result);
                        extras.putString("reg", "no");
                        firstfragment.setArguments(extras);
                        firstfragment.setReenterTransition(slideTransition);
                        firstfragment.setExitTransition(slideTransition);
                        firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.money_container, firstfragment, "MAIN")
                                .addToBackStack(null)
                                .commit();
                    } else {
                        System.out.println("===========t============");
                        ConfirmTrasferActivity firstfragment = new ConfirmTrasferActivity();
                        Bundle extras = new Bundle();
                        extras.putString("results", result);
                        extras.putString("reg", "no");
                        firstfragment.setArguments(extras);
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.money_container, firstfragment, "MAIN")
                                .addToBackStack(null)
                                .commit();
                    }

                } else {
                    if (resultObj.getMessage() != null && resultObj.getMessage().equalsIgnoreCase("failed")) {
                        Alerter.Error(getActivity(),"Error", resultObj.getError());
                    } else {
                        vars.log("=====here====");

                        NotRegistered(number);

                    }
                }


            }
        });

    }

    private void NotRegistered(final String number){
        new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Warning please")
                .setContentText("The Recipient you are trying send money to\n" +editText_number.getText().toString() +" is not yet registered " +
                        "for this service. Good news is you can register them and proceed with the transfer.")
                .setConfirmText("Register")
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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Slide slideTransition = new Slide(Gravity.BOTTOM);
                            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                            Reg_Send_Fragment firstfragment = new Reg_Send_Fragment();
                            Bundle extras = new Bundle();
                            extras.putString("number", number);
                            firstfragment.setArguments(extras);
                            firstfragment.setReenterTransition(slideTransition);
                            firstfragment.setExitTransition(slideTransition);
                            firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.money_container, firstfragment, "MAIN")
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            System.out.println("===========t============");
                            Reg_Send_Fragment firstfragment = new Reg_Send_Fragment();
                            Bundle extras = new Bundle();
                            extras.putString("number", number);
                            firstfragment.setArguments(extras);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.money_container, firstfragment, "MAIN")
                                    .addToBackStack(null)
                                    .commit();
                        }

                    }
                }).show();
    }
    public class ResultObj {

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String message;
        public String result;
        public String error;

    }

}
