package com.skynology.library;

import android.test.InstrumentationTestCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by william on 6/19/15.
 */
public class SkyACLTest extends InstrumentationTestCase {

    public void testInit(){
        SkyACL acl = new SkyACL();
        assertTrue(acl.checkReadAccess("*"));
        assertTrue(acl.checkWriteAccess("*"));
        assertFalse(acl.checkReadAccess("#"));
        assertFalse(acl.checkWriteAccess("#"));
    }

    public void testInitWithData(){
        JSONObject value = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            value.put("write", false);
            value.put("read", true);
            data.put("user1", value);
        }catch (JSONException e){

        }
        SkyACL acl = new SkyACL(data);

        assertTrue(acl.checkReadAccess("user1"));
        assertFalse(acl.checkWriteAccess("user1"));
        assertFalse(acl.checkRoleReadAccess("admin"));
        assertFalse(acl.checkRoleWriteAccess("admin"));

    }

    public void testRoleACL(){
        SkyACL acl = new SkyACL();
        acl.setRoleReadAccess("admin", true);
        acl.setRoleWriteAccess("admin", true);
        acl.setRoleReadAccess("emp", true);
        acl.setRoleWriteAccess("emp", false);

        assertTrue(acl.checkRoleReadAccess("admin"));
        assertTrue(acl.checkRoleWriteAccess("admin"));

        assertTrue(acl.checkRoleReadAccess("admin"));
        assertFalse(acl.checkRoleWriteAccess("emp"));

    }

}
