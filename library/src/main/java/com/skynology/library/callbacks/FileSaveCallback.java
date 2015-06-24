package com.skynology.library.callbacks;

import com.skynology.library.SkyException;
import com.skynology.library.SkyFile;
import com.skynology.library.SkyObject;

import org.json.JSONObject;

/**
 * Created by william on 6/24/15.
 */
public abstract  class FileSaveCallback {
    public abstract void onSuccess(SkyFile object);
    public abstract void onError(SkyException e);

    /**
     * 上传/下载进度
     * @param percent 完成百分比
     * @param doneSize 已上传/下载大小
     * @param totalSize 总文件大小
     */
    public abstract void onProgress(int percent, int doneSize, int totalSize);


}
