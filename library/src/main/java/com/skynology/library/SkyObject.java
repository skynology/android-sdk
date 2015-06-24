package com.skynology.library;

import android.provider.SyncStateContract;
import android.text.TextUtils;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.skynology.library.callbacks.BooleanCallback;
import com.skynology.library.callbacks.ObjectCallback;
import com.skynology.library.utils.SkyHelper;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by william on 6/18/15.
 */
public class SkyObject extends Object {

    private  String resourceName;

    private String objectId;
    private String createdAt;
    private String updatedAt;
    private SkyACL acl;

    private JSONObject privateData;
    private JSONObject changedData;
    private boolean fetchAfterSave = false;


    /*
     * 初始化
     */
    public SkyObject(String resourceName){
        this.resourceName = resourceName;
        JSONObject data = new JSONObject();
        this.init(data);
    }

    public SkyObject(String resourceName, String objectId){
        this.resourceName = resourceName;
        JSONObject data = new JSONObject();
        try {
            data.put("objectId", objectId);
        }catch (JSONException e){

        }
        this.init(data);
    }

    /*
     * 初始化一个对象
     */
    public SkyObject(String resourceName, JSONObject data){
        this.resourceName = resourceName;
        this.init(data);
    }


    protected void setData(JSONObject data){
        this.init(data);
    }
    protected JSONObject getData(){
        return this.privateData;
    }

    protected void setResourceName(String resourceName){
        this.resourceName = resourceName;
    }

    protected void init(JSONObject data){
        this.clear();
        if(data == null) {
            return;
        }
        try {
            this.privateData = data;
            this.objectId = this.privateData.getString("objectId");
            String createdAt = this.privateData.getString("createdAt");
            String updatedAt = this.privateData.getString("updatedAt");
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.acl = new SkyACL(data.getJSONObject("ACL"));
        } catch (JSONException e){
            Skynology.logger.debug("init object failed:%s", e.getMessage());
        }
    }

    protected void clear(){
        this.privateData = new JSONObject();
        this.changedData = new JSONObject();
        this.acl = new SkyACL();
        this.createdAt = null;
        this.updatedAt = null;
    }

    /**/
    public String getObjectId(){
        return this.objectId;
    }

    public Date getCreatedAt(){
        return SkyHelper.getDate(this.createdAt);
    }

    public Date getUpdatedAt(){
        return SkyHelper.getDate(this.updatedAt);
    }


    /**
     * 设置保存完后是否重新获取服务器数据
     * @param value
     */
    public void setFetchAfterSave(boolean value){
        this.fetchAfterSave = value;
    }

    private void copyDataFromOtherObject(SkyObject object){
        this.setData(object.privateData);
    }

    /**
     * 获取指定字段值
     * @param field 字段名
     * @return 字段值
     */
    public Object get(String field){
        try {
            return this.privateData.get(field);
        } catch (JSONException e){
            return null;
        }
    }

    public String getString(String field){
        try {
            return this.privateData.getString(field);
        } catch (JSONException e){
            return null;
        }

    }
    public int getInt(String field) throws JSONException{
        return this.privateData.getInt(field);
    }
    public double getDouble(String field) throws JSONException{
        return this.privateData.getDouble(field);
    }
    public Date getDate(String field){
        String content = this.getString(field);
        return SkyHelper.getDate(content);
    }
    public Boolean getBoolean(String field){
        try{
            return this.privateData.getBoolean(field);
        } catch (JSONException e){
            return null;
        }
    }
    public JSONObject getJSONObject(String field){
        try{
            return this.privateData.getJSONObject(field);
        } catch (JSONException e){
            return null;
        }

    }
    public JSONArray getJSONArray(String field){
        try{
            return this.privateData.getJSONArray(field);
        } catch (JSONException e){
            return null;
        }

    }
    public SkyACL getACL(){
        return this.acl;
    }

    /*
    *  直接设置字段值
     */
    public void put(String field, Object value) {
        try {
            // 时期时间类型先转为ISO 8601字符串格式
            if(value.getClass().equals(Date.class)){
                value = SkyHelper.toDateString((Date)value);
            }
            this.changedData.put(field, value);
        }catch (JSONException e){
            Skynology.logger.info(e.getMessage());
        }
    }

