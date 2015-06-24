package com.skynology.library.callbacks;

import com.skynology.library.SkyException;
import com.skynology.library.SkyObject;

/**
 *
 */
public abstract  class ObjectCallback<T extends SkyObject> {
    public abstract void onSuccess(T object);
    public abstract void onError(SkyException e);
}
