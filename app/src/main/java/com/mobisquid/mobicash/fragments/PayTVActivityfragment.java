package com.mobisquid.mobicash.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.MainActivity;
import com.mobisquid.mobicash.adapters.CustomSpinnerAdapter;
import com.mobisquid.mobicash.model.Bouquet;
import com.mobisquid.mobicash.model.BouquetOb;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by MOBICASH on 24-Apr-15.
 */
public class PayTVActivityfragment extends Fragment {
   Bundle extras;
  Spinner bouqu;
   String[] bouquest;
   ProgressDialog dialogpg = null;
   Alerter alerter;
   CustomSpinnerAdapter adapter;
   Vars vars;
   ProgressBar progressBar;
   String json_string = null;
   List<Bouquet> bList;
   String bouquetId;
   Double price;
    NestedScrollView scrollView;
   AlertDialog alertDialog;
   int month;
   String canel_pay,startime;
   EditText card_number,number_month,pin_number,amount;
   TextWatcher tt = null;
   LinearLayout remember_meter,starimelayout;
   CheckBox checkBox_remember;
   Gson gson;
   RadioButton dth,dtt;
    View rootview;
    ActionBar actionBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
       setHasOptionsMenu(true);
       actionBar =((AppCompatActivity)getActivity()).getSupportActionBar();
       if (actionBar != null){
           actionBar.setDisplayHomeAsUpEnabled(true);
           actionBar.setHomeButtonEnabled(true);
       }
       rootview = inflater.inflate(R.layout.paytv_layout, container, false);
      progressBar = (ProgressBar) rootview.findViewById(R.id.progressBar2);

