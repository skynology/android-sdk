package com.skynology.library.callbacks;

import com.skynology.library.SkyException;

import org.json.JSONObject;

/**
 * Created by william on 6/23/15.
 */
public abstract class BooleanCallback {
    public abstract void done(Boolean successed, SkyException e);
}
