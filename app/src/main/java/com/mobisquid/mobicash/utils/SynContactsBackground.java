package com.mobisquid.mobicash.utils;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import com.mobisquid.mobicash.model.ContactsDb;
import com.mobisquid.mobicash.model.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by mobicash on 1/22/16.
 */
public class SynContactsBackground extends AsyncTask<Void, Integer, ArrayList<ContactsDb>> {
    static final String TAG = SynContactsBackground.class.getSimpleName();

    String dupu="";
    String num =  "";
    ArrayList<ContactsDb> contactsDb;
    List<String> str = new ArrayList<>();
    public interface AsyncResponse {
        void processFinish(ArrayList<ContactsDb> output);
    }
    public AsyncResponse delegate = null;

    private Context mContext;

    public SynContactsBackground(Context mContextv,ArrayList<ContactsDb> contactsDb,AsyncResponse delegate){
        this.contactsDb = contactsDb;
        this.delegate = delegate;
        mContext = mContextv;

    }

    @Override
    protected ArrayList<ContactsDb> doInBackground(Void... params) {
        ArrayList<ContactsDb> contactlist = addContacts(contactsDb);
        return  contactlist;
    }

    @Override
    protected void onPostExecute(ArrayList<ContactsDb> persons) {
        delegate.processFinish(persons);
        super.onPostExecute(persons);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }


    @Override
    protected void onProgressUpdate(Integer... values) {

        super.onProgressUpdate(values);
    }

    public ArrayList<ContactsDb> addContacts(ArrayList<ContactsDb> online_contact){
        ArrayList<Person> phonelist = new ArrayList<Person>();
        ArrayList<Person> newphonelist = new ArrayList<Person>();
        ArrayList<ContactsDb> mydb_users = new ArrayList<>();
        ArrayList<ContactsDb> finalusers = new ArrayList<>();


        Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String phoneNumbe = phoneNumber.replace(" ", "");
            String phoneNumbers = phoneNumbe.replace("-", "");
            String accType = phones.getString(phones.getColumnIndexOrThrow(ContactsContract.RawContacts.ACCOUNT_TYPE));

           // str.add(phones.getString(phones.getColumnIndexOrThrow(ContactsContract.RawContacts._ID)));
           // phonelist.add(new Person(name, phoneNumbers, phones.getString(phones.getColumnIndexOrThrow(ContactsContract.Contacts._ID))));
            //Log.d(TAG,"name++++"+name);
            if(isAlpha(name)){
                Log.d(TAG,"name with number++++"+name);
            }else{
                Log.d(TAG,"name++++"+name);
                phonelist.add(new Person(name, phoneNumbers, accType));
            }


        }
        phones.close();
        //////////////////////////////////////////////////////////////////////
        Collections.sort(phonelist, new Comparator<Person>() {
            @Override
            public int compare(Person u1, Person u2) {

                return u1.getName().compareToIgnoreCase(u2.getName());
            }
        });

        for (Person person: phonelist){

            if(num.equals(person.getPhonenumber() )&& dupu.equals(person.getName())){
                Log.e(TAG,num+"====eq===="+person.getPhonenumber());
            }else{
                if(num.equals(person.getPhonenumber())){

                }else {

                    newphonelist.add(new Person(person.getName(), person.getPhonenumber(),person.getContactid()));
                    num = person.getPhonenumber();
                    dupu = person.getName();
                }
            }

        }
        Log.e(TAG,"oldlis============" + phonelist.size());
        Log.e(TAG,"new list============" + newphonelist.size());

//close
        for (Person persons: newphonelist){
            for(ContactsDb online : online_contact) {
               // Log.e(TAG,"online============"+online_contact.size());
                String finalphone;
                if(persons.getPhonenumber().substring(0, 1).matches("0")){
                    String gfg  = persons.getPhonenumber();
                    finalphone =   gfg.substring(1);
                }else{
                    finalphone = persons.getPhonenumber();
                }
                if (PhoneNumberUtils.compare(online.getMobile(), finalphone)) {

                    //they are the same do whatever you want!

                    if(persons.getContactid()!=null && persons.getContactid().equalsIgnoreCase(Globals.ACCOUNT_NAME)){
                        Log.e(TAG,"(person.getContactid()+++=" + (persons.getContactid()+"==number==="+persons.getPhonenumber()));

                    }else {
                        if(mydb_users.contains(new ContactsDb(persons.getName(), online.getMobile(), online.getUserID(), online.getUrl(), online.getOnline(),
                                online.getDateonline(), online.getLanguage(), persons.getContactid()))){
                            Log.e(TAG,persons.getName() + "=contains=++");

                        }else {
                            mydb_users.add(new ContactsDb(persons.getName(),
                                    online.getMobile(), online.getUserID(), online.getUrl(), online.getOnline(),
                                    online.getDateonline(), online.getLanguage(), persons.getContactid()));
                        }
                    }

                }

            }


        }
        ///close here

        Collections.sort(newphonelist, new Comparator<Person>() {
            @Override
            public int compare(Person u1, Person u2) {

                return u1.getName().compareToIgnoreCase(u2.getName());
            }
        });
        /////////////////////////////////////////////////////////////////////

        for(ContactsDb con :mydb_users){
            if(str.contains(con.getMobile())){

            }else{
                str.add(con.mobile);
                finalusers.add(con);
            }

        }
        Log.i(TAG,"my new size=="+finalusers.size());
        for(ContactsDb con :finalusers){
            Log.d(TAG,con.getUsername()+ "==+++==" + con.getMobile()+"===id"+con.getContactID());
        }

        return finalusers;

    }
    public boolean isAlpha(String s) {
        return s.matches(".*\\d+.*");
    }
}