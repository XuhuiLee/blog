package com.createarttechnology.blog.util;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * Created by lixuhui on 2018/9/14.
 */
public class TimeUtil {

    public static final long ONE_SEC_LONG = 1000;
    public static final long ONE_MIN_LONG = 60 * ONE_SEC_LONG;
    public static final long ONE_HOUR_LONG = 60 * ONE_MIN_LONG;
    public static final long ONE_DAY_LONG = 24 * ONE_HOUR_LONG;

    public static final long ONE_SEC_INT = 1;
    public static final long ONE_MIN_INT = 60 * ONE_SEC_INT;
    public static final long ONE_HOUR_INT = 60 * ONE_MIN_INT;
    public static final long ONE_DAY_INT = 24 * ONE_HOUR_INT;

    public static String formatYMDHMS(long time) {
        return DateFormatUtils.format(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getTimeStringLong(long time) {
        long diff = System.currentTimeMillis() - time;
        if (diff <= ONE_MIN_LONG) {
            return "刚刚";
        } else if (diff <= ONE_HOUR_LONG) {
            return diff / ONE_MIN_LONG + "分钟前";
        } else if (diff <= ONE_DAY_LONG) {
            return diff / ONE_HOUR_LONG + "小时前";
        } else {
            return formatYMDHMS(time);
        }
    }

    public static String getTimeStringInt(int time) {
        return getTimeStringLong(time * 1000L);
    }


}
