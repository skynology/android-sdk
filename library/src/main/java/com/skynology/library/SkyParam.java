package com.skynology.library;

import android.text.TextUtils;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询条件
 */
public class SkyParam {
    private int skip;
    private int take;
    private Boolean count;

    private JSONObject where;
    private List<String> orders;
    private List<String> fields;
    private List<String> includes;

    public SkyParam(){
        this.init();
    }

    private void init(){
        this.take = 20;
        this.count = false;
        this.where = new JSONObject();
        this.orders = new ArrayList<String>();
        this.fields = new ArrayList<String>();
        this.includes = new ArrayList<String>();
    }

    public SkyParam take(int take){
        this.take = take;
        return this;
    }

    public SkyParam skip(int skip){
        this.skip = skip;
        return this;
    }

    public SkyParam orderBy(String field){
        this.orders.add(field);
        return this;
    }
    public SkyParam orderByDescending(String field){
        this.orders.add("-" + field);
        return this;
    }
    public SkyParam select(String... fields){
        for(String field : fields){
            this.fields.add(field);
        }
        return this;
    }

    public void count(Boolean count){
        this.count = count;
    }

    public void include(String field){
        this.includes.add(field);
    }
    public void includeWithFields(String field, String... includes){
        List<String> fields = new ArrayList<String>();
        for(String v : includes){
            fields.add(v);
        }
        this.includes.add(field + "." + TextUtils.join("|", fields));
    }

    /**
     * 相等于
     * @param field 字段名
     * @param value 字段值
     */
    public SkyParam equal(String field, Object value){
        this.putWhere(field, value);
        return this;
    }

    /**
     * 不等于
     * @param field 字段名
     * @param value 字段值
     */
    public SkyParam notEqual(String field, Object value) {
        this.putWhere(field, this.getJSONObject("$ne", value));
        return this;
    }

    /**
     * 小于
     * @param field 字段名
     * @param value 字段值
     */
    public SkyParam lessThan(String field, Number value) {
        this.putWhere(field, this.getJSONObject("$lt", value));
        return this;
    }

    /**
     * 小于等于
     * @param field 字段名
     * @param value 字段值
     */
    public SkyParam lessThanOrEqual(String field, Number value) {
        this.putWhere(field, this.getJSONObject("$lte", value));
        return this;
    }

    /**
     * 大于
     * @param field 字段名
     * @param value 字段值
     */
    public SkyParam greaterThan(String field, Number value) {
        this.putWhere(field, this.getJSONObject("$gt", value));
        return this;
    }

    /**
     * 大于等于
     * @param field 字段名
     * @param value 字段值
     */
    public SkyParam greaterThanOrEqual(String field, Number value) {
        this.putWhere(field, this.getJSONObject("$gte", value));
        return this;
    }

    /**
     * 以指定字符开始
     * @param field 字段名
     * @param value 字段值
     */
    public SkyParam startWith(String field, String value){
        this.putWhere(field, this.getJSONObject("$regex", "/^"+value+"/i"));
        return this;
    }

    /**
     * 以指定字符结束
     * @param field 字段名
     * @param value 字段值
     */
    public SkyParam endWith(String field, String value){
        this.putWhere(field, this.getJSONObject("$regex", "/"+value+"$/i"));
        return this;
    }

    /**
     * 包含指定字符
     * @param field 字段名
     * @param value 字段值
     */
    public SkyParam contains(String field, String value){
        this.putWhere(field, this.getJSONObject("$regex", "/"+value+"/i"));
        return this;
    }

    /**
     *
     * @param field 字段名
     * @param value 字段值
     */
    public SkyParam match(String field, String value){
        this.putWhere(field, this.getJSONObject("$elemMatch", "/"+value+"$/i"));
        return this;
    }

    /**
     * 指定字段有赋值
     * @param field 字段名
     */
    public SkyParam exists(String field){
        this.putWhere(field, this.getJSONObject("$exists", true));
        return this;

    }

    /**
     * 指定字段未赋值
     * @param field 字段名
     */
    public SkyParam notExists(String field){
        this.putWhere(field, this.getJSONObject("$exists", false));
        return this;
    }

    /**
     *
     * @param field 字段名
     */
    public SkyParam in(String field, Object[] value){
        this.putWhere(field, this.getJSONObject("$in", value));
        return this;
    }

    /**
     *
     * @param field 字段名
     * @param value 数组
     */
    public SkyParam notIn(String field, Object[] value){
        this.putWhere(field, this.getJSONObject("$nin", value));
        return this;
    }

    /**
     * 匹配所有传入值
     * @param field 字段名
     * @param value 数组
     */
    public SkyParam matchAll(String field, Object[] value){
        this.putWhere(field, this.getJSONObject("$all", value));
        return this;
    }

    /**
     * 获取[Asynchronous Http Client]所用的param
     * @return Asynchronous Http Client RequestParams
     */
    public RequestParams getQueryParams() {
        RequestParams params = new RequestParams();
        if(this.count){
            params.put("count", 1);
        }
        if(this.skip>0){
            params.put("skip", this.skip);
        }
        if(this.take>0){
            params.put("take", this.take);
        }
        if(!this.orders.isEmpty()){
            String orders = TextUtils.join(",", this.orders);
            params.put("order", orders);
        }
        if(this.fields.size()> 0){
            String select = TextUtils.join(",", this.fields);
            params.put("select", select);
        }
        if(this.includes.size()>0){
            String includes = TextUtils.join(",", this.includes);
            params.put("include", includes);
        }
        if(this.where.length() > 0){
            params.put("where", this.where.toString());
        }
        return params;
    }



    private void putWhere(String field, Object value){
        try {
            this.where.put(field, value);
        }catch (JSONException e){}
    }

    private JSONObject getJSONObject(String field, Object value){
        JSONObject v = new JSONObject();
        try {
            v.put("$ne", value);
        }catch (JSONException e){}
        return v;
    }
}
