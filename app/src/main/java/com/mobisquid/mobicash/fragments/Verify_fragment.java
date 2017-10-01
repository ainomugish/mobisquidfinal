package com.mobisquid.mobicash.fragments;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.FinactionalRegistration;
import com.mobisquid.mobicash.activities.FirstActivity;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;


/**
 * Created by mobicash on 5/21/16.
 */
public class Verify_fragment extends Fragment{
    private String mParam1;
    private String mParam2;
    private String location;
    ProgressDialog alertDialog;
    ProgressDialog progressDialog;
    EditText input_code;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    Sentsms sentsms;
    Delivered delivered;
    PendingIntent sentPI,deliveredPI;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_LOCATION = "location";
    private static final String ARG_FINANCIAL ="financials";
    private static final String CHANGEPASS ="changepass";
    private First_sms_fragment.OnFragmentInteractionListener mListener;
    View rootview;
    TextView number,counter;
    String mobile,financials;
    Vars vars;
    boolean donshow = true;
    Button verify;
    Alerter alerter;
    String vefcode;
    AckReceiver ackReceiver;
    SeekBar seekerbar ;
    String msgcode,veri_code;
    String changepassword;
    int i =0;
    CountDownTimer cdt;
    public Verify_fragment() {
        // Required empty public constructor

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(getActivity());
        alerter = new Alerter(getActivity());
        alertDialog  = new ProgressDialog(getActivity());
        veri_code = Utils.randomString(4);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            location = getArguments().getString(ARG_LOCATION);
            financials = getArguments().getString(ARG_FINANCIAL);
            changepassword = getArguments().getString(CHANGEPASS);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_verify, container, false);
        number = (TextView) rootview.findViewById(R.id.number);
        input_code = (EditText) rootview.findViewById(R.id.input_code);
        input_code.addTextChangedListener(chk_text_Watcher);
        counter= (TextView) rootview.findViewById(R.id.counter);
        seekerbar = (SeekBar) rootview.findViewById(R.id.seekerbar);
        verify = (Button) rootview.findViewById(R.id.verify);
        verify.setOnClickListener(itemonclick);
        mobile =mParam1+mParam2;
        number.setText("+"+mobile);
        seekerbar.setProgress(0);
        int oneMin= 60 * 1500;
        optionsms();
        cdt = new CountDownTimer(oneMin,1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                i++;
                seekerbar.setProgress(i);
                counter.setText(""+i);
                if(i==55){
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    sendSMS(mobile,veri_code);
                }
            }

