package com.mobisquid.mobicash.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.RegistrationActivity;
import com.mobisquid.mobicash.model.ChatClass;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePassword extends Fragment {
    View rootview;
    ChatClass chatClass;
    EditText username, password,confimpass;
    TextInputLayout inp_pass,inp_confp;
    Vars vars;


    public ChangePassword() {
        // Required empty public constructor
    }

//{"mobile":"2567736300170","password":"password","userid":4,"username":"kutso9"}
    //webresources/entities.chat/edituser)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        if(getArguments()!=null){
           chatClass = new Gson().fromJson(getArguments().getString("chat"),ChatClass.class);
        }
        rootview = inflater.inflate(R.layout.fragment_change_password, container, false);
        inp_confp = (TextInputLayout) rootview.findViewById(R.id.inp_confpass);
        inp_pass = (TextInputLayout) rootview.findViewById(R.id.inp_newp);
        username = (EditText) rootview.findViewById(R.id.username);
        password = (EditText) rootview.findViewById(R.id.password);
        confimpass = (EditText) rootview.findViewById(R.id.conf_pass);
        username.setText(chatClass.getUsername());
        username.setFocusable(false);
        username.setLongClickable(false);
        rootview.findViewById(R.id.changepass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().isEmpty() || confimpass.getText().toString().isEmpty()){
                    inp_confp.setError("Fill all fields");
                    inp_pass.setError("Fill all fields");
                }else if(!password.getText().toString().equals(confimpass.getText().toString())){
                    inp_confp.setError("Passwords don't match");
                    inp_pass.setError("Passwords don't match");
                }else {
                    inp_confp.setErrorEnabled(false);
                    inp_pass.setErrorEnabled(false);
                    Alerter.showdialog(getActivity());
                    try{

                    String url = Globals.SOCILA_SERV+"entities.chat/edituser";

                        JSONObject json = new JSONObject();
                        json.put("mobile", chatClass.getMobile());
                        json.put("password", password.getText().toString().trim());
                        json.put("userid", chatClass.getUserid());
                        json.put("username", chatClass.getUsername());
                    ConnectionClass.JsonString(Request.Method.PUT,getActivity(), url, json, "CHANGE", new ConnectionClass.VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                                    Alerter.stopdialog();
                                    try {
                                        JSONObject reader = new JSONObject(result);
                                        if (reader.getString("error").equalsIgnoreCase("no_error")) {
                                           new SweetAlertDialog(getActivity(),SweetAlertDialog.SUCCESS_TYPE)
                                                   .setTitleText("Success!")
                                                   .setContentText("Your password has been successfully changed. Please login with your username as "+chatClass.getUsername()+" and your new password")
                                                   .setConfirmText("Login")
                                                   .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                       @Override
                                                       public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                           sweetAlertDialog.dismiss();
                                                           Intent login = new Intent(getActivity(), RegistrationActivity.class);
                                                           login.putExtra("login","login");
                                                           getActivity().startActivity(login);
                                                           getActivity().finish();
                                                       }
                                                   }).show();

                                        } else if(reader.getString("error").equalsIgnoreCase("error")) {
                                            Alerter.Error(getActivity(), "Error", reader.getString("message"));

                                        } else {
                                            vars.log("return ==== " + result);
                                            Alerter.Error(getActivity(), "Error", "Please check your internet connection");
                                        }
                                    }catch (JSONException e) {
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

    @Override
    public void onResume() {
        super.onResume();
        Globals.whichuser="changepass";
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Change Password");
    }

}
