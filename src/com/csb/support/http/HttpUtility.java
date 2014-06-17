package com.csb.support.http;


import java.io.InterruptedIOException;
import java.util.Map;

import com.csb.support.error.WeiboException;
import com.csb.support.file.FileDownloaderHttpHelper;
import com.csb.support.file.FileUploaderHttpHelper;

public class HttpUtility {

    private static HttpUtility httpUtility = new HttpUtility();

    private HttpUtility() {
    }

    public static HttpUtility getInstance() {
        return httpUtility;
    }


    public String executeNormalTask(HttpMethod httpMethod, String url, Map<String, String> param) throws WeiboException {
        return new JavaHttpUtility().executeNormalTask(httpMethod, url, param);
    }

    public boolean executeDownloadTask(String url, String path, FileDownloaderHttpHelper.DownloadListener downloadListener) {
        return !Thread.currentThread().isInterrupted() && new JavaHttpUtility().doGetSaveFile(url, path, downloadListener);
    }
    
    public boolean executeUploadTask(String url, Map<String, String> param, String path, String imageParamName, FileUploaderHttpHelper.ProgressListener listener) throws WeiboException {
        return !Thread.currentThread().isInterrupted() && new JavaHttpUtility().doUploadFile(url, param, path, imageParamName, listener);
    }
    
    public String executeUploadTaskWithResult(String url, Map<String, String> param, String path, String imageParamName, String fileName, FileUploaderHttpHelper.ProgressListener listener) throws WeiboException {
    	 if (Thread.currentThread().isInterrupted()) {
             throw new WeiboException("Current thread is interrupted!", new InterruptedIOException());
         }
    	return new JavaHttpUtility().doUploadFileWithResult(url, param, path, imageParamName, fileName, listener);
    }
}

