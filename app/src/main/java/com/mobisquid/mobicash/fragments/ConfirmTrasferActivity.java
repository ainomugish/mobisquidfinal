package com.mobisquid.mobicash.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.MainActivity;
import com.mobisquid.mobicash.model.ClientDetailsOb;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.AppController;
import com.mobisquid.mobicash.utils.CircleTransform;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by MOBICASH on 18-Apr-15.
 */

public class ConfirmTrasferActivity extends Fragment {
    TextView clientname, clientnumber;
    ImageView clientphoto;
    Gson gson;
    String sender_number;
    EditText edit_pin, amount;
    Vars vars;
    Bundle extras;
    private String URL_FEED;
    ClientDetailsOb clientDetailsOb;
    TextInputLayout inp_amount,inp_pin;
    View rootview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.confirm_client, container, false);
        Utils.hidekBoard(rootview.findViewById(R.id.image),getActivity());
        Utils.hidekBoard(rootview.findViewById(R.id.drawer_layout),getActivity());
        inp_amount = (TextInputLayout) rootview.findViewById(R.id.inp_amount);
        inp_pin = (TextInputLayout) rootview.findViewById(R.id.inp_pin);

        vars = new Vars(getActivity());
        gson = new Gson();
       // {"message":"","result":"Success","details":{"clientName":"Andrews  Kuteesa","client_phone":"250784700800","client_nationalid":"andy1945","clientphoto":"http:\/\/app.proxicash.in\/bio-api\/images\/2015\/9\/734_face.png"}}

        URL_FEED = vars.financial_server + "transferC2C.php";

        edit_pin = (EditText) rootview.findViewById(R.id.editTex_pin);
        clientphoto = (ImageView) rootview.findViewById(R.id.clientphoto);
        clientname = (TextView) rootview.findViewById(R.id.clientname);
        amount = (EditText) rootview.findViewById(R.id.editText_amount);
        clientnumber = (TextView) rootview.findViewById(R.id.clientnumber);

        extras = getArguments();
        if (getArguments() != null) {
            clientDetailsOb = new ClientDetailsOb(getArguments().getString("results"));
            Details details = new Gson().fromJson(clientDetailsOb.getDetails(), Details.class);
            vars.log("extras======image=========" + details.getClientphoto());

            clientname.setText(details.getClientName());
            clientnumber.setText(details.getClient_phone());

            if (details.getClientphoto()!=null) {
                Picasso.with(getActivity()).load(details.getClientphoto().trim())
                        .error(R.drawable.noimage)
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.noimage)
                        .into(clientphoto);
            }

        }
        rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        rootview.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_button();
            }
        });

        return rootview;
    }

    public void send_button() {
        if (amount.getText().toString().length()<=3) {
            inp_amount.setError("Minimum amount is 1000");
        } else if (edit_pin.getText().length() < 4) {
            inp_amount.setErrorEnabled(false);
            edit_pin.setError("Invalid pin");
        } else {
            inp_amount.setErrorEnabled(false);
            inp_pin.setErrorEnabled(false);
            new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Please confirm!!")
                    .setCancelText("cancel")
                    .setConfirmText("Yes,send")
                    .setContentText("You are about to send "+amount.getText().toString().trim()+
                            " to "+clientname.getText()+" number "+clientnumber.getText())
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            getActivity().onBackPressed();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();

                            Alerter.showdialog(getActivity());
                            String[] parameters = {"senderPhone", "recieverPhone", "amount", "senderPin", "imei"};
                            String[] values = {vars.mobile, clientnumber.getText().toString(), amount.getText().toString().trim(),
                                    edit_pin.getText().toString().trim(), vars.imei};
                            ConnectionClass.ConnectionClass(getActivity(), URL_FEED, parameters, values, "CofermTrans", new ConnectionClass.VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    vars.log("Here is the re====" + result);
                                    Alerter.stopdialog();
                                    TransactionObj trans = gson.fromJson(result, TransactionObj.class);
                                    if (trans.getResult().equalsIgnoreCase("SUCCESS")) {
                                        Alerter.Successpayment(getActivity(), amount.getText().toString(),
                                                result,"MONEY TRANSFER",R.id.money_container);

                                    } else {

                                        Alerter.Error(getActivity(), "Error", trans.getError());
                                    }

                                }
                            });
                        }
                    }).show();

        }
    }

    public class Details{
        public String clientName;
        public String client_phone;

        public String getClientphoto() {
            return clientphoto;
        }

        public void setClientphoto(String clientphoto) {
            this.clientphoto = clientphoto;
        }

        public String getClient_nationalid() {
            return client_nationalid;
        }

        public void setClient_nationalid(String client_nationalid) {
            this.client_nationalid = client_nationalid;
        }

        public String getClient_phone() {
            return client_phone;
        }

        public void setClient_phone(String client_phone) {
            this.client_phone = client_phone;
        }

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public String client_nationalid;
        public String clientphoto;


    }
}
