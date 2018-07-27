package com.pax.demoapp.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author ligq
 * @date 2018/7/18
 */

public class LogUtils {
    private static final boolean IS_DEBUG = 0 == OtherUtils.getMetaInt("SHOW_LOG");
    /**
     * custom TAG
     */
    private static final String CUSTOM_TAG_PREFIX = "demo";

    /**
     * SD card root content
     */
    private static final String ROOT = Environment.getExternalStorageDirectory()
            .getPath() + "/demo/";
    private static final String PATH_LOG_INFO = ROOT + "log/";
    /**
     * save log in SD card
     */
    private static final boolean IS_SAVE_LOG = 0 == OtherUtils.getMetaInt("SAVE_LOG");

    private LogUtils() {
        throw new IllegalArgumentException();
    }

    public static void d(String tag, String msg) {
        if (IS_DEBUG) {
            StackTraceElement caller = getCallerStackTraceElement();
            tag = tag + ":" + generateTag(caller);
            Log.d(tag, msg);
            if (IS_SAVE_LOG) {
                point(PATH_LOG_INFO, tag, msg);
            }
        }
    }

    public static void d(String msg) {
        if (IS_DEBUG) {
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller);
            Log.d(tag, msg);
            if (IS_SAVE_LOG) {
                point(PATH_LOG_INFO, tag, msg);
            }
        }
    }

    public static void e(String tag, Throwable e) {
        if (IS_DEBUG) {
            StackTraceElement caller = getCallerStackTraceElement();
            Log.e(generateTag(caller), tag, e);
            if (IS_SAVE_LOG) {
                point(PATH_LOG_INFO, generateTag(caller) + tag, e.getMessage());
            }
        }
    }

    public static void e(Throwable e) {
        if (IS_DEBUG) {
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller);
            Log.e(tag, "", e);
            if (IS_SAVE_LOG) {
                point(PATH_LOG_INFO, tag, e.getMessage());
            }
        }
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    @SuppressLint("DefaultLocale")
    private static String generateTag(StackTraceElement caller) {
        // placeholder
        String tag = "%s.%s(Line:%d)";
        // get class name
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName
                .lastIndexOf(".") + 1);
        // replace
        tag = String.format(tag, callerClazzName, caller.getMethodName(),
                caller.getLineNumber());
        tag = TextUtils.isEmpty(CUSTOM_TAG_PREFIX) ? tag : CUSTOM_TAG_PREFIX + ":"
                + tag;
        return tag;
    }

    private static void point(String path, String tag, String msg) {
        if (isSDAva()) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("",
                    Locale.US);
            dateFormat.applyPattern("yyyy");
            path = path + dateFormat.format(date) + "/";
            dateFormat.applyPattern("MM");
            path += dateFormat.format(date) + "/";
            dateFormat.applyPattern("dd");
            path += dateFormat.format(date) + ".log";
            dateFormat.applyPattern("[yyyy-MM-dd HH:mm:ss]");
            String time = dateFormat.format(date);
            File file = new File(path);
            if (!file.exists()) {
                createDipPath(path);
            }
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, true)));
                out.write(time + " " + tag + " " + msg + "\r\n");
            } catch (Exception e) {
                e(e);
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e(e);
                }
            }
        }
    }

    private static void createDipPath(String file) {
        String parentFile = file.substring(0, file.lastIndexOf("/"));
        File file1 = new File(file);
        File parent = new File(parentFile);
        if (!file1.exists()) {
            parent.mkdirs();
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e(e);
            }
        }
    }

    private static boolean isSDAva() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                || Environment.getExternalStorageDirectory().exists();
    }
}
