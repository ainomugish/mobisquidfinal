package com.mobisquid.mobicash.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.model.Transactions;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Vars;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentCompletion extends Fragment {
    View rootview;
    ImageView profile_image;
    TextView name,mobile,email,details,amount;
    Transactions trans;
    EditText pin;
    TextInputLayout inp_pin;
    String transmssge;
    Vars vars ;


    public PaymentCompletion() {
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
        rootview =  inflater.inflate(R.layout.fragment_payment_completion, container, false);
        pin = (EditText) rootview.findViewById(R.id.pin);
        inp_pin =(TextInputLayout) rootview.findViewById(R.id.inp_pin);
        // inp_email =(TextInputLayout) rootview.findViewById(R.id.inp_email);
        // editemail= (EditText) rootview.findViewById(R.id.editemail);
        profile_image =(ImageView) rootview.findViewById(R.id.profile_image);
        name = (TextView) rootview.findViewById(R.id.name);
        mobile= (TextView) rootview.findViewById(R.id.mobile);
        email= (TextView) rootview.findViewById(R.id.email);
        details= (TextView) rootview.findViewById(R.id.details);
        amount = (TextView) rootview.findViewById(R.id.amount);
       // pin.setVisibility(View.GONE);
        if (getArguments().getString("message") != null) {
            name.setText(trans.getStatus());
            mobile.setText(trans.getSenderMobile());
            details.setText(trans.getDetails());
            amount.setText(trans.getAmount());

        }
        rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        rootview.findViewById(R.id.senddata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if(!Patterns.EMAIL_ADDRESS.matcher(editemail.getText().toString().trim()).matches()){
                  inp_email.setError("Invalid email address");
                }*/
                if( pin.getVisibility()==View.VISIBLE && pin.getText().toString().length()<3){
                    inp_pin.setError("Invalid pin");
                    //  inp_email.setErrorEnabled(false);
                }else{
                    // inp_email.setErrorEnabled(false);
                    inp_pin.setErrorEnabled(false);
                    new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Please note")
                            .setContentText("You are about to make a payment to "+trans.getReceiverName()+" ,mobile "+trans.getSenderMobile()
                                    +". Buy clicking Yes,pay this transaction will be complete.")
                            .setConfirmText("Yes,pay")
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
                                    paynow();
                                }
                            }).show();
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
        String[] values={vars.mobile,pin.getText().toString().trim(),mobile.getText().toString().trim()
                ,amount.getText().toString().trim(),details.getText().toString().trim()};
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
}
