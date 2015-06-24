package com.skynology.library;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by william on 6/19/15.
 */
public class SkyException extends Exception {
    private int code = 0;
    private String message = "";
    private String description = "";

    public SkyException(int code, String message, String description){
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public SkyException(String json){
        JSONObject obj;
        try{
            obj = new JSONObject(json);
            this.code = obj.getInt("code");
            this.message = obj.getString("error");
            this.description = obj.getString("description");
        } catch (JSONException e){
        }

    }

    public SkyException(JSONObject data) {
        try {
            this.message = data.getString("error");
            this.code = data.getInt("code");
            this.description = data.getString("description");
        }catch (JSONException e){

        }
    }

    public void setCode(int code){
        this.code = code;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public String getErrorMessage(){
        return this.message;
    }

    public int getErrorCode(){
        return this.code;
    }

    public String getErrorDescription(){
        return this.description;
    }

}
