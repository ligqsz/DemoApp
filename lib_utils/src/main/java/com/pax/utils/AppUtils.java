package com.pax.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ligq
 * @date 2018/10/29
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class AppUtils {
    public static final String TAG = AppUtils.class.getSimpleName();
    private static final char[] HEX_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Register the status of application changed listener.
     *
     * @param obj      The object.
     * @param listener The status of application changed listener
     */
    public static void registerAppStatusChangedListener(@NonNull final Object obj,
                                                        @NonNull final Utils.OnAppStatusChangedListener listener) {
        Utils.getActivityLifecycle().addListener(obj, listener);
    }

    /**
     * Unregister the status of application changed listener.
     *
     * @param obj The object.
     */
    public static void unregisterAppStatusChangedListener(@NonNull final Object obj) {
        Utils.getActivityLifecycle().removeListener(obj);
    }

    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param filePath The path of file.
     */
    public static void installApp(final String filePath) {
        installApp(getFileByPath(filePath));
    }

    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param file The file.
     */
    public static void installApp(final File file) {
        if (!isFileExists(file)) {
            return;
        }
        Utils.getApp().startActivity(getInstallAppIntent(file, true));
    }

    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param activity    The activity.
     * @param filePath    The path of file.
     * @param requestCode If &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void installApp(final Activity activity,
                                  final String filePath,
                                  final int requestCode) {
        installApp(activity, getFileByPath(filePath), requestCode);
    }

    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param activity    The activity.
     * @param file        The file.
     * @param requestCode If &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void installApp(final Activity activity,
                                  final File file,
                                  final int requestCode) {
        if (!isFileExists(file)) {
            return;
        }
        activity.startActivityForResult(getInstallAppIntent(file), requestCode);
    }

    /**
     * Uninstall the app.
     *
     * @param packageName The name of the package.
     */
    public static void uninstallApp(final String packageName) {
        if (isSpace(packageName)) {
            return;
        }
        Utils.getApp().startActivity(getUninstallAppIntent(packageName, true));
    }

    /**
     * Uninstall the app.
     *
     * @param activity    The activity.
     * @param packageName The name of the package.
     * @param requestCode If &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void uninstallApp(final Activity activity,
                                    final String packageName,
                                    final int requestCode) {
        if (isSpace(packageName)) {
            return;
        }
        activity.startActivityForResult(getUninstallAppIntent(packageName), requestCode);
    }

    /**
     * Return whether the app is installed.
     *
     * @param packageName The name of the package.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppInstalled(@NonNull final String packageName) {
        return !isSpace(packageName)
                && Utils.getApp().getPackageManager().getLaunchIntentForPackage(packageName) != null;
    }

    /**
     * Return whether the app is installed.
     *
     * @param action   The Intent action, such as ACTION_VIEW.
     * @param category The desired category.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppInstalled(@NonNull final String action,
                                         @NonNull final String category) {
        Intent intent = new Intent(action);
        intent.addCategory(category);
        PackageManager pm = Utils.getApp().getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, 0);
        return info != null;
    }

    /**
     * Return whether application is foreground.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppForeground() {
        return Utils.isAppForeground();
    }

    /**
     * Return whether application is foreground.
     * <p>Target APIs greater than 21 must hold
     * {@code <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" />}</p>
     *
     * @param packageName The name of the package.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppForeground(@NonNull final String packageName) {
        return !isSpace(packageName) && packageName.equals(getForegroundProcessName());
    }

    /**
     * Launch the application.
     *
     * @param packageName The name of the package.
     */
    public static void launchApp(final String packageName) {
        if (isSpace(packageName)) {
            return;
        }
        Utils.getApp().startActivity(getLaunchAppIntent(packageName, true));
    }

    /**
     * Launch the application.
     *
     * @param activity    The activity.
     * @param packageName The name of the package.
     * @param requestCode If &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void launchApp(final Activity activity,
                                 final String packageName,
                                 final int requestCode) {
        if (isSpace(packageName)) {
            return;
        }
        activity.startActivityForResult(getLaunchAppIntent(packageName), requestCode);
    }

    /**
     * Relaunch the application.
     */
    public static void relaunchApp() {
        PackageManager packageManager = Utils.getApp().getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(Utils.getApp().getPackageName());
        if (intent == null) {
            return;
        }
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        Utils.getApp().startActivity(mainIntent);
        Process.killProcess(Process.myPid());
    }

    /**
     * Launch the application's details settings.
     */
    public static void launchAppDetailsSettings() {
        launchAppDetailsSettings(Utils.getApp().getPackageName());
    }

    /**
     * Launch the application's details settings.
     *
     * @param packageName The name of the package.
     */
    public static void launchAppDetailsSettings(final String packageName) {
        if (isSpace(packageName)) {
            return;
        }
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        Utils.getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * Exit the application.
     */
    public static void exitApp() {
        List<Activity> activityList = Utils.getActivityList();
        // remove from top
        for (int i = activityList.size() - 1; i >= 0; --i) {
            Activity activity = activityList.get(i);
            // sActivityList remove the index activity at onActivityDestroyed
            activity.finish();
        }
        Process.killProcess(Process.myPid());
    }

    /**
     * Return the application's icon.
     *
     * @return the application's icon
     */
    public static Drawable getAppIcon() {
        return getAppIcon(Utils.getApp().getPackageName());
    }

    /**
     * Return the application's icon.
     *
     * @param packageName The name of the package.
     * @return the application's icon
     */
    public static Drawable getAppIcon(final String packageName) {
        if (isSpace(packageName)) {
            return null;
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getAppIcon: ", e);
            return null;
        }
    }

    /**
     * Return the application's package name.
     *
     * @return the application's package name
     */
    public static String getAppPackageName() {
        return Utils.getApp().getPackageName();
    }

    /**
     * Return the application's name.
     *
     * @return the application's name
     */
    public static String getAppName() {
        return getAppName(Utils.getApp().getPackageName());
    }

    /**
     * Return the application's name.
     *
     * @param packageName The name of the package.
     * @return the application's name
     */
    public static String getAppName(final String packageName) {
        if (isSpace(packageName)) {
            return "";
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getAppName: ", e);
            return "";
        }
    }

    /**
     * Return the application's path.
     *
     * @return the application's path
     */
    public static String getAppPath() {
        return getAppPath(Utils.getApp().getPackageName());
    }

    /**
     * Return the application's path.
     *
     * @param packageName The name of the package.
     * @return the application's path
     */
    public static String getAppPath(final String packageName) {
        if (isSpace(packageName)) {
            return "";
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getAppPath: ", e);
            return "";
        }
    }

    /**
     * Return the application's version name.
     *
     * @return the application's version name
     */
    public static String getAppVersionName() {
        return getAppVersionName(Utils.getApp().getPackageName());
    }

    /**
     * Return the application's version name.
     *
     * @param packageName The name of the package.
     * @return the application's version name
     */
    public static String getAppVersionName(final String packageName) {
        if (isSpace(packageName)) {
            return "";
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getAppVersionName: ", e);
            return "";
        }
    }

    /**
     * Return the application's version code.
     *
     * @return the application's version code
     */
    public static int getAppVersionCode() {
        return getAppVersionCode(Utils.getApp().getPackageName());
    }

    /**
     * Return the application's version code.
     *
     * @param packageName The name of the package.
     * @return the application's version code
     */
    public static int getAppVersionCode(final String packageName) {
        if (isSpace(packageName)) {
            return -1;
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getAppVersionCode: ", e);
            return -1;
        }
    }

    /**
     * Return the application's signature.
     *
     * @return the application's signature
     */
    public static Signature[] getAppSignature() {
        return getAppSignature(Utils.getApp().getPackageName());
    }

    /**
     * Return the application's signature.
     *
     * @param packageName The name of the package.
     * @return the application's signature
     */
    public static Signature[] getAppSignature(final String packageName) {
        if (isSpace(packageName)) {
            return new Signature[0];
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return pi == null ? null : pi.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getAppSignature: ", e);
            return new Signature[0];
        }
    }

    /**
     * Return the application's signature for SHA1 value.
     *
     * @return the application's signature for SHA1 value
     */
    public static String getAppSignatureSHA1() {
        return getAppSignatureSHA1(Utils.getApp().getPackageName());
    }

    /**
     * Return the application's signature for SHA1 value.
     *
     * @param packageName The name of the package.
     * @return the application's signature for SHA1 value
     */
    public static String getAppSignatureSHA1(final String packageName) {
        if (isSpace(packageName)) {
            return "";
        }
        Signature[] signature = getAppSignature(packageName);
        if (signature == null || signature.length <= 0) {
            return "";
        }
        return encryptSHA1ToString(signature[0].toByteArray()).
                replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }

    /**
     * Return the application's information.
     * <ul>
     * <li>name of package</li>
     * <li>icon</li>
     * <li>name</li>
     * <li>path of package</li>
     * <li>version name</li>
     * <li>version code</li>
     * <li>is system</li>
     * </ul>
     *
     * @return the application's information
     */
    public static AppInfo getAppInfo() {
        return getAppInfo(Utils.getApp().getPackageName());
    }

    /**
     * Return the application's information.
     * <ul>
     * <li>name of package</li>
     * <li>icon</li>
     * <li>name</li>
     * <li>path of package</li>
     * <li>version name</li>
     * <li>version code</li>
     * <li>is system</li>
     * </ul>
     *
     * @param packageName The name of the package.
     * @return 当前应用的 AppInfo
     */
    public static AppInfo getAppInfo(final String packageName) {
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return getBean(pm, pi);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getAppInfo: ", e);
            return null;
        }
    }

    /**
     * Return the applications' information.
     *
     * @return the applications' information
     */
    public static List<AppInfo> getAppsInfo() {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = Utils.getApp().getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo pi : installedPackages) {
            AppInfo ai = getBean(pm, pi);
            if (ai == null) {
                continue;
            }
            list.add(ai);
        }
        return list;
    }

    private static String encryptSHA1ToString(final byte[] data) {
        return bytes2HexString(encryptSHA1(data));
    }

    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        int len = bytes.length;
        if (len <= 0) {
            return "";
        }
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[bytes[i] >> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    private static byte[] encryptSHA1(final byte[] data) {
        if (data == null || data.length <= 0) {
            return new byte[0];
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "encryptSHA1: ", e);
            return new byte[0];
        }
    }

    private static Intent getLaunchAppIntent(final String packageName) {
        return getLaunchAppIntent(packageName, false);
    }

    private static Intent getLaunchAppIntent(final String packageName, final boolean isNewTask) {
        Intent intent = Utils.getApp().getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return null;
        }
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

    private static Intent getUninstallAppIntent(final String packageName) {
        return getUninstallAppIntent(packageName, false);
    }

    @SuppressWarnings("SameParameterValue")
    private static Intent getUninstallAppIntent(final String packageName, final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

    private static Intent getInstallAppIntent(final File file) {
        return getInstallAppIntent(file, false);
    }

    @SuppressWarnings("SameParameterValue")
    private static Intent getInstallAppIntent(final File file, final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            data = Uri.fromFile(file);
        } else {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String authority = Utils.getApp().getPackageName() + ".utilcode.provider";
            data = FileProvider.getUriForFile(Utils.getApp(), authority, file);
        }
        intent.setDataAndType(data, type);
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

    private static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }

    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
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

    private static String getForegroundProcessName() {
        ActivityManager am =
                (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {
            return null;
        }
        List<ActivityManager.RunningAppProcessInfo> pInfo = am.getRunningAppProcesses();
        if (pInfo != null && !pInfo.isEmpty()) {
            for (ActivityManager.RunningAppProcessInfo aInfo : pInfo) {
                if (aInfo.importance
                        == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return aInfo.processName;
                }
            }
        }
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            PackageManager pm = Utils.getApp().getPackageManager();
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            List<ResolveInfo> list =
                    pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            Log.i(TAG, list.toString());
            if (list.isEmpty()) {
                Log.i("ProcessUtils",
                        "getForegroundProcessName: noun of access to usage information.");
                return "";
            }
            try {// Access to usage information.
                ApplicationInfo info =
                        pm.getApplicationInfo(Utils.getApp().getPackageName(), 0);
                AppOpsManager aom =
                        (AppOpsManager) Utils.getApp().getSystemService(Context.APP_OPS_SERVICE);
                if (aom != null) {
                    if (aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                            info.uid,
                            info.packageName) != AppOpsManager.MODE_ALLOWED) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Utils.getApp().startActivity(intent);
                    }
                    if (aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                            info.uid,
                            info.packageName) != AppOpsManager.MODE_ALLOWED) {
                        Log.i("ProcessUtils",
                                "getForegroundProcessName: refuse to device usage stats.");
                        return "";
                    }
                }
                UsageStatsManager usageStatsManager = (UsageStatsManager) Utils.getApp()
                        .getSystemService(Context.USAGE_STATS_SERVICE);
                List<UsageStats> usageStatsList = null;
                if (usageStatsManager != null) {
                    long endTime = System.currentTimeMillis();
                    long beginTime = endTime - 86400000 * 7;
                    usageStatsList = usageStatsManager
                            .queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                                    beginTime, endTime);
                }
                if (usageStatsList == null || usageStatsList.isEmpty()) {
                    return null;
                }
                UsageStats recentStats = null;
                for (UsageStats usageStats : usageStatsList) {
                    if (recentStats == null
                            || usageStats.getLastTimeUsed() > recentStats.getLastTimeUsed()) {
                        recentStats = usageStats;
                    }
                }
                return recentStats == null ? null : recentStats.getPackageName();
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "getForegroundProcessName: ", e);
            }
        }
        return "";
    }

    private static AppInfo getBean(final PackageManager pm, final PackageInfo pi) {
        if (pm == null || pi == null) {
            return null;
        }
        ApplicationInfo ai = pi.applicationInfo;
        String packageName = pi.packageName;
        String name = ai.loadLabel(pm).toString();
        Drawable icon = ai.loadIcon(pm);
        String packagePath = ai.sourceDir;
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
        return new AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem);
    }

    /**
     * The application's information.
     */
    public static class AppInfo {

        private String packageName;
        private String name;
        private Drawable icon;
        private String packagePath;
        private String versionName;
        private int versionCode;
        private boolean isSystem;

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(final Drawable icon) {
            this.icon = icon;
        }

        public boolean isSystem() {
            return isSystem;
        }

        public void setSystem(final boolean isSystem) {
            this.isSystem = isSystem;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(final String packageName) {
            this.packageName = packageName;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getPackagePath() {
            return packagePath;
        }

        public void setPackagePath(final String packagePath) {
            this.packagePath = packagePath;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(final int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(final String versionName) {
            this.versionName = versionName;
        }

        public AppInfo(String packageName, String name, Drawable icon, String packagePath,
                       String versionName, int versionCode, boolean isSystem) {
            this.setName(name);
            this.setIcon(icon);
            this.setPackageName(packageName);
            this.setPackagePath(packagePath);
            this.setVersionName(versionName);
            this.setVersionCode(versionCode);
            this.setSystem(isSystem);
        }

        @Override
        public String toString() {
            return "pkg name: " + getPackageName() +
                    "\napp icon: " + getIcon() +
                    "\napp name: " + getName() +
                    "\napp path: " + getPackagePath() +
                    "\napp v name: " + getVersionName() +
                    "\napp v code: " + getVersionCode() +
                    "\nis system: " + isSystem();
        }
    }
}
