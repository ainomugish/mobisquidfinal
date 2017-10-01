package com.mobisquid.mobicash.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Umeme_Pay extends AppCompatActivity{

	ProgressDialog dialog ,dialogtwo ,dialogthree = null;
	String recep;
	Boolean action= false;
   Gson gson;
	Boolean button = false;
   String newmobile;

   AlertDialog alertDialog,alertDialog_pro;
	DrawerLayout mainLayout;

	// CONTEXT
	Context context;

	// PREFS STUFF
	SharedPreferences prefs;
	Editor edit,editor;
	SharedPreferences preferences ;
	String PREFS_NAME = "com.example.sp.LoginPrefs";
   LinearLayout remember_meter;
	// FEILDS FOR APPTYPE

	String appTypeHolder;

	Vars vars;
	//SharedPreferences preferences ;
	//String PREFS_NAME = "com.example.sp.LoginPrefs";
	EditText editTex_pin,editText_account,editText_amount,editTex_client;
	//private RadioGroup radioGroup;
	Alerter alerter;
	String recepNumber,otherName;
	Button goback ;
   String recepnumber;
   CheckBox checkBox_electricity;
   RadioGroup radioGroup;
   RadioButton onthis_meter,another_meter;
	//ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

				
		vars = new Vars(this);
       gson = new Gson();

		alerter = new Alerter(this);
			setContentView(R.layout.umeme_pay);
       remember_meter = (LinearLayout) findViewById(R.id.remember_meter);
       editText_account = (EditText) findViewById(R.id.editText_account);
       another_meter = (RadioButton) findViewById(R.id.another_meter);
       onthis_meter = (RadioButton) findViewById(R.id.onthis_meter);

       if(vars.electricity_number!=null){
          remember_meter.setVisibility(View.GONE);
          another_meter.setChecked(true);
          editText_account.setText(vars.electricity_number);
       }else{
          remember_meter.setVisibility(View.VISIBLE);
          onthis_meter.setChecked(true);

       }
       checkBox_electricity = (CheckBox) findViewById(R.id.checkBox_electricity);
        // comf = (RelativeLayout) findViewById(R.id.conform);

     // comf.setVisibility(View.GONE);
			goback = (Button) findViewById(R.id.futurePaymentPurchaseBtn);
			NestedScrollView scrollviewtouch = (NestedScrollView) findViewById(R.id.scrollviewtouch);
      radioGroup = (RadioGroup) findViewById(R.id.radgroup_meter);
      editTex_client = (EditText) findViewById(R.id.editTex_client);
      editTex_client.setVisibility(View.GONE);

      radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
      {
         public void onCheckedChanged(RadioGroup group, int checkedId) {
            // checkedId is the RadioButton selected
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if(selectedId==R.id.onthis_meter){
               editTex_client.setVisibility(View.GONE);
            }else if(selectedId==R.id.another_meter){
               editTex_client.setVisibility(View.VISIBLE);

            }
         }
      });
			
			editTex_pin = (EditText) findViewById(R.id.editTex_pin);
      editTex_pin.setVisibility(View.GONE);

			editText_amount = (EditText) findViewById(R.id.editText_amount);
        Utils.hidekBoard(scrollviewtouch,this);
       ActionBar actionBar = getSupportActionBar();

       if (actionBar != null){
          actionBar.setDisplayHomeAsUpEnabled(true);
          actionBar.setHomeButtonEnabled(true);
       }
		
		
		
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.help_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Handle action bar actions click
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;
            case R.id.home:
                Intent home = new Intent(this,MainActivity.class);
                startActivity(home);
                finish();
                return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	

	// SET UP FONT
	public void setFont(ViewGroup group, Typeface font) {
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView || v instanceof Button /* etc. */)
				((TextView) v).setTypeface(font);
			else if (v instanceof ViewGroup)
				setFont((ViewGroup) v, font);
		}
	}

	


	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	public void buyairtime(View v){
            vars.hideKeyboard(v,this);
		 if(editText_account.getText().toString().equalsIgnoreCase("") || editText_amount.getText().toString().equalsIgnoreCase("")){
			alerter.alerterSuccessSimple(Umeme_Pay.this,"Error", "Please enter all fields");
		}else if(editTex_client.getVisibility()==View.VISIBLE && !vars.Checknumber(this,vars.country_code,editTex_client.getText().toString().trim())){
             editTex_client.setError("invalid mobile number");
         }
		else {
            if (editTex_client.getVisibility() == View.VISIBLE) {
               recepnumber = editTex_client.getText().toString();
            } else {
               recepnumber = vars.mobile;
            }
            if(action){
               if(remember_meter.getVisibility()==View.VISIBLE && checkBox_electricity.isChecked()){
                  vars.edit.putString("electricity_number", editText_account.getText().toString());
                  vars.edit.commit();
               }
            if (editTex_pin.getVisibility() == View.VISIBLE && editTex_pin.getText().length()>3) {
               dialogthree = ProgressDialog.show(this, "Processing payment", "Please wait..", true);
               String[] parameters ={"clientPin","client","amount","meter_no","phoneNumber"};
               String[] values ={editTex_pin.getText().toString(),vars.mobile,editText_amount.getText().toString(),
                       editText_account.getText().toString(),
                       recepnumber};
               ConnectionClass.ConnectionClass(this, vars.financial_server+"electricityPurchase.php", parameters, values, "UMEMEPAY", new ConnectionClass.VolleyCallback() {
                  @Override
                  public void onSuccess(String result) {

                     MeterDetails resporesults = gson.fromJson(result,MeterDetails.class);
                     if(resporesults.getResult().equalsIgnoreCase("SUCCESS")){
                        dialogthree.dismiss();
                        alerterSuccessSimple("Success","Your meter "+"("+resporesults.getMeter_no()+")"+
                                " has been credited"+" token"+resporesults.getToken_no());

                     }else{
                        dialogthree.dismiss();
                        alerter.alerterSuccessSimple(Umeme_Pay.this,"Error",resporesults.getError());
                     }

                  }
               });

            } else{
               alerter.alerterSuccessSimple(Umeme_Pay.this,"Error", "Wrong pin entered");
            }
            }else {
               dialogtwo = ProgressDialog.show(this, "Checking meter details", "Please wait..", true);
               //getMeter.php?meter_no=01311123747&amount=10000
               String[] parameter = {"meter_no", "amount"};
               String[] values = {editText_account.getText().toString(), editText_amount.getText().toString()};
               ConnectionClass.ConnectionClass(this, vars.financial_server + "electricityGetMeter.php", parameter, values, "Umeme_Pay", new ConnectionClass.VolleyCallback() {
                  @Override
                  public void onSuccess(String result) {

                     MeterDetails details = gson.fromJson(result, MeterDetails.class);
                     if (details.getResult().equalsIgnoreCase("SUCCESS")) {
                        dialogtwo.dismiss();


                         new SweetAlertDialog(Umeme_Pay.this, SweetAlertDialog.WARNING_TYPE)
                                 .setTitleText("Are you sure?")
                                 .setContentText("You are about to credit " + details.getAmount() + "\n" +
                                         "to " + details.getMeter_no() + "\n" +
                                         "belonging to \n" +
                                         details.getName())
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
                                         editTex_pin.setVisibility(View.VISIBLE);
                                         editTex_pin.setFocusable(true);
                                         action=true;
                                         goback.setText("Pay");
                                     }
                                 })
                                 .show();


                     } else {
                        dialogtwo.dismiss();
                        alerter.alerterSuccessSimple(Umeme_Pay.this,"Error", details.getError());
                     }

                  }
               });

            }
         }
	}

		public void cancle_but(View v){
			Intent can = new Intent(Umeme_Pay.this,MainActivity.class);
			startActivity(can);
			
		}
		
		public void home_button(View v){
			Intent home = new Intent (this, MainActivity.class);
			startActivity(home);
		}

   @Override
   public void onBackPressed() {
      if(alertDialog!=null){
         alertDialog.dismiss();

      }if(alertDialog_pro!=null){
         alertDialog_pro.cancel();
      }

      super.onBackPressed();
   }
   public void alerterSuccessSimple(String header, String message){
       new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
               .setTitleText(header)
               .setContentText(message)
               .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                   @Override
                   public void onClick(SweetAlertDialog sweetAlertDialog) {
                       sweetAlertDialog.dismissWithAnimation();
                       onBackPressed();

                   }
               })
               .show();
   }

   private class MeterDetails {
      String meter_no;
      String name;

      public String getToken_no() {
         return token_no;
      }

      public void setToken_no(String token_no) {
         this.token_no = token_no;
      }

      public String getReceipt_no() {
         return receipt_no;
      }

      public void setReceipt_no(String receipt_no) {
         this.receipt_no = receipt_no;
      }

      public String getUnits() {
         return units;
      }

      public void setUnits(String units) {
         this.units = units;
      }

      String token_no;
      String receipt_no;
      String units;
      String amount;
      String result;
      String error;

      public String getError() {
         return error;
      }

      public void setError(String error) {
         this.error = error;
      }

      public String getResult() {
         return result;
      }

      public void setResult(String result) {
         this.result = result;
      }

      public String getAmount() {
         return amount;
      }

      public void setAmount(String amount) {
         this.amount = amount;
      }

      public String getName() {
         return name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public String getMeter_no() {
         return meter_no;
      }

      public void setMeter_no(String meter_no) {
         this.meter_no = meter_no;
      }


   }

}
