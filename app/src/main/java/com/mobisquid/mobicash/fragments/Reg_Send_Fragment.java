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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.ClientDetailsOb;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.AppController;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by MOBICASH on 18-Apr-15.
 */

public class Reg_Send_Fragment extends Fragment {
    EditText  editText_number_register, editText_firstname,
            editText_secondname, editText_idnumber, editText_pinnumber;
    TextInputLayout inp_fn, inp_sn, inp_id, inp_pin;
    Vars vars;

    View rootview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        rootview = inflater.inflate(R.layout.fragment_reg_client_send, container, false);
        Utils.hidekBoard(rootview.findViewById(R.id.register),getActivity());
        Utils.hidekBoard(rootview.findViewById(R.id.main_layout),getActivity());
        inp_fn = (TextInputLayout) rootview.findViewById(R.id.inp_fn);
        inp_sn = (TextInputLayout) rootview.findViewById(R.id.inp_sn);
        inp_id = (TextInputLayout) rootview.findViewById(R.id.inp_id);
        inp_pin = (TextInputLayout) rootview.findViewById(R.id.inp_pin);
        editText_number_register = (EditText) rootview.findViewById(R.id.editText_number_register);
        editText_firstname = (EditText) rootview.findViewById(R.id.editText_firstname);
        editText_secondname = (EditText) rootview.findViewById(R.id.editText_secondname);
        editText_idnumber = (EditText) rootview.findViewById(R.id.editText_idnumber);
        editText_pinnumber = (EditText) rootview.findViewById(R.id.editText_pinnumber);
        if (getArguments() != null) {
            editText_number_register.setText(getArguments().getString("number"));
            editText_number_register.setFocusable(false);
            editText_number_register.setLongClickable(false);

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
                Register();
            }
        });

        return rootview;
    }

    public void Register() {
        if (editText_firstname.getText().toString().equalsIgnoreCase("")) {
            inp_fn.setError("First name required");
        } else if (editText_secondname.getText().toString().equalsIgnoreCase("")) {
            inp_sn.setError("Second name required");
            inp_fn.setErrorEnabled(false);

        } else if (editText_idnumber.getText().toString().equalsIgnoreCase("")) {
            inp_id.setError("ID number required");
            inp_fn.setErrorEnabled(false);
            inp_sn.setErrorEnabled(false);
        } else if (editText_pinnumber.getText().toString().length() < 4) {
            inp_pin.setError("Invalid pin");
            inp_fn.setErrorEnabled(false);
            inp_sn.setErrorEnabled(false);
            inp_id.setErrorEnabled(false);

        } else {
            Alerter.showdialog(getActivity());
            inp_fn.setErrorEnabled(false);
            inp_sn.setErrorEnabled(false);
            inp_id.setErrorEnabled(false);
            inp_pin.setErrorEnabled(false);
            String URL_FEED = vars.financial_server + "EnrollPartialClient.php";
            String[] parameters = {"mobile", "firstName", "lastName", "senderPhone", "senderPin", "idNo"};
            String[] values = {editText_number_register.getText().toString(), editText_firstname.getText().toString()
                    , editText_secondname.getText().toString(), vars.mobile,
                    editText_pinnumber.getText().toString(), editText_idnumber.getText().toString()};

            ConnectionClass.ConnectionClass(getActivity(), URL_FEED, parameters, values, "Sendingpartial", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    vars.log("Good===" + result);
                    Alerter.stopdialog();
                    TransactionObj transob = new Gson().fromJson(result, TransactionObj.class);
                    if (transob.getResult().equalsIgnoreCase("success")) {
                      //  {"message":"","result":"Success","details":{"clientName":"Andrews  Kuteesa","client_phone":"250784700800","client_nationalid":"andy1945","clientphoto":"http:\/\/app.proxicash.in\/bio-api\/images\/2015\/9\/734_face.png"}}
                        JSONObject main = new JSONObject();
                        try {
                            main.put("message", "success");
                            main.put("result", "Success");
                            JSONObject bbb = new JSONObject();

                            bbb.put("client_phone",editText_number_register.getText().toString().trim());
                            bbb.put("clientName",editText_firstname.getText().toString().trim()+" "+editText_secondname.getText().toString().trim());
                            bbb.put("client_nationalid",editText_idnumber.getText().toString().trim());
                            bbb.put("clientphoto",null);
                            main.put("details", bbb);

                            vars.log("MYJSON====="+main.toString());

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Slide slideTransition = new Slide(Gravity.BOTTOM);
                                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                                ConfirmTrasferActivity firstfragment = new ConfirmTrasferActivity();
                                Bundle extras = new Bundle();
                                extras.putString("results", main.toString());
                                firstfragment.setArguments(extras);
                                firstfragment.setReenterTransition(slideTransition);
                                firstfragment.setExitTransition(slideTransition);
                                firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.money_container, firstfragment, "MAIN")
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                System.out.println("===========t============");
                                ConfirmTrasferActivity firstfragment = new ConfirmTrasferActivity();
                                Bundle extras = new Bundle();
                                extras.putString("results", main.toString());
                                firstfragment.setArguments(extras);
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.money_container, firstfragment, "MAIN")
                                        .addToBackStack(null)
                                        .commit();
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    } else {

                        Alerter.Error(getActivity(), "Error", transob.getError());
                    }

                }
            });


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
