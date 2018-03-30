package com.hackathon.alphac.spot_php_test;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Aswin Chandran on 22-03-2018.
 */

public class PrefManager {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Context mContext;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "welcome to spot";
    private static final String FIRST_TIMER = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        this.mContext = context;
        sp = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sp.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(FIRST_TIMER, isFirstTime);
        editor.commit();
    }
    public boolean isFirstTimeLaunch() {
        return sp   .getBoolean(FIRST_TIMER, true);
    }

}
