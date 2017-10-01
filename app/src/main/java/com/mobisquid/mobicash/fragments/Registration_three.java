package com.mobisquid.mobicash.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.adapters.CustomSpinnerAdapter_Airtime;
import com.mobisquid.mobicash.dbstuff.TempStore;
import com.mobisquid.mobicash.model.NetworkObject;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Registration_three extends Fragment {
    Vars vars;
    EditText register_nokname,register_nok_number,register_pin,register_pin_comf,register_password,register_password_comf;
    View rootview;
    List<NetworkObject> bList,list_cities;
    CustomSpinnerAdapter_Airtime adapter,adpter_cities;
    AppCompatSpinner sp_nok_select,sp_district,sp_city;
    private String[] str_nok;
    String province=null;
    String city =null;
    ArrayAdapter<String> adp_nok;
    TextInputLayout input_noknmae,input_nok_number,input_pin,input_pin_comf,input_password,input_password_comf;
    public Registration_three() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(getActivity());
        bList = new ArrayList<>();
        list_cities= new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_registration_three, container, false);
        Utils.hidekBoard(rootview.findViewById(R.id.scroll),getActivity());
        input_noknmae = (TextInputLayout) rootview.findViewById(R.id.input_noknmae);
        input_nok_number = (TextInputLayout) rootview.findViewById(R.id.input_nok_number);
        input_pin= (TextInputLayout) rootview.findViewById(R.id.input_pin);
        input_pin_comf = (TextInputLayout) rootview.findViewById(R.id.input_pin_comf);
        input_password = (TextInputLayout) rootview.findViewById(R.id.input_password);
        input_password_comf = (TextInputLayout) rootview.findViewById(R.id.input_password_comf);
        str_nok = new  String[]{"Please select N.O.K relationship","Father","Mother","Sister","Brother","Spouse","Partner","Business partner"};
        adp_nok = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, str_nok);
        register_nok_number = (EditText) rootview.findViewById(R.id.register_nok_number);
        register_nokname = (EditText) rootview.findViewById(R.id.register_nokname);
        register_pin = (EditText) rootview.findViewById(R.id.register_pin);
        register_pin_comf = (EditText) rootview.findViewById(R.id.register_pin_comf);
        register_password = (EditText) rootview.findViewById(R.id.register_password);
        register_password_comf = (EditText) rootview.findViewById(R.id.register_password_comf);
        sp_nok_select = (AppCompatSpinner) rootview.findViewById(R.id.sp_nok_select);
        sp_city= (AppCompatSpinner) rootview.findViewById(R.id.sp_city);
        sp_district= (AppCompatSpinner) rootview.findViewById(R.id.sp_district);
        sp_nok_select.setAdapter(adp_nok);

        sp_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){

                }else {
                    NetworkObject ob = (NetworkObject) adapter.getItem(position);
                    province=ob.getDescription();
                    getcities(ob.getNetwork());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){

                }else {
                    NetworkObject ob = (NetworkObject) adpter_cities.getItem(position);
                    city=ob.getDescription();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rootview.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(register_nokname.getText().toString().isEmpty()){
                    input_noknmae.setError("NOK name required");
                }else if(sp_nok_select.getSelectedItem().toString().equalsIgnoreCase("Please select N.O.K relationship")){
                    input_noknmae.setErrorEnabled(false);
                    Toast.makeText(getActivity(),"Please select N.O.K relationship",Toast.LENGTH_SHORT).show();
                }else if(!vars.Checknumber(getActivity(),vars.country_code,register_nok_number.getText().toString().trim())){
                    vars.log("number==="+register_nok_number.getText().toString().trim());
                    input_nok_number.setError("Invalid NOK number");
                }else if(sp_district.getSelectedItemPosition()==0){
                    input_nok_number.setErrorEnabled(false);
                    Toast.makeText(getActivity(),"Select your Province/State",Toast.LENGTH_SHORT).show();
                }else if(sp_city.getSelectedItemPosition()==0){
                    input_nok_number.setErrorEnabled(false);
                    Toast.makeText(getActivity(),"Select your city/district",Toast.LENGTH_SHORT).show();
                }else if (register_pin.getText().toString().trim().length() < 4) {
                    input_pin.setError("pin too shot");
                }else if (!register_pin.getText().toString().trim().
                        equalsIgnoreCase(register_pin_comf.getText().toString().trim())) {
                    input_pin.setError("pin don't match");
                    input_pin_comf.setError("pin don't match");
                }else if (register_password.getText().toString().trim().length() < 4) {
                    input_pin.setErrorEnabled(false);
                    input_pin_comf.setErrorEnabled(false);
                    input_password.setError("password too shot");
                }
                else if (!register_password.getText().toString().trim().
                        equalsIgnoreCase(register_password_comf.getText().toString().trim())) {
                    input_password.setError("password don't match");
                    input_password_comf.setError("password don't match");
                }
                else{
                    input_password.setErrorEnabled(false);
                    input_password_comf.setErrorEnabled(false);
                    TempStore ss = TempStore.findById(TempStore.class,1);
                    vars.log("provice===="+province);
                    vars.log("city===="+city);

                    ss.setNokName(register_nokname.getText().toString().trim());
                    ss.setNokRelation(sp_nok_select.getSelectedItem().toString());
                    ss.setNokPhone(register_nok_number.getText().toString().trim());
                    ss.setProvince(province);
                    ss.setCity(city);
                    ss.setPin(register_pin.getText().toString().trim());
                    ss.setPassword(register_password.getText().toString().trim());
                    ss.save();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Slide slideTransition = new Slide(Gravity.LEFT);
                        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                        Registration_face firstfragment = new Registration_face();
                        firstfragment.setReenterTransition(slideTransition);
                        firstfragment.setExitTransition(slideTransition);
                        firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                                .addToBackStack(null)
                                .commit();
                    } else {
                        System.out.println("===========t============");
                        Registration_face firstfragment = new Registration_face();
                        firstfragment.setArguments(getActivity().getIntent().getExtras());
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                                .addToBackStack(null)
                                .commit();
                    }

                }

            }
        });


        rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        rootview.findViewById(R.id.touch_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                vars.hideKeyboard(view,getActivity());
                return false;
            }
        });
        TempStore stored = TempStore.findById(TempStore.class,1);
        if(stored.getNokName().equalsIgnoreCase("nok")){

        }else{
            if(vars.review){

            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide slideTransition = new Slide(Gravity.LEFT);
                    slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                    Registration_face firstfragment = new Registration_face();
                    firstfragment.setReenterTransition(slideTransition);
                    firstfragment.setExitTransition(slideTransition);
                    firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                } else {
                    System.out.println("===========t============");
                    Registration_face firstfragment = new Registration_face();
                    firstfragment.setArguments(getActivity().getIntent().getExtras());
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                }
            }
        }

        return rootview;
    }

    private void getcities(String id) {
        Alerter.showdialog(getActivity());
        String URL =vars.financial_server+"getDistrict.php?provinceId="+id;


        ConnectionClass.FreeString(getActivity(), URL, "getProvince", new ConnectionClass.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                vars.log("am good=="+result);
                JSONObject reader = null;
                try {
                    reader = new JSONObject(result);
                    Alerter.stopdialog();
                    JSONArray detailsArray = reader.getJSONArray("return");
                    for (int i = 0; i < detailsArray.length(); i++) {
                        JSONObject obj = new JSONObject(detailsArray.getJSONObject(i).toString());
                        NetworkObject det = new NetworkObject();
                        det.setDescription(obj.getString("value"));
                        det.setNetwork(obj.getString("parentId"));
                        list_cities.add(det);

                    }
                    adpter_cities = new CustomSpinnerAdapter_Airtime(getActivity(), list_cities);
                    sp_city.setAdapter(adpter_cities);
                    adpter_cities.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if(vars.country_code.equalsIgnoreCase("27")){
            toolbar.setTitle("Registration  3/6");
        }else{
            toolbar.setTitle("Registration  3/5");
        }

        TempStore stored = TempStore.findById(TempStore.class,1);
        if(stored.getNokName().equalsIgnoreCase("nok")){
            if(bList.isEmpty()) {
                getdistricts();
            }
        }
        if(vars.review){
            if(bList.isEmpty()) {
                getdistricts();
            }else{
                adapter = new CustomSpinnerAdapter_Airtime(getActivity(), bList);
                sp_district.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
            register_nokname.setText(stored.getNokName());
            sp_nok_select.setSelection(Arrays.asList(str_nok).indexOf(stored.getNokRelation()));
            register_nok_number.setText(stored.getNokPhone());
            register_pin.setText(stored.getPin());
            register_pin_comf.setText(stored.getPin());
            register_password.setText(stored.getPassword());
            register_password_comf.setText(stored.getPassword());

        }

    }

    private void getdistricts() {
        Alerter.showdialog(getActivity());
        String URL =vars.financial_server+"getProvince.php";

        ConnectionClass.FreeString(getActivity(), URL, "getProvince", new ConnectionClass.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                vars.log("am good=="+result);
                JSONObject reader = null;
                try {
                    reader = new JSONObject(result);
                    Alerter.stopdialog();
                    JSONArray detailsArray = reader.getJSONArray("return");
                    for (int i = 0; i < detailsArray.length(); i++) {
                        JSONObject obj = new JSONObject(detailsArray.getJSONObject(i).toString());
                        NetworkObject det = new NetworkObject();
                        det.setDescription(obj.getString("value"));
                        det.setNetwork(obj.getString("id"));
                        bList.add(det);

                    }
                    adapter = new CustomSpinnerAdapter_Airtime(getActivity(), bList);
                    sp_district.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}