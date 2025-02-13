package com.yunyou.common.utils.time;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 *
 * @author yunyou
 * @version 2017-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String[] PATTERNS = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM",
            "yyyyMMddHHmmss", "yyyyMMdd"};

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 得到当前小时字符串 格式（dd）
     */
    public static String getHours(Date date) {
        return formatDate(date, "HH");
    }

    /**
     * 得到当前分钟字符串 格式（dd）
     */
    public static String getMinus(Date date) {
        return formatDate(date, "mm");
    }

    /**
     * 得到当前秒数字符串 格式（dd）
     */
    public static String getSeconds(Date date) {
        return formatDate(date, "ss");
    }

    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
     * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), PATTERNS);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     */
    public static long pastDays(Date date) {
        return (System.currentTimeMillis() - date.getTime()) / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     */
    public static long pastHour(Date date) {
        return (System.currentTimeMillis() - date.getTime()) / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     */
    public static long pastMinutes(Date date) {
        return (System.currentTimeMillis() - date.getTime()) / (60 * 1000);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before 开启日期
     * @param after  结束日期
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        return (after.getTime() - before.getTime()) / (1000 * 60 * 60 * 24);
    }

    /**
     * 字符串时间转LONG
     */
    public static long string2long(String sdate) throws ParseException {
        if (sdate.length() < 11) {
            sdate = sdate + " 00:00:00";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
        Date dt2 = sdf.parse(sdate);
        return dt2.getTime() / 1000;
    }

    /**
     * LONG时间转字符串
     */
    public static String long2string(long ldate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
        //前面的ldate是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        Date dt = new Date(ldate * 1000);
        //得到精确到秒的表示
        String sDateTime = sdf.format(dt);
        if (sDateTime.endsWith("00:00:00")) {
            sDateTime = sDateTime.substring(0, 10);
        }
        return sDateTime;
    }

    public static String stringFormat(String dateString, String pattern) {
        return formatDate(parseDate(dateString), pattern);
    }

}
