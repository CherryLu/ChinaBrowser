package com.chinabrowser.utils;

import java.io.File;

/**
 * Created by maclay on 2017/2/16.
 */

public class FileHelper {
    /**
     * 获取指定文件夹大小
     *
     * @param f
     * @return byte
     * @throws Exception
     */
    public static long getFolderSize(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFolderSize(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 获取指定文件大小
     *
     * @param
     * @return byte
     * @throws Exception
     */
    private static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
//            FileInputStream fis = null;
//            fis = new FileInputStream(file);
//            size = fis.available();
            size = file.length();
        } else {
//            file.createNewFile();
        }
        return size;
    }

    /**
     * 递归删除文件及文件夹
     *
     * @param file
     */
    public static void deleteFileOrPath(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                deleteFileOrPath(childFiles[i]);
            }
            file.delete();
        }
    }
}
