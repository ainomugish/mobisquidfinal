package com.mobisquid.mobicash.fragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class History_input_Fragment extends Fragment {
    static final String TAG = History_input_Fragment.class.getSimpleName();
    View rootview;
    EditText editText_from,editText_todate,editText_pin;
    Vars vars;
    public History_input_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        rootview= inflater.inflate(R.layout.content_translog, container, false);
        Utils.hidekBoard(rootview.findViewById(R.id.image),getActivity());
        Utils.hidekBoard(rootview.findViewById(R.id.main_layout),getActivity());
        editText_from = (EditText)rootview. findViewById(R.id.editText_fromdate);
        editText_todate = (EditText) rootview.findViewById(R.id.editText_todate);
        editText_pin= (EditText)rootview. findViewById(R.id.editText_pin);
        editText_from.setFocusable(false);
        editText_todate.setFocusable(false);


        editText_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int day) {
                                int monthadd = month + 1;
                                String fomart_month,fomart_day ;
                                if(monthadd<10){
                                    fomart_month = "0"+monthadd;
                                }else{
                                    fomart_month= String.valueOf(monthadd);
                                }

                                if(day<10){
                                    fomart_day = "0"+day;
                                }else{
                                    fomart_day= String.valueOf(day);
                                }
                                editText_todate.setText(year + "-" + fomart_month + "-" + fomart_day);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        editText_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int day) {
                                int monthadd = month + 1;
                                String fomart_month,fomart_day ;
                                if(monthadd<10){
                                    fomart_month = "0"+monthadd;
                                }else{
                                    fomart_month= String.valueOf(monthadd);
                                }

                                if(day<10){
                                    fomart_day = "0"+day;
                                }else{
                                    fomart_day= String.valueOf(day);
                                }
                                editText_from.setText(year + "-" + fomart_month + "-" + fomart_day);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        rootview.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText_from.getText().toString().equalsIgnoreCase("") ||
                        editText_todate.getText().toString().equalsIgnoreCase("")){
                    Snackbar.make(view, "Please enter the dates", Snackbar.LENGTH_LONG)
                            .setAction("Error", null).show();

                }else if(editText_pin.getText().toString().length()<4){
                    Snackbar.make(view, "Please provide a valid pin", Snackbar.LENGTH_LONG)
                            .setAction("Error", null).show();
                }
                else{

                    Alerter.showdialog(getActivity());

                    String[] parameter ={"mobile","pin","beginDate","endDate"};
                    String[] valuse ={vars.mobile,editText_pin.getText().toString(),editText_from.
                            getText().toString(),editText_todate.getText().toString()};
                    String url= vars.financial_server+"transactionlogs.php";

                    ConnectionClass.ConnectionClass(getActivity(), url, parameter, valuse, "Logs", new ConnectionClass.VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            vars.log("My results========" + result);
                            Alerter.stopdialog();

                            JSONObject reader = null;
                            try {
                                reader = new JSONObject(result);
                                String resultant = reader.getString("result");
                                String error = reader.getString("error");
                                Object intervention = reader.get("details");

                                if (resultant.equalsIgnoreCase("Success")) {

                                    if (intervention instanceof JSONArray) {

                                        JSONArray detailsArray = reader.getJSONArray("details");
                                        vars.log("OKAY=======" + detailsArray);

                                        if(detailsArray.length()>0){
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                Slide slideTransition = new Slide(Gravity.BOTTOM);
                                                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                                                HistoryFragment firstfragment = new HistoryFragment();
                                                Bundle extras = new Bundle();
                                                extras.putString("results",result);
                                                firstfragment.setArguments(extras);
                                                firstfragment.setReenterTransition(slideTransition);
                                                firstfragment.setExitTransition(slideTransition);
                                                firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                                                getActivity().getSupportFragmentManager().beginTransaction()
                                                        .replace(R.id.trans_container, firstfragment, "MAIN")
                                                        .addToBackStack(null)
                                                        .commit();
                                            } else {
                                                System.out.println("===========t============");
                                                HistoryFragment firstfragment = new HistoryFragment();
                                                Bundle extras = new Bundle();
                                                extras.putString("results",result);
                                                firstfragment.setArguments(extras);
                                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                ft.replace(R.id.trans_container, firstfragment, "MAIN")
                                                        .addToBackStack(null)
                                                        .commit();
                                            }
                                        }else{
                                            Alerter.Error(getActivity(),"WHOOPS!!", "No history found for this period");
                                        }


                                    }else {
                                        Alerter.Error(getActivity(),"WHOOPS!!", "No history found for this period");
                                    }

                                } else {
                                    Alerter.Error(getActivity(),"ERROR", error);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

            }
        });
        return rootview;
    }
    public void onResume(){
        super.onResume();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        toolbar.setTitle("Transaction History");



    }
}
