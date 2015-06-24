package com.skynology.library.utils;

import android.util.Log;

/**
 * Created by william on 6/22/15.
 */
public class SkyLog {
    private static String TAG = "Skynology";
    private boolean enableLogging = false;

    public void setLogging(boolean logging){
        this.enableLogging = logging;
    }

    public void debug(String format, Object... args){
        if(enableLogging) Log.d(TAG, String.format(format, args));
    }
    public void info(String format, Object... args){
        if(enableLogging) Log.i(TAG, String.format(format, args));
    }
    public void warning(String format, Object... args){
        if(enableLogging) Log.w(TAG, String.format(format, args));
    }
    public void error(String format, Object... args){
        if(enableLogging) Log.e(TAG, String.format(format, args));
    }

}
