package com.mobisquid.mobicash.fragments;


import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.dbstuff.TempStore;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class Registration_one extends Fragment  {
    Vars vars;

    // UI FIEILDS
    EditText register_firstName,register_secondname;
    EditText register_userName;
    EditText register_email;
    EditText register_phone;
    protected AlertDialog dialog;
    //spinner for languages
    Spinner spidselect,marital_sp;
    boolean expdate = false;
    private String[] genders ;
    private String[] maritalstatus;
    View rootview;
    ArrayAdapter<String>genders_,marital;
    TextInputLayout in_dob ;
    TextInputLayout in_exp ;
    TextInputLayout in_id ;
    TextInputLayout in_user ;
    TextInputLayout in_first ;
    TextInputLayout in_sec ;
    String number,countrycode;
    TempStore store;


    public Registration_one() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(getActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            number = getArguments().getString("number");
            countrycode = getArguments().getString("code");
        }
        rootview = inflater.inflate(R.layout.fragment_registration_one, container, false);
        Utils.hidekBoard(rootview.findViewById(R.id.scroll),getActivity());
         in_dob = (TextInputLayout)rootview.findViewById(R.id.input_dob);
         in_exp = (TextInputLayout)rootview.findViewById(R.id.input_exp);
         in_id = (TextInputLayout)rootview.findViewById(R.id.input_idnumber);
         in_user = (TextInputLayout)rootview.findViewById(R.id.input_username);
         in_first = (TextInputLayout)rootview.findViewById(R.id.input_firstname);
         in_sec = (TextInputLayout)rootview.findViewById(R.id.input_secondname);
        register_firstName = (EditText) rootview.findViewById(R.id.register_firstName);
        register_secondname  = (EditText) rootview.findViewById(R.id.register_secondname);
        if(vars.country_code.equalsIgnoreCase("27")){
            register_firstName.setFocusable(false);
            register_secondname.setFocusable(false);
        }
        //250734596799
        register_email = (EditText) rootview.findViewById(R.id.register_email);
        register_userName = (EditText) rootview.findViewById(R.id.register_userName);
        register_phone= (EditText) rootview.findViewById(R.id.register_phone);
        register_phone.setFocusable(false);
        register_phone.setText(number);
        genders =  new String[] {"Your gender","Female","Male"};
        maritalstatus =  new String[] {"Marital status","Divorced","Single","Married"};
        marital = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, maritalstatus);
        genders_ = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, genders);
        marital_sp = (Spinner) rootview.findViewById(R.id.marital_sp);
        spidselect = (Spinner) rootview.findViewById(R.id.spidselect);
        spidselect.setAdapter(genders_);
        marital_sp.setAdapter(marital);
        if(!TempStore.listAll(TempStore.class).isEmpty()) {
            if (!vars.review && countrycode.equalsIgnoreCase("27")) {
                vars.log("TempStore==SA");
                store = TempStore.findById(TempStore.class, 1);
                register_phone.setText(store.getMobile());
                register_secondname.setText(store.getSecondname());
                register_firstName.setText(store.getFirstName());
                spidselect.setSelection(Arrays.asList(genders).indexOf(store.getGender()));
                marital_sp.setSelection(Arrays.asList(maritalstatus).indexOf(store.getMartalStatus()));
                spidselect.setFocusable(false);
            } else {

            vars.log("TempStore===ALREADY fillall");
            store = TempStore.findById(TempStore.class, 1);
            register_phone.setText(store.getMobile());
            register_userName.setText(store.getUsername());
            register_secondname.setText(store.getSecondname());
            register_firstName.setText(store.getFirstName());
            register_email.setText(store.getEmail());
            spidselect.setSelection(Arrays.asList(genders).indexOf(store.getGender()));
            marital_sp.setSelection(Arrays.asList(maritalstatus).indexOf(store.getMartalStatus()));
                if(vars.country_code.equalsIgnoreCase("27")){
                    spidselect.setFocusable(false);
                }
        }
        }
        rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        rootview.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(register_firstName.getText().toString().isEmpty()){
                    in_first.setErrorEnabled(true);
                    in_first.setError("First name required");
                }else if(register_secondname.getText().toString().isEmpty()){
                    in_first.setErrorEnabled(false);
                    in_sec.setErrorEnabled(true);
                    in_sec.setError("Second name required");

                }else if(register_userName.getText().toString().isEmpty()){
                    in_sec.setErrorEnabled(false);
                    in_user.setErrorEnabled(true);
                    in_user.setError("Username required");
                }
                else if(spidselect.getSelectedItem().toString().equalsIgnoreCase("Your gender")){
                    in_user.setErrorEnabled(false);
                    Toast.makeText(getActivity(),"Select your gender",Toast.LENGTH_LONG).show();
                }
                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(register_email.getText().toString()).matches()){
                    in_user.setErrorEnabled(false);
                    in_id.setErrorEnabled(true);
                    in_id.setError("Email required");
                }
             else if(marital_sp.getSelectedItem().toString().equalsIgnoreCase("Marital status")){
                    Toast.makeText(getActivity(),"Select marital status",Toast.LENGTH_LONG).show();
                    in_id.setErrorEnabled(false);

                }else{

                    if(TempStore.listAll(TempStore.class).isEmpty()){
                        vars.log("TempStore===EMPTY");
                         store = new TempStore(register_phone.getText().toString().trim(),
                                register_firstName.getText().toString().trim(),register_secondname.getText().toString().trim(),
                                register_userName.getText().toString().trim(),"idnum",
                                "expeid","idtyp", "lag",spidselect.getSelectedItem().toString(),marital_sp.getSelectedItem().toString()
                                ,"dob",register_email.getText().toString().trim(),"occup","nok","nokre","nokphone",
                                "addres",countrycode,"city","prov","face","resid","idimage","pass","pin","other");
                        store.save();
                    }else{
                        vars.log("TempStore===ALREADY CONTAINS");
                        store = TempStore.findById(TempStore.class,1);
                        store.setFirstName(register_firstName.getText().toString().trim());
                        store.setSecondname(register_secondname.getText().toString().trim());
                        store.setUsername(register_userName.getText().toString().trim());
                        store.setMobile(register_phone.getText().toString().trim());
                        store.setMartalStatus(marital_sp.getSelectedItem().toString());
                        store.setEmail(register_email.getText().toString().trim());
                        store.setCountrycode(countrycode);
                        store.setGender(spidselect.getSelectedItem().toString());
                        store.save();
                    }

                    vars.edit.putInt("regno",2);
                    vars.edit.commit();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Slide slideTransition = new Slide(Gravity.LEFT);
                        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                        Registration_two firstfragment = new Registration_two();
                        firstfragment.setReenterTransition(slideTransition);
                        firstfragment.setExitTransition(slideTransition);
                        firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                                .addToBackStack(null)
                                .commit();
                    } else {
                        System.out.println("===========t============");
                        Registration_two firstfragment = new Registration_two();
                        firstfragment.setArguments(getActivity().getIntent().getExtras());
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                                .addToBackStack(null)
                                .commit();
                    }
                }

            }
        });
        if(vars.regno>1){
            if(vars.review){

            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide slideTransition = new Slide(Gravity.LEFT);
                    slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                    Registration_two firstfragment = new Registration_two();
                    firstfragment.setReenterTransition(slideTransition);
                    firstfragment.setExitTransition(slideTransition);
                    firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                } else {
                    System.out.println("===========t============");
                    Registration_two firstfragment = new Registration_two();
                    firstfragment.setArguments(getActivity().getIntent().getExtras());
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                }
            }
        }
        rootview.findViewById(R.id.touch_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                vars.hideKeyboard(view,getActivity());
                return false;
            }
        });
        spidselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(vars.country_code.equalsIgnoreCase("27")) {
                    if(!TempStore.listAll(TempStore.class).isEmpty()) {
                        spidselect.setSelection(Arrays.asList(genders).indexOf(store.getGender()));
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if(vars.country_code.equalsIgnoreCase("27")){
            toolbar.setTitle("Registration  1/6");
        }else{
            toolbar.setTitle("Registration  1/5");
        }

    }
}
