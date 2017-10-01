package com.mobisquid.mobicash.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Vars;


/**
 * A simple {@link Fragment} subclass.
 */
public class Balance_first_layout extends Fragment {
    View rootview;
    Vars vars;
    EditText editTex_pin,editText_number;
    Alerter alerter;
    String pin;


    public Balance_first_layout() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        alerter = new Alerter(getActivity());
        rootview= inflater.inflate(R.layout.fragment_balance_first_layout, container, false);
        //editText_balance = (EditText) findViewById(R.id.editText_balance);
        editTex_pin = (EditText) rootview.findViewById(R.id.editTex_pin);
        editText_number = (EditText) rootview.findViewById(R.id.editText_number);

        editText_number.setFocusable(false);

        editText_number.setText(vars.mobile);


        rootview.findViewById(R.id.cancle_bal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        rootview.findViewById(R.id.checkbala).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyairtime();
            }
        });

        return rootview;
    }

    public void buyairtime(){

        if(editTex_pin.getText().toString().length()<4){
            alerter.alerterSuccessSimple(getActivity(),"Error", "Wrong pin");
        }
        else{
            pin = editTex_pin.getText().toString();
            Alerter.showdialog(getActivity());
                String[] paremeters ={"clientNumber","pin"};
                String[] values ={vars.mobile,editTex_pin.getText().toString()};
                String url=vars.financial_server+"ClientCheckBalance.php";

                ConnectionClass.ConnectionClass(getActivity(), url, paremeters, values
                        , "CheckBalance", new ConnectionClass.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                editTex_pin.setText("");
                                TransactionObj trans = new Gson().fromJson(result,TransactionObj.class);
                                if(trans.getResult().equalsIgnoreCase("Success")){
                                    Alerter.stopdialog();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        Slide slideTransition = new Slide(Gravity.BOTTOM);
                                        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                                        Balance_Fragment firstfragment = new Balance_Fragment();
                                        Bundle args = new Bundle();
                                        args.putString("pin",pin);
                                        args.putString("balance",result);
                                        firstfragment.setArguments(args);
                                        firstfragment.setReenterTransition(slideTransition);
                                        firstfragment.setExitTransition(slideTransition);
                                        firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.balance_container, firstfragment, "MAIN")
                                                .addToBackStack(null)
                                                .commit();
                                    } else {
                                        editTex_pin.setText("");
                                        System.out.println("===========t============");
                                        Balance_Fragment firstfragment = new Balance_Fragment();
                                        Bundle args = new Bundle();
                                        args.putString("pin",pin);
                                        args.putString("balance",result);
                                        firstfragment.setArguments(args);
                                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                        ft.replace(R.id.balance_container, firstfragment, "MAIN")
                                                .addToBackStack(null)
                                                .commit();
                                    }
                                }else{
                                  Alerter.stopdialog();
                                    alerter.alerterSuccess("Error",trans.getError());
                                }
                            }
                        });


        }
    }

}
