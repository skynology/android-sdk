package com.skynology.library;

import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.skynology.library.callbacks.JsonObjectCallback;
import com.skynology.library.utils.SkyLog;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by william on 6/19/15.
 */
public class Skynology {
    public static String appId;
    public static SkyLog logger;
    public static Context appContext;

    private static String appKey;
    private static String appRequestSign;

    private static String baseURL = "https://skynology.com/api/1.0";
    private final static String APPID_KEY = "X-Sky-Application-Id";
    private final static String APPKEY_KEY = "X-Sky-Application-Key";
    private final static String REQUEST_SIGN_KEY = "X-Sky-Request-Sign";
    private final static String SESSION_KEY = "X-Sky-Session-Token";
    private final static String CLIENT_KEY = "X-Sky-Client-Id";
    private final static String clientId = "android-sdk/1.0.0";

    private static AsyncHttpClient client;



    public static void initilize(Context context, String applcationId, String applcationKey){
        appContext = context;
        appId = applcationId;
        appKey = applcationKey;
        init();
    }

    /*
     * 用 request sign 来初始化
     */
    public static void initilizeWithSign(Context context, String applcationId, String requestSign){
        appContext = context;
        appId = applcationId;
        appRequestSign = requestSign;
        init();
    }


    private static void init(){
        logger = new SkyLog();
        client = new AsyncHttpClient();
    }

    public static void setBaseURL(String url){
        baseURL = url;
    }


    /**
     * 调用函数
     * @param name 函数名
     * @param data 参数
     * @param callback 回调事件
     */
    public static void func(String name, JSONObject data, final JsonObjectCallback callback){
        String url = "functions/"+name;
        StringEntity entity;
        try{
            entity = new StringEntity(data.toString());
        }catch (UnsupportedEncodingException e){
            logger.debug("format function data error:%s", e.getMessage());
            if(callback != null){
                callback.onError(new SkyException(0, e.getMessage(), ""));
            }
            return;
        }

        post(url, entity, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                SkyUser user = new SkyUser(response);
                if(callback != null){
                    callback.onSuccess(response);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if(callback != null){
                    callback.onError(new SkyException(errorResponse));
                }
            }
        });
    }

    /**
     * 调用SDK提供的方法外的API
     * @param url 调用URL地址(不包含基础地址, 应以反斜杠开头)
     * @param type http方法
     * @param params url参数
     * @param data 提交数据
     * @param callback 回调事件
     */
    public static void call(String url, CallType type, RequestParams params, JSONObject data, final JsonObjectCallback callback){

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(callback != null){
                    callback.onSuccess(response);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if(callback != null){
                    callback.onError(new SkyException(errorResponse));
                }
            }
        };

        StringEntity entity = null;
        if(data != null){
            try{
                entity = new StringEntity(data.toString());
            }catch (UnsupportedEncodingException e){
                logger.debug("format data error:%s", e.getMessage());
                if(callback != null){
                    callback.onError(new SkyException(0, e.getMessage(), ""));
                    return;
                }
            }
        }

        if(type == CallType.GET){
            get(url, params, handler);
        }else if(type == CallType.POST){
            post(url, entity, handler);
        }else if(type == CallType.PUT){
            put(url, entity, handler);
        }else if(type == CallType.DELETE){
            delete(url, params, handler);
        }
    }

    protected static AsyncHttpClient getClient(){
        setClientHeaders();
        return client;
    }

    protected static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        Skynology.logger.debug("start get");
        setClientHeaders();
//        client.get(getAbsoluteUrl(url), params, responseHandler);
        client.get(appContext, getAbsoluteUrl(url), params, responseHandler);
    }

    protected static void post(String url, StringEntity content, AsyncHttpResponseHandler responseHandler) {
        setClientHeaders();
        client.post(appContext, getAbsoluteUrl(url), content, "application/json", responseHandler);
    }
    protected static void put(String url, StringEntity content, AsyncHttpResponseHandler responseHandler) {
        setClientHeaders();
        client.post(appContext, getAbsoluteUrl(url), content, "application/json", responseHandler);
    }
    protected static void delete(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        setClientHeaders();
        client.delete(getAbsoluteUrl(url), responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        String url = baseURL + relativeUrl;
        logger.debug("absolute url is: %s", url);
        return url;
    }
    private static void setClientHeaders(){
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-Type", "application/json");
        client.setUserAgent("Skynology android sdk 1.0.0");
        client.addHeader(APPID_KEY, appId);
        if(!TextUtils.isEmpty(appRequestSign)){
            client.addHeader(REQUEST_SIGN_KEY, appRequestSign);
        }
        if(!TextUtils.isEmpty(appKey)){
            client.addHeader(APPKEY_KEY, appKey);
        }
        client.addHeader(CLIENT_KEY, clientId);
        SkyUser user = SkyUser.current();
        if(user  != null && !TextUtils.isEmpty(user.getSessionToken())){
            client.addHeader(SESSION_KEY, user.getSessionToken());
        }
    }




    public static enum CallType{
        GET,
        POST,
        PUT,
        DELETE;

        private CallType(){
        }
    }
}

