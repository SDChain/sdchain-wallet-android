package com.io.sdchain.common;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * @author xiey
 */
public class Pa {


    /**
     * get current application package name
     *
     * @param context
     * @return
     */
    public static String getAppProcessName(Context context) {
        //current application pid
        int pid = android.os.Process.myPid();
        //task manager class
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //traversing all application
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//get current application pid
                return info.processName;//back package
        }
        return "";
    }

    /**
     * get application icon
     *
     * @param context
     * @param packname
     * @return
     */
    public Drawable getAppIcon(Context context, String packname) {
        try {
            //package manager operating manager class
            PackageManager pm = context.getPackageManager();
            //get application info
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get application version
     *
     * @param context
     * @param packname
     * @return
     */
    public String getAppVersion(Context context, String packname) {
        //package manager operating manager class
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, 0);
            return packinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return packname;
    }


    /**
     * get application name
     *
     * @param context
     * @param packname
     * @return
     */
    public String getAppName(Context context, String packname) {
        //package manager operating manager class
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return packname;
    }

    /**
     * get application permission
     *
     * @param context
     * @param packname
     * @return
     */
    public String[] getAllPermissions(Context context, String packname) {
        try {
            //package manager operating manager class
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_PERMISSIONS);
            //get all permission
            return packinfo.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }


    /**
     * get application sign
     *
     * @param context
     * @param packname
     * @return
     */
    public static String getAppSignature(Context context, String packname) {
        try {
            //package manager operating manager class
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
            //get current application sign
            return packinfo.signatures[0].toCharsString();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return packname;
    }

    /**
     * get current show activity name
     *
     * @return
     */
    private static String getCurrentActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }

}
