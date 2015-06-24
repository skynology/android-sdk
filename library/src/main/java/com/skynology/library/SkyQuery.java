package com.skynology.library;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.skynology.library.callbacks.ObjectCallback;
import com.skynology.library.callbacks.QueryCacllback;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by william on 6/19/15.
 */
public class SkyQuery<T extends SkyObject> {
    private Class<T> t;
    private String resourceName;

    private SkyParam params;

    Class<T> getT() {
        return this.t;
    }
    void setT(Class<T> t) {
        this.t = t;
    }

    public SkyQuery(String resourceName){
        this.resourceName = resourceName;
        this.init();
    }

    private void init(){
        this.params = new SkyParam();
    }


    public void setParams(SkyParam param){
        this.params = param;
    }

    public SkyParam param(){
        return this.params;
    }

    /**
     * 转换json数组到SkyObject(子类)对象
     * @param response json数组
     * @return SkyObject(子类)数组
     * @throws Exception 转换错误信息
     */
    protected List<T> convertResults(JSONObject response) throws Exception{
        LinkedList result = new LinkedList();
        try{
            JSONArray data = response.getJSONArray("results");
            for (int i =0; i < data.length(); i ++) {
                JSONObject json = data.getJSONObject(i);
                T object = this.covertResult(json);
                result.add(object);
            }
        } catch (JSONException e){
            throw e;
        }
        return result;
    }

    /**
     * 转换单个json数据到SkyObject对象.
     * @param response json数据对象
     * @return SkyObject或继承类型
     * @throws Exception 转换错误
     */
    protected T covertResult(JSONObject response) throws Exception{
        SkyObject object = null;
        try{
            if(this.t != null){
                object =(SkyObject)SkyQuery.this.t.newInstance();
                object.setData(response);
            } else {
                object = new SkyObject(this.resourceName, response);
            }

        } catch (Exception e){
            throw e;
        }
        return (T)object;
    }

    /**
     * 查询
     * @param callback 返回数据处理
     */
    public void find(final QueryCacllback<T> callback){
        final String name = this.resourceName;
        String url = "/resources/"+name;
        RequestParams param = this.params.getQueryParams();

//        if(callback != null) {
            final  List<T> tt = new ArrayList<T>();
//            callback.onSuccess(tt, 10);
//            return;
//        }


        Skynology.get(url, param, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Skynology.logger.debug("query finished with code:%d", statusCode);
                List<T> objects = new ArrayList<T>();
                long count = 0;
                try {
                    count = response.getLong("count");
                }catch (JSONException e){
                    count = 0;
                }
                List<T> list;
                try {
                    list = convertResults(response);
                }catch (Exception e){
                    list = new ArrayList<T>();
                    Skynology.logger.debug("convert query result failed>%s", e.getMessage());
                }
                callback.onSuccess(objects, count);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Skynology.logger.debug("query finished with error:%s", responseString);

                SkyException e =  new SkyException(responseString);
                callback.onError(e);
            }

        });
    }

    /**
     * 获取单个对象
     * @param objectId 对象id
     * @param callback 回调事件
     */
    public void get(String objectId, final ObjectCallback<T> callback){
        this.getWithParam(objectId, null, callback);
    }

    /**
     * 查询单个对象, 包含更多条件
     * @param objectId 对象id
     * @param param 查询参数
     * @param callback 回掉事件
     */
    public void getWithParam(String objectId, SkyParam param, final ObjectCallback<T> callback){
        String name = this.resourceName;
        String url = "/resources/"+name+"/"+objectId;
        RequestParams p = null;
        if(param != null){
            p = param.getQueryParams();
        }
        Skynology.get(url, p, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                T object = null;
                try {
                    SkyQuery.this.covertResult(response);
                }catch (Exception e){

                }
                callback.onSuccess(object);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                SkyException e =  new SkyException(responseString);
                callback.onError(e);
            }
        });
    }


}
