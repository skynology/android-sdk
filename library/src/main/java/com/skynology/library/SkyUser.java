package com.skynology.library;

import android.text.TextUtils;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.skynology.library.callbacks.LoginCallback;
import com.skynology.library.callbacks.ObjectCallback;
import com.skynology.library.callbacks.RegisterCallback;
import com.skynology.library.utils.SkyHelper;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by william on 6/19/15.
 */
public class SkyUser extends SkyObject {
    private static SkyUser current;
    private final static String currentUserKey = "CURRENTUSER";


    public SkyUser(String resourceName, JSONObject data) {
        super(resourceName, data);
    }

    public SkyUser(JSONObject data){
        super("_User", data);
    }

    /*
     *   获取当前已经登陆的用户
     */
    public static SkyUser current() {
        if (current != null) {
            return current;
        }

        // 从本地获取
        JSONObject userData = SkyHelper.readFromLocal(Skynology.appContext, currentUserKey);
        if(userData == null){
            return null;
        }
        SkyUser user = new SkyUser(userData);
        SkyUser.current = user;
        return user;
    }

    private static void setCurrent(SkyUser user){
        current = user;
        SkyHelper.saveToLocal(Skynology.appContext, currentUserKey, user.getData());
    }

    /*
     * 判断当前用户是否已经登陆的用户
     */
    public Boolean isCurrent(){
        SkyUser currentUser = SkyUser.current();
        if(currentUser == null) {
            return false;
        }
        return currentUser.getObjectId() == this.getObjectId();
    }


    public String getUserName(){
        return this.getString("username");
    }
    public String getEmail(){
        return this.getString("email");
    }
    public String getPhone(){
        return this.getString("phone");
    }
    public String getSessionToken(){
        return this.getString("sessionToken");
    }
    public Boolean getActive(){
        return this.getBoolean("active");
    }


    public String[] getRoles(){
        return null;
    }

    public void setUsername(String username) {
        this.put("password", username);
    }
    public void setEmail(String email) {
        this.put("email", email);
    }
    public void setPhone(String phone){
        this.put("phone", phone);
    }
    public void setPassword(String password) {
        this.put("password", password);
    }
    public void setActive(Boolean value){
        this.put("active", value);
    }


    public static void login(String username, String password, final LoginCallback callback) {
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", params);
        login("", params, callback);
    }
    public static void loginWithEmail(String email, String password, final LoginCallback callback) {
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", params);
        login("", params, callback);
    }
    public static void loginWithPhone(String phone, String password, final LoginCallback callback) {
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("password", params);
        login("", params, callback);
    }

//    public static void loginWithPhoneAndKey(String phone, String code, final LoginCallback callback) {
//        RequestParams params = new RequestParams();
//        params.put("phone", phone);
//        params.put("code", code);
//
//    }

    /**
     * 注册新用户
     * @param callback 回掉事件
     */
    public void register(final RegisterCallback callback){
        String url = "/register";
        StringEntity entity;
        try {
            entity = new StringEntity(this.getData().toString());
        } catch (UnsupportedEncodingException e){
            Skynology.logger.debug("format register entity error:%s", e.getMessage());
            if(callback != null){
                callback.done(null, new SkyException(0, e.getMessage(), ""));
            }
            return;
        }

        Skynology.post(url, entity, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                SkyUser user = new SkyUser(response);
                if(callback != null){
                    callback.done(user, null);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if(callback != null){
                    callback.done(null, new SkyException(errorResponse));
                }
            }
        });
    }

    private static void login(String url, RequestParams params, final LoginCallback callback){
        if(TextUtils.isEmpty(url)){
            url = "/login";
        }
        StringEntity entity;
        try{
            entity =new StringEntity(params.toString());
        } catch (UnsupportedEncodingException e){
            Skynology.logger.debug("format login entity error:%s", e.getMessage());
            if(callback != null){
                callback.done(null, new SkyException(0, e.getMessage(), ""));
            }
            return;
        }
        Skynology.post(url, entity, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                SkyUser user = new SkyUser(response);
                // 保存到本地
                setCurrent(user);

                if(callback != null){
                    callback.done(user, null);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if(callback != null){
                    callback.done(null, new SkyException(errorResponse));
                }
            }
        });
    }

    /**
     * 删除登陆用户状态(包含存于磁盘的信息)
     */
    public static  void logout(){
        current = null;
        SkyHelper.clearLocal(Skynology.appContext, currentUserKey);

    }


}
