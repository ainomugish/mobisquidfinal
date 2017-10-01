package com.mobisquid.mobicash.ecommerce.essentials;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.ecommerce.models.ProductModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Administrator on 11/11/2016.
 */

public class SharedPrefManager {

    private SharedPreferences mSharedPrefrence;
    private Context mContext;

    public SharedPrefManager(Context ctx) {
        mContext = ctx;
        mSharedPrefrence = ctx.getSharedPreferences(ctx.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public void setVal(Object val, String key) {
        SharedPreferences.Editor editor = mSharedPrefrence.edit();
        if (val instanceof String)
            editor.putString(key, (String) val);
        else if (val instanceof Long)
            editor.putLong(key, (Long) val);
        else if (val instanceof Boolean)
            editor.putBoolean(key, (Boolean) val);
        else if (val instanceof Float)
            editor.putFloat(key, (Float) val);
        else if (val instanceof Integer)
            editor.putInt(key, (Integer) val);
        editor.apply();
    }

    public String getStringByKey(String key) {
        if (mSharedPrefrence.contains(key)) {
            return mSharedPrefrence.getString(key, "");
        }
        return "";
    }

    public long getLongByKey(String key) {
        if (mSharedPrefrence.contains(key)) {
            return mSharedPrefrence.getLong(key, 0L);
        }
        return 0L;
    }

    public Boolean getBooleanByKey(String key) {
        if (mSharedPrefrence.contains(key)) {
            return mSharedPrefrence.getBoolean(key, false);
        }
        return null;
    }

    public float getFloatByKey(String key) {
        if (mSharedPrefrence.contains(key)) {
            return mSharedPrefrence.getFloat(key, 0);
        }
        return 0;
    }

    public int getIntegerByKey(String key) {
        if (mSharedPrefrence.contains(key)) {
            return mSharedPrefrence.getInt(key, -1);
        }
        return -1;
    }

    public void saveObjectInSharedPref(Object obj, String key) {
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        setVal(json, key);
    }

    public ArrayList<ProductModel> getProducts(String key){
        Gson gson = new Gson();
        String json = getStringByKey(key);
        if (json!=null&&!json.equals("")){
            Type type = new TypeToken<ArrayList<ProductModel>>() {}.getType();
           return gson.fromJson(json, type);
        }else{
            return null;
        }
    }

    public void saveProducts(String key,ArrayList<ProductModel> productModels){
        Gson gson = new Gson();
        String json = gson.toJson(productModels);
        setVal(json,key);
    }

    public Object getObjectFromSharedPref(String key,Class toCast) {
        Gson gson = new Gson();
        String json = getStringByKey(key);
        if (json!=null&&!json.equals("")){
            return gson.fromJson(json,toCast);
        }else{
            return null;
        }
    }
}
