package me.nijraj.expenses.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by buddha on 1/2/18.
 */

public class Currency {
    static String CURRENCY_KEY = "currency";
    static String CURRENCY_DEFAULT = "₹";

    public static void updateCurrency(Activity activity, String currency){
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CURRENCY_KEY, currency);
        editor.apply();
    }

    public static String getCurrency(Activity activity){
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        return preferences.getString(CURRENCY_KEY, CURRENCY_DEFAULT);
    }
}
