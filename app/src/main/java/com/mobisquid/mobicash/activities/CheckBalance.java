package com.mobisquid.mobicash.activities;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.fragments.Balance_first_layout;
import com.mobisquid.mobicash.utils.Vars;


public class CheckBalance extends AppCompatActivity {
	Vars vars;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vars = new Vars(this);
			setContentView(R.layout.checkbalance);
		ActionBar actionBar = getSupportActionBar();

		if (actionBar != null){
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Slide slideTransition = new Slide(Gravity.BOTTOM);
			slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
			Balance_first_layout firstfragment = new Balance_first_layout();
			firstfragment.setReenterTransition(slideTransition);
			firstfragment.setExitTransition(slideTransition);
			firstfragment.setSharedElementEnterTransition(new ChangeBounds());

			getSupportFragmentManager().beginTransaction()
					.add(R.id.balance_container, firstfragment, "MAIN")
					.commit();
		} else {
			System.out.println("===========t============");
			Balance_first_layout firstfragment = new Balance_first_layout();
			firstfragment.setArguments(getIntent().getExtras());
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.balance_container, firstfragment, "MAIN")
					.commit();
		}



	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.help_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title

		// Handle action bar actions click
		switch (item.getItemId()) {

			case android.R.id.home:
				onBackPressed();
				return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}




	void log(String msg) {
		System.out.println(msg);
	}

}
