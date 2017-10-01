package com.mobisquid.mobicash.ecommerce.utitlities;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Zeera on 7/18/2017 bt ${File}
 */

public class ViewUtility {

    public static void hideKeyboard(Activity ctx, @Nullable View view) {
        View currentFocus = null;
        if (view == null)
            currentFocus = ctx.getCurrentFocus();
        else
            currentFocus = view;
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }
}