    /*
     * 递增指定字段值
     */
    public void increment(String field) {
        increment(field, 1);
    }

    public void increment(String field, Number amount){
        JSONObject obj = new JSONObject();
        try {
            obj.put("__op", "Increment");
            obj.put("amount", amount);
            this.changedData.put(field, obj);
        }catch (JSONException e){
        }
    }

    public void addValueToArray(String field, Object value) throws JSONException{
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        arr.put(value);
        obj.put("__op", "Add");
        obj.put("objects", arr);
        this.changedData.put(field, obj);
    }

    public void addUniqueValueToArray(String field, Object value) throws JSONException{
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        arr.put(value);
        obj.put("__op", "AddUnique");
        obj.put("objects", arr);
        this.changedData.put(field, obj);
    }
    public void removeValueFromArray(String field, Object value) throws JSONException{
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        arr.put(value);
        obj.put("__op", "Remove");
        obj.put("objects", arr);
        this.changedData.put(field, obj);
    }
    public void addListToArray(String field, JSONArray value) throws JSONException{
        JSONObject obj = new JSONObject();
        obj.put("__op", "Add");
        obj.put("objects", value);
        this.changedData.put(field, obj);
    }
    public void addUniqueListToArray(String field, JSONArray value) throws JSONException{
        JSONObject obj = new JSONObject();
        obj.put("__op", "AddUnique");
        obj.put("objects", value);
        this.changedData.put(field, obj);
    }
    public void removeListToArray(String field, JSONArray value) throws JSONException{
        JSONObject obj = new JSONObject();
        obj.put("__op", "Remove");
        obj.put("objects", value);
        this.changedData.put(field, obj);
    }
    public void removeObjectToArray(String field, Object value) throws JSONException{
        JSONObject obj = new JSONObject();
        obj.put("__op", "RemoveObject");
        obj.put("query", value);
        this.changedData.put(field, obj);
    }

    public void setACL(SkyACL acl){
        this.acl = acl;
    }

    public void save(final BooleanCallback callback){
        StringEntity entity;
        try {
            entity = new StringEntity(this.changedData.toString());
        }catch (UnsupportedEncodingException e){
            SkyException err = new SkyException(0, e.getMessage(), "");
            callback.done(false, err);
            return;
        }

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try{
                    SkyObject.this.objectId = response.getString("objectId");
                    SkyObject.this.createdAt = response.getString("createdAt");
                    SkyObject.this.createdAt = response.getString("createdAt");
                }catch (JSONException e){
                    Skynology.logger.debug("set objectId error:%s", e.getMessage());
                }
                if(callback == null) return;

                // 当不用从服务器获取数据并同步时
                if(!SkyObject.this.fetchAfterSave){
                    callback.done(true, null);
                    return;
                }

                SkyObject.this.fetch(new ObjectCallback<SkyObject>() {
                    @Override
                    public void onSuccess(SkyObject object) {
                        SkyObject.this.copyDataFromOtherObject(object);
                        callback.done(true,  null);
                    }

                    @Override
                    public void onError(SkyException e) {
                        callback.done(false, e);
                    }
                });

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                callback.done(false, new SkyException(errorResponse));
            }
        };

        String url = "/resources/"+this.resourceName;
        if (!TextUtils.isEmpty(this.objectId)) {
            url += ("/"+this.objectId);
            Skynology.put(url,entity, handler);
        } else {
            Skynology.post(url, entity, handler);
        }
    }

    /**
     * 当不是新对象时, 用fetch来从服务器获取对象数据
     * @param callback
     */
    public void fetch(final ObjectCallback<SkyObject> callback){
        String url = "/resources/"+this.resourceName+"/"+objectId;
        Skynology.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                SkyObject object = new SkyObject(SkyObject.this.resourceName, response);
                callback.onSuccess(object);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                SkyException e = new SkyException(errorResponse);
                callback.onError(e);
            }
        });
    }

    public void delete(final BooleanCallback callback){
        String url = "/resources/"+this.resourceName+"/"+objectId;
        Skynology.delete(url, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                SkyObject.this.clear();
                callback.done(true, null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                callback.done(false, new SkyException(errorResponse));
            }
        });

    }


    private String getQueryParams(){
        String params = "";


        return params;
    }

}
