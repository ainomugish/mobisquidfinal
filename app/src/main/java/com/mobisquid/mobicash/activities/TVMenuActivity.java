package com.mobisquid.mobicash.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.fragments.TvMenuFragment;
import com.mobisquid.mobicash.utils.Utils;


/**
 * Created by MOBICASH on 24-Apr-15.
 */
public class TVMenuActivity extends AppCompatActivity implements TvMenuFragment.OnFragmentInteractionListener {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.tv_menu);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      toolbar.setTitle("TV Menu");
      setSupportActionBar(toolbar);
      ActionBar actionBar = getSupportActionBar();

      if (actionBar != null){
         actionBar.setDisplayHomeAsUpEnabled(true);
         actionBar.setHomeButtonEnabled(true);
      }
      if (savedInstanceState == null) {

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slideTransition = new Slide(Gravity.LEFT);
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
            TvMenuFragment firstfragment = new TvMenuFragment();
            firstfragment.setArguments(getIntent().getExtras());
            firstfragment.setReenterTransition(slideTransition);
            firstfragment.setExitTransition(slideTransition);
            firstfragment.setSharedElementEnterTransition(new ChangeBounds());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.tvmenu_container, firstfragment, "MAIN")
                    .commit();
         } else {
            System.out.println("===========t============");
            TvMenuFragment firstfragment = new TvMenuFragment();
            firstfragment.setArguments(getIntent().getExtras());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.tvmenu_container, firstfragment, "MAIN").commit();
         }
      }

   }
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.home, menu);

      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case android.R.id.home:
            finish();
            return true;
         case R.id.home:
            Utils.home(this);
            return true;
      }
      return super.onOptionsItemSelected(item);
   }

   @Override
   public void onFragmentInteraction(Uri uri) {

   }
}