      starimelayout = (LinearLayout) rootview.findViewById(R.id.starimelayout);
      dth =(RadioButton) rootview.findViewById(R.id.dth);
      dtt =(RadioButton) rootview.findViewById(R.id.dtt);
      dtt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
            //   progressBar.setVisibility(View.VISIBLE);
               getstertimes("1");
            }
         }
      });
      dth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
              // progressBar.setVisibility(View.VISIBLE);
               getstertimes("2");
            }
         }
      });
      starimelayout.setVisibility(View.GONE);
      remember_meter = (LinearLayout) rootview.findViewById(R.id.remember_meter);
      remember_meter.setVisibility(View.GONE);
      checkBox_remember = (CheckBox) rootview.findViewById(R.id.checkBox_remember);
      Bundle extras = getArguments();
      if(extras!= null){
         canel_pay = extras.getString("canel");
         startime = extras.getString("startime");
      }
      if(canel_pay!=null){
          actionBar.setTitle("Canal Payment");
         checkBox_remember.setText("Remember Canal smart card");

      }else if(startime!=null){
          actionBar.setTitle("Startime Payment");
         checkBox_remember.setText("Remember Startime smart card");
      }else{
          actionBar.setTitle("DSTV Payment");
         checkBox_remember.setText("Remember DSTV smart card");
      }
      vars = new Vars(getActivity());
      gson = new Gson();
      alerter = new Alerter(getActivity());
      bouqu =(Spinner) rootview.findViewById(R.id.spinner_bou);

      progressBar.setVisibility(View.GONE);
      card_number = (EditText) rootview.findViewById(R.id.cardnumber);
      number_month = (EditText) rootview.findViewById(R.id.number_month);
      pin_number = (EditText) rootview.findViewById(R.id.pin_number);
      amount = (EditText) rootview.findViewById(R.id.amount);
      amount.setFocusable(false);
      scrollView= (NestedScrollView) rootview.findViewById(R.id.scrollView);
       Utils.hidekBoard(scrollView,getActivity());

      bouqu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final Bouquet  list = (Bouquet) adapter.getItem(position);
            if (position==0){

            }else {
               vars.log("name===" + list.getName());
               vars.log("type===" + list.getType());
               vars.log("prce===" + list.getPrice());
               vars.log("id===" + list.getId());
               bouquetId = list.getId();
               price = Double.parseDouble(list.getPrice());

            }

         }

         @Override
         public void onNothingSelected(AdapterView<?> parent) {

         }
          });

      tt = new TextWatcher() {
         public void afterTextChanged(Editable s){

            number_month.setSelection(s.length());
         }
         public void beforeTextChanged(CharSequence s,int start,int count, int after){

         }
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            number_month.removeTextChangedListener(tt);
            try {
               if(price!=null) {
                  month = Integer.parseInt(number_month.getText().toString());
                  Double final_amount = month * price;
                  amount.setText(final_amount.toString());
               }else{

               }
              
            }catch (NumberFormatException e){
               e.printStackTrace();
            }
            number_month.addTextChangedListener(tt);
         }
      };
      number_month.addTextChangedListener(tt);
       rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               getActivity().onBackPressed();
           }
       });
       rootview.findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               pay ();
           }
       });
       return rootview;
   }
   @Override
   public void onPause(){
       super.onPause();
      // canel_pay =null;
     //  startime = null;
   }

   @Override
   public void onResume() {
       if(canel_pay!=null && vars.smartcard_canal==null){
           remember_meter.setVisibility(View.VISIBLE);
       }else if(canel_pay!=null && vars.smartcard_canal!= null){
           card_number.setText(vars.smartcard_canal);
       }else if(startime!=null && vars.smartcard_startime==null){
           remember_meter.setVisibility(View.VISIBLE);
       }else if(startime!=null && vars.smartcard_startime!=null){
           card_number.setText(vars.smartcard_startime);
       }else if(canel_pay==null && startime ==null){
           if(vars.smartcard_dstv==null){
               remember_meter.setVisibility(View.VISIBLE);
           }else{
               card_number.setText(vars.smartcard_dstv);
           }
       }
       if(getActivity()!=null){
           getlist();
       }

      super.onResume();
   }
    protected void getlist(){
        String url = null;
        progressBar.setVisibility(View.VISIBLE);
        String[] parameters = null;
        String[] values = null;
        if(startime!=null){
            starimelayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }else {
            if (canel_pay != null) {
                vars.log("canal payment================================");
                parameters = new String[]{"broadcaster", "broadcasterId"};
                values = new String[]{"CANAL", "3"};
                url = "clientGetBouquet.php";


            } else {
                parameters = new String[]{"broadcaster", "broadcasterId"};
                values = new String[]{"DSTV", "1"};
                url = "clientGetBouquet.php";


            }
            ConnectionClass.ConnectionClass(getActivity(), vars.financial_server + url, parameters, values, "PayTvaCTION", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    vars.log("hererre====" + result);
                    if(getActivity()!=null) {

                        BouquetOb trans = new BouquetOb(result);

                        if (trans.getResult().equalsIgnoreCase("Success")) {
                            //alerter.alerterSuccessSimple("Error", bList.toString());
                            progressBar.setVisibility(View.GONE);
                            bList = trans.showResults(result);

                            adapter = new CustomSpinnerAdapter(getActivity(), bList);
                            bouqu.setAdapter(adapter);
                        } else {
                            vars.log("=============== error =======");
                            progressBar.setVisibility(View.GONE);
                            error_retry("Error", "Please try again latter or check your internet connection");
                        }
                    }
                }
            });
        }
    }

   public void pay (){
      if(card_number.getText().toString().equalsIgnoreCase("")){
         Toast.makeText(getActivity(),"Input your smart card number",Toast.LENGTH_LONG).show();
      }
     else if(amount.getText().toString().equalsIgnoreCase("") || number_month.getText().toString().equalsIgnoreCase("")) {
         Toast.makeText(getActivity(),"Input month and Bouquet",Toast.LENGTH_LONG).show();
      }else if(pin_number.getText().toString().equalsIgnoreCase("")){
         Toast.makeText(getActivity(),"Put your pin number",Toast.LENGTH_LONG).show();
      }
      else{

         month = Integer.parseInt(number_month.getText().toString());
         Double final_amount = month * price;
         amount.setText(final_amount.toString());

          new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                  .setTitleText("Are you sure?")
                  .setContentText("You are about to pay "+final_amount+" for a subscription of \n" +
                          number_month.getText().toString()+" month(s)"+"\n"
                          +" to "+card_number.getText().toString()+" card number")
                  .setConfirmText("Yes,buy")
                  .setCancelText("NO")
                  .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                      @Override
                      public void onClick(SweetAlertDialog sweetAlertDialog) {
                          sweetAlertDialog.dismissWithAnimation();
                      }
                  })
                  .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                      @Override
                      public void onClick(SweetAlertDialog sweetAlertDialog) {
                          sweetAlertDialog.dismiss();
                          if(canel_pay!=null){
                              if(remember_meter.getVisibility()==View.VISIBLE && checkBox_remember.isChecked() ){
                                  vars.edit.putString("smartcard_canal", card_number.getText().toString());
                                  vars.edit.commit();
                              }
                              vars.log("===canal===");
                              send(bouquetId, "CANAL", "3");
                          }else if(startime!=null){
                              if(remember_meter.getVisibility()==View.VISIBLE && checkBox_remember.isChecked() ) {
                                  vars.edit.putString("smartcard_startime", card_number.getText().toString());
                                  vars.edit.commit();
                              }
                              send(bouquetId, "STARTIMES", "1");
                          }
                          else {
                              if(remember_meter.getVisibility()==View.VISIBLE && checkBox_remember.isChecked() ){
                                  vars.edit.putString("smartcard_dstv", card_number.getText().toString());
                                  vars.edit.commit();
                                  vars.log("===DSTV===");
                              }

                              send(bouquetId, "DSTV", "1");
                          }
                      }
                  })
                  .show();


      }
   }

   protected void send(String bouquetId,String broadcaster,String broadcasterId){
      String url = null;
      String[] paramters = null;
      String[] values = null;
      if(startime!=null){

       url =  "clientStartime.php";
         paramters =new String[] {"clientPin","client","broadcasterId","bouquetId","cardnumber","month","amount"};
         values = new String[]{pin_number.getText().toString(),vars.mobile,broadcasterId,bouquetId,
                 card_number.getText().toString(),number_month.getText().toString(),amount.getText().toString()};

      }else{
         paramters =new String[] {"clientPin","client","broadcaster","broadcasterId","bouquetId","cardnumber","month","amount"};
         values = new String[]{pin_number.getText().toString(),vars.mobile,broadcaster,broadcasterId,bouquetId,
                 card_number.getText().toString(),number_month.getText().toString(),amount.getText().toString()};
         url =  "clientDstvCanal.php";
      }

      dialogpg = ProgressDialog.show(getActivity(), "Processing Tv payment", "please wait...", true);


      ConnectionClass.ConnectionClass(getActivity(), vars.financial_server + url, paramters, values, "PayingTV", new ConnectionClass.VolleyCallback() {
         @Override
         public void onSuccess(String result) {
            TransactionObj transob = gson.fromJson(result, TransactionObj.class);
            if (transob.getResult().equalsIgnoreCase("Success")) {
               dialogpg.dismiss();
               alerterSuccessSimple("Success", transob.getMessage());

            } else {
               dialogpg.dismiss();
               alerter.alerterSuccessSimple(getActivity(),"Error", transob.getError());
            }

         }
      });

   }
   public void alerterSuccessSimple(String header, String message){
       new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
               .setTitleText(header)
               .setContentText(message)
               .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                   @Override
                   public void onClick(SweetAlertDialog sweetAlertDialog) {
                       sweetAlertDialog.dismissWithAnimation();
                       card_number.setText("");
                       amount.setText("");
                       pin_number.setText("");

                   }
               })
               .show();
   }


   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case android.R.id.home:
             getActivity().finish();
            return true;
      }
      return super.onOptionsItemSelected(item);
   }

   public void getstertimes(String broadcasterId){

      String[] parameters= new String[]{"broadcaster", "broadcasterId"};
      String[] values =new String[]{"STARTIMES",broadcasterId};
      String url="starTimes_bouquet.php";
       progressBar.setVisibility(View.VISIBLE);

   ConnectionClass.ConnectionClass(getActivity(), vars.financial_server + url, parameters, values, "PayTvaCTION", new ConnectionClass.VolleyCallback() {
      @Override
      public void onSuccess(String result) {
         vars.log("hererre====" + result);
         BouquetOb trans = new BouquetOb(result);


         if (trans.getResult().equalsIgnoreCase("Success")) {
            //alerter.alerterSuccessSimple("Error", bList.toString());
            progressBar.setVisibility(View.GONE);
            bList = trans.showResults(result);
            adapter = new CustomSpinnerAdapter(getActivity(), bList);
            bouqu.setAdapter(adapter);
         } else {
            vars.log("=============== error =======");
            progressBar.setVisibility(View.GONE);
             error_retry("Error", "Please try again latter or check your internet connection");
         }
      }
   });


   }
    protected void error_retry(String header,String message) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText(header)
                .setContentText(message)
                .setCancelText("No")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        getActivity().onBackPressed();
                    }
                })
                .setConfirmText("Retry")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        if(startime!=null){
                            if(dtt.isChecked()){
                                getstertimes("1");
                            }
                            if(dth.isChecked()){
                                getstertimes("2");

                            }
                        }else {
                            getlist();
                        }

                    }
                })
                .show();

    }

}
