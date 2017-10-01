package com.mobisquid.mobicash.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatSpinner;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.dbstuff.TempStore;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class Verify_ID extends Fragment {
    View rootview;
    String number,countrycode,idtype;
    Vars vars;
    Button submit;
    AppCompatSpinner sp_id;
    EditText edit_idnumber;
    TextInputLayout inputLayout_id;
    ArrayAdapter<String> adp_lang;
    String[] stringID = new  String[]{"Please select ID type","National-ID","Passport","Permit"};

    public Verify_ID() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        if (getArguments() != null) {
            number = getArguments().getString("number");
            countrycode = getArguments().getString("code");
        }
        rootview = inflater.inflate(R.layout.fragment_verify__id, container, false);
        Utils.hidekBoard(rootview.findViewById(R.id.scroll),getActivity());
        submit = (Button) rootview.findViewById(R.id.submit);
        edit_idnumber =(EditText) rootview.findViewById(R.id.edit_idnumber);
        sp_id = (AppCompatSpinner) rootview.findViewById(R.id.sp_id);
        inputLayout_id =(TextInputLayout) rootview.findViewById(R.id.inp_id) ;
        adp_lang = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, stringID);
        sp_id.setAdapter(adp_lang);
        sp_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idtype = adapterView.getSelectedItem().toString();
                vars.log("idtype===="+idtype);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sp_id.getSelectedItemPosition()==0){
                    Toast.makeText(getActivity(),"Select id type",Toast.LENGTH_LONG).show();
                }
                else if(edit_idnumber.getText().toString().equalsIgnoreCase("")){
                    inputLayout_id.setError("ID number required");
                }else{
                    Alerter.showdialog(getActivity());
                    inputLayout_id.setErrorEnabled(false);
                    String[] params ={"identifier"};
                    String [] values ={edit_idnumber.getText().toString().trim()};
                    String url ="http://test.mobicash.co.za/bio-api/getInfo/checkClientID.php";
                    ConnectionClass.ConnectionClass(getActivity(), url, params, values, "ID", new ConnectionClass.VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            vars.log("rse===="+result);
                            Alerter.stopdialog();
                            try {
                                JSONObject OB = new JSONObject(result);
                                if(OB.getString("status").equalsIgnoreCase("SUCCESS")){
                                    JSONObject details = new JSONObject(OB.getString("response"));

                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = null;
                                    try {
                                        date = format.parse(details.getString("date_of_birth"));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    SimpleDateFormat df = new SimpleDateFormat("MM");
                                    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
                                    SimpleDateFormat dfd = new SimpleDateFormat("dd");
                                    String month = df.format(date);
                                    String year = dfy.format(date);
                                    String day = dfd.format(date);
                                    String monthda = null;
                                    if(month.startsWith("0")){
                                        monthda =  month.substring(1);
                                    }else{
                                        monthda =  month;
                                    }
                                    String mydate = year+"-"+monthda+"-"+day;
                                    vars.log("====month=="+mydate);

                                    TempStore store = new TempStore(number,
                                            details.getString("first_name"),details.getString("surname"),
                                            "username",details.getString("identity_number"),
                                            "expeid",idtype, "lag",details.getString("gender"),"marital"
                                            ,mydate,"email","occup","nok","nokre","nokphone",
                                            "addres",countrycode,"city","prov","face","resid","idimage","pass","pin","other");
                                    store.save();

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        Slide slideTransition = new Slide(Gravity.LEFT);
                                        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                                        System.out.println("===========frgment============");
                                        Bundle args = new Bundle();
                                        args.putString("number", number);
                                        args.putString("code", countrycode);

                                        Registration_one firstfragment = new Registration_one();
                                        firstfragment.setReenterTransition(slideTransition);
                                        firstfragment.setExitTransition(slideTransition);
                                        firstfragment.setArguments(args);
                                        firstfragment.setSharedElementEnterTransition(new ChangeBounds());
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .add(R.id.content_finactional_registration, firstfragment, "MAIN")
                                                .commit();
                                    } else {
                                        System.out.println("===========t============");
                                        Registration_one firstfragment = new Registration_one();
                                        Bundle args = new Bundle();
                                        args.putString("number", number);
                                        args.putString("code", countrycode);
                                        firstfragment.setArguments(args);
                                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                        ft.add(R.id.content_finactional_registration, firstfragment, "MAIN").commit();
                                    }

                                }else{
                                    inputLayout_id.setErrorEnabled(true);
                                    inputLayout_id.setError("Invalid ID");
                                    Toast.makeText(getActivity(),"Invalid ID",Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }
        });
        if(!TempStore.listAll(TempStore.class).isEmpty()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Slide slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                System.out.println("===========frgment============");
                Bundle args = new Bundle();
                args.putString("number", number);
                args.putString("code", countrycode);

                Registration_one firstfragment = new Registration_one();
                firstfragment.setReenterTransition(slideTransition);
                firstfragment.setExitTransition(slideTransition);
                firstfragment.setArguments(args);
                firstfragment.setSharedElementEnterTransition(new ChangeBounds());
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_finactional_registration, firstfragment, "MAIN")
                        .commit();
            } else {
                System.out.println("===========t============");
                Registration_one firstfragment = new Registration_one();
                Bundle args = new Bundle();
                args.putString("number", number);
                args.putString("code", countrycode);
                firstfragment.setArguments(args);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.content_finactional_registration, firstfragment, "MAIN").commit();
            }
        }
        return rootview;
    }

}
