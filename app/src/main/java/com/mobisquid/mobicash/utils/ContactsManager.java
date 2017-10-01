package com.mobisquid.mobicash.utils;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.mobisquid.mobicash.dbstuff.ContactDetailsDB;
import com.mobisquid.mobicash.model.ContactsDb;

import java.util.ArrayList;
import java.util.List;

public class ContactsManager {
    static final String TAG = ContactsManager.class.getSimpleName();
    static Long userid;
    private static String MIMETYPE = "vnd.android.cursor.item/com.mobisquid.mobicash";

    public static void addContact(Context context,ContactsDb contact){

        ContentResolver resolver = context.getContentResolver();
        //System.out.println("n==sdsdd==="+contact.getContactID());
        //retrieveContactNumber(resolver);

       // System.out.println(getContactAccount(Long.valueOf(contact.getContactID()),resolver));
       boolean mHasAccount = isAlreadyRegistered(resolver, contact.getContactID());

        if(mHasAccount){

            Log.i(TAG,"already de==="+contact.getMobile());
        } else {
            Log.i(TAG,"am adding===="+contact.getUsername()+"====mobile==="+contact.getMobile());
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            ContactDetailsDB save = new ContactDetailsDB(contact.getUsername(),
                    contact.getMobile(), contact.getUserID(), contact.getUrl(), contact.getOnline(),
                    contact.getDateonline(), contact.getLanguage());
            save.save();
            userid = save.getId();

            // insert account name and account type
            ops.add(ContentProviderOperation
                    .newInsert(addCallerIsSyncAdapterParameter(RawContacts.CONTENT_URI, true))
                    .withValue(RawContacts.ACCOUNT_NAME, Globals.ACCOUNT_NAME)
                    .withValue(RawContacts.ACCOUNT_TYPE, Globals.ACCOUNT_TYPE)
                    .withValue(RawContacts.AGGREGATION_MODE,
                            RawContacts.AGGREGATION_MODE_DEFAULT)
                    .build());

            // insert contact number
            ops.add(ContentProviderOperation
                    .newInsert(addCallerIsSyncAdapterParameter(Data.CONTENT_URI, true))
                    .withValueBackReference(Data.RAW_CONTACT_ID, 0)
                    .withValue(Data.MIMETYPE, CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(CommonDataKinds.Phone.NUMBER, contact.getMobile())
                    .build());

            // insert contact name
            ops.add(ContentProviderOperation
                    .newInsert(addCallerIsSyncAdapterParameter(Data.CONTENT_URI, true))
                    .withValueBackReference(Data.RAW_CONTACT_ID, 0)
                    .withValue(Data.MIMETYPE,
                            CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getUsername())
                    .build());

            // insert mime-type data
            ops.add(ContentProviderOperation
                    .newInsert(addCallerIsSyncAdapterParameter(Data.CONTENT_URI, true))
                    .withValueBackReference(Data.RAW_CONTACT_ID, 0)
                    .withValue(Data.MIMETYPE, MIMETYPE)
                    .withValue(Data.DATA1, contact.getMobile())
                    .withValue(Data.DATA2, userid)
                    .withValue(Data.DATA3, "Message "+contact.getMobile())
                    .build());
           // updateMyContact(context,contact.getContactID());

            Intent pushNotification = new Intent(Globals.PUSH_STATUS);
            pushNotification.putExtra("status", "Adding "+contact.getUsername()+"...");
            LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotification);


            Globals.NUMUSERS=Globals.NUMUSERS+1;
            Log.d(TAG,"Global==="+Globals.NUMUSERS);
            try {
                resolver.applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Check if contact is already registered with app
     * @param resolver
     * @param id
     * @return
     */
    private static boolean isAlreadyRegistered(ContentResolver resolver, String id){

        boolean isRegistered = false;
        List<String> str = new ArrayList<>();
        if(id!=null) {
            //query raw contact id's from the contact id
            Cursor c = resolver.query(RawContacts.CONTENT_URI, new String[]{RawContacts._ID},
                    RawContacts.CONTACT_ID + "=?",
                    new String[]{id}, null);


            //fetch all raw contact id's and save them in a list of string
            if (c != null && c.moveToFirst()) {
                do {
                    Log.i(TAG, "ACCOUNT========" + c.getString(c.getColumnIndexOrThrow(RawContacts.ACCOUNT_TYPE)));

                    str.add(c.getString(c.getColumnIndexOrThrow(RawContacts._ID)));
                } while (c.moveToNext());
                c.close();
            }

            //query account types and check the account type for each raw contact id
            for (int i = 0; i < str.size(); i++) {
                Cursor c1 = resolver.query(RawContacts.CONTENT_URI, new String[]{RawContacts.ACCOUNT_TYPE},
                        RawContacts._ID + "=?",
                        new String[]{str.get(i)}, null);

                if (c1 != null) {
                    c1.moveToFirst();
                    String accType = c1.getString(c1.getColumnIndexOrThrow(RawContacts.ACCOUNT_TYPE));
                    if (accType != null && accType.equals(Globals.ACCOUNT_TYPE)) {
                        isRegistered = true;
                        break;
                    }
                    c1.close();
                }
            }
        }
        return isRegistered;
    }

    /**
     * Check for sync call
     * @param uri
     * @param isSyncOperation
     * @return
     */
    private static Uri addCallerIsSyncAdapterParameter(Uri uri, boolean isSyncOperation) {
        if (isSyncOperation) {
            return uri.buildUpon()
                    .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                    .build();
        }
        return uri;
    }

    public static void updateMyContact(Context context,String id){

        Cursor ids = context.getContentResolver().query(Contacts.CONTENT_URI, null,
                Contacts._ID + " = ?",
                new String[]{id}, null);

//        int rawContactId = ids.getColumnIndexOrThrow(ContactsContract.RawContacts.);

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                .withValueBackReference(Data.RAW_CONTACT_ID,0)
                .withValue(Contacts._ID, id)
                .withValue(Data.DATA1, "Data1")
                .withValue(Data.DATA2, "Data2")
                .withValue(Data.DATA3, "MyData")
                .build());

        ids.close();

        try{
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY,ops);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }
}