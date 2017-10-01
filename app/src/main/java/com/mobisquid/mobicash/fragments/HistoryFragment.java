package com.mobisquid.mobicash.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.adapters.TranslogAdapter;
import com.mobisquid.mobicash.model.TransObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    View rootview;
    RecyclerView recList;
    List<TransObj> listlog;
    TranslogAdapter translogAdapter;
    String results;
    Vars vars;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    if(getArguments()!=null){
        results = getArguments().getString("results");
    }
    vars = new Vars(getActivity());
        rootview = inflater.inflate(R.layout.fragment_history, container, false);
        listlog = new ArrayList<>();
        recList = (RecyclerView) rootview.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        translogAdapter = new TranslogAdapter(getActivity(),listlog);
        recList.setAdapter(translogAdapter);
        if(results!=null){
            JSONObject reader = null;
            try {
                reader = new JSONObject(results);
                String resultant = reader.getString("result");
                String error = reader.getString("error");
                Object intervention = reader.get("details");

                if (resultant.equalsIgnoreCase("Success")) {
                    listlog.clear();
                    if (intervention instanceof JSONArray) {

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
                            clintlog.setResult(results);
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
                        clintlog.setResult(results);
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

                    recList.setAdapter(translogAdapter);
                    translogAdapter.notifyDataSetChanged();
                    } else {
                        Alerter.Error(getActivity(),"ERROR", error);
                    }
            }catch (Exception e){

            }
        }
        return rootview;
    }
    public void onResume(){
        super.onResume();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        toolbar.setTitle("Transaction History");



    }

}
