package com.mobisquid.mobicash.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.dbstuff.ContactDetailsDB;
import com.mobisquid.mobicash.model.ChatClass;
import com.mobisquid.mobicash.model.ContactsDb;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.ContactsManager;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.SynContactsBackground;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChatsetUp extends AppCompatActivity {
    TextView status;
    Vars vars;
    int listofcontacts =0;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    static final String TAG = ChatsetUp.class.getSimpleName();
    static int PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(this);
        setContentView(R.layout.activity_chatset_up);
        status = (TextView) findViewById(R.id.status);
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                        PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {
                if(vars.chk!=null && vars.accesscode!=null) {
                    snyncontacts(vars.chk, vars.accesscode);
                }else{
                    status.setText("We can not proceed without you login to social");
                }

            }

        }else{
            if(vars.chk!=null && vars.accesscode!=null) {
                snyncontacts(vars.chk, vars.accesscode);
            }else{
                status.setText("We can not proceed without you login to social");
            }
        }



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Globals.PUSH_STATUS)) {
                   status.setText(intent.getExtras().getString("status"));

                }

            }
        };


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(vars.chk!=null && vars.accesscode!=null) {
                snyncontacts(vars.chk, vars.accesscode);
            }else{
                status.setText("We can not proceed without you login to social");
            }

        } else {
            new SweetAlertDialog(ChatsetUp.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Please note!!")
                    .setContentText("Mobisquid populates your contact list based on your phone contacts.")
                    .setCancelText("Don't allow")
                    .setConfirmText("Yes,allow")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                                            PERMISSIONS_REQUEST_READ_CONTACTS);
                                } else {
                                    if(vars.chk!=null && vars.accesscode!=null) {
                                        snyncontacts(vars.chk, vars.accesscode);
                                    }else{
                                        status.setText("We can not proceed without you login to social");
                                    }
                                }
                            }
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            onBackPressed();
                        }
                    }).show();
        }
    }
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();

    }
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Globals.PUSH_NOTIFICATION));
        Log.i(TAG,"====resume====");

    }
    public void snyncontacts(String userid,String accesscode){
        final List<String> str = new ArrayList<>();
        final ArrayList<ContactsDb> necontactlist = new ArrayList<>();
        final ArrayList<ContactsDb> online_contact= new ArrayList<>();
        online_contact.clear();
        necontactlist.clear();
        status.setText("Fetching Contacts...");

        String url = Globals.SOCILA_SERV+"entities.chat/allusers";

        try{
            JSONObject json = new JSONObject();
            json.put("accesscode", accesscode);
            json.put("userid",userid);
            ConnectionClass.JsonString(Request.Method.POST,this, url, json, "Contacts", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONArray detailsArray = new JSONArray(result);
                        if(detailsArray.length()==0){
                            status.setText("Something went wrong.");
                            Toast.makeText(ChatsetUp.this,"Could not get contacts", Toast.LENGTH_LONG).show();
                        }else{
                            status.setText("Populating contacts...");
                            Log.e("Main","==online users==" + detailsArray.length());
                            for (int i = 0; i < detailsArray.length(); i++) {
                                ChatClass theObject =new Gson().fromJson(detailsArray.getString(i),
                                        ChatClass.class);
                                online_contact.add(new ContactsDb(theObject.getUsername(), theObject.getMobile(),
                                        String.valueOf(theObject.getUserid()), theObject.getProfileurl(), "offline",
                                        "offline", theObject.getLanguage(),"fake"));
                            }

                            if (online_contact.size() == 0) {
                                Log.e("main","==========online_contact========is=0=");
                            } else {
                                Log.e("main","==========online_contact======not empty");

                                if(ContactDetailsDB.listAll(ContactDetailsDB.class).isEmpty()){
                                    Log.i(TAG,"========contacts are=== empty");
                                    new SynContactsBackground(ChatsetUp.this,online_contact, new SynContactsBackground.AsyncResponse() {
                                        @Override
                                        public void processFinish(ArrayList<ContactsDb> output) {
                                            listofcontacts = output.size();
                                            if(output.size()>0){
                                                status.setText("Found "+output.size()+" contacts to chat with....");
                                            }else {
                                                status.setText("Found no contacts....");
                                            }
                                           Log.e("main","++++++++++++++++++++++++++++" + output.size());
                                            for(ContactsDb db :output){
                                                ContactsManager.addContact(ChatsetUp.this, db);
                                                Log.e("INLIST",db.getMobile()+"+++name" + db.getUsername()+"+++account"+db.getContactID());
                                            }

                                            if(listofcontacts==Globals.NUMUSERS){
                                                vars.edit.putBoolean("chaton",true);
                                                vars.edit.apply();
                                                status.setText(".....Done....");
                                                Toast.makeText(ChatsetUp.this,"Done",Toast.LENGTH_SHORT).show();
                                                Intent chat = new Intent(ChatsetUp.this,MainActivity.class);
                                                startActivity(chat);
                                                finish();
                                            }


                                        }
                                    }).execute();
                                }else{
                                    Log.i("main","========contacts are=noooooot ===== empty");
                                    List<ContactDetailsDB> allcon = ContactDetailsDB.listAll(ContactDetailsDB.class);
                                    for (ContactDetailsDB con :allcon){
                                        str.add(con.getMobile());
                                    }
                                    for (ContactsDb servercon :online_contact){
                                        if(str.contains(servercon.getMobile())){
                                            Log.e("SAME","++++alredaystored+++" + servercon.getMobile());
                                        }else{
                                            Log.d(TAG,servercon.getUsername()+"++++adding+++" + servercon.getMobile());
                                            necontactlist.add(new ContactsDb(servercon.getUsername(), servercon.getMobile(),
                                                    String.valueOf(servercon.getUserID()), servercon.getUrl(), "offline",
                                                    "offline", servercon.getLanguage(),"fake"));
                                        }
                                    }
                                    Log.e("main","necontactlist+++++++++++++" + necontactlist.size());
                                    new SynContactsBackground(ChatsetUp.this,necontactlist, new SynContactsBackground.AsyncResponse() {
                                        @Override
                                        public void processFinish(ArrayList<ContactsDb> output) {
                                            Log.e("main","++++++++++++++++++++++++++++" + output.size());
                                            listofcontacts = output.size();
                                            if(output.size()>0){
                                                status.setText("Found "+output.size()+" contacts to chat with....");
                                            }else {
                                                status.setText("Found no contacts to add to your list....");
                                            }
                                            for(ContactsDb db :output){
                                                ContactsManager.addContact(ChatsetUp.this, db);
                                                Log.e("main","++++++++++++++++++++++++++++" + db.getMobile());
                                            }
                                            if(listofcontacts==Globals.NUMUSERS){
                                                status.setText(".....Done....");
                                                vars.edit.putBoolean("chaton",true);
                                                vars.edit.apply();
                                               Toast.makeText(ChatsetUp.this,"Done",Toast.LENGTH_SHORT).show();
                                                Intent chat = new Intent(ChatsetUp.this,MainActivity.class);
                                                startActivity(chat);
                                                finish();
                                            }

                                        }
                                    }).execute();

                                }

                            }

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
