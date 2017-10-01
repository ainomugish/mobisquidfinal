package com.mobisquid.mobicash.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.ChatActivity;
import com.mobisquid.mobicash.activities.MainActivity;
import com.mobisquid.mobicash.adapters.RecentAdapter;
import com.mobisquid.mobicash.dbstuff.MessageDb;
import com.mobisquid.mobicash.utils.Vars;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentChat extends Fragment implements RecentAdapter.OnItemClickListener {

    RecentAdapter recentAdapter;
    Vars vars;
    String recep;
    CardView listrecent;
    LinearLayout invite_layout;
    RecyclerView convo_list;
    private static String EXTRA_USERID = "userid";
    Date resultdatetwo, date;
    SimpleDateFormat formatter, formatter_two, today;
    List<MessageDb> groupMsg, allmessages;
    View rootView;
    static final String TAG = RecentChat.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        vars = new Vars(getActivity());
        rootView = inflater.inflate(R.layout.fragment_recent_chat, container, false);
        setHasOptionsMenu(true);
        invite_layout = (LinearLayout) rootView.findViewById(R.id.invite_layout);
        listrecent =(CardView) rootView.findViewById(R.id.listrecent);
        listrecent.setVisibility(View.GONE);
        vars = new Vars(getActivity());
        convo_list = (RecyclerView) rootView.findViewById(R.id.convo_list);
        convo_list.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        convo_list.setLayoutManager(llm);
        convo_list.setItemAnimator(new DefaultItemAnimator());

        rootView.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Try (MobiSquid) for Android!");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "I'm using MobiSquid for Android and I recommend it. Click here: https://play.google.com/store/apps/details?id=com.mobisquid.mobicash");

                Intent chooserIntent = Intent.createChooser(shareIntent, "Share with");
                chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chooserIntent);
            }
        });
        rootView.findViewById(R.id.invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide slideTransition = new Slide(Gravity.LEFT);
                    slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                    InviteFriends firstfragment = new InviteFriends();
                    firstfragment.setReenterTransition(slideTransition);
                    firstfragment.setExitTransition(slideTransition);
                    firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                } else {
                    System.out.println("===========t============");
                    InviteFriends firstfragment = new InviteFriends();
                    firstfragment.setArguments(getArguments());
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        groupMsg = new ArrayList<>();
        reload();
        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.recent_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Try (MobiSquid) for Android!");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "I'm using MobiSquid for Android and I recommend it. Click here: https://play.google.com/store/apps/details?id=com.mobisquid.mobicash");

                Intent chooserIntent = Intent.createChooser(shareIntent, "Share with");
                chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chooserIntent);
                break;
            case R.id.home:
                Intent home = new Intent(getActivity(), MainActivity.class);
                startActivity(home);
                getActivity().finish();

                return false;

            default:
                break;
        }

        return false;
    }

    public void reload() {

        convo_list = (RecyclerView) rootView.findViewById(R.id.convo_list);
        convo_list.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        convo_list.setLayoutManager(llm);
        convo_list.setItemAnimator(new DefaultItemAnimator());
        groupMsg = Select.from(MessageDb.class).
                where(Condition.prop("recep").eq(vars.chk))
                .or(Condition.prop("sender").eq(vars.chk))
                .groupBy("thread").list();
        if(groupMsg.isEmpty()){
            listrecent.setVisibility(View.GONE);
            invite_layout.setVisibility(View.VISIBLE);
        }else {
            invite_layout.setVisibility(View.GONE);
            listrecent.setVisibility(View.VISIBLE);
        }

     //   groupMsg = MessageDb.findWithQuery(MessageDb.class, "Select * from MessageDb groupBy name =?", "thread");

        for (MessageDb nnn : groupMsg) {
            Log.i(TAG, nnn.getThread()+"==MES=="+nnn.getMessage());
        }
        /*groupMsg =  Select.from(MessageDb.class).
                where(Condition.prop("recep").notEq(vars.chk))
                .and(Condition.prop("sender").notEq(vars.chk))
                .groupBy("thread").list();*/
        if (getActivity() != null) {
            recentAdapter = new RecentAdapter(getActivity(), groupMsg);
        }
        Collections.sort(groupMsg, new Comparator<MessageDb>() {
            @Override
            public int compare(MessageDb u1, MessageDb u2) {

                return u2.getId().compareTo(u1.getId());
            }
        });

        convo_list.setAdapter(recentAdapter);
        if (getActivity() != null) {
            vars.log("getActivity()====================notnull=============");
            recentAdapter.setOnItemClickListener(this);
        }

    }


    @Override
    public void onItemClick(View itemView, int position) {
        MessageDb cb = recentAdapter.getItem(position);
        Intent notice = new Intent(getActivity(), ChatActivity.class);
        if (cb.getRecep().equalsIgnoreCase(vars.chk)) {
            recep = cb.getSender();
            notice.putExtra("recep", recep);
            notice.putExtra("mobile", cb.getMobileNumber());
            notice.putExtra("recepname", cb.getUserName());
        } else {
            recep = cb.getRecep();
            notice.putExtra("recep", recep);
            notice.putExtra("mobile", cb.getRecepNumber());
            notice.putExtra("recepname", cb.getOtherName());
        }
        //notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), itemView.findViewById(R.id.list_image), "profile");
        ActivityCompat.startActivity(getActivity(), notice, options.toBundle());
    }


    @Override
    public void onPause() {
        if (getActivity() != null) {
            // getActivity().unregisterReceiver(ackReceiver);
        }
        super.onPause();
    }


    @Override
    public void onResume() {
        vars.log("=========reload========");
        if (getActivity() != null) {
            reload();

        }
        super.onResume();
    }

}