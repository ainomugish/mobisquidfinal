package com.mobisquid.mobicash.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.adapters.MenuAdapters;
import com.mobisquid.mobicash.fragments.BroacastMessages;
import com.mobisquid.mobicash.fragments.GroupMessages;
import com.mobisquid.mobicash.fragments.InviteFriends;
import com.mobisquid.mobicash.fragments.MainChatFragment;
import com.mobisquid.mobicash.fragments.Settings_fragment;
import com.mobisquid.mobicash.fragments.SetupChatFragment;
import com.mobisquid.mobicash.utils.CircleTransform;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Vars;
import com.mobisquid.mobicash.wedget.drawer.views.DuoDrawerLayout;
import com.mobisquid.mobicash.wedget.drawer.views.DuoMenuView;
import com.mobisquid.mobicash.wedget.drawer.widgets.DuoDrawerToggle;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatMain extends AppCompatActivity
        implements DuoMenuView.OnMenuClickListener,Settings_fragment.OnFragmentInteractionListener  {
    MenuAdapters mMenuAdapter;
    private ViewHolder mViewHolder;
    Vars vars;
    Toolbar toolbar;
    private ArrayList<String> mTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(this);
        setContentView(R.layout.activity_chat_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));

        ImageView imageview = (ImageView) findViewById(R.id.image) ;
        TextView name = (TextView) findViewById(R.id.duo_view_header_text_title) ;
        TextView mobile = (TextView) findViewById(R.id.duo_view_header_text_sub_title) ;
        mobile.setText(vars.mobile);
        name.setText(vars.fullname);
        Picasso.with(this).load(Globals.USERIMAGE+vars.chk+".png")
                .transform(new CircleTransform())
                .into(imageview);
        // Initialize the views
        mViewHolder = new ViewHolder();

        // Handle toolbar actions
        handleToolbar();

        // Handle menu actions
        handleMenu();

        // Handle drawer actions
        handleDrawer();

        if(!vars.chaton){
           // goToFragment(new SetupChatFragment(), false);
            Intent notice = new Intent(this, ChatsetUp.class);
            notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageview, "money");
            ActivityCompat.startActivity(this, notice, options.toBundle());
            finish();
            //mViewHolder.mDuoDrawerLayout.setDrawerLockMode(DuoDrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }else {
            goToFragment(new MainChatFragment(), false);
        }
        mMenuAdapter.setViewSelected(0, true);
        setTitle(mTitles.get(0));
    }

    private void handleToolbar() {
        setSupportActionBar(mViewHolder.mToolbar);
    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mViewHolder.mDuoDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();

    }

    private void handleMenu() {
        mMenuAdapter = new MenuAdapters(mTitles);

        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
    }

    @Override
    public void onFooterClicked() {
        Toast.makeText(this, "onFooterClicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHeaderClicked(View v) {

        if(v.getId() == R.id.image){
            Toast.makeText(this, "image", Toast.LENGTH_SHORT).show();
        }
    }

   /* @Override
    public void onHeaderClicked() {
        Toast.makeText(this, "onHeaderClicked", Toast.LENGTH_SHORT).show();
    }*/

    public void goToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


            if (addToBackStack) {
                transaction.addToBackStack(null);
            }

            transaction.replace(R.id.container, fragment).commit();

    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
        setTitle(mTitles.get(position));

        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);

        // Navigate to the right fragment
        switch (position) {
            case 0:
                goToFragment(new MainChatFragment(),false);
                break;
            case 1:
                goToFragment(new BroacastMessages(),false);
                break;
            case 2:
                goToFragment(new InviteFriends(),false);
                break;
            case 3:
                goToFragment(new Settings_fragment(),false);
                break;
            case 4:
                goToFragment(new GroupMessages(),false);
                break;
            default:
                goToFragment(new MainChatFragment(), false);
                break;
        }

        // Close the drawer
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Globals.whichfag="";
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
    public void onFragmentInteraction(Uri uri) {

    }


    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
        }
    }
}
