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
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.dbstuff.TempStore;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class Registration_two extends Fragment {
    Vars vars;
    EditText dobText,register_nationalaIID,register_imigration;
    EditText expeirlydate,register_address;
    View rootview;
    AppCompatSpinner spidselect,sp_language,sp_occupation;
    private String[] idtype,languages,str_occup;
    ArrayAdapter<String> langs,adapter_occup,adapter2,IDTYPEAD, provinceadp;
    TextInputLayout input_dob,input_idnumber,input_exp,input_address;
    public Registration_two() {
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
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_registration_two, container, false);
        Utils.hidekBoard(rootview.findViewById(R.id.scroll),getActivity());
        input_dob = (TextInputLayout) rootview.findViewById(R.id.input_dob);
        input_idnumber = (TextInputLayout) rootview.findViewById(R.id.input_idnumber);
        input_address= (TextInputLayout) rootview.findViewById(R.id.input_address);
        input_exp = (TextInputLayout) rootview.findViewById(R.id.input_exp);
        idtype =new String[] {"Select ID-Type","National-ID", "Passport","Temp Residence"," Permit","Refugee-ID"};
        str_occup = new String[] {"Select occupation", "Business","Informal","Salaried","Self-employed","Student","Others"};
    if(vars.country_code.equalsIgnoreCase("27")){
            languages = new String[] {"Your language","English","Afrikaans","Xhosa","Zulu"};
        }else{
            languages = new String[] {"Your language","English","Chinese","French","Kinyarwanda","Kirundi","Spanish","Swahili"};
        }

        langs = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, languages);
        IDTYPEAD = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, idtype);
        adapter_occup = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, str_occup);
        dobText = (EditText) rootview.findViewById(R.id.enroll_dob_show);
        register_imigration = (EditText) rootview.findViewById(R.id.register_imigration);
        register_nationalaIID = (EditText) rootview.findViewById(R.id.register_nationalaIID);
        spidselect = (AppCompatSpinner) rootview.findViewById(R.id.spidselect);
        sp_occupation= (AppCompatSpinner) rootview.findViewById(R.id.sp_occupation);
        sp_language= (AppCompatSpinner) rootview.findViewById(R.id.sp_language);
        spidselect.setAdapter(IDTYPEAD);
        sp_language.setAdapter(langs);
        sp_occupation.setAdapter(adapter_occup);
        expeirlydate = (EditText) rootview.findViewById(R.id.expire_date);
        register_address= (EditText) rootview.findViewById(R.id.register_address);
        dobText.setFocusable(false);
        expeirlydate.setFocusable(false);
        if(vars.country_code.equalsIgnoreCase("27")) {

                TempStore stored = TempStore.findById(TempStore.class,1);
                spidselect.setSelection(Arrays.asList(idtype).indexOf(stored.getIdtype()));
            vars.log("stored.getIdtype()=="+stored.getIdtype());

        }else{
            dobText.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Utils.getDate(getActivity(), new Utils.StringCallback() {
                        @Override
                        public void onSuccess(String result) {

                            dobText.setText(result);

                        }
                    });

                }
            });
        }

        expeirlydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getDate(getActivity(), new Utils.StringCallback() {
                    @Override
                    public void onSuccess(String result) {
                        expeirlydate.setText(result);
                    }
                });

            }
        });
        rootview.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dobText.getText().toString().isEmpty()){
                    input_dob.setError("Date of birth required");
                }else if(spidselect.getSelectedItem().toString().equalsIgnoreCase("Select ID-Type")){
                    input_dob.setErrorEnabled(false);
                    Toast.makeText(getActivity(),"Select id type",Toast.LENGTH_SHORT).show();
                }else if(register_nationalaIID.getText().toString().isEmpty()){
                    input_idnumber.setError("ID number required");
                }else if(expeirlydate.getText().toString().isEmpty()){
                    input_idnumber.setErrorEnabled(false);
                    input_exp.setError("ID expiration date required");
                }else if(sp_language.getSelectedItem().toString().equalsIgnoreCase("Your language")){
                    input_exp.setErrorEnabled(false);
                    Toast.makeText(getActivity(),"Select your language",Toast.LENGTH_SHORT).show();
                }else if(sp_occupation.getSelectedItem().toString().equalsIgnoreCase("Select occupation")){
                    Toast.makeText(getActivity(),"Select your occupation",Toast.LENGTH_SHORT).show();
                }else if(register_address.getText().toString().isEmpty()){
                    input_address.setError("Your address required");
                }else{
                    input_address.setErrorEnabled(false);
                    TempStore ss = TempStore.findById(TempStore.class,1);
                    ss.setDob(dobText.getText().toString().trim());
                    ss.setIdtype(spidselect.getSelectedItem().toString());
                    ss.setIdNo(register_nationalaIID.getText().toString().trim());
                    ss.setIdexpiration(expeirlydate.getText().toString().trim());
                    ss.setLanguage(sp_language.getSelectedItem().toString());
                    ss.setOccupation(sp_occupation.getSelectedItem().toString());
                    ss.setAddress(register_address.getText().toString());
                    if(register_imigration.getText().toString().isEmpty()){
                        ss.setTempresnr("NO");
                    }else{
                        ss.setTempresnr(register_imigration.getText().toString().trim());
                    }
                    ss.save();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Slide slideTransition = new Slide(Gravity.LEFT);
                        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                        Registration_three firstfragment = new Registration_three();
                        firstfragment.setReenterTransition(slideTransition);
                        firstfragment.setExitTransition(slideTransition);
                        firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                                .addToBackStack(null)
                                .commit();
                    } else {
                        System.out.println("===========t============");
                        Registration_three firstfragment = new Registration_three();
                        firstfragment.setArguments(getActivity().getIntent().getExtras());
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                                .addToBackStack(null)
                                .commit();
                    }

                }

            }
        });

        TempStore stored = TempStore.findById(TempStore.class,1);
        if(stored.getLanguage().equalsIgnoreCase("lag")){

        }else{
            if(vars.review){

            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide slideTransition = new Slide(Gravity.LEFT);
                    slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                    Registration_three firstfragment = new Registration_three();
                    firstfragment.setReenterTransition(slideTransition);
                    firstfragment.setExitTransition(slideTransition);
                    firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                } else {
                    System.out.println("===========t============");
                    Registration_three firstfragment = new Registration_three();
                    firstfragment.setArguments(getActivity().getIntent().getExtras());
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                }
            }
        }

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
        spidselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(vars.country_code.equalsIgnoreCase("27")){
                    TempStore stored = TempStore.findById(TempStore.class,1);
                    spidselect.setSelection(Arrays.asList(idtype).indexOf(stored.getIdtype()));
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
        TempStore stored = TempStore.findById(TempStore.class,1);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if(vars.country_code.equalsIgnoreCase("27")){
            toolbar.setTitle("Registration  2/6");
            dobText.setText(stored.getDob());
            register_nationalaIID.setText(stored.getIdNo());
            dobText.setFocusable(false);
            register_nationalaIID.setFocusable(false);

        }else{
            toolbar.setTitle("Registration  2/5");
        }

        if(vars.review){
            dobText.setText(stored.getDob());
            spidselect.setSelection(Arrays.asList(idtype).indexOf(stored.getIdtype()));
            register_nationalaIID.setText(stored.getIdNo());
            expeirlydate.setText(stored.getIdexpiration());
            sp_language.setSelection(Arrays.asList(languages).indexOf(stored.getLanguage()));
            sp_occupation.setSelection(Arrays.asList(str_occup).indexOf(stored.getOccupation()));
            register_address.setText(stored.getAddress());

            if(stored.getTempresnr().equalsIgnoreCase("NO")){

            }else{
                register_imigration.setText(stored.getTempresnr());
            }

        }
        expeirlydate.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        dobText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        register_nationalaIID.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
    }
}
