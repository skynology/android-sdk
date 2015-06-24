package com.skynology.library.callbacks;

import com.skynology.library.SkyException;
import com.skynology.library.SkyUser;

/**
 * Created by william on 6/23/15.
 */
public abstract  class RegisterCallback {
    public abstract void done(SkyUser user, SkyException e);
}
