package com.io.sdchain.utils;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author xiey
 * @date created at 2018/3/14 16:34
 * @package com.io.sdchain.utils
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public class TimeConverterUtil {

    /**
     * local time ---> UTC time
     *
     * @return
     */
    public static String Local2UTC() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = sdf.format(new Date());
        return gmtTime;
    }

    /**
     * UTC time ---> local time
     *
     * @param time time
     * @return
     */
    public static String utc2Local(String time) {
        if (time != null) {
            if (time.endsWith("Z")) {
                return utc2Local(time, "utc");
            } else {
                return local(time);
            }
        }
        return "";
    }

    public static String utc2Local(String utcTime, String utc) {
        SimpleDateFormat utcFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");//UTC time style
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            Logger.e("Received date format[yyyy-MM-dd'T'HH:mm:ss.SSS'Z']Incorrect, original return");
            return utcTime;
        }
        SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//local time style
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }

    public static String local(String localTime) {
        SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//local time style
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = localFormater.parse(localTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTimeStr = localFormater.format(gpsUTCDate.getTime());
        return localTimeStr;
    }

    /**
     * 2018-05-11T03:05:00+00:00
     *
     * @param utcTime
     * @return
     */
    public static String utc2Local2(String utcTime) {
        utcTime = utcTime.substring(0, utcTime.indexOf('+'));
        SimpleDateFormat utcFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");//UTC time style
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            Logger.e("Received date format[yyyy-MM-dd'T'HH:mm:ss.SSS'Z']Incorrect, original return");
            return utcTime;
        }
        SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//local time style
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }

    /**
     * The timestamp is converted to a date format string
     *
     * @param seconds   A string that is accurate to seconds
     * @return
     */
    public static String timeStamp2Date(String seconds) {
        String format = "yyyy-MM-dd HH:mm:ss";
        return timeStamp2Date(seconds, format);
    }

    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }
}
