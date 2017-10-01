package com.mobisquid.mobicash.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.FinactionalRegistration;
import com.mobisquid.mobicash.activities.Login;
import com.mobisquid.mobicash.activities.RegistrationActivity;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;


import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link First_sms_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class First_sms_fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_LOCATION ="location";
    private static final String ARG_FINANCIAL ="financials";
    private static final String CHANGEPASS ="changepass";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String location,financials,changepass;
    View rootview;
    AlertDialog.Builder downloadDialog;
    EditText input_mobile;
    Spinner spin_country;
    Button btn,verify;
    String countrycode;
    String[] countryCode;
    LinearLayout usenumber;
    CheckBox usemynumber;
    String mynumber;
    Vars vars;
    ArrayAdapter<CharSequence> adapter;
    private OnFragmentInteractionListener mListener;

    public First_sms_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment First_sms_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static First_sms_fragment newInstance(String param1, String param2) {
        First_sms_fragment fragment = new First_sms_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            location = getArguments().getString(ARG_LOCATION);
            financials = getArguments().getString(ARG_FINANCIAL);
            changepass = getArguments().getString(CHANGEPASS);
            vars.log("changepass==="+changepass);
            vars.log("financials==="+financials);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_first_sms, container, false);
        Utils.hidekBoard(getActivity().findViewById(R.id.main_layout),getActivity());
        Utils.hidekBoard(getActivity().findViewById(R.id.header_card),getActivity());
        usenumber =(LinearLayout) rootview.findViewById(R.id.usenumber);
        usenumber.setVisibility(View.GONE);
        usemynumber = (CheckBox) rootview.findViewById(R.id.usemynumber);

     /*   if(vars.chk!=null && financials!=null) {
            if(vars.mobile.substring(0,1).equalsIgnoreCase("+")){
                mynumber = vars.mobile.substring(1);
            }else{
                mynumber = vars.mobile;
            }
            usenumber.setVisibility(View.VISIBLE);
            // usemynumber.setChecked(false);
            usemynumber.setText("Would you like to use "+mynumber+" ?");
            usemynumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    usemynumber.setChecked(true);
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Attention")
                            .setContentText("Please note that "+mynumber+" has been verified and will be permanently used as your financial number. ")
                            .setCancelText("No")
                            .setConfirmText("Use this")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    checkfinacila(mynumber,true);
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    usemynumber.setChecked(false);
                                }
                            })
                            .show();

                }
            });
        }*/

        btn = (Button) rootview.findViewById(R.id.btn);
        verify = (Button) rootview.findViewById(R.id.verify);
        verify.setOnClickListener(this);
        input_mobile = (EditText) rootview.findViewById(R.id.input_mobile);
        spin_country = (Spinner) rootview.findViewById(R.id.spin_country);
        countryCode= getResources().getStringArray(R.array.option_array_code);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.option_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_country.setAdapter(adapter);

        spin_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                countrycode = countryCode[position];
                if(countrycode.equalsIgnoreCase("")) {

                }else{
                    btn.setText("+" + countryCode[position]);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int position) {
        if (mListener != null) {
            mListener.onFragmentInteraction(position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.verify:
                if(countrycode.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(),"Please select your country",Toast.LENGTH_SHORT).show();
                    Snackbar.make(v, "Please select your country", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
                else if(!vars.Checknumber(getActivity(),countrycode,input_mobile.getText().toString())){
                    input_mobile.setError("Invalid number");
                }else if(countrycode.equalsIgnoreCase("250")&& input_mobile.getText().length()>9){
                    input_mobile.setError("Remove 0 as first digit");
                }else if(String.valueOf(input_mobile.getText().toString().charAt(0)).equalsIgnoreCase("0")){
                    input_mobile.setError("Remove 0 as first digit");
                }
                else {
                    checknumber("Confirm","Are you sure this is your number\n"+"+"+countrycode+input_mobile.getText().toString()+" Note standard sms costs may be incurred");
                }

                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int position);
    }
    public void checknumber_socil(final String num){
      Alerter.showdialog(getActivity());

        String url = Globals.SOCILA_SERV+"entities.chat/"+num;

        String[] parameters ={};
        String[] values ={};
        ConnectionClass.stringRequest(Request.Method.GET, url, parameters, values,
                new ConnectionClass.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                    Alerter.stopdialog();
                        try {
                            JSONObject reader = new JSONObject(result);
                            if (reader.getString("error").equalsIgnoreCase("no_error")) {
                                if(changepass!=null){
                                    ChangePassword secondfragment = new ChangePassword();
                                    Bundle args = new Bundle();
                                    args.putString(ARG_PARAM1, countrycode);
                                    args.putString(ARG_PARAM2,input_mobile.getText().toString());
                                    args.putString(ARG_LOCATION,spin_country.getSelectedItem().toString());
                                    args.putString("chat",reader.getString("chat"));
                                    args.putString(CHANGEPASS,changepass);
                                    secondfragment.setArguments(args);
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.fragment_container, secondfragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();

                                }else {

                                    sociel_outputmesg("REGISTRATION FAILURE", "Please note that this number " + num
                                            + " is already registered. Please login or reset your password");
                                }

                            } else if(reader.getString("error").equalsIgnoreCase("error")) {
                                if (getActivity() != null) {
                                    if(financials!=null){
                                        New_verification_frag secondfragment = new New_verification_frag();
                                        Bundle args = new Bundle();
                                        args.putString(ARG_PARAM1, countrycode);
                                        args.putString(ARG_PARAM2, input_mobile.getText().toString());
                                        args.putString(ARG_LOCATION, spin_country.getSelectedItem().toString());
                                        args.putString(ARG_FINANCIAL, financials);
                                        args.putString(CHANGEPASS, changepass);
                                        secondfragment.setArguments(args);
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragment_container, secondfragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }else{
                                        if(changepass!=null){
                                           Alerter.Error(getActivity(),"Error","Your number "+input_mobile.getText().toString()+" is not registered for social.");
                                        }else {
                                            New_verification_frag secondfragment = new New_verification_frag();
                                            Bundle args = new Bundle();
                                            args.putString(ARG_PARAM1, countrycode);
                                            args.putString(ARG_PARAM2, input_mobile.getText().toString());
                                            args.putString(ARG_LOCATION, spin_country.getSelectedItem().toString());
                                            args.putString(ARG_FINANCIAL, financials);
                                            args.putString(CHANGEPASS, changepass);
                                            secondfragment.setArguments(args);
                                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                            transaction.replace(R.id.fragment_container, secondfragment);
                                            transaction.addToBackStack(null);
                                            transaction.commit();
                                        }
                                    }

                                }

                            } else {
                                vars.log("return ==== " + result);
                                Alerter.Error(getActivity(), "Error", "Please check your internet connection");
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    private void checknumber(String title,String message){
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(getActivity());
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                    if(financials!=null) {
                        if(!countrycode.equalsIgnoreCase("27")&&!countrycode.equalsIgnoreCase("250")
                                &&!countrycode.equalsIgnoreCase("265")){
                            outpCountry(true);
                        }else {
                            checkfinacila(input_mobile.getText().toString().trim(), false);
                        }
                    }else{
                        checknumber_socil("+"+countrycode+input_mobile.getText().toString().trim());
                    }
            }
        });
        downloadDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        downloadDialog.show();
    }

    public void checkfinacila(final String num , final boolean checkbox){
        Alerter.showdialog(getActivity());
        String[] parameters ={"client"};
        String[] values ={num};

        String url = vars.financial_server+"check_Client.php";

        ConnectionClass.ConnectionClass(getActivity(), url, parameters, values, "Checknumber",
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
                                if(checkbox){
                                    Intent login = new Intent(getActivity(), FinactionalRegistration.class);
                                    login.putExtra("number", num);
                                    login.putExtra("code", vars.country_code);
                                    login.putExtra("location", location);
                                    Alerter.stopdialog();
                                    startActivity(login);
                                    getActivity().finish();
                                }else{
                                    if (getActivity() != null) {
                                        New_verification_frag secondfragment = new New_verification_frag();
                                        Bundle args = new Bundle();
                                        args.putString(ARG_PARAM1, countrycode);
                                        args.putString(ARG_PARAM2, input_mobile.getText().toString());
                                        args.putString(ARG_LOCATION, spin_country.getSelectedItem().toString());
                                        args.putString(ARG_FINANCIAL, "financials");
                                        args.putString(CHANGEPASS, changepass);
                                        secondfragment.setArguments(args);
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragment_container, secondfragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }
                                }

                            } else if (resultstr.equalsIgnoreCase("Success")) {
                                vars.log("=======registered======");
                                Alerter.stopdialog();
                                String resultant = reader.getString("details");
                                checknumber_ser detailsObj = new Gson().fromJson(resultant, checknumber_ser.class);
                                outputmesg("REGISTRATION FAILURE", "Hello " + detailsObj.getClientname() + " you already registered for this services with this number " +
                                        detailsObj.getClientnumber() + ". Please login with your pin number");
                            } else {
                                Alerter.stopdialog();
                                Alerter.Error(getActivity(),"Error", "Please check your connection");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }
    private void sociel_outputmesg(String title,String message){
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(getActivity());
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setCancelable(false);
        downloadDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent login = new Intent(getActivity(), RegistrationActivity.class);
                login.putExtra("login","login");
                startActivity(login);
                getActivity().finish();

            }
        });
        downloadDialog.setNegativeButton("New number", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

             //   FragmentManager fm = getActivity().getSupportFragmentManager();
              //  fm.popBackStack();
            }
        });
        downloadDialog.show();
    }
    private void outputmesg(String title,String message){
        downloadDialog = new AlertDialog.Builder(getActivity());
        downloadDialog.setTitle(title);
        downloadDialog.setCancelable(false);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent login = new Intent(getActivity(), Login.class);
                login.putExtra("financials", "financials");
                startActivity(login);
                getActivity().finish();

            }
        });
        downloadDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                usemynumber.setChecked(false);
                getActivity().finish();
            }
        });
        downloadDialog.show();
    }

    private void outpCountry(boolean message){
        downloadDialog = new AlertDialog.Builder(getActivity());
        downloadDialog.setTitle("PLEASE NOTE");
        downloadDialog.setCancelable(false);
        downloadDialog.setMessage("This country is not yet supported for our financial services, you will be notified when its added.");
        if(message) {
            downloadDialog.setPositiveButton("New Country", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });
        }
        downloadDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getActivity().finish();
            }
        });
        downloadDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(vars.chk!=null && financials!=null) {
            if(downloadDialog==null ) {
                if(!vars.country_code.equalsIgnoreCase("27")&&!vars.country_code.equalsIgnoreCase("250")
                        &&!vars.country_code.equalsIgnoreCase("265")){
                    outpCountry(false);
                }else {
                    if (vars.mobile.substring(0, 1).equalsIgnoreCase("+")) {
                        mynumber = vars.mobile.substring(1);
                    } else {
                        mynumber = vars.mobile;
                    }
                    if (downloadDialog == null) {
                        checkfinacila(mynumber, true);
                    }
                }
            }
        }
    }

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