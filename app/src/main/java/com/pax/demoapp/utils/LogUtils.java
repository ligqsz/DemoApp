package com.pax.demoapp.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author ligq
 * @date 2018/2/1
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class LogUtils {
    private static Application app;

    public static final int VERBOSE = Log.VERBOSE;
    public static final int DEBUG = Log.DEBUG;
    public static final int INFO = Log.INFO;
    public static final int WARN = Log.WARN;
    public static final int ERROR = Log.ERROR;
    public static final int ASSERT = Log.ASSERT;
    private static final String TAG = LogUtils.class.getSimpleName();

    @IntDef({VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT})
    @Retention(RetentionPolicy.SOURCE)
    private @interface TYPE {
    }

    private static final char[] T = new char[]{'V', 'D', 'I', 'W', 'E', 'A'};

    private static final int FILE = 0x10;
    private static final int JSON = 0x20;
    private static final int XML = 0x30;

    private static final String FILE_SEP = System.getProperty("file.separator");
    private static final String LINE_SEP = System.getProperty("line.separator");
    private static final String TOP_CORNER = "┌";
    private static final String MIDDLE_CORNER = "├";
    private static final String LEFT_BORDER = "│ ";
    private static final String BOTTOM_CORNER = "└";
    private static final String SIDE_DIVIDER = "────────────────────────────────────────────────────────";
    private static final String MIDDLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = TOP_CORNER + SIDE_DIVIDER + SIDE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + MIDDLE_DIVIDER + MIDDLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_CORNER + SIDE_DIVIDER + SIDE_DIVIDER;
    private static final int MAX_LEN = 4000;
    @SuppressLint("SimpleDateFormat")
    private static final Format FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS ");
    private static final String NOTHING = "log nothing";
    private static final String NULL = "null";
    private static final String ARGS = "args";
    private static Config config;

    private static ExecutorService sExecutor;
    /**
     * log 默认存储目录
     */
    private static String sDefaultDir;
    /**
     * log 存储目录
     */
    private static String sDir;
    /**
     * log 文件前缀
     */
    private static String sFilePrefix = "util";
    /**
     * log 总开关，默认关闭
     */
    private static boolean sLogSwitch = false;
    /**
     * logcat 是否打印，默认打印
     */
    private static boolean sLog2ConsoleSwitch = true;
    /**
     * log 标签
     */
    private static String sGlobalTag = null;
    /**
     * log 标签是否为空白
     */
    private static boolean sTagIsSpace = true;
    /**
     * log 头部开关，默认开
     */
    private static boolean sLogHeadSwitch = true;
    /**
     * log 写入文件开关，默认关
     */
    private static boolean sLog2FileSwitch = false;
    /**
     * log 边框开关，默认开
     */
    private static boolean sLogBorderSwitch = true;
    /**
     * log 控制台过滤器
     */
    private static int sConsoleFilter = VERBOSE;
    /**
     * log 文件过滤器
     */
    private static int sFileFilter = VERBOSE;
    /**
     * log 栈深度
     */
    private static int sStackDeep = 1;

    private LogUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Config getConfig() {
        return config;
    }

    public static void v(final Object... contents) {
        log(VERBOSE, sGlobalTag, contents);
    }

    public static void vTag(final String tag, final Object... contents) {
        log(VERBOSE, tag, contents);
    }

    public static void d(final Object... contents) {
        log(DEBUG, sGlobalTag, contents);
    }

    public static void dTag(final String tag, final Object... contents) {
        log(DEBUG, tag, contents);
    }

    public static void i(final Object... contents) {
        log(INFO, sGlobalTag, contents);
    }

    public static void iTag(final String tag, final Object... contents) {
        log(INFO, tag, contents);
    }

    public static void w(final Object... contents) {
        log(WARN, sGlobalTag, contents);
    }

    public static void wTag(final String tag, final Object... contents) {
        log(WARN, tag, contents);
    }

    public static void e(final Object... contents) {
        log(ERROR, sGlobalTag, contents);
    }

    public static void eTag(final String tag, final Object... contents) {
        log(ERROR, tag, contents);
    }

    public static void a(final Object... contents) {
        log(ASSERT, sGlobalTag, contents);
    }

    public static void aTag(final String tag, final Object... contents) {
        log(ASSERT, tag, contents);
    }

    public static void file(final Object content) {
        log(FILE | DEBUG, sGlobalTag, content);
    }

    public static void file(@TYPE final int type, final Object content) {
        log(FILE | type, sGlobalTag, content);
    }

    public static void file(final String tag, final Object content) {
        log(FILE | DEBUG, tag, content);
    }

    public static void file(@TYPE final int type, final String tag, final Object content) {
        log(FILE | type, tag, content);
    }

    public static void json(final String content) {
        log(JSON | DEBUG, sGlobalTag, content);
    }

    public static void json(@TYPE final int type, final String content) {
        log(JSON | type, sGlobalTag, content);
    }

    public static void json(final String tag, final String content) {
        log(JSON | DEBUG, tag, content);
    }

    public static void json(@TYPE final int type, final String tag, final String content) {
        log(JSON | type, tag, content);
    }

    public static void xml(final String content) {
        log(XML | DEBUG, sGlobalTag, content);
    }

    public static void xml(@TYPE final int type, final String content) {
        log(XML | type, sGlobalTag, content);
    }

    public static void xml(final String tag, final String content) {
        log(XML | DEBUG, tag, content);
    }

    public static void xml(@TYPE final int type, final String tag, final String content) {
        log(XML | type, tag, content);
    }

    private static void log(final int type, final String tag, final Object... contents) {
        if (!sLogSwitch || (!sLog2ConsoleSwitch && !sLog2FileSwitch)) {
            return;
        }
        int typeLow = type & 0x0f;
        int typeHigh = type & 0xf0;
        if (typeLow < sConsoleFilter && typeLow < sFileFilter) {
            return;
        }
        final TagHead tagHead = processTagAndHead(tag);
        String body = processBody(typeHigh, contents);
        if (sLog2ConsoleSwitch && typeLow >= sConsoleFilter && typeHigh != FILE) {
            print2Console(typeLow, tagHead.tag, tagHead.consoleHead, body);
        }
        if ((sLog2FileSwitch || typeHigh == FILE) && typeLow >= sFileFilter) {
            try {
                print2File(typeLow, tagHead.tag, tagHead.fileHead + body);
            } catch (InterruptedException e) {
                Log.e(TAG, "log: ", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void init(Application application) {
        app = application;
        config = new Config();
    }

    private static TagHead processTagAndHead(String tag) {
        if (!sTagIsSpace && !sLogHeadSwitch) {
            tag = sGlobalTag;
        } else {
            final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            StackTraceElement targetElement = stackTrace[3];
            String fileName = targetElement.getFileName();
            String className;
            // 混淆可能会导致获取为空 加-keepattributes SourceFile,LineNumberTable
            if (fileName == null) {
                className = targetElement.getClassName();
                String[] classNameInfo = className.split("\\.");
                if (classNameInfo.length > 0) {
                    className = classNameInfo[classNameInfo.length - 1];
                }
                int index = className.indexOf('$');
                if (index != -1) {
                    className = className.substring(0, index);
                }
                fileName = className + ".java";
            } else {
                // 混淆可能导致文件名被改变从而找不到"."
                int index = fileName.indexOf('.');
                className = index == -1 ? fileName : fileName.substring(0, index);
            }
            if (sTagIsSpace) {
                tag = isSpace(tag) ? className : tag;
            }

            if (sLogHeadSwitch) {
                String tName = Thread.currentThread().getName();
                String head;
                String fileHead;
                Formatter formatter1 = null;
                try (Formatter formatTemp = new Formatter()) {
                    formatter1 = formatTemp;
                    head = formatTemp
                            .format("%s, %s(%s:%d)",
                                    tName,
                                    targetElement.getMethodName(),
                                    fileName,
                                    targetElement.getLineNumber())
                            .toString();
                    fileHead = " [" + head + "]: ";
                } catch (Exception e) {
                    Log.e(TAG, "processTagAndHead: ", e);
                    head = "";
                    fileHead = "";
                } finally {
                    if (formatter1 != null) {
                        formatter1.close();
                    }
                }

                if (sStackDeep <= 1) {
                    return new TagHead(tag, new String[]{head}, fileHead);
                } else {
                    final String[] consoleHead =
                            new String[Math.min(sStackDeep, stackTrace.length - 3)];
                    consoleHead[0] = head;
                    int spaceLen = tName.length() + 2;
                    Formatter formatter2 = null;
                    Formatter formatter3 = null;
                    try (Formatter formatTemp = new Formatter(); Formatter formatTemp2 = new Formatter()) {
                        formatter2 = formatTemp;
                        formatter3 = formatTemp2;
                        String format2 = "%" + spaceLen + "s";
                        String space = formatTemp.format(format2, "").toString();
                        for (int i = 1, len = consoleHead.length; i < len; ++i) {
                            targetElement = stackTrace[i + 3];
                            consoleHead[i] = formatTemp2
                                    .format("%s%s(%s:%d)",
                                            space,
                                            targetElement.getMethodName(),
                                            targetElement.getFileName(),
                                            targetElement.getLineNumber())
                                    .toString();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "processTagAndHead: ", e);
                    } finally {
                        if (formatter2 != null) {
                            formatter2.close();
                        }
                        if (formatter3 != null) {
                            formatter3.close();
                        }
                    }
                    return new TagHead(tag, consoleHead, fileHead);
                }
            }
        }
        return new TagHead(tag, null, ": ");
    }

    private static String processBody(final int type, final Object... contents) {
        String body = NULL;
        if (contents != null) {
            if (contents.length == 1) {
                Object object = contents[0];
                if (object != null) {
                    body = object.toString();
                }
                if (type == JSON) {
                    body = formatJson(body);
                } else if (type == XML) {
                    body = formatXml(body);
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

    private static String formatJson(String json) {
        try {
            if (json.startsWith("{")) {
                json = new JSONObject(json).toString(4);
            } else if (json.startsWith("[")) {
                json = new JSONArray(json).toString(4);
            }
        } catch (JSONException e) {
            Log.i(TAG, "formatJson: ", e);
        }
        return json;
    }

    private static String formatXml(String xml) {
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(xmlInput, xmlOutput);
            xml = xmlOutput.getWriter().toString().replaceFirst(">", ">" + LINE_SEP);
        } catch (Exception e) {
            Log.e(TAG, "formatXml: ", e);
        }
        return xml;
    }

    private static void print2Console(final int type,
                                      final String tag,
                                      final String[] head,
                                      final String msg) {
        printBorder(type, tag, true);
        printHead(type, tag, head);
        printMsg(type, tag, msg);
        printBorder(type, tag, false);
    }

    private static void printBorder(final int type, final String tag, boolean isTop) {
        if (sLogBorderSwitch) {
            Log.println(type, tag, isTop ? TOP_BORDER : BOTTOM_BORDER);
        }
    }

    private static void printHead(final int type, final String tag, final String[] head) {
        if (head != null) {
            for (String aHead : head) {
                Log.println(type, tag, sLogBorderSwitch ? LEFT_BORDER + aHead : aHead);
            }
            if (sLogBorderSwitch) {
                Log.println(type, tag, MIDDLE_BORDER);
            }
        }
    }

    private static void printMsg(final int type, final String tag, final String msg) {
        int len = msg.length();
        int countOfSub = len / MAX_LEN;
        if (countOfSub > 0) {
            int index = 0;
            for (int i = 0; i < countOfSub; i++) {
                printSubMsg(type, tag, msg.substring(index, index + MAX_LEN));
                index += MAX_LEN;
            }
            if (index != len) {
                printSubMsg(type, tag, msg.substring(index, len));
            }
        } else {
            printSubMsg(type, tag, msg);
        }
    }

    private static void printSubMsg(final int type, final String tag, final String msg) {
        if (!sLogBorderSwitch) {
            Log.println(type, tag, msg);
            return;
        }
        String[] lines = msg.split(LINE_SEP);
        for (String line : lines) {
            Log.println(type, tag, LEFT_BORDER + line);
        }
    }

    private static synchronized void print2File(final int type, final String tag, final String msg) throws InterruptedException {
        Date now = new Date(System.currentTimeMillis());
        String format = FORMAT.format(now);
        String date = format.substring(0, 5);
        String time = format.substring(6);
        final String fullPath =
                (sDir == null ? sDefaultDir : sDir) + sFilePrefix + "-" + date + ".txt";
        String s = "log to ";
        if (!createOrExistsFile(fullPath)) {
            Log.e(tag, s + fullPath + " failed!");
            return;
        }
        final String content = time +
                T[type - VERBOSE] +
                "/" +
                tag +
                msg +
                LINE_SEP;
        if (input2File(content, fullPath)) {
            Log.d(tag, s + fullPath + " success!");
        } else {
            Log.e(tag, s + fullPath + " failed!");
        }
    }

    @SuppressWarnings("SingleStatementInBlock")
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
                try {
                    printDeviceInfo(filePath);
                } catch (InterruptedException e) {
                    Log.e(TAG, "createOrExistsFile: ", e);
                    Thread.currentThread().interrupt();
                }
            }
            return isCreate;
        } catch (IOException e) {
            Log.e(TAG, "createOrExistsFile: ", e);
            return false;
        }
    }

    private static void printDeviceInfo(final String filePath) throws InterruptedException {
        String versionName = "";
        int versionCode = 0;
        try {
            PackageInfo pi = app
                    .getPackageManager()
                    .getPackageInfo(app.getPackageName(), 0);
            if (pi != null) {
                versionName = pi.versionName;
                versionCode = pi.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "printDeviceInfo: ", e);
        }
        final String head = "************* Log Head ****************" +
                "\nDevice Manufacturer: " + Build.MANUFACTURER +// 设备厂商
                "\nDevice Model       : " + Build.MODEL +// 设备型号
                "\nAndroid Version    : " + Build.VERSION.RELEASE +// 系统版本
                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +// SDK 版本
                "\nApp VersionName    : " + versionName +
                "\nApp VersionCode    : " + versionCode +
                "\n************* Log Head ****************\n\n";
        input2File(head, filePath);
    }

    private static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

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

    private static boolean input2File(final String input, final String filePath) throws InterruptedException {
        if (sExecutor == null) {
            sExecutor = new ThreadPoolExecutor(3, 10,
                    5, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(128),
                    r -> {
                        Thread thread = new Thread(r, "Background executor service");
                        thread.setPriority(Thread.MIN_PRIORITY);
                        thread.setDaemon(true);
                        return thread;
                    });
        }
        Future<Boolean> submit = sExecutor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                BufferedWriter bw = null;
                try (BufferedWriter bwTemp = new BufferedWriter(new FileWriter(filePath, true))) {
                    bw = bwTemp;
                    bwTemp.write(input);
                    return true;
                } catch (IOException e) {
                    Log.e(TAG, "call: ", e);
                    return false;
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "call: ", e);
                    }
                }
            }
        });
        try {
            return submit.get();
        } catch (ExecutionException e) {
            Log.e(TAG, "input2File: ", e);
        }
        return false;
    }

    public static class Config {
        private Config() {
            if (sDefaultDir != null) {
                return;
            }
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    && app.getExternalCacheDir() != null) {
                sDefaultDir = app.getExternalCacheDir() + FILE_SEP + "log" + FILE_SEP;
            } else {
                sDefaultDir = app.getCacheDir() + FILE_SEP + "log" + FILE_SEP;
            }
        }

        public Config setLogSwitch(final boolean logSwitch) {
            sLogSwitch = logSwitch;
            return this;
        }

        public Config setConsoleSwitch(final boolean consoleSwitch) {
            sLog2ConsoleSwitch = consoleSwitch;
            return this;
        }

        public Config setGlobalTag(final String tag) {
            if (isSpace(tag)) {
                sGlobalTag = "";
                sTagIsSpace = true;
            } else {
                sGlobalTag = tag;
                sTagIsSpace = false;
            }
            return this;
        }

        public Config setLogHeadSwitch(final boolean logHeadSwitch) {
            sLogHeadSwitch = logHeadSwitch;
            return this;
        }

        public Config setLog2FileSwitch(final boolean log2FileSwitch) {
            sLog2FileSwitch = log2FileSwitch;
            return this;
        }

        public Config setDir(final String dir) {
            if (isSpace(dir)) {
                sDir = null;
            } else {
                sDir = dir.endsWith(FILE_SEP) ? dir : dir + FILE_SEP;
            }
            return this;
        }

        public Config setDir(final File dir) {
            sDir = dir == null ? null : dir.getAbsolutePath() + FILE_SEP;
            return this;
        }

        public Config setFilePrefix(final String filePrefix) {
            if (isSpace(filePrefix)) {
                sFilePrefix = "util";
            } else {
                sFilePrefix = filePrefix;
            }
            return this;
        }

        public Config setBorderSwitch(final boolean borderSwitch) {
            sLogBorderSwitch = borderSwitch;
            return this;
        }

        public Config setConsoleFilter(@TYPE final int consoleFilter) {
            sConsoleFilter = consoleFilter;
            return this;
        }

        public Config setFileFilter(@TYPE final int fileFilter) {
            sFileFilter = fileFilter;
            return this;
        }

        public Config setStackDeep(@IntRange(from = 1) final int stackDeep) {
            sStackDeep = stackDeep;
            return this;
        }

        @Override
        public String toString() {
            return "switch: " + sLogSwitch
                    + LINE_SEP + "console: " + sLog2ConsoleSwitch
                    + LINE_SEP + "tag: " + (sTagIsSpace ? "null" : sGlobalTag)
                    + LINE_SEP + "head: " + sLogHeadSwitch
                    + LINE_SEP + "file: " + sLog2FileSwitch
                    + LINE_SEP + "dir: " + (sDir == null ? sDefaultDir : sDir)
                    + LINE_SEP + "filePrefix" + sFilePrefix
                    + LINE_SEP + "border: " + sLogBorderSwitch
                    + LINE_SEP + "consoleFilter: " + T[sConsoleFilter - VERBOSE]
                    + LINE_SEP + "fileFilter: " + T[sFileFilter - VERBOSE]
                    + LINE_SEP + "stackDeep: " + sStackDeep;
        }
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
}