            @Override
            public void onFinish() {
                //Do what you want
                i++;
                seekerbar.setProgress(i);
                counter.setText("0");
            }
        };
        cdt.start();

        return  rootview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof First_sms_fragment.OnFragmentInteractionListener) {
            mListener = (First_sms_fragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void optionsms(){
        progressDialog = ProgressDialog.show(getActivity(),null, "Please wait...",true,false);

        String url;
        if (mParam1.equalsIgnoreCase("27")) {
            // url = "http://121.241.242.114:8080/sendsms?username=mca-mobisquid&password=mobisqui&type=0&dlr=1&destination=" + number + "&source=mobicash&message=" + veri_code;

            url = "http://54.187.96.135:13013/cgi-bin/sendsms?user=Mobicash&pass=Mobicash&smsc=panaceamobile&&text=" + veri_code + "&to=" + mobile + "&from=43066";
        } else{

            url = "http://121.241.242.114:8080/sendsms?username=mca-mobisquid&password=mobisqui&type=0&dlr=1&destination=" + mobile + "&source=mobicash&message=" + veri_code;
        }
        ConnectionClass.returnString(getActivity(), url, "SMS", new ConnectionClass.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                TransactionObj details = new Gson().fromJson(result,TransactionObj.class);
                vars.log("message======"+details.getMessage());
                if(details.getResult().equalsIgnoreCase("Success")){

                    progressDialog.dismiss();

                }else{
                    progressDialog.dismiss();


                    alerter.alerterSuccessSimple(getActivity(),"Error", details.getError());
                    if(progressDialog!=null){
                        progressDialog.dismiss();

                    }
                }
            }
        });

    }
    View.OnClickListener itemonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.verify:
                    if(input_code.getText().length()>3){
                        if(input_code.getText().toString().equalsIgnoreCase(veri_code)) {
                            if(financials!=null){
                                finacialEntry(mobile);
                            }else {
                                socialEntry(number.getText().toString().trim());
                            }

                        }}
                    break;
            }
        }
    };
    private void socialEntry(String num){
        Intent login = new Intent(getActivity(), FirstActivity.class);
        login.putExtra("number", num);
        login.putExtra("location", location);
        login.putExtra("code", mParam1);
        startActivity(login);
        getActivity().finish();
    }
    private void finacialEntry(String num){
        Intent login = new Intent(getActivity(), FinactionalRegistration.class);
        login.putExtra("number", num);
        login.putExtra("code", mParam1);
        login.putExtra("location", location);
        alertDialog.dismiss();
        startActivity(login);
        getActivity().finish();
    }
    public class AckReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if(extras!=null){
                if(progressDialog!=null){
                    progressDialog.dismiss();
                    progressDialog=null;
                }else if(alertDialog!=null){
                    alertDialog.dismiss();
                    alertDialog=null;
                }
                vars.log("===============AckReceiver=====================");
                msgcode = extras.getString("msgcode");
                vefcode = msgcode.substring(0, Math.min(msgcode.length(), 4));
                if(veri_code.equalsIgnoreCase(vefcode)) {
                    input_code.setText(vefcode);
                    cdt.cancel();
                    seekerbar.setProgress(0);
                    counter.setText("");


                }

            }
            if (intent.getAction() != null) {


            }
        }
    }
    @Override
    public void onPause() {
        if(getActivity()!=null){
            getActivity().unregisterReceiver(ackReceiver);
            getActivity().unregisterReceiver(sentsms);
            getActivity().unregisterReceiver(delivered);


        }
        super.onPause();
    }

    private final TextWatcher chk_text_Watcher = new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            vars.log("===onTextChanged===");

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().equalsIgnoreCase(veri_code)){
                if(financials!=null){
                    finacialEntry(mobile);
                }else {
                    socialEntry(number.getText().toString().trim());
                }
            }
            vars.log("===afterTextChanged===");
        }
    };

    @Override
    public void onResume() {
        ackReceiver = new AckReceiver();
        sentsms = new Sentsms();
        delivered = new Delivered();
        IntentFilter smsprove = new IntentFilter(Globals.SMSPROVE);
        smsprove.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(ackReceiver, smsprove);
        IntentFilter sent = new IntentFilter(SENT);
        sent.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(sentsms,sent);

        IntentFilter diliver = new IntentFilter(DELIVERED);
        diliver.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(delivered,diliver);
        super.onResume();
    }

    private void sendSMS(String phoneNumber, String message)
    {
        if(getActivity()!=null) {
            sentPI = PendingIntent.getBroadcast(getActivity(), 0,
                    new Intent(SENT), 0);

            deliveredPI = PendingIntent.getBroadcast(getActivity(), 0,
                    new Intent(DELIVERED), 0);

            //---when the SMS has been sent---

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        }
    }
    class  Sentsms  extends BroadcastReceiver{
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode())
            {
                case Activity.RESULT_OK:

                    Toast.makeText(getActivity().getBaseContext(), "SMS sent successfully",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    //optionsms();
                    // Toast.makeText(getBaseContext(), "We couldn't send sms probably you don't have credit", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    // optionsms();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    vars.log("=================RESULT_ERROR_NULL_PDU=============");

                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    alerter.alerterSuccessSimple(getActivity(),"Sms error", "Please make sure your phone is not in airplane mode. Or you have a working sms platform");
                    break;
            }
        }
    }

    //---when the SMS has been delivered---
    class Delivered extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    // Toast.makeText(getBaseContext(), "SMS delivered",
                    //          Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    alerter.alerterSuccessSimple(getActivity(),"Sms error","Your sms not delivered yet.");
                    break;
            }
        }
    }
 /*   public void checkfinacila(final String num){
        donshow = true;
        alertDialog = ProgressDialog.show(getActivity(), "Verifying number", "Please wait...",true);
        String[] parameters ={"client"};
        String[] values ={num};
        String url =vars.financial_server+"check_Client.php";

        ConnectionClass.ConnectionClass(getActivity(),url, parameters, values, "Checknumber",
                new ConnectionClass.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        vars.log("return ==== " + result);

                        JSONObject reader = null;

                        try {
                            reader = new JSONObject(result);
                            String message = reader.getString("message");
                            String resultstr = reader.getString("result");

                            if (message.equalsIgnoreCase("Client not registered")) {
                                vars.log("=======Not registered======");
                                Intent login = new Intent(getActivity(), FinactionalRegistration.class);
                                login.putExtra("number", num);
                                login.putExtra("code", mParam1);
                                login.putExtra("location", location);
                                alertDialog.dismiss();
                                startActivity(login);
                                getActivity().finish();
                            } else if (resultstr.equalsIgnoreCase("Success")) {
                                vars.log("=======registered======");
                                alertDialog.dismiss();
                                String resultant = reader.getString("details");
                                checknumber_ser detailsObj = new Gson().fromJson(resultant, checknumber_ser.class);
                                if(donshow) {
                                    donshow = false;
                                    outputmesg("REGISTRATION FAILURE", "Hello " + detailsObj.getClientname() + " you already registered for this services with this number " +
                                            detailsObj.getClientnumber() + ". Please login with your pin number");
                                }
                            } else {
                                alertDialog.dismiss();
                                alerter.alerterSuccessSimple(getActivity(),"Error", "Please check your connection");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }
 */
    class checknumber_ser {
        String result;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        String error;
        String clientname;
        String clientnumber;

        public String getClientname() {
            return clientname;
        }

        public void setClientname(String clientname) {
            this.clientname = clientname;
        }

        public String getClientnumber() {
            return clientnumber;
        }

        public void setClientnumber(String clientnumber) {
            this.clientnumber = clientnumber;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        String message;
        String details;


    }

}