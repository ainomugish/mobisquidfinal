package com.mobisquid.mobicash.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.ChatActivity;
import com.mobisquid.mobicash.adapters.Contact_Adapter;
import com.mobisquid.mobicash.dbstuff.ContactDetailsDB;
import com.mobisquid.mobicash.model.ChatClass;
import com.mobisquid.mobicash.model.ContactsDb;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.ContactsManager;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.SynContactsBackground;
import com.mobisquid.mobicash.utils.Vars;
import com.mobisquid.mobicash.wedget.drawer.views.SimpleDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactDetails extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    Menu mOptionsMenu;
    View rootview;
    Vars vars;
    int listofcontacts =0;
    Contact_Adapter contactAdapter;
    List<ContactDetailsDB> online_contact;
    List<ContactDetailsDB> union ;
    LinearLayout invite_layout;
    RecyclerView recList;
    static final String TAG = ContactDetails.class.getSimpleName();
    private static final int VERTICAL_ITEM_SPACE = 10;
    final ArrayList<ContactsDb> servercontacts= new ArrayList<>();
    final ArrayList<ContactsDb> necontactlist = new ArrayList<>();
    final List<String> str = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    private static final String EXTRA_IMAGE = "com.mobisquid.mobicash.extraImage";
    public ContactDetails() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        vars = new Vars(getActivity());
        online_contact = ContactDetailsDB.listAll(ContactDetailsDB.class);
        union = ContactDetailsDB.listAll(ContactDetailsDB.class);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_contacts, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        setHasOptionsMenu(true);
        online_contact = new ArrayList<>();
        invite_layout = (LinearLayout) rootview.findViewById(R.id.invite_layout);
       // invite_layout.setOnClickListener(this);
        invite_layout.setVisibility(View.GONE);
        recList = (RecyclerView) rootview.findViewById(R.id.cardList);
        recList.setVisibility(View.GONE);

        vars.log("size====="+union.size());
        if(union.isEmpty()){
            invite_layout.setVisibility(View.VISIBLE);
            recList.setVisibility(View.GONE);
        }else {
            invite_layout.setVisibility(View.GONE);
            recList.setVisibility(View.VISIBLE);
            recList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            recList.setLayoutManager(llm);
            //recList.setItemAnimator(new DefaultItemAnimator());

            contactAdapter = new Contact_Adapter(getActivity(),union);
            recList.setAdapter(contactAdapter);
           // recList.addItemDecoration(new SimpleDividerItemDecoration(getActivity(),VERTICAL_ITEM_SPACE));
            recList.setItemAnimator(new DefaultItemAnimator());
            contactAdapter.setOnItemClickListener(new Contact_Adapter.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    ContactDetailsDB cb = contactAdapter.getItem(position);
                    Log.i("MMMMMMMM","ID=============="+cb.getId());
                    Intent notice = new Intent(getActivity(),ChatActivity.class);
                    notice.putExtra("recep",cb.getUserid());
                    notice.putExtra("mobile",cb.getMobile());
                    notice.putExtra("recepname",cb.getUsername());
                    //notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), itemView.findViewById(R.id.person_photo), "profile");
                    ActivityCompat.startActivity(getActivity(), notice, options.toBundle());



                }
            });
        }
        return rootview;
    }
    private void roloadsuper(){
        union = ContactDetailsDB.listAll(ContactDetailsDB.class);
        contactAdapter = new Contact_Adapter(getActivity(),union);
        recList.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mOptionsMenu = menu;
        inflater.inflate(R.menu.contacts, menu);
        SearchManager SManager =  (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        //SearchView searchViewAction = (SearchView) searchMenuItem.getActionView();
        android.support.v7.widget.SearchView searchViewAction = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchViewAction.setSearchableInfo(SManager.getSearchableInfo(getActivity().getComponentName()));

        searchViewAction.setIconifiedByDefault(true);
        android.support.v7.widget.SearchView.OnQueryTextListener textChangeListener = new android.support.v7.widget.SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                // this is your adapter that will be filtered

                contactAdapter.getFilter().filter(newText);
                System.out.println("on text chnge text: "+newText);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // this is your adapter that will be filtered
                contactAdapter.getFilter().filter(query);
                System.out.println("on query submit: "+query);
                return true;
            }
        };
        searchViewAction.setOnQueryTextListener(textChangeListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.invite:
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
                break;
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }

        return false;
    }

    private void snyncontacts() {
        necontactlist.clear();
        servercontacts.clear();
        str.clear();
        mSwipeRefreshLayout.setRefreshing(true);
        String url = Globals.SOCILA_SERV+"entities.chat/allusers";

        try{
            JSONObject json = new JSONObject();
            json.put("accesscode", vars.accesscode);
            json.put("userid",vars.chk);
            ConnectionClass.JsonString(Request.Method.POST,getActivity(), url, json, "Contacts", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONArray detailsArray = new JSONArray(result);
                        if(detailsArray.length()==0){
                            mSwipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getActivity(),"Could not get contacts", Toast.LENGTH_LONG).show();
                        }else{

                            Log.e("Main","==online users==" + detailsArray.length());
                            for (int i = 0; i < detailsArray.length(); i++) {
                                ChatClass theObject =new Gson().fromJson(detailsArray.getString(i),
                                        ChatClass.class);
                                servercontacts.add(new ContactsDb(theObject.getUsername(), theObject.getMobile(),
                                        String.valueOf(theObject.getUserid()), theObject.getProfileurl(), "offline",
                                        "offline", theObject.getLanguage(),"fake"));
                            }

                            if (servercontacts.size() == 0) {
                                Log.e("main","==========online_contact========is=0=");
                            } else {
                                Log.e("main","==========online_contact======not empty");

                                if(ContactDetailsDB.listAll(ContactDetailsDB.class).isEmpty()){
                                    Log.i(TAG,"========contacts are=== empty");
                                    new SynContactsBackground(getActivity(),servercontacts, new SynContactsBackground.AsyncResponse() {
                                        @Override
                                        public void processFinish(ArrayList<ContactsDb> output) {
                                            listofcontacts = output.size();
                                            if(output.size()>0){

                                            }else {

                                            }
                                            Log.e("main","++++++++++++++++++++++++++++" + output.size());
                                            for(ContactsDb db :output){
                                                ContactsManager.addContact(getActivity(), db);
                                                Log.e("INLIST",db.getMobile()+"+++name" + db.getUsername()+"+++account"+db.getContactID());
                                            }

                                            if(listofcontacts==Globals.NUMUSERS){
                                                if(getActivity()!=null) {
                                                    mSwipeRefreshLayout.setRefreshing(false);
                                                    roloadsuper();
                                                }

                                            }


                                        }
                                    }).execute();
                                }else{
                                    Log.i("main","========contacts are=noooooot ===== empty");
                                    List<ContactDetailsDB> allcon = ContactDetailsDB.listAll(ContactDetailsDB.class);
                                    for (ContactDetailsDB con :allcon){
                                        str.add(con.getMobile());
                                    }
                                    for (ContactsDb servercon :servercontacts){
                                        if(str.contains(servercon.getMobile())){
                                            Log.e("SAME","++++alredaystored+++" + servercon.getMobile());
                                        }else{
                                            Log.d(TAG,servercon.getUsername()+"++++adding+++" + servercon.getMobile());
                                            necontactlist.add(new ContactsDb(servercon.getUsername(), servercon.getMobile(),
                                                    String.valueOf(servercon.getUserID()), servercon.getUrl(), "offline",
                                                    "offline", servercon.getLanguage(),"fake"));
                                        }
                                    }
                                    Log.e("main","necontactlist+++++++++++++" + necontactlist.size());
                                    new SynContactsBackground(getActivity(),necontactlist, new SynContactsBackground.AsyncResponse() {
                                        @Override
                                        public void processFinish(ArrayList<ContactsDb> output) {
                                            Log.e("main","++++++++++++++++++++++++++++" + output.size());
                                            listofcontacts = output.size();

                                            for(ContactsDb db :output){
                                                ContactsManager.addContact(getActivity(), db);
                                                Log.e("main","++++++++++++++++++++++++++++" + db.getMobile());
                                            }
                                            if(listofcontacts==Globals.NUMUSERS){
                                                if(getActivity()!=null) {
                                                    mSwipeRefreshLayout.setRefreshing(false);
                                                    roloadsuper();
                                                }

                                            }

                                        }
                                    }).execute();

                                }

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Chat");
    }

    @Override
    public void onRefresh() {
        snyncontacts();
    }
}
