package com.mobisquid.mobicash.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatCheckBox;
import android.telephony.TelephonyManager;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.FirstActivity;
import com.mobisquid.mobicash.activities.Login;
import com.mobisquid.mobicash.activities.RegistrationActivity;
import com.mobisquid.mobicash.dbstuff.Socialdb;
import com.mobisquid.mobicash.model.ChatClass;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.SendEmailService;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment_Reg extends Fragment {
    View rootview;
    EditText register_confirm_password,register_password,
            register_secondname,register_phone,register_firstName
            ,register_username;
  //  ImageView mImageView;
    String mylocation,number,countrycode,encodedFaceString,accesscode;
    TextInputLayout inp_confpass,inp_password,inp_username,inp_fn,inp_sn;
    TelephonyManager telephonyManager;
    AppCompatCheckBox tc;
    Vars vars;

    public FirstFragment_Reg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        rootview= inflater.inflate(R.layout.fragment_first_fragment__reg, container, false);
        tc = (AppCompatCheckBox) rootview.findViewById(R.id.t_c);
        Utils.hidekBoard(rootview.findViewById(R.id.main_layout),getActivity());
        Utils.hidekBoard(rootview.findViewById(R.id.layout),getActivity());
        inp_confpass = (TextInputLayout) rootview.findViewById(R.id.inp_confpass);
        inp_password= (TextInputLayout) rootview.findViewById(R.id.inp_password);
        inp_username= (TextInputLayout) rootview.findViewById(R.id.inp_username);
        inp_fn= (TextInputLayout) rootview.findViewById(R.id.inp_fn);
        inp_sn= (TextInputLayout) rootview.findViewById(R.id.inp_sn);
        register_secondname =(EditText) rootview.findViewById(R.id.register_secondname);
        register_confirm_password = (EditText) rootview.findViewById(R.id.register_confirm_password);
        register_password = (EditText) rootview.findViewById(R.id.register_password);

        //  register_email = (EditText) findViewById(R.id.register_email);
        register_phone = (EditText) rootview.findViewById(R.id.register_phone);
        register_firstName = (EditText) rootview.findViewById(R.id.register_firstName);
        register_username = (EditText) rootview.findViewById(R.id.register_username);
      //  mImageView= (ImageView) rootview.findViewById(R.id.register_facePic);
        if(getArguments()!=null){
            mylocation = getArguments().getString("location");
            number = getArguments().getString("number");
            countrycode =getArguments().getString("code");

            register_phone.setText(number);
            register_phone.setFocusable(false);
            register_phone.setLongClickable(false);
            vars.log("register_phone===="+number);

        }
    rootview.findViewById(R.id.terms).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Slide slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                TermsandCondiftions firstfragment = new TermsandCondiftions();
                firstfragment.setReenterTransition(slideTransition);
                Bundle extra = new Bundle();
                firstfragment.setArguments(extra);
                firstfragment.setExitTransition(slideTransition);
                firstfragment.setSharedElementEnterTransition(new ChangeBounds());
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, firstfragment, "MAIN")
                        .addToBackStack(null)
                        .commit();
            } else {
                System.out.println("===========t============");
                TermsandCondiftions firstfragment = new TermsandCondiftions();
                Bundle extra = new Bundle();
                firstfragment.setArguments(extra);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, firstfragment, "MAIN").commit();
            }
        }
    });
        rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Confirm!!")
                        .setContentText("Are you sure you want to abort registration?")
                        .setCancelText("Cancel")
                        .setConfirmText("Yes,close")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                android.os.Process.killProcess(android.os.Process
                                        .myPid());

                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();

                            }
                        }).show();

            }
        });
        rootview.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (register_firstName.getText().toString().isEmpty()){
                    inp_fn.setError("Name too empty");

                }else if (register_secondname.getText().toString().isEmpty()) {
                    inp_fn.setError("Name too empty");
                }else if (register_username.getText().toString().length() < 2) {
                    inp_username.setError("Username too short");
                }else if(register_password.getText().toString().equalsIgnoreCase("")){
                    inp_password.setError("Password required");
                }else if(register_confirm_password.getText().toString().equalsIgnoreCase("")){
                    inp_confpass.setError("Password required");
                }else if(!register_confirm_password.getText().toString().equals(register_password.getText().toString())) {
                    inp_confpass.setError("Password don't match");
                    inp_password.setError("Password don't match");
                }else {
                    inp_confpass.setErrorEnabled(false);
                    inp_password.setErrorEnabled(false);
                    inp_username.setErrorEnabled(false);
                    inp_sn.setErrorEnabled(false);
                    inp_fn.setErrorEnabled(false);
                    if (tc.isChecked()) {

                    try {
                        // vars.log("{{{{{"+encodedFaceString+"}}}}}}");
                        Alerter.showdialog(getActivity());
                        String url = Globals.SOCILA_SERV + "entities.chat";
                        JSONObject json = new JSONObject();
                        json.put("accesscode", "no");
                        json.put("created", "no");
                        json.put("code", countrycode);
                        json.put("fullname", register_firstName.getText().toString() + " " +
                                register_secondname.getText().toString());
                        json.put("language", "English");
                        json.put("mobile", register_phone.getText().toString().trim());
                        json.put("imei",getDeviceImei().trim());
                        json.put("password", register_password.getText().toString().trim());
                        //json.put("profileurl", encodedFaceString);
                        json.put("status", "no");
                        json.put("username", register_username.getText().toString().trim());
                        json.put("location", mylocation);
                        vars.log("json=====" + json);

                        ConnectionClass.JsonString(Request.Method.POST, getActivity(), url, json, "Register", new ConnectionClass.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject reader = new JSONObject(result);
                                    Alerter.stopdialog();
                                    if (reader.getString("error").equalsIgnoreCase("no_error")) {
                                        ChatClass servermsg = new Gson().fromJson(reader.getString("chat"), ChatClass.class);
                                        String mobile = servermsg.getMobile();
                                        String newmobile = mobile.substring(4);
                                        newmobile = "0" + newmobile;
                                        vars.log("full name===" + servermsg.getFullname());
                                        vars.log("id=====" + servermsg.getUserid());
                                        accesscode = servermsg.getAccesscode();
                                        vars.edit.putString("userId", String.valueOf(servermsg.getUserid()));
                                        vars.edit.putString("username", servermsg.getFullname());
                                        vars.edit.putString("profile_number", servermsg.getMobile());
                                        vars.edit.putString("social", "social");
                                        vars.edit.putString("language", servermsg.getLanguage());
                                        if (vars.mobile == null) {
                                            vars.edit.putString("mobile", servermsg.getMobile());
                                        }
                                        vars.edit.putString("accesscode", servermsg.getAccesscode());
                                        vars.edit.putString("country_code", servermsg.getCode());
                                        vars.edit.putString("fullname", servermsg.getFullname());
                                        vars.edit.putString("financial_server", Utils.FincialServer(servermsg.getCode()));
                                        vars.edit.putString("profile_url", servermsg.getProfileurl());

                                        vars.log("profile_===" + servermsg.getProfileurl());
                                        vars.edit.commit();
                                        Utils.isFinance(getActivity(),false);
                                        if(Socialdb.listAll(Socialdb.class).isEmpty()){
                                            Socialdb savedb = new Socialdb(servermsg.getUserid(),servermsg.getFullname(),
                                                    servermsg.getMobile(),servermsg.getPrivacy(),
                                                    servermsg.getImei(),servermsg.getLocation(),servermsg.getLongitude(),
                                                    servermsg.getLatitude(),servermsg.getLanguage(),servermsg.getProfileurl(),servermsg.getCode(),
                                                    servermsg.getAccesscode(),servermsg.getStatus(),servermsg.getCreated(),servermsg.getUsername());
                                            savedb.save();
                                        }

                                        Intent sendmail = new Intent(getActivity(), SendEmailService.class);
                                        sendmail.putExtra("fullname", servermsg.getFullname());
                                        sendmail.putExtra("mobile", mobile.replaceAll("\\s+", ""));
                                        getActivity().startService(sendmail);
                                        Alerter.stopdialog();

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            Slide slideTransition = new Slide(Gravity.LEFT);
                                            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                                            FaceImageFragment firstfragment = new FaceImageFragment();
                                            firstfragment.setReenterTransition(slideTransition);
                                            Bundle extra = new Bundle();
                                            extra.putString("chat", reader.getString("chat"));
                                            firstfragment.setArguments(extra);
                                            firstfragment.setExitTransition(slideTransition);
                                            firstfragment.setSharedElementEnterTransition(new ChangeBounds());
                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.fragment_container, firstfragment, "MAIN")
                                                    .addToBackStack(null)
                                                    .commit();
                                        } else {
                                            System.out.println("===========t============");
                                            FaceImageFragment firstfragment = new FaceImageFragment();
                                            Bundle extra = new Bundle();
                                            extra.putString("chat", reader.getString("chat"));
                                            firstfragment.setArguments(extra);
                                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                            ft.replace(R.id.fragment_container, firstfragment, "MAIN").commit();
                                        }

                                    } else {
                                        Alerter.stopdialog();
                                        Alerter.Error(getActivity(), "Error", reader.getString("message"));
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{

                    Toast.makeText(getActivity(),"Terms and conditions not accepted",Toast.LENGTH_LONG).show();
                }
                }

            }
        });
        return rootview;
    }
    @Override
    public void onPause() {
        super.onPause();
        Globals.whichuser="";
    }
    @Override
    public void onResume() {
        super.onResume();
        Globals.whichuser = "registration";
    }
    private String getDeviceImei() {

        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

    }
}
