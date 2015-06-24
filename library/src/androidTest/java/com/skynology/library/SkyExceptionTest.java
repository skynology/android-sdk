package com.skynology.library;

import android.test.InstrumentationTestCase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by william on 6/21/15.
 */
public class SkyExceptionTest extends InstrumentationTestCase {

    public void testInitByJSONString(){
        String json = "{\"code\":11,\"error\":\"just for test\",\"description\":\"description\"}";
        SkyException e = new SkyException(json);

        assertEquals(e.getErrorCode(), 11);
        assertEquals(e.getErrorMessage(), "just for test");
        assertEquals(e.getErrorDescription(), "description");
    }

    public void testInitByJSONObject() throws JSONException{
        JSONObject obj = new JSONObject();
        obj.put("code", 12);
        obj.put("error", "test test");
        obj.put("description", "description description");

        SkyException e = new SkyException(obj);

        assertEquals(e.getErrorCode(), 12);
        assertEquals(e.getErrorMessage(), "test test");
        assertEquals(e.getErrorDescription(), "description description");
    }

    public void testSet(){
        SkyException e = new SkyException(0, "test", "test desc");
        e.setCode(20);
        e.setMessage("message1 m");
        e.setDescription("desc desc&desc");
        assertEquals(e.getErrorCode(), 20);
        assertEquals(e.getErrorMessage(), "message1 m");
        assertEquals(e.getErrorDescription(), "desc desc&desc");
    }

}

