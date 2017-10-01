package com.mobisquid.mobicash.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.adapters.CustomSpinnerAdapter_Airtime;
import com.mobisquid.mobicash.model.NetworkObject;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.IntentIntegrator;
import com.mobisquid.mobicash.utils.IntentResult;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class AirtimeBuy extends AppCompatActivity {

	ProgressDialog dialogpg = null;
	Context context;
	String recieverPhone;
	Editor edit;
	List<NetworkObject> bList;
	CustomSpinnerAdapter_Airtime adapter;
	RadioButton onthis_number,another;
	Vars vars;
	//SharedPreferences preferences ;
	//String PREFS_NAME = "com.example.sp.LoginPrefs";
	EditText editText_amount,editTex_pin,editText_number;
	private RadioGroup radioGroup;
	Alerter alerter;
	Gson gson;
	Bundle extras;
	String tonumber;
	TextInputLayout inp_number,inp_amount,inp_pin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		vars = new Vars(this);
		alerter = new Alerter(this);
		bList = new ArrayList<>();
			setContentView(R.layout.payairtime_layout);
		inp_number = (TextInputLayout) findViewById(R.id.inp_number);
		inp_amount= (TextInputLayout) findViewById(R.id.inp_amount);
		inp_pin= (TextInputLayout) findViewById(R.id.inp_pin);
		ActionBar actionBar = getSupportActionBar();

		if (actionBar != null){
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
		}
		extras = getIntent().getExtras();
		gson = new Gson();
			ScrollView scrollviewtouch = (ScrollView) findViewById(R.id.scrollviewtouch);
			editText_amount = (EditText) findViewById(R.id.editText_amount);
			editTex_pin = (EditText) findViewById(R.id.editTex_pin);
			editText_number = (EditText) findViewById(R.id.editText_number);
			onthis_number = (RadioButton) findViewById(R.id.onthis_number);
			another= (RadioButton) findViewById(R.id.another);
			radioGroup = (RadioGroup) findViewById(R.id.radgroup_number);
		    radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		    {
		        public void onCheckedChanged(RadioGroup group, int checkedId) {
		            // checkedId is the RadioButton selected
		        	int selectedId = radioGroup.getCheckedRadioButtonId();
			    	 if(selectedId==R.id.onthis_number){
			    		 editText_number.setText(vars.mobile);

			    	 }else if(selectedId==R.id.another){
						 if(editText_number.getText().toString().trim().equalsIgnoreCase(vars.mobile)){
							 editText_number.setText("");
						 }

			    		 
			    	 }       
		        }
		    });

			scrollviewtouch.setOnTouchListener(new OnTouchListener(){
	            @Override
	            public boolean onTouch(View view, MotionEvent ev)
	            {
	                hideKeyboard(view);
	                return false;
	            }
	        });
		context = this;

	if(extras!=null){
		tonumber = extras.getString("recepNumber");
		vars.log("tonumber=========="+tonumber);
		if(tonumber.length()>10){
			another.setChecked(true);
			String num = tonumber.substring(4);
			tonumber = "0"+num;
			editText_number.setVisibility(View.VISIBLE);
			editText_number.setText(tonumber);
			editText_number.setFocusable(false);


		}else{
			another.setChecked(true);
			editText_number.setVisibility(View.VISIBLE);
			editText_number.setText(tonumber);
			editText_number.setFocusable(false);

		}

	}else{
		onthis_number.setChecked(true);
	}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (scanningResult != null) {
			//we have a result
			String scanContent = scanningResult.getContents();

			String scanFormat = scanningResult.getFormatName();


			//Long amt = Long.valueOf(scanContent.toString().trim()).longValue();
			//log("amount==="+amt);

			if (scanContent!= null){
				//get application content to the next activity


				try {
					//Long amt = Long.valueOf(scanContent.toString().trim()).longValue();
					editText_amount.setText(scanContent);

				} catch(NumberFormatException nfe) {
					System.out.println("Could not parse " + nfe);
					finish();
				}


			}else{
				Intent back = new Intent(this,MobistoreApps.class);
				startActivity(back);

			}
		}
		else{
			Toast toast = Toast.makeText(getApplicationContext(),
					"No scan data received!", Toast.LENGTH_SHORT);
			toast.show();
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.airtime_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.qrtime:
			IntentIntegrator scanIntegrator = new IntentIntegrator(AirtimeBuy.this);
			scanIntegrator.initiateScan();
			return true;

				case R.id.home:
					Intent mainw = new Intent(this,MainActivity.class);
					startActivity(mainw);
					finish();
					return true;
			case android.R.id.home:
				finish();
				return true;


		default:
			return super.onOptionsItemSelected(item);
		}
	}


	
	public void buyairtime(View v){
		vars.log("=====imei========" + vars.imei+"====");
		if(!vars.Checknumber(this,vars.country_code,editText_number.getText().toString().trim())){
			inp_number.setError("Invalid number to send to");
		}
		else if(editText_amount.getText().toString().isEmpty()){
			inp_amount.setError("Amount is empty");
			inp_number.setErrorEnabled(false);

		}
		else if(editTex_pin.getText().toString().length()<4){
			inp_pin.setError("Invalid pin");
			inp_amount.setErrorEnabled(false);
		}
		else if(vars.imei==null){
			alerter.alerterSuccessSimple(AirtimeBuy.this,"Error device id", "This device doesn't support this service");
		}
		else{
			inp_amount.setErrorEnabled(false);
			inp_number.setErrorEnabled(false);
			inp_pin.setErrorEnabled(false);
				recieverPhone = editText_number.getText().toString().trim();


			vars.hideKeyboard(v,AirtimeBuy.this);

			new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
					.setTitleText("Are you sure?")
					.setContentText("You are about to buy Airtime worth\n" +
							editText_amount.getText().toString()+"\n" +
							"for this number\n"+
							recieverPhone)
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
							dialogpg = ProgressDialog.show(AirtimeBuy.this, "Processing Airtime", "Submiting request...", true);
							if(vars.country_code.equalsIgnoreCase("27")) {
								JSONObject json = new JSONObject();
								try {
									json.put("phoneNumber", vars.mobile);
									json.put("amount", editText_amount.getText().toString().trim());
									json.put("pin", editTex_pin.getText().toString().trim());
									json.put("otherNumber", recieverPhone);
									json.put("network", "");
									String url = Globals.SA_URL_AIRTIME+"makePinlessOrder";
									ConnectionClass.JsonString(Request.Method.POST,AirtimeBuy.this, url, json, "BUY AIR", new ConnectionClass.VolleyCallback() {
										@Override
										public void onSuccess(String result) {
											try {
												JSONObject reader = new JSONObject(result);
												vars.log("results===="+result);
												try{
													if(reader.getString("result").equalsIgnoreCase("Failed")){
														if (dialogpg != null && dialogpg.isShowing()) {
															dialogpg.dismiss();
															dialogpg = null;
														}
														alerter.alerterSuccessSimple(AirtimeBuy.this, "Error",
																reader.getString("error"));

													}

												}catch (Exception e){
													e.printStackTrace();
												}
												boolean res = reader.getBoolean("result");
												if(res){
													if (dialogpg != null && dialogpg.isShowing()) {
														dialogpg.dismiss();
														dialogpg = null;
													}
													alerterSuccessSimple("Success", reader.getString("message"));
												}else {
													if (dialogpg != null && dialogpg.isShowing()) {
														dialogpg.dismiss();
														dialogpg = null;
													}
													alerter.alerterSuccessSimple(AirtimeBuy.this, "Error",
															reader.getString("errorMessage"));
												}

											} catch (JSONException e) {
												e.printStackTrace();
											}
										}
									});

								}catch (JSONException e) {
									e.printStackTrace();
								}

							}else{
							String[] parameter = {"clientPin", "client", "recieverPhone", "amount", "imei"};
							String[] values = {editTex_pin.getText().toString().trim(), vars.mobile, recieverPhone, editText_amount.getText().toString().trim()
									, vars.imei};
							ConnectionClass.ConnectionClass(AirtimeBuy.this, vars.financial_server + "airtimePurchase.php", parameter,
									values, "AirtimeBuy", new ConnectionClass.VolleyCallback() {
										@Override
										public void onSuccess(String result) {
											TransactionObj trans = gson.fromJson(result, TransactionObj.class);
											vars.log("message" + trans.getMessage());
											vars.log("result" + trans.getResult());
											if (!trans.getResult().equalsIgnoreCase("FAILED")) {

												if (dialogpg != null && dialogpg.isShowing()) {
													dialogpg.dismiss();
													dialogpg = null;
												}


												alerterSuccessSimple("Success", trans.getMessage());
											} else {
												if (dialogpg != null && dialogpg.isShowing()) {
													dialogpg.dismiss();
													dialogpg = null;
												}
												alerter.alerterSuccessSimple(AirtimeBuy.this, "Error", trans.getError());
											}


										}
									});
						}
						}
					})
					.show();

		}
	}


	 protected void hideKeyboard(View view)
	    {
	        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }

	
		public void cancle_but(View v){
			Intent canc = new Intent(this,
					MobistoreApps.class);
			startActivity(canc);
			
		}
	public void alerterSuccessSimple(String header, String message) {
		new SweetAlertDialog(AirtimeBuy.this, SweetAlertDialog.SUCCESS_TYPE)
				.setTitleText(header)
				.setContentText(message)
				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						sweetAlertDialog.dismissWithAnimation();
						editTex_pin.setText("");
						editText_amount.setText("");

					}
				})
				.show();

	}

	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		return super.onRetainCustomNonConfigurationInstance();
	}

	@Override
	protected void onResume() {
		if(vars.country_code.equalsIgnoreCase("27") && bList.isEmpty()) {

		}
		if(vars.active==null) {
			Intent login = new Intent(this,MainActivity.class);
			startActivity(login);
			finish();
		}else {

		}
		super.onResume();
	}

}
