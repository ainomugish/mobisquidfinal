package com.mobisquid.mobicash.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.adapters.TranslogAdapter;
import com.mobisquid.mobicash.model.TransObj;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Balance_Fragment extends Fragment {
    View rootview;
    Vars vars;
    String balance,pin;
    TransactionObj trans;
    Alerter alerter;
    TextView balance_B;
    List<TransObj> listlog;
    TranslogAdapter translogAdapter;
    RecyclerView recList;
    ProgressBar progressBar_b;
    String today_date="";
    String last_date="";

    public Balance_Fragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(getActivity());
        listlog = new ArrayList<>();
        alerter = new Alerter(getActivity());
        if (getArguments() != null) {
            pin = getArguments().getString("pin");
            balance = getArguments().getString("balance");
            trans = new Gson().fromJson(balance,TransactionObj.class);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ActionBar actionBar =((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        rootview = inflater.inflate(R.layout.fragment_balance_, container, false);
        balance_B =(TextView) rootview.findViewById(R.id.balance);
        progressBar_b = (ProgressBar) rootview.findViewById(R.id.progressBar_b);
        recList = (RecyclerView) rootview.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setVisibility(View.GONE);
        if(balance!=null){
            balance_B.setText(trans.getMessage());
            Calendar cal = Calendar.getInstance();
            int today_Year = cal.get(Calendar.YEAR);
            int today_Month = cal.get(Calendar.MONTH); // current month
            int today_mDay = cal.get(Calendar.DAY_OF_MONTH); // current day
            today_Month =today_Month+1;
            String fomart_montht,fomart_dayt ;
            if(today_Month<10){
                fomart_montht = "0"+today_Month;
            }else{
                fomart_montht= String.valueOf(today_Month);
            }

            if(today_mDay<10){
                fomart_dayt = "0"+today_mDay;
            }else{
                fomart_dayt= String.valueOf(today_mDay);
            }
            today_date = today_Year + "-" + fomart_montht + "-" + fomart_dayt;
            Calendar calReturn = Calendar.getInstance();
            //jDate_timeOfExpectedReturn1.setText(dateFormat.format(cal.getTime()));
            calReturn.add(Calendar.DATE, -30);
            int last_Year = calReturn.get(Calendar.YEAR);
            int last_Month = calReturn.get(Calendar.MONTH); // current month
            int last_mDay = calReturn.get(Calendar.DAY_OF_MONTH); // current day
            last_Month =last_Month+1;
            String fomart_month,fomart_day ;
            if(last_Month<10){
                fomart_month = "0"+last_Month;
            }else{
                fomart_month= String.valueOf(last_Month);
            }

            if(last_mDay<10){
                fomart_day = "0"+last_mDay;
            }else{
                fomart_day= String.valueOf(last_mDay);
            }
            last_date = last_Year + "-" + fomart_month + "-" + fomart_day;
            //balance_B.setText("to="+today_date+"  "+"las=="+last_date);
        }
        if(balance!=null || !balance.equalsIgnoreCase("")){
            getTrans();
        }
        return rootview;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void getTrans(){
        recList = (RecyclerView) rootview.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        translogAdapter = new TranslogAdapter(getActivity(),listlog);
        recList.setAdapter(translogAdapter);
            listlog.clear();

            String[] parameter ={"mobile","pin","beginDate","endDate"};
            String[] valuse ={vars.mobile,pin,last_date,today_date};
            String url= vars.financial_server+"transactionlogs.php";

            ConnectionClass.ConnectionClass(vars.context, url, parameter, valuse, "Logs", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    vars.log("My results========" + result);


                    JSONObject reader = null;
                    try {
                        reader = new JSONObject(result);
                        String resultant = reader.getString("result");
                        String error = reader.getString("error");
                        Object intervention = reader.get("details");

                        if (resultant.equalsIgnoreCase("Success")) {

                            if (intervention instanceof JSONArray) {
                                recList.setVisibility(View.VISIBLE);
                                JSONArray detailsArray = reader.getJSONArray("details");
                                vars.log("OKAY=======" + detailsArray);

                                for (int i = 0; i < detailsArray.length(); i++) {
                                    TransObj clintlog = new TransObj();
                                    JSONObject transObject = detailsArray.getJSONObject(i);
                                    String transferId = transObject.getString("id");
                                    String date = transObject.getString("date");
                                    String amount = transObject.getString("formattedAmount");
                                    String status = transObject.getString("status");
                                    String description = transObject.getString("description");
                                    JSONObject transferTypeObject = transObject
                                            .getJSONObject("transferType");
                                    String transferName = transferTypeObject.getString("name");


                                    String name = "";
                                    if (transObject.has("member")) {
                                        JSONObject memberObject = transObject.getJSONObject("member");
                                        name = memberObject.getString("name");
                                    }
                                    clintlog.setResult(result);
                                    clintlog.setError(error);
                                    clintlog.setTransferId(transferId);
                                    clintlog.setDate(date);
                                    clintlog.setAmount(amount);
                                    clintlog.setStatus(status);
                                    clintlog.setDescription(description);
                                    clintlog.setTransferName(transferName);
                                    clintlog.setName(name);
                                    listlog.add(clintlog);

                                }
                            }
                            else if (intervention instanceof JSONObject) {
                                TransObj clintlog = new TransObj();
                                JSONObject transObject = reader.getJSONObject("details");
                                String transferId = transObject.getString("id");
                                String date = transObject.getString("date");
                                String amount = transObject.getString("formattedAmount");
                                String status = transObject.getString("status");
                                String description = transObject.getString("description");
                                JSONObject transferTypeObject = transObject
                                        .getJSONObject("transferType");
                                String transferName = transferTypeObject.getString("name");
                                String name = "";
                                if (transObject.has("member")) {
                                    JSONObject memberObject = transObject.getJSONObject("member");
                                    name = memberObject.getString("name");
                                }
                                clintlog.setResult(result);
                                clintlog.setError(error);
                                clintlog.setTransferId(transferId);
                                clintlog.setDate(date);
                                clintlog.setAmount(amount);
                                clintlog.setStatus(status);
                                clintlog.setDescription(description);
                                clintlog.setTransferName(transferName);
                                clintlog.setName(name);
                                listlog.add(clintlog);
                            }

                            vars.log("List size is=============="+listlog.size());
                            if(listlog.size()==0){
                                Toast.makeText(getActivity(),"No results found",Toast.LENGTH_LONG).show();

                            }
                            recList.setVisibility(View.VISIBLE);
                            progressBar_b.setVisibility(View.GONE);
                            recList.setAdapter(translogAdapter);
                            translogAdapter.notifyDataSetChanged();

                        } else {
                            progressBar_b.setVisibility(View.GONE);
                            alerter.alerterSuccessSimple(getActivity(),"ERROR", error);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

    @Override
    public void onResume() {
        /*Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Check balance");*/
        Globals.CLOSE = false;
        super.onResume();
    }


}
