package com.skynology.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.loopj.android.http.RequestParams;
import com.skynology.library.Skynology;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.SimpleFormatter;

/**
 * Created by william on 6/19/15.
 */
public class SkyHelper {
    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static Date getDate(String content) {
        if(TextUtils.isEmpty(content)){
            return null;
        }
        String layout = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat formater = new SimpleDateFormat(layout);
        Date d;
        try {
            d = formater.parse(content);
        }catch (ParseException e){
            d = null;
        }
        return d;
    }
    public static String toDateString(Date date){
        String layout = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat formater = new SimpleDateFormat(layout);
        String result = formater.format(date);
        return result;
    }

    public static void saveToLocal(Context context, String name, JSONObject data){
        if(context == null) return;
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.putString("data", data.toString());
        editor.commit();
    }

    public static JSONObject readFromLocal(Context context, String name){
        if(context == null) return null;
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        String strJson = "";
        try {
            JSONObject obj = new JSONObject(strJson);
            return obj;
        }catch (JSONException e){
        }
        return null;
    }

    public static void clearLocal(Context context, String name){
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

}
