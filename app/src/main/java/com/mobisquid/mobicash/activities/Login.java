package com.mobisquid.mobicash.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.format.Formatter;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.dbstuff.Apps;
import com.mobisquid.mobicash.model.ChatClass;
import com.mobisquid.mobicash.model.LoginMailObj;
import com.mobisquid.mobicash.model.NewApp;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.Appinstallation;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.SendEmailService;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {
    String accesscode, location;
    String[] countryCode;
    String[] Allcountryies;
    EditText tv_username, password;
    TextInputLayout inp_username, inp_password;
    TelephonyManager telephonyManager;
    private ProgressDialog mDialog;
    Vars vars;
    Alerter alerter;
    ProgressDialog dialog;
    Gson gson;

    String imei;
    Bundle extras;
    String financials;
    static int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 3;
    String code = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        countryCode = this.getResources().getStringArray(R.array.option_array_code);
        Allcountryies = this.getResources().getStringArray(R.array.option_array);
        setContentView(R.layout.login);
        Utils.hidekBoard(findViewById(R.id.main_layout),this);
        Utils.hidekBoard(findViewById(R.id.image),this);
        Utils.hidekBoard(findViewById(R.id.main),this);
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            } else {
                getDeviceImei();
            }

        }
        inp_username = (TextInputLayout) findViewById(R.id.inp_username);
        inp_password = (TextInputLayout) findViewById(R.id.inp_password);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("Financial Login");
        }
        extras = getIntent().getExtras();
        //online_contact = new ArrayList<>();

        vars = new Vars(this);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		vars.log("===============+++++++++=="+tel.getSimSerialNumber());
        alerter = new Alerter(this);
        if (extras != null) {
            financials = extras.getString("financials");
        }
        gson = new Gson();
        tv_username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.ev_password);
        if (vars.mobile != null) {
            if (vars.mobile != null && String.valueOf(vars.mobile.charAt(0)).equalsIgnoreCase("+")) {
                tv_username.setText(vars.mobile.substring(1));
            } else if (vars.mobile != null && !String.valueOf(vars.mobile.charAt(0)).equalsIgnoreCase("+")) {
                tv_username.setText(vars.mobile);
            }
            tv_username.setFocusable(false);
            tv_username.setLongClickable(false);
        }


    }

    public void upgrade(View v) {

        Intent notice = new Intent(this, SmsActivity.class);
        notice.putExtra("financials", "financials");
        notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, this.findViewById(R.id.image), "login");
        ActivityCompat.startActivity(this, notice, options.toBundle());

        //	Intent upgrade = new Intent(this, Full_Registration.class);
    /*	if(vars.chk!=null) {
            Intent upgrade = new Intent(this, SmsActivity.class);
			upgrade.putExtra("financials", "financials");
			startActivity(upgrade);
		}else{
			new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
					.setTitleText("NOTE")
					.setContentText("Please login to social or create an account first for social, to use Financial services")
					.setCancelText("Create account")
					.setConfirmText("Login")
					.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
						@Override
						public void onClick(SweetAlertDialog sweetAlertDialog) {
							sweetAlertDialog.dismiss();
							Intent tem = new Intent(Login.this,TermsAndConditions.class);
							startActivity(tem);

						}
					})
					.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
						@Override
						public void onClick(SweetAlertDialog sweetAlertDialog) {
							sweetAlertDialog.dismiss();
							Intent join = new Intent(Login.this,RegistrationActivity.class);
							join.putExtra("login","login");
							startActivity(join);
							finish();

						}
					}).show();
		}*/

    }

    public void login_now(View v) {
        vars.hideKeyboard(v, this);
        vars.log("Using code =============" + code);

        if (!vars.Checknumber(this, code, tv_username.getText().toString().trim())) {
            inp_username.setError("Invalid number");
            //Toast.makeText(this, "Enter all fields", Toast.LENGTH_LONG).show();
        } else if (password.getText().toString().equalsIgnoreCase("")) {
            inp_password.setError("Pin is empty");
            inp_username.setErrorEnabled(false);
        } else {
            inp_password.setErrorEnabled(false);
            inp_username.setErrorEnabled(false);

            if (getDeviceImei() == null) {
                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Please note!!")
                        .setContentText("Financial services will not work with out getting your device id. Please allow permission to get your device id\n" +
                                "OR the device doesn't have the id")
                        .setCancelText("Don't allow")
                        .setConfirmText("Yes,allow")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                if (Build.VERSION.SDK_INT >= 23) {
                                    if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                                                MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                                    } else {
                                        getDeviceImei();
                                    }
                                }
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                onBackPressed();
                            }
                        }).show();
            } else {

                dialog = ProgressDialog.show(this, "Checking Credentials", "Please wait...");
                String url = Utils.FincialServer(code) + "client_login.php";

                String[] parameter = {"clientPin", "clientNumber", "imei"};
                String[] values = {password.getText().toString().trim(), tv_username.getText().toString().trim(), getDeviceImei().trim()};

                ConnectionClass.ConnectionClass(this, url, parameter, values, "Loginn", new ConnectionClass.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        vars.log("results ====" + result);

                        final TransactionObj gsonMessage = new Gson().fromJson(result, TransactionObj.class);
                        if (gsonMessage.getResult().equalsIgnoreCase("Success")) {

                            Intent sendmail = new Intent(Login.this, SendEmailService.class);
                            sendmail.putExtra("fullname", gsonMessage.getDetails());
                            sendmail.putExtra("mobile", gsonMessage.getClient());
                            sendmail.putExtra("email", gsonMessage.getExtra1());
                            startService(sendmail);
                            if (getDeviceImei().trim().equalsIgnoreCase(gsonMessage.getClientId())) {
                                String profile_number = null;
                                vars.log("client number ====" + gsonMessage.getClient());
                                vars.edit.putString("active", "activenow");
                                //vars.edit.putString("social", "social");
                                vars.edit.putString("email", gsonMessage.getExtra1());
                                vars.edit.putString("imei", getDeviceImei().trim());
                                vars.edit.putString("profile_number", gsonMessage.getClient());
                                vars.edit.putString("fullname", gsonMessage.getDetails());
                                vars.edit.putString("mobile", gsonMessage.getClient());
                                if (gsonMessage.getExtra2() != null) {
                                    vars.edit.putString("qrcode", gsonMessage.getExtra2());
                                }
                                vars.edit.apply();
                                Utils.isFinance(Login.this,true);

                                dialog.dismiss();
                                if (gsonMessage.getExtra2() == null) {
                                    Intent mainpage = new Intent(Login.this, QrcodeUpdates.class);
                                    startActivity(mainpage);
                                    finish();

                                } else {
                                    Utils.home(Login.this);
                                }
                            } else {
                                dialog.dismiss();

                                Intent caution = new Intent(Login.this, Update_Financial.class);
                                caution.putExtra("name", gsonMessage.getDetails());
                                startActivity(caution);
                                finish();

                            }

                        } else {
                            dialog.dismiss();
                            alerter.alerterSuccessSimple(Login.this, "Error", gsonMessage.getError());
                            //}

                        }

                    }
                });

            }
        }


    }

    public void register(View view) {
        vars.hideKeyboard(view, this);
        if (getDeviceImei() == null) {
            new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Please note!!")
                    .setContentText("Financial services will not work with out getting your device id. Please allow permission to get your device id\n" +
                            "OR the device doesn't have the id")
                    .setCancelText("Don't allow")
                    .setConfirmText("Yes,allow")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                                            MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                                } else {
                                    getDeviceImei();
                                }
                            }
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            onBackPressed();
                        }
                    }).show();
        } else {
            String number = vars.mobile.substring(1);
            Intent login = new Intent(this, FinactionalRegistration.class);
            login.putExtra("number", code + number);
            login.putExtra("location", vars.location);
            login.putExtra("code", code);
            startActivity(login);
            finish();
        }

		/*if(vars.active==null && vars.mobile!=null) {
			if(getDeviceImei()==null){
				new SweetAlertDialog(Login.this,SweetAlertDialog.WARNING_TYPE)
						.setTitleText("Please note!!")
						.setContentText("Financial services will not work with out getting your device id. Please allow permission to get your device id\n" +
								"OR the device doesn't have the id")
						.setCancelText("Don't allow")
						.setConfirmText("Yes,allow")
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismiss();
								if (Build.VERSION.SDK_INT >= 23) {
									if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
											!= PackageManager.PERMISSION_GRANTED) {
										requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
												MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
									} else {
										getDeviceImei();
									}
								}
							}
						})
						.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismiss();
								onBackPressed();
							}
						}).show();
			}else {
				String number = vars.mobile.substring(1);
				Intent login = new Intent(this, FinactionalRegistration.class);
				login.putExtra("number", vars.country_code + number);
				login.putExtra("location", vars.location);
				login.putExtra("code", vars.country_code);
				startActivity(login);
				finish();
			}
		}else {
			Intent reg = new Intent(this, MainActivity.class);
			startActivity(reg);
		}*/
    }

    public void alerterSuccessSimple(String header, String message) {

        final String header2 = header;

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView;
        promptsView = li.inflate(R.layout.dialog_success_simple, null);

        TextView headerTxt = (TextView) promptsView.findViewById(R.id.success_simple_header);
        TextView messageTxt = (TextView) promptsView.findViewById(R.id.success_simple_message);

        headerTxt.setText(header);
        messageTxt.setText(message);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)

                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent mainpage = new Intent(Login.this, MainActivity.class);
                                startActivity(mainpage);
                                finish();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (vars.social == null) {
                    //	Intent chk = new Intent(this, IndexPageActivity.class);
                    //	startActivity(chk);
                    finish();
                } else {
                    finish();
                }
                return true;

        }
        return super.onOptionsItemSelected(item);

    }

    public void forgotpassword(View view) {
        //Intent forgot = new Intent(this, FinactionalRegistration.class);
        //startActivity(forgot);
        //	Intent forgot = new Intent(this, ForgotPassword_Activity.class);
        //	startActivity(forgot);
    }

    public interface VolleyCallback {
        void onSuccess(String result);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (vars.country_code == null) {
            //get counrtrtrt
            getIPAddress();
        } else {
            code = vars.country_code;
        }

    }

    public void getIPAddress() {
        Alerter.showdialog(this);
        ConnectionClass.FreeString(this, "http://v4.ipv6-test.com/api/myip.php", "location", new ConnectionClass.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                vars.log("Ip===" + result);
                if (SendEmailService.validIP(result)) {
                    String params[] = {};
                    String value[] = {};
                    String url = "http://api.ipinfodb.com/v3/ip-city/?key=baf504d8f523141f935087719d5bc4a79c31636309b8b8e02ca3d13c5f15cad8&ip=" + result + "&format=json";
                    ConnectionClass.ConnectionClass(Login.this, url, params, value, "loc", new ConnectionClass.VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            vars.log("JSON======" + result);
                            Alerter.stopdialog();
                            if (result != null) {
                                try {
                                    SendEmailService.LocationDetails loc = new Gson().fromJson(result, SendEmailService.LocationDetails.class);
                                    if (loc.getCountryName().length() > 2 && loc.getCityName().length() > 2) {
                                        //country = loc.getCountryName();
                                        vars.log("Got one LOC=========" + loc.getCountryName());
                                        code = countryCode[Arrays.asList(Allcountryies).indexOf(loc.getCountryName())];
                                        Log.e("CODE==", "YOUR CODE+++++" + code);
                                        vars.edit.putString("country_code", code);
                                        vars.edit.putString("financial_server", Utils.FincialServer(code));
                                        vars.edit.apply();

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                location = null;
                            }

                        }
                    });


                } else {

                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getDeviceImei();
        } else {
            new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Please note!!")
                    .setContentText("Financial services won't work with out getting your device id. Please allow permission to get your device id")
                    .setCancelText("Don't allow")
                    .setConfirmText("Yes,allow")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                                            MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                                } else {
                                    getDeviceImei();
                                }
                            }
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            onBackPressed();
                        }
                    }).show();
        }
    }

    private String getDeviceImei() {

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

    }

}