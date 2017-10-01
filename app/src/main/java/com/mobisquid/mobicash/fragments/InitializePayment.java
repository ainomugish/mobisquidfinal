package com.mobisquid.mobicash.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.IntentIntegrator;
import com.mobisquid.mobicash.utils.IntentResult;
import com.mobisquid.mobicash.utils.Vars;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class InitializePayment extends Fragment {
    View rootview;
    EditText amount,details;
    TextView name,mobile,email;

    TextInputLayout inp_amount,inp_detail;
    ImageView profile_image;
    Bundle extras;
    Vars vars;

    public InitializePayment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        if (getArguments() != null) {
            if(getArguments().getString("scan")!=null){
                IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
                scanIntegrator.initiateScan();
            }
        }
        rootview =  inflater.inflate(R.layout.initialize_payment, container, false);
        profile_image = (ImageView) rootview.findViewById(R.id.profile_image);
        name =(TextView) rootview.findViewById(R.id.name);
        mobile =(TextView) rootview.findViewById(R.id.mobile);
        email =(TextView) rootview.findViewById(R.id.email);
        amount =(EditText) rootview.findViewById(R.id.amount);
        details =(EditText) rootview.findViewById(R.id.details);
        inp_detail =(TextInputLayout) rootview.findViewById(R.id.inp_detail);
        inp_amount =(TextInputLayout) rootview.findViewById(R.id.inp_amount);

        rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              getActivity().onBackPressed();
            }
        });
        rootview.findViewById(R.id.senddata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amount.getText().toString().isEmpty()){
                    inp_amount.setError("Amount can't be empty");
                }else if(details.getText().toString().isEmpty()){
                    inp_detail.setError("Details can't be empty");
                    inp_amount.setErrorEnabled(false);
                }else {
                    inp_detail.setErrorEnabled(false);
                    inp_amount.setErrorEnabled(false);
                }
            }
        });
        return rootview;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //vars.log("=======Got results====");
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null) {

            String scanContent = scanningResult.getContents();
            System.out.println("scanContent=="+scanContent);
            // vars.log("scanContent=="+scanContent);

            if (scanContent!= null){
                String URL =null;
                if(vars.country_code.equalsIgnoreCase("27")||vars.country_code.equalsIgnoreCase("250")){
                    if(vars.country_code.equalsIgnoreCase("27")){
                        URL = "http://test.mobicash.co.za/bio-api/mobiSquid/checkClientByQrcode.php";

                    }else{
                        URL = "http://test.mcash.rw/bio-api/mobiSquid/checkClientByQrcode.php";
                    }
                    String [] params={"codeString"};
                    String [] values={scanContent};
                    Alerter.showdialog(getActivity());
                    ConnectionClass.ConnectionClass(getActivity(), URL, params, values, "PAYMENT", new ConnectionClass.VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Alerter.stopdialog();
                            TransactionObj trans = new Gson().fromJson(result, TransactionObj.class);
                            if(trans.getResult().equalsIgnoreCase("success")){
                                trans = new Gson().fromJson(result,TransactionObj.class);
                                name.setText(trans.getDetails());
                                mobile.setText(trans.getClient());
                                email.setText(trans.getExtra1());
                                if(trans.getStage()!=null){
                                    Picasso.with(getActivity()).load(trans.getStage())

                                            .into(profile_image);
                                }


                            }else {
                                Alerter.Error(getActivity(),"Error occurred",trans.getMessage());
                            }

                        }
                    });

                }else{
                    Toast.makeText(getActivity(),"Country not yet supported",Toast.LENGTH_LONG).show();
                }

            }else{
                getActivity().onBackPressed();
                vars.log("value==back=");
            }
        }
        else{
            Toast toast = Toast.makeText(getActivity(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Globals.WHICHUSER="pay";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Globals.WHICHUSER="";
    }
}
