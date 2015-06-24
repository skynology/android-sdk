package com.skynology.library;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.skynology.library.callbacks.QueryCacllback;

import junit.framework.Assert;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by william on 6/21/15.
 */
public class SkyQueryTest extends InstrumentationTestCase {
    private final static String TAG = "SkynologyTestQuery";
    private final CountDownLatch signal = new CountDownLatch(1);

    private long total = 0;

    @Override
    public void setUp() throws InterruptedException {
        Skynology.initilize(getInstrumentation().getContext(), "550997313a859a09da000011", "a9ac971749800dc46fd77047596c10d17bef2ee9");
        Skynology.setBaseURL("http://192.168.1.101:3002");

        Skynology.logger.setLogging(true);
    }


    public void testGlobalParams(){
        Skynology.initilize(getInstrumentation().getContext(), "550997313a859a09da000011", "a9ac971749800dc46fd77047596c10d17bef2ee9");
        assertEquals("550997313a859a09da000011", Skynology.appId);
    }

    public void testQuery() {
        SkyParam param = new SkyParam();
        param.take(3);
        param.exists("covers");
        param.select("subject", "city");

        SkyQuery<SkyObject> query = new SkyQuery<SkyObject>("event");
        query.setParams(param);
        query.find(new QueryCacllback<SkyObject>() {
            @Override
            public void onSuccess(List<SkyObject> objects, long count) {

                total = 10;
                signal.countDown();
                Log.d(TAG, String.format("query success:%d, %d", count, objects.size()));
            }

            @Override
            public void onError(SkyException e) {

                signal.countDown();

                Log.d(TAG, String.format("query error:%s", e.getMessage()));
            }
        });

        try {
            Skynology.logger.debug("start query from test case");
            signal.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        Assert.assertTrue(total > 0);
    }

}
