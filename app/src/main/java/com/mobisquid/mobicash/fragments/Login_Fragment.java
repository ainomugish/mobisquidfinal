package com.mobisquid.mobicash.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.ChatsetUp;
import com.mobisquid.mobicash.activities.FirstActivity;
import com.mobisquid.mobicash.activities.Login;
import com.mobisquid.mobicash.activities.MainActivity;
import com.mobisquid.mobicash.activities.SmsActivity;
import com.mobisquid.mobicash.dbstuff.Apps;
import com.mobisquid.mobicash.dbstuff.ContactDetailsDB;
import com.mobisquid.mobicash.dbstuff.Socialdb;
import com.mobisquid.mobicash.model.ChatClass;
import com.mobisquid.mobicash.model.NewApp;
import com.mobisquid.mobicash.mqtt.GcmIntentService;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.Appinstallation;
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
public class Login_Fragment extends Fragment {
    View rootview;
    TextInputLayout inp_password,inp_username;
    EditText password,username;
    Vars vars;
    String token;
    Button join ;
    TelephonyManager telephonyManager;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public Login_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        rootview = inflater.inflate(R.layout.login_fragment, container, false);
        join = (Button) rootview.findViewById(R.id.join);
        join.setVisibility(View.GONE);
        Utils.hidekBoard(rootview.findViewById(R.id.main_layout),getActivity());
        Utils.hidekBoard(rootview.findViewById(R.id.image),getActivity());
        Utils.hidekBoard(rootview.findViewById(R.id.main),getActivity());
        username =(EditText) rootview.findViewById(R.id.username);
        password =(EditText) rootview.findViewById(R.id.password);
        inp_password =(TextInputLayout) rootview.findViewById(R.id.inp_password);
        inp_username =(TextInputLayout) rootview.findViewById(R.id.inp_username);

