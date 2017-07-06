package com.coder.kzxt.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期时间工具类
 */
public class DateUtil
{
    public final static long DIS_INTERVAL = 300;

    public static String getDateStr(long timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(new Date(timestamp * 1000));
    }

    public static String getDateStr2(long timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(timestamp * 1000));
    }

    public static String getDateStrDot(long timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        return sdf.format(new Date(timestamp * 1000));
    }

    public static String getDateOrderStrDot(long timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return sdf.format(new Date(timestamp * 1000));
    }

    public static String getDateStrText(long timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(new Date(timestamp * 1000));
    }

    public static String getDateYear(long timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(new Date(timestamp * 1000));
    }

    public static String getDateMonth(long timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return sdf.format(new Date(timestamp * 1000));
    }

    public static String getDateDay(long timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(new Date(timestamp * 1000));
    }

    public static String getDateMouthOrDay(long timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
        return sdf.format(new Date(timestamp * 1000));
    }

    public static String getDateString(long timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(new Date(timestamp * 1000));
    }

    public static String getDateTime(long timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date(timestamp * 1000));
    }

    public static String getDateAndSecond(long timestamp)
    {
        if (timestamp < 1) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp * 1000));
    }

    public static String getDateAndMinute(long timestamp)
    {
        if (timestamp < 1) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date(timestamp * 1000));
    }

    public static String getDateSecond(long timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(new Date(timestamp * 1000));
    }

    public static String getHourSecond(long timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date(timestamp * 1000));
    }

    public static String getHourMin(long timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date(timestamp * 1000));
    }


    public static boolean LongInterval(long current, long last)
    {
        return (current - last) > DIS_INTERVAL ? true : false;
    }

    /**
     * @param timestamp 毫秒数
     * @return 00:00:00
     */
    public static String getStandardTime(long timestamp)
    {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date = new Date(timestamp * 1000);
        sdf.format(date);
        return sdf.format(date);

    }

    /**
     * 获取MM和dd
     *
     * @param timestamp
     * @return MM-dd
     */
    public static String getMonthAndDay(long timestamp)
    {
        return getDateMonth(timestamp) + "-" + getDateDay(timestamp);
    }

    /**
     * 获取年月日
     *
     * @param timestamp
     * @return yyyy-mm-dd
     */
    public static String getYearMonthAndDay(long timestamp)
    {
        return getDateYear(timestamp) + "-" + getDateMonth(timestamp) + "-" + getDateDay(timestamp);
    }

    /**
     * 转毫秒
     */
    public static Long getLongTimes(String times)
    {
        long millionSeconds = 0;
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            millionSeconds = sdf.parse(times).getTime();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return millionSeconds;

    }

    // 字符串类型日期转化成date类型
    public static Date strToDate(String style, String date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        try
        {
            return formatter.parse(date);
        } catch (ParseException e)
        {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String dateToStr(String style, Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(date);
    }

    /**
     * 秒转换成时分秒
     *
     * @param second
     * @return
     */
    public static String cal(int second)
    {
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600)
        {
            h = second / 3600;
            if (temp != 0)
            {
                if (temp > 60)
                {
                    d = temp / 60;
                    if (temp % 60 != 0)
                    {
                        s = temp % 60;
                    }
                } else
                {
                    s = temp;
                }
            }
        } else
        {
            d = second / 60;
            if (second % 60 != 0)
            {
                s = second % 60;
            }
        }
        if (h == 0)
        {
            return d + ":" + s;
        } else
        {
            return h + ":" + d + ":" + s;
        }

    }

    /**
     * @param second
     * @return 返回视频时间总长
     */
    public static String formatSecond(int second)
    {
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String strTemp = null;
        if (0 != hh)
        {
            strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else
        {
            strTemp = String.format("%02d:%02d", mm, ss);
        }
        return strTemp;
    }


    /**
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatDuring(long mss)
    {
        long days = mss / (1000 * 60 * 60 * 24); // 天
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60); // 小时
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);// 分钟
        long seconds = (mss % (1000 * 60)) / 1000;// 秒
        if (days == 0)
        {
            return hours + "小时 " + minutes + "分钟 ";
        } else
        {
            return days + "天" + hours + "小时 " + minutes + "分钟 ";
        }

    }

    /**
     * @param begin 时间段的开始
     * @param end   时间段的结束
     * @return 输入的两个Date类型数据之间的时间间格用* days * hours * minutes * seconds的格式展示
     * @author fy.zhang
     */
    public static String formatDuring(Date begin, Date end)
    {
        return formatDuring(end.getTime() - begin.getTime());
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 设置的需要判断的时间 //格式如2012-09-08
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */

    public static String getWeek(Long pTime)
    {
        String Week = "星期";
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        String data = format.format(new Date(pTime * 1000));
        Calendar c = Calendar.getInstance();
        try
        {
            c.setTime(format.parse(data));

        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
        {
            Week += "日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
        {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)
        {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY)
        {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
        {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
        {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
        {
            Week += "六";
        }
        return data + "  " + Week;
    }


    public static String getWeekSimple(Long pTime)
    {
        String Week = "星期";
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        String data = format.format(new Date(pTime * 1000));
        Calendar c = Calendar.getInstance();
        try
        {
            c.setTime(format.parse(data));

        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
        {
            Week += "日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
        {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)
        {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY)
        {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
        {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
        {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
        {
            Week += "六";
        }
        return Week;
    }


    /**
     * @param timeString
     * @return 大于24小时显示日期
     */
    public static String getDayBef(long timeString)
    {
        long time = timeString * 1000L;
        long now = System.currentTimeMillis();
        long t = now - time;

        int minutes = (int) (t / (60 * 1000L));
        if (minutes > 0 && minutes < 60)
        {
            return minutes + "分钟前";
        }
        if (minutes == 0)
        {
            return "1分钟前";
        }
        int hour = (int) (t / (60 * 60 * 1000L));
        if (hour > 0 && hour < 24)
        {
            return hour + "小时前";
        } else
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            return sdf.format(new Date(timeString * 1000));
        }
    }


    /**
     * 获取距离时间
     *
     * @param timeString
     * @return
     */
    public static String getDistanceTime(String timeString)
    {
        if (TextUtils.isEmpty(timeString))
        {
            return "";
        }
        long time = Long.parseLong(timeString) * 1000L;
        long now = System.currentTimeMillis();
        long t = now - time;
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        int yearInt = Integer.valueOf(sdfYear.format(new Date(Long.valueOf(timeString) * 1000)));
        SimpleDateFormat sdfMouth = new SimpleDateFormat("MM");
        int mouthInt = Integer.valueOf(sdfMouth.format(new Date(Long.valueOf(timeString) * 1000)));

        // Log.i("data", "---time=" + time + ",---now=" + now + "----t=" + t);
        int year = 0;
        if (judgeYear(yearInt))
        {
            year = (int) (t / (60 * 60 * 24 * 366 * 1000L));
        } else
        {
            year = (int) (t / (60 * 60 * 24 * 365 * 1000L));
        }
        if (year > 0)
        {
            return year + "年前";
            // return getDateString(Long.valueOf(timeString)).substring(0, 10);
        }
        int month = 0;
        switch (mouthInt)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                month = (int) (t / (60 * 60 * 24 * 31 * 1000L));
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                month = (int) (t / (60 * 60 * 24 * 30 * 1000L));
                break;
            case 2:
                if (judgeYear(yearInt))
                {
                    month = (int) (t / (60 * 60 * 24 * 29 * 1000L));
                } else
                {
                    month = (int) (t / (60 * 60 * 24 * 28 * 1000L));
                }
                break;
        }
        if (month > 0)
        {
            return month + "月前";
            // return getDateString(Long.valueOf(timeString)).substring(0, 10);
        }

        int day = (int) (t / (60 * 60 * 24 * 1000L));
        if (day > 0)
        {
            return day + "天前";
        }

        int hour = (int) (t / (60 * 60 * 1000L));
        if (hour > 0)
        {
            return hour + "小时前";
        }

        int minutes = (int) (t / (60 * 1000L));
        if (minutes > 0)
        {
            return minutes + "分钟前";
        }

        int second = (int) (t / (1 * 1000L));
        if (second > 0)
        {
            return second + "秒前";
        }
        return "刚刚";
    }

    private static boolean judgeYear(int timeString)
    {
        return timeString % 4 == 0 ? true : (timeString % 400 == 0 ? true : false);
    }

    /**
     * 计算距离时间
     *
     * @param distanceTime 限制小时数
     * @param creatTime
     * @param serverTime
     * @return
     */
    public static String getDistanceOrderTime(int distanceTime, String creatTime, String serverTime)
    {
        long distanceLong = distanceTime * 60 * 60 * 1000;
        if (creatTime == null || creatTime.equals(""))
        {
            return "";
        }
        long time = Long.parseLong(creatTime) * 1000L;
        long now = Long.parseLong(serverTime) * 1000L;
        long deadlineLong = distanceLong + time;
        long t = deadlineLong - now;
        int hour = (int) (t / (60 * 60 * 1000L));
        if (hour > 0)
        {
            return hour + "小时";
        }
        int minutes = (int) (t / (60 * 1000L));
        if (minutes > 0)
        {
            return minutes + "分钟";
        }

        int second = (int) (t / (1 * 1000L));
        if (second > 0)
        {
            return second + "秒";
        }
        return "支付已关闭";
    }

    /**
     * 获取时间 多少时间之前
     *
     * @param timeString
     */
    public static String getTime(String timeString)
    {
        long now = System.currentTimeMillis();
        return getTime(timeString, now + "");
    }

    public static String getTime(String timeString, String nowTime)
    {
        if (TextUtils.isEmpty(timeString) || TextUtils.isEmpty(nowTime))
        {
            return "";
        }
        long time = Long.parseLong(timeString);
        long now = Long.valueOf(nowTime);

        long t = now - time;
        int year = (int) (t / (60 * 60 * 24 * 365 * 1L));
        if (year > 0)
        {
            return year + "年前";
        }
        int month = (int) (t / (60 * 60 * 24 * 30 * 1L));
        if (month > 0)
        {
            return month + "月前";
        }
        int day = (int) (t / (60 * 60 * 24 * 1L));
        if (day > 0)
        {
            return day + "天前";
        }

        int hour = (int) (t / (60 * 60 * 1L));

        if (hour > 0)
        {
            return hour + "小时前";
        }

        int minutes = (int) (t / (60 * 1L));

        if (minutes > 0)
        {
            return minutes + "分钟前";
        }

        int second = (int) (t / (1 * 1L));
        if (second > 0)
        {
            return second + "秒前";
        }
        return "刚刚";
    }

    public static String getChargeTime(String timeString, String nowTime)
    {
        if (TextUtils.isEmpty(timeString) || TextUtils.isEmpty(nowTime))
        {
            return "48小时";
        }
        long time = Long.parseLong(timeString);
        long now = Long.valueOf(nowTime);

        long t = now - time;

        int hour = (int) (t / (60 * 60 * 1L));

        // 小于48小时显示 多少小时之内
        if (hour < 0)
        {
            return (48 - hour) + "小时";
        }
        if (hour < 48)
        {
            return (48 - hour) + "小时";
        }
        t = t - (60 * 60 * 1L) * 2;
        int minutes = (int) (t / (60 * 1L));
        if (minutes < 60)
        {
            return (60 - minutes) + "分钟";
        }
        t = t - (60 * 1L) * 59;
        int second = (int) (t / (1 * 1L));
        if (second < 60)
        {
            return (60 - second) + "秒";
        }
        return "48小时";
    }

    public static String timeStamp(String time)
    {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date;
        String times = null;
        try
        {
            date = sdr.parse(time);
            long l = date.getTime();
            if (l >= 0)
            {
                String stf = String.valueOf(l);
                times = stf.substring(0, 10);
            } else
            {
                times = "0000000000";
            }
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return times;
    }

    public static String getChatTime(long time)
    {
        if (time == 0.0)
        {
            return "";
        }

        long now = System.currentTimeMillis() / 1000;
        long t = now - time;
        float day = (float) (Math.round(t * 10 / (60 * 60 * 24 * 1L))) / 10;
        Log.d("DateUtil", now + " ;" + time + " ;" + t + " ;" + day + "  ; " + 60 * 60 * 24);
        if (day > 2)
        {
            return getDateStrText(time);
        } else if (day > 1)
        {
            return "前天 " + getDateTime(time);
        } else if (day > 0)
        {
            return "昨天  " + getDateTime(time);
        } else
        {
            return getDateTime(time);
        }

    }

    /**
     * 获取时间差.
     *
     * @param time
     * @return
     */
    public static String getDiffTime(long time)
    {
        long now = System.currentTimeMillis() / 1000;
        long t = now - time;
        int day = (int) (Math.round(t * 10 / (60 * 60 * 24 * 1L))) / 10;
        if (day <= 1)
        {
            return getDateTime(time);
        } else if (day < 365 && day > 1)
        {
            return getMonthAndDay(time);
        } else if (day >= 365)
        {
            return getYearMonthAndDay(time);
        }
        return null;
    }

    /**
     * 转换秒数成秒，分，小时字符串
     */
    public static String getTime(long second)
    {
        long minute = 0;
        if (second > 60)
        {
            minute = second / 60;
            second = second % 60;
            if (second == 0)
                return minute + "分钟";
            return minute + "分" + second + "秒";
        } else
        {
            return second + "秒";
        }
    }
}
