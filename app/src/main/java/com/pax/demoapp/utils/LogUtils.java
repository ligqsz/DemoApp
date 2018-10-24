/*
 *
 * ============================================================================
 * PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 * This software is supplied under the terms of a license agreement or nondisclosure
 * agreement with PAX Computer Technology(Shenzhen) CO., LTD and may not be copied or
 * disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2018-?  PAX Computer Technology(Shenzhen) CO., LTD All rights reserved.
 * Description:
 * Revision History:
 * Date	             Author	                Action
 * 20181024   	     ligq           	Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.demoapp.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pax.demoapp.BuildConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ligq
 * @date 2018/10/24
 */
@SuppressWarnings("unused")
public class LogUtils {
    private static final String TAG = LogUtils.class.getSimpleName();
    private static final int VERBOSE = Log.VERBOSE;
    private static final int DEBUG = Log.DEBUG;
    private static final int INFO = Log.INFO;
    private static final int WARN = Log.WARN;
    private static final int ERROR = Log.ERROR;
    private static final int ASSERT = Log.ASSERT;
    private static final char[] T = new char[]{'V', 'D', 'I', 'W', 'E', 'A'};
    /**
     * config
     */
    private static final Config CONFIG = new Config();

    private static final String FILE_SEP = System.getProperty("file.separator");
    private static final String LINE_SEP = System.getProperty("line.separator");
    private static final int MAX_LEN = 3000;
    private static final String NOTHING = "log nothing";
    private static final String NULL = "null";
    private static final String ARGS = "args";
    private static final String PLACEHOLDER = " ";

    public static Config getConfig() {
        return CONFIG;
    }

    private LogUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void v(final Object contents) {
        log(VERBOSE, null, contents);
    }

    public static void v(final String tag, final Object... contents) {
        log(VERBOSE, tag, contents);
    }

    public static void d(final Object contents) {
        log(DEBUG, null, contents);
    }

    public static void d(final String tag, final Object... contents) {
        log(DEBUG, tag, contents);
    }

    public static void i(final Object contents) {
        log(INFO, null, contents);
    }

    public static void i(final String tag, final Object... contents) {
        log(INFO, tag, contents);
    }

    public static void w(final Object contents) {
        log(WARN, null, contents);
    }

    public static void w(final String tag, final Object... contents) {
        log(WARN, tag, contents);
    }

    public static void e(final Object contents) {
        log(ERROR, null, contents);
    }

    public static void e(final String tag, final Object... contents) {
        log(ERROR, tag, contents);
    }

    public static void a(final Object contents) {
        log(ASSERT, null, contents);
    }

    public static void a(final String tag, final Object... contents) {
        log(ASSERT, tag, contents);
    }

    /**
     * check the content is empty or not
     *
     * @param s content
     * @return true:is empty(null,"");false:is not empty
     */
    private static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static void log(final int type, final String tag, final Object... contents) {
        boolean closeLog = !CONFIG.showLog;
        if (closeLog) {
            return;
        }
        int typeLow = type & 0x0f;
        final TagHead tagHead = processTagAndHead(tag);
        String body = processBody(contents);
        if (CONFIG.showLog) {
            print2Console(typeLow, tagHead.tag, tagHead.consoleHead, body);
        }
        if (CONFIG.saveLog) {
            print2File(typeLow, tagHead.tag, tagHead.fileHead + body);
        }
    }

    private static TagHead processTagAndHead(String tag) {
        final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        final int stackIndex = 3;
        if (stackIndex >= stackTrace.length) {
            return new TagHead(tag, null, ": ");
        }
        StackTraceElement targetElement = stackTrace[stackIndex];
        final String fileName = getFileName(targetElement);
        if (isSpace(tag)) {
            // Use proguard may not find '.'.
            int index = fileName.indexOf('.');
            tag = index == -1 ? fileName : fileName.substring(0, index);
        }
        String tName = Thread.currentThread().getName();
        try (Formatter formatter = new Formatter()) {
            final String head = formatter
                    .format("%s,%s(%s:%d)",
                            tName,
                            targetElement.getMethodName(),
                            fileName,
                            targetElement.getLineNumber())
                    .toString();
            final String fileHead = " [" + head + "]: ";
            return new TagHead(tag, new String[]{head}, fileHead);
        } catch (Exception e) {
            Log.e(TAG, "processTagAndHead: ", e);
            return new TagHead(tag, null, ": ");
        }
    }

