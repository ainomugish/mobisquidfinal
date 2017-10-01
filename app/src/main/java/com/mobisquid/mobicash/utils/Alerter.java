package com.mobisquid.mobicash.utils;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
;import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.Login;
import com.mobisquid.mobicash.activities.MainActivity;
import com.mobisquid.mobicash.activities.RegistrationActivity;
import com.mobisquid.mobicash.fragments.Success_Fragment;

public class Alerter {
	
	//Shared prefs
	SharedPreferences prefs;
	Editor edit;
	Vars vars;
	TextView serverText;
	AlertDialog levelDialog;
	public static SweetAlertDialog pDialog;
	Context context;

	public Alerter(Context context) {
		this.context = context;
		
		
	}
	
	//----------------------------------------------------------------------------
	// ALERT GENERATOR
	//public void alerter(String title, String message,Context context) {
	public void alerter(String title, String message, Context context) {


		// public void onClick(View arg0) {
		// loadEm();
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder.setMessage(message).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						// MainActivity.this.finish();
										
						
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
		// }
	}


	
	public void alerterSuccess(String header, String message){
		
		final String header2 = header;
		
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView;
		 promptsView = li.inflate(R.layout.dialog_success_simple, null);
		 
		 TextView headerTxt = (TextView) promptsView.findViewById(R.id.success_simple_header);
		 TextView messageTxt = (TextView) promptsView.findViewById(R.id.success_simple_message);

		 headerTxt.setText(header);
		 messageTxt.setText(message);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);

			// set prompts.xml to alertdialog builder
			alertDialogBuilder.setView(promptsView);
			// set dialog message
			alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
				    	//dialog.cancel();
				    	dialog.cancel();
				    	
				    	//CHECK IF ITS A PAYMENT BEING MAKE
				    	if(header2.equalsIgnoreCase("Success")){
				    		Intent i = new Intent();
							i.setClass(context, MainActivity.class);
							context.startActivity(i);
				    	}
				    
				    }
				  })
				.setNegativeButton("Cancel",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				    }
				  });

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
	}
	public void alerterSuccessSimple(Context context,String header, String message){
		new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
				.setTitleText(header)
				.setContentText(message)
				.show();
	}


	public static void showdialog(Context context){
		pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
		pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
		pDialog.setTitleText("Please wait...");
		pDialog.setCancelable(false);
		pDialog.show();
	}
	public static void stopdialog(){
		if(pDialog!=null){
			pDialog.dismiss();
		}
	}
	public static void Error(Context context,String header, String message){
		new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
				.setTitleText(header)
				.setContentText(message)
				.show();
	}
	public static void Sociallogin(final Context context , String header, String message){

		final String header2 = header;

		LayoutInflater li = LayoutInflater.from(context);
		View promptsView;
		promptsView = li.inflate(R.layout.dialog_success_simple, null);

		TextView headerTxt = (TextView) promptsView.findViewById(R.id.success_simple_header);
		TextView messageTxt = (TextView) promptsView.findViewById(R.id.success_simple_message);

		headerTxt.setText(header);
		messageTxt.setText(message);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);
		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Login",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {

								//	dialog.cancel();

								//CHECK IF ITS A PAYMENT BEING MAKE
								//	if(header2.equalsIgnoreCase("Success")){
								Intent i = new Intent();
								i.setClass(context, RegistrationActivity.class);
								i.putExtra("login","login");
								context.startActivity(i);
								((Activity) context).finish();
								//	}

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {

								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	public void login(String header, String message){

		final String header2 = header;

		LayoutInflater li = LayoutInflater.from(context);
		View promptsView;
		promptsView = li.inflate(R.layout.dialog_success_simple, null);

		TextView headerTxt = (TextView) promptsView.findViewById(R.id.success_simple_header);
		TextView messageTxt = (TextView) promptsView.findViewById(R.id.success_simple_message);

		headerTxt.setText(header);
		messageTxt.setText(message);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);
		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Login",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {

								//	dialog.cancel();

								//CHECK IF ITS A PAYMENT BEING MAKE
								//	if(header2.equalsIgnoreCase("Success")){
								Intent i = new Intent();
								i.setClass(context, Login.class);
								//i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
								i.putExtra("financials","financials");
								context.startActivity(i);
								((Activity) context).finish();
								//	}

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {

								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	public static void Successpayment(Activity mContext, String amount, String result,String type,int container){
		Bundle extra = new Bundle();
		extra.putString("amount",amount);
		extra.putString("result",result);
		extra.putString("type",type);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Slide slideTransition = new Slide(Gravity.LEFT);
			slideTransition.setDuration(mContext.getResources().getInteger(R.integer.anim_duration_long));
			Success_Fragment firstfragment = new Success_Fragment();
			firstfragment.setArguments(extra);
			firstfragment.setReenterTransition(slideTransition);
			firstfragment.setExitTransition(slideTransition);
			firstfragment.setSharedElementEnterTransition(new ChangeBounds());
			((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
					.replace(container, firstfragment, "MAIN")
					.commit();
		} else {
			System.out.println("===========t============");
			Success_Fragment firstfragment = new Success_Fragment();
			firstfragment.setArguments(extra);
			FragmentTransaction ft = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
			ft.replace(container, firstfragment, "MAIN").commit();
		}

	}

}
