package com.mobisquid.mobicash.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.dbstuff.Apps;
import com.mobisquid.mobicash.fragments.FirstFragment_Reg;
import com.mobisquid.mobicash.utils.Globals;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class FirstActivity extends AppCompatActivity {
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        extras = getIntent().getExtras();

        if (savedInstanceState == null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Slide slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                FirstFragment_Reg firstfragment = new FirstFragment_Reg();
                firstfragment.setArguments(getIntent().getExtras());
                firstfragment.setReenterTransition(slideTransition);
                firstfragment.setExitTransition(slideTransition);
                firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, firstfragment, "MAIN")
                        .commit();
            } else {
                System.out.println("===========t============");
                FirstFragment_Reg firstfragment = new FirstFragment_Reg();
                firstfragment.setArguments(getIntent().getExtras());
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.fragment_container, firstfragment, "MAIN").commit();
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_first, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