    private static String getFileName(final StackTraceElement targetElement) {
        String fileName = targetElement.getFileName();
        if (fileName != null) {
            return fileName;
        }
        // If name of file is null, should add
        // "-keepattributes SourceFile,LineNumberTable" in proguard file.
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1];
        }
        int index = className.indexOf('$');
        if (index != -1) {
            className = className.substring(0, index);
        }
        return className + ".java";
    }

    private static String processBody(final Object... contents) {
        String body = NULL;
        if (contents != null) {
            if (contents.length == 1) {
                Object object = contents[0];
                if (object != null) {
                    body = object.toString();
                }
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0, len = contents.length; i < len; ++i) {
                    Object content = contents[i];
                    sb.append(ARGS)
                            .append("[")
                            .append(i)
                            .append("]")
                            .append(" = ")
                            .append(content == null ? NULL : content.toString())
                            .append(LINE_SEP);
                }
                body = sb.toString();
            }
        }
        return body.length() == 0 ? NOTHING : body;
    }

    private static void print2Console(final int type, final String tag
            , final String[] head, final String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(PLACEHOLDER);
        if (head != null) {
            for (String aHead : head) {
                //去掉换行.append(LINE_SEP)
                sb.append(aHead).append(":");
            }
        }
        sb.append(msg);
        printMsgSingleTag(type, tag, sb.toString());
    }

    private static void printMsgSingleTag(final int type, final String tag, final String msg) {
        int len = msg.length();
        int countOfSub = len / MAX_LEN;
        if (countOfSub > 0) {
            int index = 0;
            for (int i = 0; i < countOfSub; i++) {
                Log.println(type, tag, msg.substring(index, index + MAX_LEN));
                index += MAX_LEN;
            }
            if (index != len) {
                Log.println(type, tag, msg.substring(index, len));
            }

        } else {
            Log.println(type, tag, msg);
        }
    }

    private static void print2File(final int type, final String tag, final String msg) {
        final Format dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS ", Locale.getDefault());
        Date now = new Date(System.currentTimeMillis());
        String format = dateFormat.format(now);
        String date = format.substring(0, 5);
        String time = format.substring(6);
        final String fullPath = CONFIG.mDefaultDir + "-" + date + ".txt";
        if (!createOrExistsFile(fullPath)) {
            Log.e("LogUtils", "create " + fullPath + " failed!");
            return;
        }
        final String content = time +
                T[type - VERBOSE] +
                "/" +
                tag +
                msg +
                LINE_SEP;
        input2File(content, fullPath);
    }

    private static boolean createOrExistsFile(final String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.isFile();
        }
        if (!createOrExistsDir(file.getParentFile())) {
            return false;
        }
        try {
            boolean isCreate = file.createNewFile();
            if (isCreate) {
                printDeviceInfo(filePath);
            }
            return isCreate;
        } catch (IOException e) {
            Log.e(TAG, "createOrExistsFile: ", e);
            return false;
        }
    }

    private static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    private static void printDeviceInfo(final String filePath) {
        String versionName = "";
        int versionCode = 0;
        try {
            PackageInfo pi = Utils.getApp()
                    .getPackageManager()
                    .getPackageInfo(Utils.getApp().getPackageName(), 0);
            if (pi != null) {
                versionName = pi.versionName;
                versionCode = pi.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "printDeviceInfo: ", e);
        }
        String time = filePath.substring(filePath.length() - 9, filePath.length() - 4);
        final String head = "************* Log Head ****************" +
                "\nDate of Log        : " + time +
                "\nDevice Manufacturer: " + Build.MANUFACTURER +
                "\nDevice Model       : " + Build.MODEL +
                "\nAndroid Version    : " + Build.VERSION.RELEASE +
                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +
                "\nApp VersionName    : " + versionName +
                "\nApp VersionCode    : " + versionCode +
                "\n************* Log Head ****************\n\n";
        input2File(head, filePath);
    }

    private static void input2File(final String input, final String filePath) {
        ExecutorService sExecutor = new ThreadPoolExecutor(3, 10,
                5, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(128), new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                Thread thread = new Thread(runnable, "LogUtils executor service");
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.setDaemon(true);
                return thread;
            }
        });
        Future<Boolean> submit = sExecutor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
                    bw.write(input);
                    return true;
                } catch (IOException e) {
                    Log.e(TAG, "call: ", e);
                    return false;
                }
            }
        });
        try {
            if (submit.get()) {
                return;
            }
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "input2File: ", e);
        }
        Log.e("LogUtils", "log to " + filePath + " failed!");
    }

    private static class TagHead {
        String tag;
        String[] consoleHead;
        String fileHead;

        TagHead(String tag, String[] consoleHead, String fileHead) {
            this.tag = tag;
            this.consoleHead = consoleHead;
            this.fileHead = fileHead;
        }
    }

    /**
     * Configs for LogUtils
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static class Config {
        /**
         * show log or not
         */
        private boolean showLog;
        /**
         * save log or not
         */
        private boolean saveLog;
        /**
         * the default dir for save file
         */
        private String mDefaultDir;

        public Config() {
            if (mDefaultDir == null) {
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                        && Utils.getApp().getExternalCacheDir() != null) {
                    mDefaultDir = Utils.getApp().getExternalCacheDir() + FILE_SEP + "log" + FILE_SEP;
                } else {
                    mDefaultDir = Utils.getApp().getCacheDir() + FILE_SEP + "log" + FILE_SEP;
                }
            }
            showLog = BuildConfig.DEBUG;
            saveLog = false;
        }

        public Config setShowLog(boolean showLog) {
            this.showLog = showLog;
            return this;
        }

        public Config setSaveLog(boolean saveLog) {
            this.saveLog = saveLog;
            return this;
        }

        public Config setDefaultDir(String dir) {
            this.mDefaultDir = dir;
            return this;
        }
    }
}
