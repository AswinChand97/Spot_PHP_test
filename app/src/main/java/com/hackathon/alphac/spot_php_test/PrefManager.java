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
    public static final String TAMIL_PREF_NAME = "TamilLanguage";
    private static final String IS_TAMIL = "IsTamil";
    private static final String IS_TAMIL_COUNT = "IsTamilCount";
    private static  int countLanguage = 0;
    public PrefManager(Context context) {
        this.mContext = context;
        sp = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        sp = mContext.getSharedPreferences(TAMIL_PREF_NAME,PRIVATE_MODE);
        editor = sp.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(FIRST_TIMER, isFirstTime);
        editor.commit();
    }
    public boolean isFirstTimeLaunch() {
        return sp   .getBoolean(FIRST_TIMER, true);
    }
    public void setTamil (boolean isTamil)
    {

        editor.putBoolean(IS_TAMIL,isTamil);
        editor.commit();
    }
    public boolean isTamil()
    {
        return sp.getBoolean(IS_TAMIL,true);
    }
    public void increment()
    {
        countLanguage++;
        editor.putInt(IS_TAMIL_COUNT,countLanguage);
        editor.commit();
    }
    public int returnLanguageCount()
    {
        return sp.getInt(IS_TAMIL_COUNT,0);
    }

}
