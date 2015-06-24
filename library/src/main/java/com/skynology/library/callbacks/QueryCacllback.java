package com.skynology.library.callbacks;

import com.skynology.library.SkyException;
import com.skynology.library.SkyObject;

import java.util.List;

/**
 * Created by william on 6/21/15.
 */
public abstract class QueryCacllback<T extends SkyObject> {
    public abstract void onSuccess(List<T> objects, long count);
    public abstract void onError(SkyException e);
}

