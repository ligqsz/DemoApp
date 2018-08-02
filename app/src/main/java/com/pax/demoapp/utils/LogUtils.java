package com.pax.demoapp.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
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
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
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
    private static final String TAG = LogUtils.class.getSimpleName();
    public static final int VERBOSE = Log.VERBOSE;
    public static final int DEBUG = Log.DEBUG;
    public static final int INFO = Log.INFO;
    public static final int WARN = Log.WARN;
    public static final int ERROR = Log.ERROR;
    public static final int ASSERT = Log.ASSERT;

    @IntDef({VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {
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
    private static final String SIDE_DIVIDER =
            "────────────────────────────────────────────────────────";
    private static final String MIDDLE_DIVIDER =
            "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = TOP_CORNER + SIDE_DIVIDER + SIDE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + MIDDLE_DIVIDER + MIDDLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_CORNER + SIDE_DIVIDER + SIDE_DIVIDER;
    private static final int MAX_LEN = 3000;
    /*<p>private static final Format FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS ");</p>*/
    /**
     * nothing
     */
    private static final String NOTHING = "log nothing";
    private static final String NULL = "null";
    private static final String ARGS = "args";
    private static final String PLACEHOLDER = " ";
    private static final Config CONFIG = new Config();
    private static ExecutorService sExecutor;

    private LogUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Config getConfig() {
        return CONFIG;
    }

    public static void v(final Object... contents) {
        log(VERBOSE, CONFIG.mGlobalTag, contents);
    }

    public static void vTag(final String tag, final Object... contents) {
        log(VERBOSE, tag, contents);
    }

    public static void d(final Object... contents) {
        log(DEBUG, CONFIG.mGlobalTag, contents);
    }

    public static void dTag(final String tag, final Object... contents) {
        log(DEBUG, tag, contents);
    }

    public static void i(final Object... contents) {
        log(INFO, CONFIG.mGlobalTag, contents);
    }

    public static void iTag(final String tag, final Object... contents) {
        log(INFO, tag, contents);
    }

    public static void w(final Object... contents) {
        log(WARN, CONFIG.mGlobalTag, contents);
    }

    public static void wTag(final String tag, final Object... contents) {
        log(WARN, tag, contents);
    }

    public static void e(final Object... contents) {
        log(ERROR, CONFIG.mGlobalTag, contents);
    }

    public static void eTag(final String tag, final Object... contents) {
        log(ERROR, tag, contents);
    }

    public static void a(final Object... contents) {
        log(ASSERT, CONFIG.mGlobalTag, contents);
    }

    public static void aTag(final String tag, final Object... contents) {
        log(ASSERT, tag, contents);
    }

    public static void file(final Object content) {
        log(FILE | DEBUG, CONFIG.mGlobalTag, content);
    }

    public static void file(@TYPE final int type, final Object content) {
        log(FILE | type, CONFIG.mGlobalTag, content);
    }

    public static void file(final String tag, final Object content) {
        log(FILE | DEBUG, tag, content);
    }

    public static void file(@TYPE final int type, final String tag, final Object content) {
        log(FILE | type, tag, content);
    }

    public static void json(final String content) {
        log(JSON | DEBUG, CONFIG.mGlobalTag, content);
    }

    public static void json(@TYPE final int type, final String content) {
        log(JSON | type, CONFIG.mGlobalTag, content);
    }

    public static void json(final String tag, final String content) {
        log(JSON | DEBUG, tag, content);
    }

    public static void json(@TYPE final int type, final String tag, final String content) {
        log(JSON | type, tag, content);
    }

    public static void xml(final String content) {
        log(XML | DEBUG, CONFIG.mGlobalTag, content);
    }

    public static void xml(@TYPE final int type, final String content) {
        log(XML | type, CONFIG.mGlobalTag, content);
    }

    public static void xml(final String tag, final String content) {
        log(XML | DEBUG, tag, content);
    }

    public static void xml(@TYPE final int type, final String tag, final String content) {
        log(XML | type, tag, content);
    }

    public static void log(final int type, final String tag, final Object... contents) {
        boolean closeLog = !CONFIG.mLogSwitch || (!CONFIG.mLog2ConsoleSwitch && !CONFIG.mLog2FileSwitch);
        if (closeLog) {
            return;
        }
        int typeLow = type & 0x0f;
        int typeHigh = type & 0xf0;
        if (typeLow < CONFIG.mConsoleFilter && typeLow < CONFIG.mFileFilter) {
            return;
        }
        final TagHead tagHead = processTagAndHead(tag);
        String body = processBody(typeHigh, contents);
        if (CONFIG.mLog2ConsoleSwitch && typeLow >= CONFIG.mConsoleFilter && typeHigh != FILE) {
            print2Console(typeLow, tagHead.tag, tagHead.consoleHead, body);
        }
        boolean b = (CONFIG.mLog2FileSwitch || typeHigh == FILE) && typeLow >= CONFIG.mFileFilter;
        if (b) {
            print2File(typeLow, tagHead.tag, tagHead.fileHead + body);
        }
    }

    private static TagHead processTagAndHead(String tag) {
        if (!CONFIG.mTagIsSpace && !CONFIG.mLogHeadSwitch) {
            tag = CONFIG.mGlobalTag;
        } else {
            final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            final int stackIndex = 3 + CONFIG.mStackOffset;
            if (stackIndex >= stackTrace.length) {
                return handleStackIndex(tag, stackTrace[3]);
            }
            StackTraceElement targetElement = stackTrace[stackIndex];
            final String fileName = getFileName(targetElement);
            if (CONFIG.mTagIsSpace && isSpace(tag)) {
                // Use proguard may not find '.'.
                int index = fileName.indexOf('.');
                tag = index == -1 ? fileName : fileName.substring(0, index);
            }
            if (CONFIG.mLogHeadSwitch) {
                return getTagHead(tag, stackTrace, stackIndex, targetElement, fileName);
            }
        }
        return new TagHead(tag, null, ": ");
    }

    @NonNull
    private static TagHead handleStackIndex(String tag, StackTraceElement stackTraceElement) {
        final String fileName = getFileName(stackTraceElement);
        if (CONFIG.mTagIsSpace && isSpace(tag)) {
            // Use proguard may not find '.'.
            int index = fileName.indexOf('.');
            tag = index == -1 ? fileName : fileName.substring(0, index);
        }
        return new TagHead(tag, null, ": ");
    }

    @NonNull
    private static TagHead getTagHead(String tag, StackTraceElement[] stackTrace, int stackIndex, StackTraceElement targetElement, String fileName) {
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
        if (CONFIG.mStackDeep <= 1) {
            return new TagHead(tag, new String[]{head}, fileHead);
        } else {
            final String[] consoleHead =
                    new String[Math.min(
                            CONFIG.mStackDeep,
                            stackTrace.length - stackIndex
                    )];
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

    private static String processBody(final int type, final Object... contents) {
        String body = NULL;
        if (contents != null) {
            body = handleContentNotNull(type, body, contents);
        }
        return body.length() == 0 ? NOTHING : body;
    }

    private static String handleContentNotNull(int type, String body, Object[] contents) {
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
        return body;
    }

    private static String formatJson(String json) {
        String prefix = "{";
        String prefix1 = "[";
        try {
            if (json.startsWith(prefix)) {
                json = new JSONObject(json).toString(4);
            } else {
                if (json.startsWith(prefix1)) {
                    json = new JSONArray(json).toString(4);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "formatJson: ", e);
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
        if (CONFIG.mSingleTagSwitch) {
            handleSingleTag(type, tag, head, msg);
        } else {
            printBorder(type, tag, true);
            printHead(type, tag, head);
            printMsg(type, tag, msg);
            printBorder(type, tag, false);
        }
    }

    private static void handleSingleTag(int type, String tag, String[] head, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(PLACEHOLDER).append(LINE_SEP);
        if (CONFIG.mLogBorderSwitch) {
            sb.append(TOP_BORDER).append(LINE_SEP);
            if (head != null) {
                for (String aHead : head) {
                    sb.append(LEFT_BORDER).append(aHead).append(LINE_SEP);
                }
                sb.append(MIDDLE_BORDER).append(LINE_SEP);
            }
            for (String line : msg.split(LINE_SEP)) {
                sb.append(LEFT_BORDER).append(line).append(LINE_SEP);
            }
            sb.append(BOTTOM_BORDER);
        } else {
            if (head != null) {
                for (String aHead : head) {
                    sb.append(aHead).append(LINE_SEP);
                }
            }
            sb.append(msg);
        }
        printMsgSingleTag(type, tag, sb.toString());
    }

    private static void printBorder(final int type, final String tag, boolean isTop) {
        if (CONFIG.mLogBorderSwitch) {
            Log.println(type, tag, isTop ? TOP_BORDER : BOTTOM_BORDER);
        }
    }

    private static void printHead(final int type, final String tag, final String[] head) {
        if (head != null) {
            for (String aHead : head) {
                Log.println(type, tag, CONFIG.mLogBorderSwitch ? LEFT_BORDER + aHead : aHead);
            }
            if (CONFIG.mLogBorderSwitch) {
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

    private static void printMsgSingleTag(final int type, final String tag, final String msg) {
        int len = msg.length();
        int countOfSub = len / MAX_LEN;
        if (countOfSub > 0) {
            if (CONFIG.mLogBorderSwitch) {
                handleBorderLog(type, tag, msg, len, countOfSub);
            } else {
                int index = 0;
                for (int i = 0; i < countOfSub; i++) {
                    Log.println(type, tag, msg.substring(index, index + MAX_LEN));
                    index += MAX_LEN;
                }
                if (index != len) {
                    Log.println(type, tag, msg.substring(index, len));
                }
            }
        } else {
            Log.println(type, tag, msg);
        }
    }

    private static void handleBorderLog(int type, String tag, String msg, int len, int countOfSub) {
        Log.println(type, tag, msg.substring(0, MAX_LEN) + LINE_SEP + BOTTOM_BORDER);
        int index = MAX_LEN;
        for (int i = 1; i < countOfSub; i++) {
            Log.println(type, tag, PLACEHOLDER + LINE_SEP + TOP_BORDER + LINE_SEP
                    + LEFT_BORDER + msg.substring(index, index + MAX_LEN)
                    + LINE_SEP + BOTTOM_BORDER);
            index += MAX_LEN;
        }
        if (index != len) {
            Log.println(type, tag, PLACEHOLDER + LINE_SEP + TOP_BORDER + LINE_SEP
                    + LEFT_BORDER + msg.substring(index, len));
        }
    }

    private static void printSubMsg(final int type, final String tag, final String msg) {
        if (!CONFIG.mLogBorderSwitch) {
            Log.println(type, tag, msg);
            return;
        }
        String[] lines = msg.split(LINE_SEP);
        for (String line : lines) {
            Log.println(type, tag, LEFT_BORDER + line);
        }
    }

    public static void printSubMsg1(final int type, final String tag, final String msg) {
        if (!CONFIG.mLogBorderSwitch) {
            return;
        }
        String[] lines = msg.split(LINE_SEP);
        for (String line : lines) {
            Log.println(type, tag, LEFT_BORDER + line);
        }
    }

    private static synchronized void print2File(final int type, final String tag, final String msg) {
        Date now = new Date(System.currentTimeMillis());
        final Format dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS ", Locale.US);
        String format = dateFormat.format(now);
        String date = format.substring(0, 5);
        String time = format.substring(6);
        final String fullPath =
                (CONFIG.mDir == null ? CONFIG.mDefaultDir : CONFIG.mDir)
                        + CONFIG.mFilePrefix + "-" + date + ".txt";
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

    private static void input2File(final String input, final String filePath) {
        if (sExecutor == null) {
            sExecutor = new ThreadPoolExecutor(3, 10,
                    5, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(128),
                    r -> {
                        Thread thread = new Thread(r, "LogUtils executor service");
                        thread.setPriority(Thread.MIN_PRIORITY);
                        thread.setDaemon(true);
                        return thread;
                    });
        }
        Future<Boolean> submit = sExecutor.submit(() -> {
            BufferedWriter bw = null;
            try (BufferedWriter bwTemp = new BufferedWriter(new FileWriter(filePath, true))) {
                bw = bwTemp;
                bw.write(input);
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
                    Log.e(TAG, "input2File: ", e);
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

    @SuppressWarnings("UnusedReturnValue")
    public static class Config {
        /**
         * The default storage directory of log.
         */
        private String mDefaultDir;
        /**
         * The storage directory of log.
         */
        private String mDir;
        /**
         * The file prefix of log.
         */
        private String mFilePrefix = "util";
        /**
         * The switch of log.
         */
        private boolean mLogSwitch = true;
        /**
         * The logcat's switch of log.
         */
        private boolean mLog2ConsoleSwitch = true;
        /**
         * The global tag of log.
         */
        private String mGlobalTag = null;
        /**
         * The global tag is space.
         */
        private boolean mTagIsSpace = true;
        /**
         * The head's switch of log.
         */
        private boolean mLogHeadSwitch = true;
        /**
         * The file's switch of log.
         */
        private boolean mLog2FileSwitch = false;
        /**
         * The border's switch of log.
         */
        private boolean mLogBorderSwitch = true;
        /**
         * The single tag of log.
         */
        private boolean mSingleTagSwitch = true;
        /**
         * The console's filter of log.
         */
        private int mConsoleFilter = VERBOSE;
        /**
         * The file's filter of log.
         */
        private int mFileFilter = VERBOSE;
        /**
         * The stack's deep of log.
         */
        private int mStackDeep = 1;
        /**
         * The stack's offset of log.
         */
        private int mStackOffset = 0;

        private Config() {
            if (mDefaultDir != null) {
                return;
            }
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    && Utils.getApp().getExternalCacheDir() != null) {
                mDefaultDir = Utils.getApp().getExternalCacheDir() + FILE_SEP + "log" + FILE_SEP;
            } else {
                mDefaultDir = Utils.getApp().getCacheDir() + FILE_SEP + "log" + FILE_SEP;
            }
        }

        public Config setLogSwitch(final boolean logSwitch) {
            mLogSwitch = logSwitch;
            return this;
        }

        public Config setConsoleSwitch(final boolean consoleSwitch) {
            mLog2ConsoleSwitch = consoleSwitch;
            return this;
        }

        public Config setGlobalTag(final String tag) {
            if (isSpace(tag)) {
                mGlobalTag = "";
                mTagIsSpace = true;
            } else {
                mGlobalTag = tag;
                mTagIsSpace = false;
            }
            return this;
        }

        public Config setLogHeadSwitch(final boolean logHeadSwitch) {
            mLogHeadSwitch = logHeadSwitch;
            return this;
        }

        public Config setLog2FileSwitch(final boolean log2FileSwitch) {
            mLog2FileSwitch = log2FileSwitch;
            return this;
        }

        public Config setDir(final String dir) {
            if (isSpace(dir)) {
                mDir = null;
            } else {
                mDir = dir.endsWith(FILE_SEP) ? dir : dir + FILE_SEP;
            }
            return this;
        }

        public Config setDir(final File dir) {
            mDir = dir == null ? null : dir.getAbsolutePath() + FILE_SEP;
            return this;
        }

        public Config setFilePrefix(final String filePrefix) {
            if (isSpace(filePrefix)) {
                mFilePrefix = "util";
            } else {
                mFilePrefix = filePrefix;
            }
            return this;
        }

        public Config setBorderSwitch(final boolean borderSwitch) {
            mLogBorderSwitch = borderSwitch;
            return this;
        }

        public Config setSingleTagSwitch(final boolean singleTagSwitch) {
            mSingleTagSwitch = singleTagSwitch;
            return this;
        }

        public Config setConsoleFilter(@TYPE final int consoleFilter) {
            mConsoleFilter = consoleFilter;
            return this;
        }

        public Config setFileFilter(@TYPE final int fileFilter) {
            mFileFilter = fileFilter;
            return this;
        }

        public Config setStackDeep(@IntRange(from = 1) final int stackDeep) {
            mStackDeep = stackDeep;
            return this;
        }

        public Config setStackOffset(@IntRange(from = 0) final int stackOffset) {
            mStackOffset = stackOffset;
            return this;
        }

        @Override
        public String toString() {
            return "switch: " + mLogSwitch
                    + LINE_SEP + "console: " + mLog2ConsoleSwitch
                    + LINE_SEP + "tag: " + (mTagIsSpace ? "null" : mGlobalTag)
                    + LINE_SEP + "head: " + mLogHeadSwitch
                    + LINE_SEP + "file: " + mLog2FileSwitch
                    + LINE_SEP + "dir: " + (mDir == null ? mDefaultDir : mDir)
                    + LINE_SEP + "filePrefix: " + mFilePrefix
                    + LINE_SEP + "border: " + mLogBorderSwitch
                    + LINE_SEP + "singleTag: " + mSingleTagSwitch
                    + LINE_SEP + "consoleFilter: " + T[mConsoleFilter - VERBOSE]
                    + LINE_SEP + "fileFilter: " + T[mFileFilter - VERBOSE]
                    + LINE_SEP + "stackDeep: " + mStackDeep
                    + LINE_SEP + "mStackOffset: " + mStackOffset;
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
