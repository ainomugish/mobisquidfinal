package com.mobisquid.mobicash.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.ChatsetUp;
import com.mobisquid.mobicash.activities.MainActivity;
import com.mobisquid.mobicash.dbstuff.ContactDetailsDB;
import com.mobisquid.mobicash.dbstuff.MessageDb;
import com.mobisquid.mobicash.dbstuff.Socialdb;
import com.mobisquid.mobicash.dbstuff.TempStore;
import com.mobisquid.mobicash.dbstuff.Transactiondb;
import com.mobisquid.mobicash.model.ChatClass;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.model.Transactions;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.NotificationUtils;
import com.mobisquid.mobicash.utils.SendEmailService;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ImeiFragment extends Fragment {
    ChatClass servermsg;
    View rootView;
    TextView text;
    String token;
     TelephonyManager telephonyManager;

    Vars vars;

    public ImeiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            servermsg = new Gson().fromJson(getArguments().getString("chat"),ChatClass.class);
            token = getArguments().getString("token");


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_imeiupdate, container, false);
        text = (TextView) rootView.findViewById(R.id.text);


        if (getArguments() != null) {
            text.setText("Hello "+servermsg.getFullname()+"\n We detected another device with your account. Please logout from other device and keep this or keep the other device active and cancel use of this device.");

        }
        rootView.findViewById(R.id.usethis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateDevice();
            }
        });
        rootView.findViewById(R.id.keep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TempStore.deleteAll(TempStore.class);
                MessageDb.deleteAll(MessageDb.class);
                Socialdb.deleteAll(Socialdb.class);
                ContactDetailsDB.deleteAll(ContactDetailsDB.class);
                vars.edit.clear().commit();
                System.exit(0);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        Globals.whichuser="update";
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Social Device update");
    }
    private String getDeviceImei() {

        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

    }
    private void UpdateDevice(MessageDb servermsg){
        String url ="https://support.mobisquid.com/webresources/entities.chat/updatedevice";

        try{
            JSONObject json = new JSONObject(new Gson().toJson(servermsg));
            vars.log("JSON==="+json.toString());
            ConnectionClass.JsonString(Request.Method.POST, getActivity(), url, json, "SENDMESGAE", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Alerter.stopdialog();
                    vars.log("REsults sentmessage==="+result);

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }


    }
    private void UpdateDevice(){
        Alerter.showdialog(getActivity());
        ChatClass chatClass = new ChatClass();
        chatClass.setImei(getDeviceImei().trim());
        chatClass.setMobile(servermsg.getMobile());
        chatClass.setUserid(servermsg.getUserid());
        chatClass.setUsername(servermsg.getUsername());
        chatClass.setToken(token);

        String url = Globals.SOCILA_SERV+"entities.chat/edituser";
        try{
            JSONObject json = new JSONObject(new Gson().toJson(chatClass));
            vars.log("JSON==="+json.toString());
            ConnectionClass.JsonString(Request.Method.PUT, getActivity(), url, json, "DEVICE", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Alerter.stopdialog();
                    try {
                        JSONObject reader = new JSONObject(result);
                        if(reader.getString("error").equalsIgnoreCase("no_error")){
                            MessageDb msg = new MessageDb();

                            msg.setMessageType("clear");
                            msg.setMessage(servermsg.getImei());
                            UpdateDevice(msg);
                            ChatClass servermsg = new Gson().fromJson(reader.getString("chat"),ChatClass.class);
                            savedata(servermsg);

                        }else{
                            Alerter.Error(getActivity(),"Error", reader.getString("message"));
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
    private void savedata(ChatClass servermsg){

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
}
