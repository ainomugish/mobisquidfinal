package com.mobisquid.mobicash.ecommerce.essentials;

/**
 * Created by Zeera on 7/18/2017 bt ${File}
 */

public class SessionClass {

    private static SessionClass mInstance;
    public static final String APPLICATION_ID="2";
    public static final String MERCHANT_ACCOUNT = "27843700200";

    private SessionClass() {
        //no instance
    }

    public static SessionClass getInstance() {
        if (mInstance == null) {
            mInstance = new SessionClass();
        }
        return mInstance;
    }
}
