package com.mobisquid.mobicash.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.Person;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayTaxes_Fragment extends Fragment {
    View rootview;
    Gson gson;
    Alerter alerter;
    ArrayList<Person> mSelectedItems;
    Vars vars;
    EditText  edit_pin;
    ARRresponse arRresponse;
    TextView bank_name, RRA_REF, TIN, TAX_PAYER_NAME, TAX_TYPE_DESC, TAX_CENTRE_NO, TAX_TYPE_NO, ASSESS_NO,
            RRA_ORIGIN_NO, AMOUNT_TO_PAY, DEC_ID, DEC_DATE;


    public PayTaxes_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        if(getArguments()!=null){
            arRresponse = new Gson().fromJson(getArguments().getString("results"), ARRresponse.class);
            vars.log("=trrr==========="+getArguments().getString("results"));
        }
        rootview= inflater.inflate(R.layout.fragment_tax_payment, container, false);
        edit_pin =(EditText) rootview.findViewById(R.id.edit_pin);
        Utils.hidekBoard(rootview.findViewById(R.id.nestted),getActivity());
        mSelectedItems = new ArrayList();
        bank_name = (TextView) rootview.findViewById(R.id.text_bank_name);
        RRA_REF = (TextView) rootview.findViewById(R.id.text_refnumber);
        TIN= (TextView) rootview.findViewById(R.id.text_tin);
        TAX_PAYER_NAME= (TextView) rootview.findViewById(R.id.text_name);
        TAX_TYPE_DESC= (TextView) rootview.findViewById(R.id.text_tax_type_dsc);
        TAX_CENTRE_NO= (TextView) rootview.findViewById(R.id.text_TAX_CENTRE_NO);
        TAX_TYPE_NO= (TextView) rootview.findViewById(R.id.text_tax_type_number);
        ASSESS_NO= (TextView) rootview.findViewById(R.id.text_assess_no);
        RRA_ORIGIN_NO= (TextView) rootview.findViewById(R.id.text_RRA_ORIGIN_NO);
        AMOUNT_TO_PAY= (TextView) rootview.findViewById(R.id.text_AMOUNT_TO_PAY);
        DEC_ID= (TextView) rootview.findViewById(R.id.text_DEC_ID);
        DEC_DATE= (TextView) rootview.findViewById(R.id.text_DEC_DATE);
        if(getArguments()!=null){
            bank_name.setText(arRresponse.getBank_name());
            RRA_REF.setText(arRresponse.getRRA_REF());
            TIN.setText(arRresponse.getTIN());
            TAX_PAYER_NAME.setText(arRresponse.getTAX_PAYER_NAME());
            TAX_TYPE_DESC.setText(arRresponse.getTAX_TYPE_DESC());
            TAX_CENTRE_NO.setText(arRresponse.getTAX_CENTRE_NO());
            TAX_TYPE_NO.setText(arRresponse.getTAX_TYPE_NO());
            ASSESS_NO.setText(arRresponse.getASSESS_NO());
            RRA_ORIGIN_NO.setText(arRresponse.getRRA_ORIGIN_NO());
            AMOUNT_TO_PAY.setText(arRresponse.getAMOUNT_TO_PAY());
            DEC_ID.setText(arRresponse.getDEC_ID());
            DEC_DATE.setText(arRresponse.getDEC_DATE());

            mSelectedItems.add(new Person("TAX PAYER NAME", arRresponse.getTAX_PAYER_NAME(),"p"));
            mSelectedItems.add(new Person("RRA_REF", arRresponse.getRRA_REF(),"p"));
            mSelectedItems.add(new Person("TIN", arRresponse.getTIN(),"p"));
            mSelectedItems.add(new Person("AMOUNT TO PAY", arRresponse.getAMOUNT_TO_PAY(),"p"));
            mSelectedItems.add(new Person("BANK NAME", arRresponse.getBank_name(),"p"));
            mSelectedItems.add(new Person("TAX TYPE DESC", arRresponse.getTAX_TYPE_DESC(),"p"));
            mSelectedItems.add(new Person("TAX TYPE NO", arRresponse.getTAX_TYPE_NO(),"p"));
            mSelectedItems.add(new Person("TAX CENTRE NO", arRresponse.getTAX_CENTRE_NO(),"p"));
            mSelectedItems.add(new Person("ASSESS NO", arRresponse.getASSESS_NO(),"p"));
            mSelectedItems.add(new Person("RRA ORIGIN NO", arRresponse.getRRA_ORIGIN_NO(),"p"));
            mSelectedItems.add(new Person("DEC ID", arRresponse.getDEC_ID(),"p"));
            mSelectedItems.add(new Person("DEC DATE", arRresponse.getDEC_DATE(),"p"));
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
               if(edit_pin.getText().toString().length()<3){
                   edit_pin.setError("Invalid pin");
               }else{
                   onCreateDialog().show();
               }
            }
        });
        return rootview;
    }


    private class ARRresponse {
        String result;

        String bank_name;

        String TIN;

        String error;

        String DEC_ID;

        String TAX_TYPE_NO;

        String RRA_REF;

        String message;

        String TAX_TYPE_DESC;

        String DEC_DATE;

        String TAX_CENTRE_NO;

        String RRA_ORIGIN_NO;

        public String getASSESS_NO() {
            return ASSESS_NO;
        }

        public void setASSESS_NO(String ASSESS_NO) {
            this.ASSESS_NO = ASSESS_NO;
        }

        public String getAMOUNT_TO_PAY() {
            return AMOUNT_TO_PAY;
        }

        public void setAMOUNT_TO_PAY(String AMOUNT_TO_PAY) {
            this.AMOUNT_TO_PAY = AMOUNT_TO_PAY;
        }

        public String getTAX_PAYER_NAME() {
            return TAX_PAYER_NAME;
        }

        public void setTAX_PAYER_NAME(String TAX_PAYER_NAME) {
            this.TAX_PAYER_NAME = TAX_PAYER_NAME;
        }

        public String getRRA_ORIGIN_NO() {
            return RRA_ORIGIN_NO;
        }

        public void setRRA_ORIGIN_NO(String RRA_ORIGIN_NO) {
            this.RRA_ORIGIN_NO = RRA_ORIGIN_NO;
        }

        public String getTAX_CENTRE_NO() {
            return TAX_CENTRE_NO;
        }

        public void setTAX_CENTRE_NO(String TAX_CENTRE_NO) {
            this.TAX_CENTRE_NO = TAX_CENTRE_NO;
        }

        public String getDEC_DATE() {
            return DEC_DATE;
        }

        public void setDEC_DATE(String DEC_DATE) {
            this.DEC_DATE = DEC_DATE;
        }

        public String getTAX_TYPE_DESC() {
            return TAX_TYPE_DESC;
        }

        public void setTAX_TYPE_DESC(String TAX_TYPE_DESC) {
            this.TAX_TYPE_DESC = TAX_TYPE_DESC;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRRA_REF() {
            return RRA_REF;
        }

        public void setRRA_REF(String RRA_REF) {
            this.RRA_REF = RRA_REF;
        }

        public String getTAX_TYPE_NO() {
            return TAX_TYPE_NO;
        }

        public void setTAX_TYPE_NO(String TAX_TYPE_NO) {
            this.TAX_TYPE_NO = TAX_TYPE_NO;
        }

        public String getDEC_ID() {
            return DEC_ID;
        }

        public void setDEC_ID(String DEC_ID) {
            this.DEC_ID = DEC_ID;
        }

        public String getTIN() {
            return TIN;
        }

        public void setTIN(String TIN) {
            this.TIN = TIN;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        String TAX_PAYER_NAME;

        String AMOUNT_TO_PAY;

        String ASSESS_NO;

    }

    public Dialog onCreateDialog() {

        // Where we track the selected items
        simpleAdapter myadpter = new simpleAdapter(mSelectedItems);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View promptsView =  inflater.inflate(R.layout.simaple_group_list, null);
        builder.setView(promptsView);

        ListView listView =  (ListView) promptsView.findViewById(R.id.simple_list);
        listView.setAdapter(myadpter);

        builder.setTitle("Please confirm details below")
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        onCreateDialog().dismiss();
                        vars.log("=====Am paying====");
                        if (edit_pin.getText().toString().length()!=4&&edit_pin.getText().toString().length()!=5) {
                            Toast.makeText(getActivity(),"Wrong pin entered",Toast.LENGTH_LONG).show();
                        } else {

                            Alerter.showdialog(getActivity());

                            String[] parameters = {"bankName","rraRef","tin","taxPayerName",
                                    "taxTypeDesc","taxCentreNo","taxTypeNo","assessNo","rraOriginNo",
                                    "amountToPay","phoneNumber","pin"};
                            String[] values = {bank_name.getText().toString(),RRA_REF.getText().toString(),TIN.getText().toString(),
                                    TAX_PAYER_NAME.getText().toString(),TAX_TYPE_DESC.getText().toString(),TAX_CENTRE_NO.getText().toString()
                                    ,TAX_TYPE_NO.getText().toString(),ASSESS_NO.getText().toString(),RRA_ORIGIN_NO.getText().toString()
                                    ,AMOUNT_TO_PAY.getText().toString(),vars.mobile,edit_pin.getText().toString()};
                            ConnectionClass.ConnectionClass(getActivity(), vars.financial_server+"payTax.php",
                                    parameters, values, "EtaxPay", new ConnectionClass.VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    Alerter.stopdialog();
                                    vars.log("results====="+result);
                                    TransactionObj transob = new Gson().fromJson(result,TransactionObj.class);

                                    if(transob.getResult().equalsIgnoreCase("Success")){
                                        Alerter.Successpayment(getActivity(), arRresponse.getAMOUNT_TO_PAY(),
                                                result,"RRA PAYMENT",R.id.government_container);

                                    }else{
                                        Alerter.Error(getActivity(),"Error",transob.getError());
                                    }

                                }
                            });
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onCreateDialog().dismiss();
                    }
                });


        return builder.create();
    }

    class simpleAdapter extends BaseAdapter {
        List<Person> mylist;
        LayoutInflater inflater = null;

        public simpleAdapter(List<Person> mylistS){

            this.mylist = mylistS;
            inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public int getCount() {
            return mylist.size();
        }

        @Override
        public Object getItem(int position) {
            return mylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.simplelist_row, null);
            TextView title = (TextView) vi.findViewById(R.id.title);
            TextView content = (TextView) vi.findViewById(R.id.myitem);
            title.setText(mylist.get(position).getName());
            content.setText(mylist.get(position).getPhonenumber());
            return vi;
        }
    }
}
