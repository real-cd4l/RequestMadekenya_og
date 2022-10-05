package com.madekenyarequest.madekenya.utilities;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.madekenyarequest.madekenya.pojos.Subscriber;

public class LocalDb {
    public static void saveCustomerLocal(Context context, Subscriber customer) {
        SharedPreferences mPrefs = context.getSharedPreferences("CUSTOMER_REF", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(customer);
        prefsEditor.putString("CUSTOMER", json);
        prefsEditor.apply();
    }

    public static Subscriber getSavedCustomer(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("CUSTOMER_REF", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("CUSTOMER", null);
        return gson.fromJson(json, Subscriber.class);
    }
}
