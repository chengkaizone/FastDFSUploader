package com.tony.fastdfsuploader.jni;

/**
 * Created by tony on 16/9/12.
 */
public class FastDFSUploader {

    static {

        try {
            System.loadLibrary("fastdfsuploader");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行上传
     * @param path 传入的文件路径
     * @return
     */
    public static native int upload(String path);

}