        rootview.findViewById(R.id.forgot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent notice = new Intent(getActivity(),SmsActivity.class);
                notice.putExtra("changepass","changepass");
                notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(getActivity(), rootview.findViewById(R.id.image), "login");
                ActivityCompat.startActivity(getActivity(), notice, options.toBundle());


            }
        });
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                vars.log("========onReceive========");
                // checking for type intent filter
                if (intent.getAction().equals(Globals.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    token = intent.getStringExtra("token");
                    vars.edit.putString("token",token);
                    vars.edit.apply();
                    //Toast.makeText(RegistrationActivity.this, "GCM registration token: " + token, Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Globals.SENT_TOKEN_TO_SERVER)) {

                }
            }
        };
        if (token == null) {
            vars.log("======checkPlayServices========");
            registerGCM();
        }

        if(vars.username!=null){
            join.setVisibility(View.GONE);
        }else{
            join.setVisibility(View.VISIBLE);
        }

        rootview.findViewById(R.id.join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /*Intent chk = new Intent(getActivity(), FirstActivity.class);
                chk.putExtra("location","Rwanda");
                chk.putExtra("number","+250784700900");
                chk.putExtra("code","250");
                startActivity(chk);*/


               Intent notice = new Intent(getActivity(),SmsActivity.class);
                notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(getActivity(), rootview.findViewById(R.id.image), "login");
                ActivityCompat.startActivity(getActivity(), notice, options.toBundle());
            }
        });
        rootview.findViewById(R.id.frqquestion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vars.log("===========asked=============");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide slideTransition = new Slide(Gravity.LEFT);
                    slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                    Frequentlyasked firstfragment = new Frequentlyasked();
                    firstfragment.setReenterTransition(slideTransition);
                    firstfragment.setExitTransition(slideTransition);
                    firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                } else {
                    System.out.println("===========t============");
                    Frequentlyasked firstfragment = new Frequentlyasked();
                    firstfragment.setArguments(getArguments());
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                }

            }
        });
        rootview.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vars.log("==========login==============");
                if(username.getText().toString().isEmpty()){
                  inp_username.setError("Username can't be empty");
                }else if(password.getText().toString().isEmpty()){
                    inp_password.setError("Password can't be empty");
                    inp_username.setErrorEnabled(false);
                }else if(vars.username!=null && !username.getText().toString().trim().equals(vars.username)) {
                    new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Whoops!!")
                            .setContentText("Expected username for \n"+vars.fullname +"\nPlease input the username and try again")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            }).show();
                } else{
                    inp_username.setErrorEnabled(false);
                    inp_password.setErrorEnabled(false);
                    try{
                        Alerter.showdialog(getActivity());
                        String url = Globals.SOCILA_SERV+"entities.chat/login";
                        JSONObject json = new JSONObject();
                        json.put("username", username.getText().toString().trim());
                        json.put("password", password.getText().toString().trim());
                        ConnectionClass.JsonString(Request.Method.POST,getActivity(), url, json, "LOGIN", new ConnectionClass.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                               Alerter.stopdialog();
                                try {
                                    JSONObject reader = new JSONObject(result);
                                    if(reader.getString("error").equalsIgnoreCase("no_error")){

                                        ChatClass servermsg = new Gson().fromJson(reader.getString("chat"),ChatClass.class);
                                        if(servermsg.getImei()==null){
                                            savedata(servermsg,true);
                                        }else {
                                            if(!servermsg.getImei().equalsIgnoreCase(getDeviceImei().trim())){
                                                savedata(servermsg,false);
                                            }else{
                                                savedata(servermsg);
                                            }
                                        }


                                    }else{
                                        Alerter.Error(getActivity(),"Error", reader.getString("message"));
                                    }


                                } catch (JSONException e) {
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
    private String getDeviceImei() {

        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

    }
    private void savedata(final ChatClass servermsg, boolean update){
        if(update){
            Alerter.showdialog(getActivity());
            String url = Globals.SOCILA_SERV+"entities.chat/edituser";
            try {
                JSONObject json = new JSONObject();
                json.put("imei", getDeviceImei().trim());
                json.put("token", token);
                json.put("userid", servermsg.getUserid());
                json.put("username",servermsg.getUsername());
                json.put("mobile", servermsg.getMobile());

                ConnectionClass.JsonString(Request.Method.PUT, getActivity(), url, json, "IMEI", new ConnectionClass.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                      Alerter.stopdialog();
                        vars.log("Imei update===========" + result);
                        savedata(servermsg);

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            Bundle extras = new Bundle();
            extras.putString("chat",new Gson().toJson(servermsg));
            extras.putString("token",token);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Slide slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                ImeiFragment firstfragment = new ImeiFragment();
                firstfragment.setArguments(extras);
                firstfragment.setReenterTransition(slideTransition);
                firstfragment.setExitTransition(slideTransition);
                firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, firstfragment, "MAIN")
                        .commit();
            } else {
                System.out.println("===========t============");
                ImeiFragment firstfragment = new ImeiFragment();
                firstfragment.setArguments(extras);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, firstfragment, "MAIN")
                        .commit();
            }
        }

    }

    private void savedata(ChatClass servermsg){
        String mobile = servermsg.getMobile();
        vars.log("full name===" + servermsg.getFullname());
        vars.log("id=====" + servermsg.getUserid());

        vars.edit.putString("userId", String.valueOf(servermsg.getUserid()));
        vars.edit.putString("username", servermsg.getUsername());
        vars.edit.putString("profile_number",servermsg.getMobile());
        vars.edit.putString("social", "social");
        vars.edit.putString("language", servermsg.getLanguage());
        //vars.edit.putString("imei", tel.getDeviceId().toString());
        if(vars.mobile==null){
            vars.edit.putString("mobile", servermsg.getMobile());
        }

        vars.edit.putString("accesscode",servermsg.getAccesscode());
        vars.edit.putString("country_code", servermsg.getCode());
        vars.edit.putString("financial_server", Utils.FincialServer(servermsg.getCode()));
        vars.edit.putString("fullname", servermsg.getFullname());
        vars.edit.putString("profile_url", servermsg.getProfileurl());
        vars.log("profile_===" + servermsg.getProfileurl());
        vars.edit.commit();
        updateUser(token, String.valueOf(servermsg.getUserid()), servermsg.getMobile(), servermsg.getUsername());
        Utils.isFinance(getActivity(),false);
        Intent sendmail = new Intent(getActivity(), SendEmailService.class);
        sendmail.putExtra("fullname",servermsg.getFullname());
        sendmail.putExtra("mobile", servermsg.getMobile());
        getActivity().startService(sendmail);

        if(Socialdb.listAll(Socialdb.class).isEmpty()){
            Socialdb savedb = new Socialdb(servermsg.getUserid(),servermsg.getFullname(),
                    servermsg.getMobile(),servermsg.getPrivacy(),
                    servermsg.getImei(),servermsg.getLocation(),servermsg.getLongitude(),
                    servermsg.getLatitude(),servermsg.getLanguage(),servermsg.getProfileurl(),servermsg.getCode(),
                    servermsg.getAccesscode(),servermsg.getStatus(),servermsg.getCreated(),servermsg.getUsername());
            savedb.save();
        }

        if(ContactDetailsDB.listAll(ContactDetailsDB.class).isEmpty()){
            Intent notice = new Intent(getActivity(), ChatsetUp.class);
            // notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(getActivity()
                            , getActivity().findViewById(R.id.image), "login");
            ActivityCompat.startActivity(getActivity(), notice, options.toBundle());
            getActivity().finish();
        }else{
            Intent main = new Intent(getActivity(), MainActivity.class);
            startActivity(main);
            getActivity().finish();
        }

    }
    private void registerGCM() {
        Intent intent = new Intent(getActivity(), GcmIntentService.class);
        intent.putExtra("key", "register");
        getActivity().startService(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        Globals.whichuser="login";
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Social Login|Registration");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Globals.REGISTRATION_COMPLETE));
    }

    @Override
    public void onPause() {
        super.onPause();
        Globals.whichuser="";
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
    private void updateUser(String token, String chk, String mobile, String username) {
        String url = Globals.SOCILA_SERV+"entities.chat/edituser";
        try {
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("token", token);
            json.put("userid", chk);
            json.put("mobile", mobile);

            ConnectionClass.JsonString(Request.Method.PUT, getActivity(), url, json, "FIREBASETOKEN", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    vars.log("Firebase reg===========" + result);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
