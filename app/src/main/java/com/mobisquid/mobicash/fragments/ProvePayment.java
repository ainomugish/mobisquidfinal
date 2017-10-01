package com.mobisquid.mobicash.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.dbstuff.Transactiondb;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.model.Transactions;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.NotificationUtils;
import com.mobisquid.mobicash.utils.SendEmailService;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProvePayment extends Fragment {
    View rootview;
    Transactions trans;
    String transmssge;
    Vars vars ;
    TextView counter,name,details,amount,mobile;
    EditText pin;
    TextInputLayout inp_pin;
    int i =60;
    CountDownTimer cdt;
    Button senddata,cancle;

    boolean pay =false;


    public ProvePayment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(getActivity());
        if (getArguments() != null) {
           trans = new Gson().fromJson(getArguments().getString("message"),Transactions.class);
            transmssge =getArguments().getString("message");
            vars.log("transmssge=="+transmssge);
            vars.log("transID=="+trans.getTransid());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.prove_payment, container, false);
        senddata =(Button) rootview.findViewById(R.id.senddata);
        cancle =(Button) rootview.findViewById(R.id.cancel);
        counter =(TextView) rootview.findViewById(R.id.counter);
        name =(TextView) rootview.findViewById(R.id.name);
        amount = (TextView) rootview.findViewById(R.id.amount);
        details = (TextView) rootview.findViewById(R.id.details);
        mobile =(TextView)  rootview.findViewById(R.id.mobile);
        pin = (EditText) rootview.findViewById(R.id.pin);
        inp_pin =(TextInputLayout) rootview.findViewById(R.id.inp_pin);
        pin.setVisibility(View.GONE);
        name.setText("To: "+trans.getReceiverName());
        amount.setText("Amount: "+trans.getAmount());
        details.setText("Details: "+trans.getDetails());
        mobile.setText("Mobile: "+trans.getSenderMobile());
        int oneMin= 60 * 1500;
        cdt = new CountDownTimer(oneMin,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                i--;
                counter.setText(""+i);
                if(i==0){
                    counter.setText("0");
                    cdt.cancel();
                    if(getActivity()!=null){
                        sendMessage();
                        getActivity().onBackPressed();
                    }

                }
                if(i==10){
                    counter.setBackgroundResource(R.drawable.roundcircle);
                }
                if(i==20){
                    counter.setBackgroundResource(R.drawable.blueround);
                }
            }

            @Override
            public void onFinish() {
                //Do what you want
                i--;
                counter.setText("0");
            }
        };
        cdt.start();
        rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdt.cancel();
                sendMessage();
                if(!Transactiondb.listAll(Transactiondb.class).isEmpty()){
                    Transactiondb.deleteAll(Transactiondb.class);
                    vars.log("DELETED======");
                }
                getActivity().onBackPressed();
            }
        });
        rootview.findViewById(R.id.senddata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pay){
                    if( pin.getVisibility()==View.VISIBLE && pin.getText().toString().length()<3){
                        inp_pin.setError("Invalid pin");
                        //  inp_email.setErrorEnabled(false);
                    }else {

                        paynow();

                    }

                }else{
                    pay = true;
                    senddata.setText("Pay");
                    //cancle.setEnabled(false);
                    pin.setVisibility(View.VISIBLE);
                    cdt.cancel();
                }



            }
        });
        return rootview;
    }
    private void paynow(){
        String url=null;
        if(vars.country_code.equalsIgnoreCase("27")){
            url ="http://test.mobicash.co.za/bio-api/mobiSeller/payMerchant.php";
        }else{
            url ="http://test.mcash.rw/bio-api/mobiSeller/payMerchant.php";
        }
        String[] params={"client","pin","merchantAccount","amount","details"};
        String[] values={vars.mobile,pin.getText().toString().trim(),trans.getSenderMobile()
                ,trans.getAmount(),trans.getDetails()};
        Alerter.showdialog(getActivity());
        ConnectionClass.ConnectionClass(getActivity(), url, params, values, "Payment", new ConnectionClass.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Alerter.stopdialog();
                if(result!=null){
                    TransactionObj trans = new Gson().fromJson(result, TransactionObj.class);
                    if(trans.getResult().equalsIgnoreCase("success")){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Slide slideTransition = new Slide(Gravity.LEFT);
                            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                            ConfirmationPayment firstfragment = new ConfirmationPayment();
                            Bundle extras = new Bundle();
                            extras.putString("result",result);
                            //extras.putString("email",editemail.getText().toString().trim());
                            extras.putString("mess",transmssge);
                            firstfragment.setArguments(extras);
                            firstfragment.setReenterTransition(slideTransition);
                            firstfragment.setExitTransition(slideTransition);
                            firstfragment.setSharedElementEnterTransition(new ChangeBounds());
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.activity_payment_details, firstfragment, "MAIN")
                                    // .addToBackStack(null)
                                    .commit();
                        } else {
                            System.out.println("===========t============");
                            ConfirmationPayment firstfragment = new ConfirmationPayment();
                            Bundle extras = new Bundle();
                            extras.putString("result",result);
                            // extras.putString("email",editemail.getText().toString().trim());
                            extras.putString("mess",transmssge);
                            firstfragment.setArguments(extras);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.activity_payment_details, firstfragment, "MAIN").commit();
                        }

                    }else{
                        Alerter.Error(getActivity(),"ERROR OCCURRED",trans.getMessage());
                    }
                }


            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Payment Request");
        NotificationUtils.clearNotifications();

    }

    private void sendMessage(){
       // Alerter.showdialog(getActivity());
        vars.log("re trans===="+trans.getReceiverMobile());
        vars.log("re trans===="+trans.getReceiverName());
        vars.log("re trans===E="+trans.getReceivereMail()+"vas==="+vars.email);

        vars.log("se trans===="+trans.getSenderMobile());
        vars.log("se trans===="+trans.getSenderName());
        vars.log("se trans===="+trans.getSendereMail());
        Transactions take = trans;
        take.setType("canceled");
        take.setReceiverName(trans.getSenderName());
        take.setReceiverMobile(trans.getSenderMobile());
        take.setReceivereMail(trans.getSendereMail());
        take.setSendereMail(trans.getReceivereMail());
        take.setSenderMobile(vars.mobile);
        take.setSenderName(vars.fullname);
      //  Utils.sendMessage(getActivity(),take,2);
        vars.log("canceling====="+new Gson().toJson(take));
        Intent cancelIntent = new Intent(getActivity(), SendEmailService.class);
        cancelIntent.putExtra("sendcancelmsg",new Gson().toJson(take));
        getActivity().startService(cancelIntent);


    }
}
