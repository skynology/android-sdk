package com.skynology.library;

import android.test.InstrumentationTestCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by william on 6/19/15.
 */
public class SkyObjectTest extends InstrumentationTestCase {

    private static String json ="{\"ACL\":{\"*\":{\"read\":true,\"write\":true}},\"bgcolor\":\"#9669E1\",\"createdAt\":\"2015-03-20T07:11:42.778Z\",\"icon\":\"conf\",\"name\":\"讲座\",\"objectId\":\"550bc82e3a859a07a7000002\",\"order\":10,\"updatedAt\":\"2015-03-20T11:18:22.329Z\"}";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Skynology.initilize(getInstrumentation().getContext(), "", "");
        Skynology.logger.setLogging(true);
    }

    public void testInitWithData() throws JSONException{
        JSONObject data = new JSONObject(json);
        SkyObject obj = new SkyObject("test", data);
        assertNotNull(obj);
        assertEquals("550bc82e3a859a07a7000002", obj.getObjectId());
        assertEquals(10, obj.getInt("order"));
        assertEquals("#9669E1", obj.getString("bgcolor"));
    }

    public void testGetDate() throws JSONException{
        JSONObject data = new JSONObject(json);
        SkyObject obj = new SkyObject("test", data);
        Date d = obj.getCreatedAt();

        assertNotNull(d);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String result = format.format(d);
        assertEquals("2015-03-20T07:11:42.778Z", result);

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
