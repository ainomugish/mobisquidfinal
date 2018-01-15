package com.mobisquid.mobicash.payment.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mobisquid.mobicash.payment.model.AllProxyBankData;
import com.mobisquid.mobicash.payment.model.AllProxyCardData;
import com.mobisquid.mobicash.payment.model.AllProxyCashData;
import com.mobisquid.mobicash.payment.model.AllProxyFoneData;
import com.mobisquid.mobicash.payment.model.ProxyCash;

import java.util.List;

/**
 * Created by Manish jain on 26-04-2017.
 */

public class PrefrenceData {
    public static final String PROXY_PHONE_TAG = "proxyPhone";
    public static final String PROXY_CASH_TAG = "proxyCash";
    public static final String PROXY_CARD_TAG = "proxyCard";
    public static final String PROXY_BANK_TAG = "proxyBank";
    public static final String PROXY_PIN = "proxyPin";
    public static final String KEY_ALIAS = "mobisquid";

    public static void saveProxyCash(AllProxyCashData data, Context context) {
        SharedPreferences shared = context.getSharedPreferences("App_settings", context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = shared.edit();
        if (data != null) {
            Gson gson = new Gson();
            String json = gson.toJson(data); // myObject - instance of MyObject
            String encrypt = KeyStoreHelper.encrypt(KEY_ALIAS, json);
            prefsEditor.putString(PROXY_CASH_TAG, encrypt);
        } else {
            prefsEditor.putString(PROXY_CASH_TAG, null);
        }
        prefsEditor.commit();
    }

    public static AllProxyCashData getProxyCashdData(Context context) {
        AllProxyCashData obj = null;
        SharedPreferences shared = context.getSharedPreferences("App_settings", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = shared.getString(PROXY_CASH_TAG, "");
        if (json != null && !json.isEmpty()) {
            String decrypt = KeyStoreHelper.decrypt(KEY_ALIAS, json);
            obj = gson.fromJson(decrypt, AllProxyCashData.class);
        }

        return obj;
    }

    public static void saveProxyCard(AllProxyCardData data, Context context) {
        SharedPreferences shared = context.getSharedPreferences("App_settings", context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = shared.edit();
        if (data != null) {
            Gson gson = new Gson();
            String json = gson.toJson(data); // myObject - instance of MyObject
            String encrypt = KeyStoreHelper.encrypt(KEY_ALIAS, json);
            prefsEditor.putString(PROXY_CARD_TAG, encrypt);
        } else {
            prefsEditor.putString(PROXY_CARD_TAG, null);
        }
        prefsEditor.commit();
    }

    public static AllProxyCardData getProxyCardData(Context context) {
        AllProxyCardData obj = null;
        SharedPreferences shared = context.getSharedPreferences("App_settings", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = shared.getString(PROXY_CARD_TAG, "");
        if (json != null && !json.isEmpty()) {
            String decrypt = KeyStoreHelper.decrypt(KEY_ALIAS, json);
            obj = gson.fromJson(decrypt, AllProxyCardData.class);
        }

        return obj;
    }

    public static void saveProxyBank(AllProxyBankData data, Context context) {
        SharedPreferences shared = context.getSharedPreferences("App_settings", context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = shared.edit();
        if (data != null) {
            Gson gson = new Gson();
            String json = gson.toJson(data); // myObject - instance of MyObject
            prefsEditor.putString(PROXY_BANK_TAG, json);
        } else {
            prefsEditor.putString(PROXY_BANK_TAG, null);
        }
        prefsEditor.commit();
    }

    public static AllProxyBankData getProxyBankData(Context context) {
        SharedPreferences shared = context.getSharedPreferences("App_settings", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = shared.getString(PROXY_BANK_TAG, "");
        AllProxyBankData obj = gson.fromJson(json, AllProxyBankData.class);
        return obj;
    }

    public static Boolean isPinEnable(Context context) {
        SharedPreferences shared = context.getSharedPreferences("App_settings", context.MODE_PRIVATE);
        Boolean json = shared.getBoolean(PROXY_PIN, false);
        return json;
    }

    public static void setProxyPin(Context context, Boolean pin) {
        SharedPreferences shared = context.getSharedPreferences("App_settings", context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = shared.edit();
        prefsEditor.putBoolean(PROXY_PIN, pin);
        prefsEditor.commit();
    }

    public static void saveProxyFone(AllProxyFoneData data, Context context) {
        SharedPreferences shared = context.getSharedPreferences("App_settings", context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = shared.edit();
        if (data != null) {
            Gson gson = new Gson();
            String json = gson.toJson(data); // myObject - instance of MyObject
            prefsEditor.putString(PROXY_PHONE_TAG, json);
        } else {
            prefsEditor.putString(PROXY_PHONE_TAG, null);
        }
        prefsEditor.commit();
    }

    public static AllProxyFoneData getProxyFoneData(Context context) {
        SharedPreferences shared = context.getSharedPreferences("App_settings", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = shared.getString(PROXY_PHONE_TAG, "");
        AllProxyFoneData obj = gson.fromJson(json, AllProxyFoneData.class);
        return obj;
    }


}
