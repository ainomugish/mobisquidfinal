package com.mobisquid.mobicash.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.fragments.TourFragment;

public class Mainmenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Slide slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                TourFragment firstfragment = new TourFragment();
                //firstfragment.setArguments(extras);
                firstfragment.setReenterTransition(slideTransition);
                firstfragment.setExitTransition(slideTransition);
                firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main_apps, firstfragment, "MAIN")
                        .commit();
            } else {
                System.out.println("===========t============");
                TourFragment firstfragment = new TourFragment();
                //firstfragment.setArguments(extras);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.content_main_apps, firstfragment, "MAIN").commit();
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
