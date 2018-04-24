package com.chinabrowser.utils;

import android.content.Context;

import java.io.File;

/**
 * Created by maclay on 2017/2/16.
 */

public class AppCacheUtils {
    public static long getTotalCacheSize(Context context) {
        if (context != null) {
            File cacheFile = context.getExternalCacheDir();
            if (cacheFile != null) {
                return FileHelper.getFolderSize(cacheFile);
            }
        }
        return 0;
    }

    public static void clearCache(Context context) {
        if (context != null) {
            File cacheFile = context.getExternalCacheDir();
            if (cacheFile != null) {
                FileHelper.deleteFileOrPath(cacheFile);
            }
        }
    }
}
