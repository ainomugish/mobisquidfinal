package com.mobisquid.mobicash.fragments;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.adapters.Contact_new_Adapter;
import com.mobisquid.mobicash.dbstuff.ContactDetailsDB;
import com.mobisquid.mobicash.model.Person;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Vars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;



public class InviteFriends extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    List<Person> phonelist,dbaddedlist,sortedlist,finalusers;
    static final String TAG = InviteFriends.class.getSimpleName();
    String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    Vars vars;
    String numb="";
    String dupname="";
    String phonenum ="";
    String dbnum="";
    String fffff = "";
    RecyclerView recList;
    ProgressBar progressBar;
    List<ContactDetailsDB> dbcontactlist;
    Contact_new_Adapter contactAdapter;
    Cursor cursorgenal;
    Menu mOptionsMenu;
    List<String> str = new ArrayList<>();
    private static final String STATE_PREVIOUSLY_SELECTED_KEY = "SELECTED_ITEM";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        phonelist = new ArrayList<>();
        dbaddedlist = new ArrayList<>();
        sortedlist = new ArrayList<>();
        finalusers =new ArrayList<>();
        vars = new Vars(getActivity());

        View rootView = inflater.inflate(R.layout.fragment_invitefriends, container, false);
        setHasOptionsMenu(true);
        getActivity().getSupportLoaderManager().initLoader(1, null, this);


        recList = (RecyclerView) rootView.findViewById(R.id.cardList);
        progressBar =(ProgressBar) rootView.findViewById(R.id.progressBar);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        contactAdapter = new Contact_new_Adapter(getActivity(), sortedlist) ;
        recList.setAdapter(contactAdapter);

        // recList.setItemAnimator(new DefaultItemAnimator());

        if(savedInstanceState!=null){
            vars.log("=====================savedInstanceState!=null==============");
            contactAdapter.notifyDataSetChanged();
        } else {
            vars.log("=====================savedInstanceState==null==============");

            contactAdapter.notifyDataSetChanged();
        }

        contactAdapter.setOnItemClickListener(new Contact_new_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                Person items = (Person) contactAdapter.getItem(position);
                Uri uri = Uri.parse("smsto:" + items.getPhonenumber());
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", "Hello buddy Mobisquid is really cool join me by downloading it from: https://play.google.com/store/apps/details?id=com.mobisquid.mobicash");
                startActivity(it);
            }
        });

        return rootView ;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        recList.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();
        super.onSaveInstanceState(outState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        vars.log("onCreateLoader====================now");
        if(recList!=null){
            vars.log("list=not null===========now+++++++++++++++++++++++++++");
        }

        Uri CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        return new CursorLoader(getActivity(), CONTENT_URI, null,null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorgenal = cursor;
        new Contactssync(loader).execute();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        vars.log("onLoaderReset==============================now");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mOptionsMenu = menu;
        getActivity().getMenuInflater().inflate(R.menu.contact_frag_menu, menu);
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

        // toggle nav drawer on selecting action bar app icon/title
      /*  if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/
        // Handle action bar actions click
        switch (item.getItemId()) {
           /* case R.id.contact_settings:
                Intent con_set = new Intent(this,Contact_Settings.class);
                startActivity(con_set);

                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class Contactssync extends AsyncTask <Void, Integer, List<Person>>  {
        Loader<Cursor> loader;

        public Contactssync(Loader<Cursor> loader){
            this.loader = loader;

        }

        @Override
        protected List<Person> doInBackground(Void... params) {

            dbcontactlist = ContactDetailsDB.listAll(ContactDetailsDB.class);
            if(dbcontactlist.isEmpty()){

            }else{
                for(ContactDetailsDB conct:dbcontactlist){
                    String substr = conct.getMobile().substring(conct.getMobile().length() - 9);
                    dbnum = substr.replaceAll("\\+", "");
                    dbaddedlist.add(new Person(conct.getUsername(),dbnum,""));

                }
            }
            //  StringBuilder sb = new StringBuilder();
            Cursor cursorgenal = vars.context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);

            cursorgenal.moveToFirst();
            while (!cursorgenal.isAfterLast()) {
                String phoneNumber =  cursorgenal.getString(cursorgenal.getColumnIndex(NUMBER));
                String phoneNumbe = phoneNumber.replace(" ", "");
                String phoneNumbers = phoneNumbe.replace("-", "");
                String finalno = phoneNumbers.replaceAll("\\+", "");
                String accType = cursorgenal.getString(cursorgenal.getColumnIndexOrThrow(ContactsContract.RawContacts.ACCOUNT_TYPE));
                if(finalno.length()>7) {
                    if(isAlpha(cursorgenal.getString(cursorgenal.getColumnIndex(DISPLAY_NAME)))){
                        Log.d(TAG,"name with number++++"+cursorgenal.getString(cursorgenal.getColumnIndex(DISPLAY_NAME)));
                    }else{
                        Log.d(TAG,"name++++"+cursorgenal.getString(cursorgenal.getColumnIndex(DISPLAY_NAME)));
                        phonelist.add(new Person(cursorgenal.getString(cursorgenal.getColumnIndex(DISPLAY_NAME)), finalno,accType));

                    }

                }
                cursorgenal.moveToNext();
            }
            Collections.sort(phonelist, new Comparator<Person>() {
                @Override
                public int compare(Person u1, Person u2) {

                    return u1.getName().compareToIgnoreCase(u2.getName());
                }
            });
            for (int i = 0; i < phonelist.size(); i++) {

                Person person = phonelist.get(i);
                if (person.getPhonenumber().length() >= 9) {

                    String substr = person.getPhonenumber().substring(person.getPhonenumber().length() - 9);
                    if (numb.equalsIgnoreCase(substr) && dupname.equalsIgnoreCase(person.getName())) {
                        vars.log("They are same=====" + numb + "***" + substr);
                        //  sortedlist.remove(person);
                    } else {
                        for (ContactDetailsDB mycon : dbcontactlist) {

                            if (PhoneNumberUtils.compare(mycon.getMobile(), person.getPhonenumber())) {
                                vars.log("They are same=====" + numb + "***" + mycon.getMobile());
                            } else {
                                sortedlist.add(new Person(person.getName(), person.getPhonenumber(),""));
                                numb = substr;
                                dupname = person.getName();
                                phonenum = person.getPhonenumber();
                                break;

                            }
                            //  sortedlist.add(new Person(person.getName(), person.getPhonenumber()));

                        }
                    }
                }
            }

            Collections.sort(sortedlist, new Comparator<Person>() {
                @Override
                public int compare(Person u1, Person u2) {

                    return u1.getName().compareToIgnoreCase(u2.getName());
                }
            });
            vars.log("SIZE===="+dbaddedlist.size());
            vars.log("SIZE====" + sortedlist.size());
            for(Iterator<Person> it = sortedlist.iterator(); it.hasNext();) {
                Person s = it.next();
                String substr = s.getPhonenumber().substring(s.getPhonenumber().length() - 9);

                for(Person myc:dbaddedlist){
                    if(substr.equalsIgnoreCase(myc.getPhonenumber())){

                        try {
                            it.remove();
                            break;
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                }
            }
            for(Person con :phonelist){
                if(str.contains(con.getPhonenumber())){

                }else{
                    str.add(con.getPhonenumber());
                    finalusers.add(con);
                }

            }

            return finalusers;
        }

        @Override
        protected void onPostExecute(List<Person> persons) {
            vars.log("phonelist====="+phonelist.size()+"sorted list========="+sortedlist.size());
            if(sortedlist.size()==0){
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                    Collections.sort(phonelist, new Comparator<Person>() {
                        @Override
                        public int compare(Person u1, Person u2) {

                            return u1.getName().compareToIgnoreCase(u2.getName());
                        }
                    });
                    contactAdapter = new Contact_new_Adapter(getActivity(), phonelist) ;
                    recList.setAdapter(contactAdapter);
                    contactAdapter.notifyDataSetChanged();
                }

            }else {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                    reload();
                }

            }

            super.onPostExecute(persons);
        }
    }

    public void reload(){
        if(getActivity()!=null) {

            contactAdapter = new Contact_new_Adapter(getActivity(), sortedlist);
            recList.setAdapter(contactAdapter);
        }
    }

    @Override
    public void onResume() {
        Globals.whichfag = "invite";
        super.onResume();
    }

    private boolean isAlpha(String s) {
        return s.matches(".*\\d+.*");
    }
}















