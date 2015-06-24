package com.skynology.library;

import com.loopj.android.http.RequestParams;
import com.skynology.library.callbacks.BooleanCallback;
import com.skynology.library.callbacks.FileDownloadCallback;
import com.skynology.library.callbacks.FileSaveCallback;

import org.json.JSONObject;

import java.io.File;
import java.util.Date;

/**
 * Created by william on 6/24/15.
 */
public class SkyFile extends SkyObject{
    private String objectId;
    private String key;

    private String path;
    private byte[] data;
    private SkyObject fileData;

    private static String bucket;
    private static String token;
    private static Date tokenExpired;

    public SkyFile(){
        super("_File");
    }

    public SkyFile(JSONObject data){
        super("_File", data);
    }


    /**
     * 上传文件到服务器
     * @param newName 文件名, 存于_File表中
     * @param data 文件数据
     * @param callback 回掉函数
     */
    public static void upload(String newName, byte[] data, final FileSaveCallback callback){
        upload(newName, data, null, null, callback);
    }

    /**
     * 上传文件
     * @param newName 文件名, 存于_File表中
     * @param file 要上传的文件
     * @param callback 回掉函数
     */
    public static void upload(String newName, File file, final  FileSaveCallback callback){
        upload(newName, null, null, file, callback);
    }

    /**
     * 上传文件
     * @param newName 文件名,存于_File表中
     * @param filePath 文件本地路径
     * @param callback 回掉函数
     */
    public static void upload(String newName, String filePath, final  FileSaveCallback callback){
        upload(newName, null, filePath, null,callback);
    }

    /**
     * 下载指定url文件到服务器上
     * @param newName 要保存的文件名
     * @param url 要下载的文件url
     * @param callback 回调函数
     */
    public static void fetch(String newName, String url, final FileSaveCallback callback){

    }

    private static void upload(String name, byte[] data, String path, File file, final FileSaveCallback callback){

    }

    public static void download(final FileDownloadCallback callback){

    }

    private static void getToken(){

    }

}
