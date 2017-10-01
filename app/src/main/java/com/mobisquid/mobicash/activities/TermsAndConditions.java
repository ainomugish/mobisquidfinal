package com.mobisquid.mobicash.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mobisquid.mobicash.R;


public class TermsAndConditions extends AppCompatActivity {
	Button nxt;
	Bundle extr;
	String location;

	String financials;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terms_and_conditions);

		nxt = (Button) findViewById(R.id.nxtbut);
		nxt.setVisibility(View.GONE);
		TextView foo = (TextView)findViewById(R.id.read);
		foo.setText(Html.fromHtml(getString(R.string.nice_html)));

		extr = getIntent().getExtras();

		if(extr!=null){
			location = extr.getString("location");
			financials = extr.getString("financials");
		}
	}
	public void nextbut(View v){
		if(financials!=null){
			Intent upgrade = new Intent(this, SmsActivity.class);
			upgrade.putExtra("financials", "financials");
			upgrade.putExtra("location", location);
			startActivity(upgrade);
			finish();

		}else {
			Intent next = new Intent(this, SmsActivity.class);

			next.putExtra("location", location);
			startActivity(next);
			finish();
		}
	}
	public void onCheckboxClicked(View v){
		boolean checked = ((CheckBox) v).isChecked();
		
		switch (v.getId()){
		case R.id.checkBox1:
			if(checked){
				nxt.setVisibility(View.VISIBLE);	
			}
			else{
				nxt.setVisibility(View.GONE);	
			}
		}

		
	}
	

}
