package com.io.sdchain.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.io.sdchain.base.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.io.sdchain.BuildConfig.DEBUG;

/**
 * @author : xiey
 * @project name : sdchian.
 * @package name  : com.io.sdchian.utils.
 * @date : 2018/6/27.
 * @signature : do my best.
 * @explain :
 */
public class FileUtils {
    public static boolean saveBitmap(Bitmap bitmap, String name, String path) {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(path, name + ".jpg");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            if (DEBUG) {
                e.printStackTrace();
            } else {
                return false;
            }
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        BaseApplication.getApplicationInstance().sendBroadcast(intent);
        return true;
    }
}
