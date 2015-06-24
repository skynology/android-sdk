package com.skynology.library;


import android.text.BoringLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by william on 6/19/15.
 */
public class SkyACL {
    private JSONObject privateData;
    private static String Read = "read";
    private static String Write = "write";

    public SkyACL(){
        this.privateData = new JSONObject();
        JSONObject global = new JSONObject();
        try {
            global.put(Write, true);
            global.put(Read, true);
            this.privateData.put("*", global);
        } catch (JSONException e){

        }
    }

    public SkyACL(JSONObject acl){
        this.privateData = acl;
    }

    public Boolean checkReadAccess(String userId){
        return this.check(userId, Read);
    }
    public Boolean checkWriteAccess(String userId){
        return this.check(userId, Write);
    }
    public Boolean checkRoleReadAccess(String roleName){
        return this.check("role:"+roleName, Read);
    }
    public Boolean checkRoleWriteAccess(String roleName) {
        return this.check("role:"+roleName, Write);
    }
    private Boolean check(String key, String type) {
        Boolean allowed = false;
        try {
            JSONObject acl= this.privateData.getJSONObject(key);
            allowed = acl.getBoolean(type);
        } catch (JSONException e){

        }
        return allowed;
    }


    public void setReadAccess(String userId, Boolean allowed){
        this.set(userId, Read, allowed);
    }
    public void setWriteAccess(String userId, Boolean allowed){
        this.set(userId, Write, allowed);
    }

    public void setRoleReadAccess(String roleName, Boolean allowed){
        this.set("role:"+roleName, Read, allowed);
    }
    public void setRoleWriteAccess(String roleName, Boolean allowed){
        this.set("role:"+roleName, Write, allowed);
    }

    private void set(String key, String type, Boolean allowed){
        JSONObject access;
        try {
            access = this.privateData.getJSONObject(key);
        } catch (JSONException e) {
            access = new JSONObject();
        }
        try{
            access.put(type, allowed);
            this.privateData.put(key, access);
        } catch (JSONException e){
        }
    }


}
