package com.skynology.library.callbacks;

import com.skynology.library.SkyException;

import org.json.JSONObject;

/**
 * Created by william on 6/21/15.
 */
public abstract class JsonObjectCallback {
    public abstract void onSuccess(JSONObject object);
    public abstract void onError(SkyException e);
}
