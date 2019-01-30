/**
 * Copyright(c)2015
 * All right reserved.
 */
package com.io.sdchain.utils;

/**
 * Copyright(c)2015
 * All right reserved.
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.EditText;

import com.io.sdchain.R;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author rulang1
 */
public final class Common {
    /**
     * @param context
     * @return screen width
     */
    public static int getWidth(Context context) {
        DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
        return dm2.widthPixels;
    }

    /**
     * @param context
     * @return screen height
     */
    public static int getHeight(Context context) {
        DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
        return dm2.heightPixels;
    }

    /**
     * @param context
     * @param dipValue
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * convert sp to its equivalent px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * @param context
     * @return current version
     */
    public static String getVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (NameNotFoundException e1) {
            LogInfo.e(e1.getMessage());
        }
        return "0.0.0";
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (NameNotFoundException e1) {
            LogInfo.e(e1.getMessage());
        }
        return 0;
    }

    public static enum StateNet {
        BadNet, WifiNet, G2G3G4Net
    }

    ;

    /**
     * @param context
     * @return new state
     */
    public static StateNet netState(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            NetworkInfo.State wifi = cManager.getNetworkInfo(
                    ConnectivityManager.TYPE_WIFI).getState();
            if (wifi == NetworkInfo.State.CONNECTED
                    || wifi == NetworkInfo.State.CONNECTING) {
                return StateNet.WifiNet;
            } else {
                return StateNet.G2G3G4Net;
            }
            // return true;
        }
        return StateNet.BadNet;
    }

    /**
     * @param algorithm
     */
    public static SecretKey createSecretKey(String algorithm) {
        KeyGenerator keygen;
        SecretKey deskey = null;
        try {
            keygen = KeyGenerator.getInstance(algorithm);
            deskey = keygen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            LogInfo.e(e.getMessage());
        }
        return deskey;
    }

    /**
     */
    public static String encryptToDES(String info) {
        String skey = "12345678";
        SecretKey key = new SecretKeySpec(skey.getBytes(), "DES");
        String Algorithm = "DES";
        SecureRandom sr = new SecureRandom();
        byte[] cipherByte = null;
        try {
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, key, sr);
            cipherByte = c1.doFinal(info.getBytes());
        } catch (Exception e) {
            LogInfo.e(e.getMessage());
        }
        return byte2hex(cipherByte);
    }

    public String decryptByDES(String sInfo) {
        String skey = "12345678";
        SecretKey key = new SecretKeySpec(skey.getBytes(), "DES");
        String Algorithm = "DES";
        SecureRandom sr = new SecureRandom();
        byte[] cipherByte = null;
        try {
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, key, sr);
            cipherByte = c1.doFinal(hex2byte(sInfo));
        } catch (Exception e) {
            LogInfo.e(e.getMessage());
        }
        // return byte2hex(cipherByte);
        return new String(cipherByte);
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    public byte[] hex2byte(String hex) {
        byte[] ret = new byte[8];
        byte[] tmp = hex.getBytes();
        for (int i = 0; i < 8; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");
            s = s.replaceAll("[.]$", "");
        }
        return s;
    }

    public static String dealUserName(Context context, String s) {
        String name = "";
        if (!TextUtils.isEmpty(s)) {
            int l = s.length();
            if (l > 2) {
                name = s.substring(0, 1) + "***" + s.substring(l - 1);
            } else {
                name = context.getString(R.string.info416);
            }
        }
        return name;
    }

    public static String getDeviceIdIMEI(Context context) {
        String name = "";
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = telephonyManager.getDeviceId();
        return IMEI;
    }

    public static String getSiginValue() {
        return "GuJinSuoIL6pvSsve9P0l8JswjcP4w";
    }

    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    public static String getSigin(Map<String, String> map) {
        String mStr = "";
        String[] mSort = new String[map.size()];
        int i = 0;
        for (String string : map.keySet()) {
            mSort[i] = string;
            i++;
        }
        Arrays.sort(mSort, null);
        for (int j = 0; j < mSort.length; j++) {
            if (map.get(mSort[j]) != null) {
                mStr = mStr + mSort[j] + "=" + map.get(mSort[j]) + "&";
            }
        }
        if (mStr.length() > 0)
            return mStr.substring(0, mStr.length() - 1);
        return mStr;
    }

    public static String getStr(EditText view) {
        return view.getText().toString().trim();
    }

    public static int getStatusBarHeight(Activity activity) {
        int statusBarHeight = 0;
        if (activity != null) {
            int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    public static String getTimeDate(long time,Context context){
        // 101156000
        time=time/1000; // ms->s
        if(time/60 < 10 ){
            return  context.getResources().getString(R.string.key000277);
        }else if(time/60 < 60){
            return  (time/60)+context.getString(R.string.key000278);
        }else if(time/3600 < 24) {
            return  (time/3600)+context.getString(R.string.key000279);
        }else if(time/3600/24< 30) {
            return  (time/3600/24)+context.getString(R.string.key000280)   ;
        }else if(time/3600/24/30<12){
            return  (time/3600/24/30)+context.getString(R.string.key000281);
        }else {
            return  (time/3600/24/30/12)+context.getString(R.string.key000282);
        }
    }

}
