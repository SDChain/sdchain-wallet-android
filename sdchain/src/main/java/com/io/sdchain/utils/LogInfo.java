/**  
 * 无锡五洲国际电子商务有限公司 
 * Copyright(c)2014 Wuxi WuZhouHui Co.,Ltd. 
 * All right reserved. 
 */
package com.io.sdchain.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class LogInfo {

    private static boolean loggable = true;//BuildConfig.DEBUG;
    private final static String tag = "ccer";
    private static String logfilename = "ccer_File.txt";
    private final static boolean recordable = false;

    /**
     * 
     * @params lvl
     * @params action
     */
    static public void record(String s) {
        if(!recordable)
            return;
        try {
            File root;
            if (loggable) {
                root = Environment.getExternalStorageDirectory();
            }
            else {
                root = new File("/");
            }
            if (root.canWrite()) {
                File gpxfile = new File(root, logfilename);
                if (!gpxfile.exists())
                    gpxfile.createNewFile();
                FileWriter gpxwriter = new FileWriter(gpxfile, true);
                BufferedWriter out = new BufferedWriter(gpxwriter);
                out.write(s + "\n");
                out.close();
            }
        } catch (IOException e) {
            LogInfo.e("Could not write file " + e.getMessage());
        }
    }

    public static void e(String msg) {
        if (loggable) {
            Log.e(tag, msg);
            record(msg);
        }
    }
}
