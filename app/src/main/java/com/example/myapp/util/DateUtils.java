package com.example.myapp.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/**
 * 时间工具
 */
public class DateUtils {

    // 建议用下面的格式常量
    public static final String Y_M_D = "yyyy-MM-dd";
    public static final String Y_M_D_HM = "yyyy-MM-dd HH:mm";
    public static final String Y_M_D_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD = "yyyyMMdd";
    public static final String YMDHM = "yyyyMMddHHmm";
    public static final String YMDHMS = "yyyyMMddHHmmss";
    public static final String ymd = "yyyy/MM/dd";
    public static final String ymd_HM = "yyyy/MM/dd HH:mm";
    public static final String ymd_HMS = "yyyy/MM/dd HH:mm:ss";
    public static final String HMS = "HHmmss";
    public static final String H_m_s = "HH:mm:ss";




    // 过时的格式
    public static final String ISO_EXPANDED_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO_DATE_FORMAT = "yyyyMMdd";
    public static final String CHINESE_EXPANDED_DATE_FORMAT = "yyyy年MM月dd日";
    private static final boolean LENIENT_DATE = false;
    public static final String HHMM_DATA_FORMATE = "HH:mm";
    public static final String HHMMSS_DATA_FORMATE = "HH:mm:ss";
    public static final String YYYYMMDD_HHMM_DATA_FORMATE = "yyyy-MM-dd HH:mm";
    public final static String NON_YEAR_DATE_FORMAT = "MM月dd日";
    public static final String YYYYMMDD_HHMMSS_DATA_FORMATE = "yyyyMMddHHmmss";





    /**
     * 智能转换日期
     *
     * @param text
     * @return
     */
    public static Date smartFormat(String text) {
        Date date = null;
        if (text.contains("/"))
            text = text.replace("/", "-");
        if (text.contains("."))
            text = text.replace(".", "-");
        if (text.contains("年"))
            text = text.replace("年", "-");
        if (text.contains("月"))
            text = text.replace("月", "-");
        if (text.contains("日"))
            text = text.replace("日", "-");
        try {
            if (text == null || text.length() == 0) {
                date = null;
            } else if (text.length() == 10) {
                date = formatStringToDate(text, Y_M_D);
            } else if (text.length() == 13) {
                date = new Date(Long.parseLong(text));
            } else if (text.length() == 16) {
                date = formatStringToDate(text, Y_M_D_HM);
            } else if (text.length() == 19) {
                date = formatStringToDate(text, Y_M_D_HMS);
            } else if (text.length() == YMD.length()) {
                date = formatStringToDate(text, YMD);
            } else if (text.length() == YMDHM.length()) {
                date = formatStringToDate(text, YMDHM);
            } else if (text.length() == YMDHMS.length()) {
                date = formatStringToDate(text, YMDHMS);
            } else if (text.length() == ymd.length()) {
                date = formatStringToDate(text, ymd);
            } else if (text.length() == ymd_HM.length()) {
                date = formatStringToDate(text, ymd_HM);
            } else if (text.length() == ymd_HMS.length()) {
                date = formatStringToDate(text, ymd_HMS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date formatStringToDate(String dateStr, String format) {
        if (dateStr == null || dateStr.trim().length() < 1) {
            return null;
        }
        String strFormat = format;
        if (StringUtil.isEmpty(strFormat)) {
            strFormat = Y_M_D;
            if (dateStr.length() > 16) {
                strFormat = Y_M_D_HMS;
            } else if (dateStr.length() > 10) {
                strFormat = Y_M_D_HM;
            }
        }
        SimpleDateFormat sdfFormat = new SimpleDateFormat(strFormat);
        //严格模式
        sdfFormat.setLenient(false);
        try {
            return sdfFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFormatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 比较date1-date2差几天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int dateDiff(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        long ldate1 = date1.getTime() + cal1.get(Calendar.ZONE_OFFSET) + cal1.get(Calendar.DST_OFFSET);
        long ldate2 = date2.getTime() + cal2.get(Calendar.ZONE_OFFSET) + cal2.get(Calendar.DST_OFFSET);
        // Use integer calculation, truncate the decimals
        int hr1 = (int) (ldate1 / 3600000); // 60*60*1000
        int hr2 = (int) (ldate2 / 3600000);

        int days1 = (int) hr1 / 24;
        int days2 = (int) hr2 / 24;

        int dateDiff = days1 - days2;
        return dateDiff;
    }

    /**
     * 获取当前的日期，不包括时间
     *
     * @return
     */
    public static Date getTodayDate() {
        return formatToDate(new Date());
    }

    /**
     * <p>
     * Description:把Calendar类型转换为Timestamp
     * </p>
     *
     * @param calendar
     * @return Timestamp
     */
    public static Timestamp convertCalToTs(Calendar calendar) {
        return new Timestamp(calendar.getTime().getTime());
    }

    /**
     * <p>
     * Description:把Timestamp类型转换为Calendar
     * </p>
     *
     * @param ts
     * @return Calendar
     */
    public static Calendar convertTsToCal(Timestamp ts) {
        Calendar cald = Calendar.getInstance();
        cald.setTime(new Date(ts.getTime()));
        return cald;
    }

    /**
     * <p>
     * Description:把Timestamp类型转换为Date
     * </p>
     *
     * @param ts
     * @return Date
     */
    public static Date convertTsToDt(Timestamp ts) {
        return new Date(ts.getTime());
    }

    /**
     * <p>
     * Description:把Date类型转换为Timestamp
     * </p>
     *
     * @param dt
     * @return Timestamp
     */
    public static Timestamp convertDtToTs(Date dt) {
        return new Timestamp(dt.getTime());
    }

    /**
     * <p>
     * Description:把Timestamp类型转换为String类型,返回数据截至到日期
     * </p>
     *
     * @param ts
     * @return String
     */
    public static String convertTsToStr(Timestamp ts) {
        if (ts != null) {
            return ts.toString().substring(0, 10);
        }
        return "";
    }

    /**
     * <p>
     * Description:把Timestamp类型转换为String类型,返回数据截至到秒,格式为2003-02-05 14:23:05
     * </p>
     *
     * @param ts
     * @return String
     */
    public static String convertTsToStrWithSecs(Timestamp ts) {
        if (ts != null) {
            return ts.toString().substring(0, 19);
        }
        return "";
    }

    /**
     * <p>
     * Description:把Timestamp类型转换为String类型,返回数据带有星期几显示,格式为2003-11-04 星期二
     * </p>
     *
     * @param ts
     * @return String
     */
    public static String convertTsToStrWithDayOfWeek(Timestamp ts) {
        if (ts != null) {
            return ts.toString().substring(0, 10) + " " + getStrDay(ts);
        }
        return "";
    }

    /**
     * <p>
     * Description:根据传入的参数生成一个日期类型
     * </p>
     *
     * @param year :年份如1999 month:月份如3月（为实际需要创建的月份） date:日期如25
     * @param hour :小时如15 minute:分钟如25 second:秒如34
     * @return String
     */
    public static Timestamp createTimestamp(int year, int month, int date, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date, hour, minute, second);
        cal.set(Calendar.MILLISECOND, 0);
        return convertCalToTs(cal);
    }

    /**
     * <p>
     * Description:根据传入的参数生成一个日期类型
     * </p>
     *
     * @param year :年份如1999 month:月份如3月（为实际需要创建的月份） date:日期如25
     * @return String
     */
    public static Timestamp createTimestamp(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return convertCalToTs(cal);
    }

    /**
     * @param str      传入的字符串格式如2003-02-01,2003/02/01
     * @param splitStr 传入字符串当中的分隔符 '-' '/'
     * @return Timestamp 日期类型
     */
    public static Timestamp createTimestamp(String str, String splitStr) {
        if ((str == null) || (str.trim().length() < 1)) {
            return null;
        }
        if ("".equals(splitStr)) {
            splitStr = "-";
        }
        if (str.lastIndexOf(' ') != -1) {
            str = str.substring(0, 10);
        }
        StringTokenizer st = new StringTokenizer(str, splitStr);
        int year = Integer.parseInt(st.nextToken());
        int month = Integer.parseInt(st.nextToken());
        int date = Integer.parseInt(st.nextToken());
        return createTimestamp(year, month, date);
    }

    /**
     * @param ts 日期类型
     * @return 传入日期的对应年数
     */
    public static int getYear(Timestamp ts) {
        return convertTsToCal(ts).get(Calendar.YEAR);
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * @param ts 日期类型
     * @return 传入日期的对应月份
     */
    public static int getMonth(Timestamp ts) {
        return convertTsToCal(ts).get(Calendar.MONTH) + 1;
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getQuarter(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                return 1;
            else if (currentMonth >= 4 && currentMonth <= 6)
                return 1;
            else if (currentMonth >= 7 && currentMonth <= 9)
                return 1;
            else if (currentMonth >= 10 && currentMonth <= 12)
                return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * @param ts 日期类型
     * @return 传入日期的对应日期
     */
    public static int getDate(Timestamp ts) {
        return convertTsToCal(ts).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @param ts 日期类型
     * @return 传入日期类型的对应小时数
     */
    public static int getHour(Timestamp ts) {
        return convertTsToCal(ts).get(Calendar.HOUR_OF_DAY);
    }

    /**
     * @param ts 日期类型
     * @return 传入日期类型的对应分钟数
     */
    public static int getMinute(Timestamp ts) {
        return convertTsToCal(ts).get(Calendar.MINUTE);
    }

    /**
     * @param ts 日期类型
     * @return 传入日期类型的对应秒数
     */
    public static int getSecond(Timestamp ts) {
        return convertTsToCal(ts).get(Calendar.SECOND);
    }

    /**
     * @param ts 日期类型
     * @return 传入日期类型的对应秒中的毫秒数
     */
    public static int getMillisecond(Timestamp ts) {
        return convertTsToCal(ts).get(Calendar.MILLISECOND);
    }

    /**
     * @param ts 日期类型
     * @return 传入日期类型的对应毫秒数
     */
    public static long getMilliseconds(Timestamp ts) {
        return ts.getTime();
    }

    /**
     * @param date 日期类型
     * @return 传入日期类型的对应毫秒数
     */
    public static Long getMilliseconds(Date date) {
        return date == null ? null : date.getTime();
    }

    /**
     * @param ts 日期类型
     * @return 传入日期类型的对应星期数，数字为1表示星期天，为2表示星期一，依次类推
     */
    public static int getDay(Timestamp ts) {
        return convertTsToCal(ts).get(Calendar.DAY_OF_WEEK);
    }

    /**
     * @param ts 日期类型
     * @return 传入日期类型的对应星期几，返回为中文，如星期一星期二等
     */
    public static String getStrDay(Timestamp ts) {
        if (ts == null) {
            return null;
        }
        int day = getDay(ts);
        String weekDay = "";
        switch (day) {
            case 1:
                weekDay = "星期天";
                break;
            case 2:
                weekDay = "星期一";
                break;
            case 3:
                weekDay = "星期二";
                break;
            case 4:
                weekDay = "星期三";
                break;
            case 5:
                weekDay = "星期四";
                break;
            case 6:
                weekDay = "星期五";
                break;
            case 7:
                weekDay = "星期六";
                break;
            default:
                weekDay = "";
                break;
        }
        return weekDay;
    }

    /**
     * @param addpart 为添加的部分可以为yy年数mm月份数dd天数hh小时数mi分钟数ss秒数
     * @param ts      需要改动的日期类型
     * @param addnum  添加数目 可以为负数
     * @return 改动后的日期类型
     */
    public static Timestamp dateAdd(String addpart, Timestamp ts, int addnum) {
        Calendar cal = convertTsToCal(ts);
        if ("yy".equals(addpart)) {
            cal.add(Calendar.YEAR, addnum);
        } else if ("mm".equals(addpart)) {
            cal.add(Calendar.MONTH, addnum);
        } else if ("dd".equals(addpart)) {
            cal.add(Calendar.DATE, addnum);
        } else if ("hh".equals(addpart)) {
            cal.add(Calendar.HOUR, addnum);
        } else if ("mi".equals(addpart)) {
            cal.add(Calendar.MINUTE, addnum);
        } else if ("ss".equals(addpart)) {
            cal.add(Calendar.SECOND, addnum);
        } else {
            return null;
        }
        return convertCalToTs(cal);
    }

    /**
     * @param addpart 为添加的部分可以为yy年数mm月份数dd天数hh小时数mi分钟数ss秒数
     * @param ts      需要改动的日期类型
     * @param addnum  添加数目 可以为负数
     * @return 改动后的日期类型
     */
    public static Date dateAdd(String addpart, Date ts, int addnum) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        if ("yy".equals(addpart)) {
            cal.add(Calendar.YEAR, addnum);
        } else if ("mm".equals(addpart)) {
            cal.add(Calendar.MONTH, addnum);
        } else if ("dd".equals(addpart)) {
            cal.add(Calendar.DATE, addnum);
        } else if ("hh".equals(addpart)) {
            cal.add(Calendar.HOUR, addnum);
        } else if ("mi".equals(addpart)) {
            cal.add(Calendar.MINUTE, addnum);
        } else if ("ss".equals(addpart)) {
            cal.add(Calendar.SECOND, addnum);
        } else {
            return null;
        }
        return cal.getTime();
    }

    /**
     * @param diffpart 比较部分YEAR为比较年份 MONTH为比较月份 DATE为比较天数 WEEK比较星期数,
     *                 如果传入的diffpart参数不为以上范围默认返回相差天数
     * @param ts1      需要比较的日期
     * @param ts2      需要比较的日期
     * @return 相差的大小
     */
    public static int dateDiff(String diffpart, Timestamp ts1, Timestamp ts2) {
        if ((ts1 == null) || (ts2 == null)) {
            return -1;
        }

        Date date1, date2;
        date1 = new Date(ts1.getTime());
        date2 = new Date(ts2.getTime());

        Calendar cal1, cal2;
        cal1 = Calendar.getInstance();
        cal2 = Calendar.getInstance();

        // different date might have different offset
        cal1.setTime(date1);
        long ldate1 = date1.getTime() + cal1.get(Calendar.ZONE_OFFSET) + cal1.get(Calendar.DST_OFFSET);

        cal2.setTime(date2);
        long ldate2 = date2.getTime() + cal2.get(Calendar.ZONE_OFFSET) + cal2.get(Calendar.DST_OFFSET);

        // Use integer calculation, truncate the decimals
        int hr1 = (int) (ldate1 / 3600000); // 60*60*1000
        int hr2 = (int) (ldate2 / 3600000);

        int days1 = hr1 / 24;
        int days2 = hr2 / 24;

        int dateDiff = days2 - days1;
        int weekOffset = (cal2.get(Calendar.DAY_OF_WEEK) - cal1.get(Calendar.DAY_OF_WEEK)) < 0 ? 1 : 0;
        int weekDiff = dateDiff / 7 + weekOffset;
        int yearDiff = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
        int monthDiff = yearDiff * 12 + cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);

        if ("YEAR".equals(diffpart)) {
            return yearDiff;
        } else if ("MONTH".equals(diffpart)) {
            return monthDiff;
        } else if ("DATE".equals(diffpart)) {
            return dateDiff;
        } else if ("WEEK".equals(diffpart)) {
            return weekDiff;
        } else {
            return dateDiff;
        }
    }

    /**
     * @param ts
     * @return true表示ts为闰年 false表示ts不是闰年
     */
    public static boolean isLeapyear(Timestamp ts) {
        Calendar cal = Calendar.getInstance();
        boolean booleanleapYear = ((GregorianCalendar) cal).isLeapYear(getYear(ts));
        return booleanleapYear;
    }

    /**
     * @param year 年
     * @return true表示year为闰年 false表示ts不是闰年
     */
    public static boolean isLeapyear(int year) {
        Calendar cal = Calendar.getInstance();
        return ((GregorianCalendar) cal).isLeapYear(year);
    }

    /**
     * 比较传入的年月日是否与ts对应为同一天
     *
     * @param year  年份 传入－1表示不需要比较
     * @param month 月份 传入－1表示不需要比较
     * @param date  日期 传入－1表示不需要比较
     * @param ts    完成的Timestamp日期类型
     * @return
     */
    public static boolean isMatchDate(int year, int month, int date, Timestamp ts) {
        int year1 = getYear(ts);
        int month1 = getMonth(ts);
        int date1 = getDay(ts);

        if ((year != -1) && (year != year1)) {
            return false;
        }
        if ((month != -1) && (month != month1)) {
            return false;
        }
        if ((date != -1) && (date != date1)) {
            return false;
        }
        return true;
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    public static Date getFirstDayOfWeek(Date date, int firstDayOfWeek) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(firstDayOfWeek);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }

    /**
     * 去除时间后面的小时分秒，只取具体时间日期
     *
     * @param ts
     * @return
     */
    public static Timestamp formatToDate(Timestamp ts) {
        Calendar cd = Calendar.getInstance();
        cd.setTimeInMillis(ts.getTime());
        cd.set(Calendar.HOUR_OF_DAY, 0);
        cd.set(Calendar.MINUTE, 0);
        cd.set(Calendar.SECOND, 0);
        cd.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cd.getTime().getTime());
    }

    /**
     * 去除时间后面的小时分秒，只取具体时间日期
     *
     * @param dt
     * @return
     */
    public static Date formatToDate(Date dt) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(dt);
        cd.set(Calendar.HOUR_OF_DAY, 0);
        cd.set(Calendar.MINUTE, 0);
        cd.set(Calendar.SECOND, 0);
        cd.set(Calendar.MILLISECOND, 0);
        return cd.getTime();
    }

    /**
     * 去除时间后面的小时分秒，只取具体时间日期
     *
     * @param dt
     * @return
     */
    public static java.sql.Date formatToDate(java.sql.Date dt) {
        Calendar cd = Calendar.getInstance();
        cd.setTimeInMillis(dt.getTime());
        cd.set(Calendar.HOUR_OF_DAY, 0);
        cd.set(Calendar.MINUTE, 0);
        cd.set(Calendar.SECOND, 0);
        cd.set(Calendar.MILLISECOND, 0);
        return new java.sql.Date(cd.getTime().getTime());
    }

    /**
     * 取得指定月的上个月。
     *
     * @return
     */
    public static String getLastMonth(String yearM) {
        String yymmdd = yearM + "01";
        Date date = DateUtils.valueOf(yymmdd, ISO_DATE_FORMAT);
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, -1);
        // 设置时间为0时
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);

        String lastMonth = dateToString(cal.getTime(), ISO_DATE_FORMAT);

        return lastMonth.substring(0, lastMonth.length() - 2);
    }

    /**
     * 取得指定月的前第十二个月。
     *
     * @return
     */
    public static String getBeforTwelveMonth(String yearM) {
        String yymmdd = yearM + "01";
        Date date = DateUtils.valueOf(yymmdd, ISO_DATE_FORMAT);
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, -12);
        // 设置时间为0时
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);

        String lastMonth = dateToString(cal.getTime(), ISO_DATE_FORMAT);

        return lastMonth.substring(0, lastMonth.length() - 2);
    }

    /**
     * 根据时间变量返回时间字符串 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        return dateToString(date, ISO_EXPANDED_DATE_FORMAT);
    }

    /**
     * 根据时间变量返回时间字符串
     *
     * @param pattern 时间字符串样式
     * @param date    时间变量
     * @return 返回时间字符串
     */
    public static String dateToString(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        try {
            SimpleDateFormat sfDate = new SimpleDateFormat(pattern);
            sfDate.setLenient(false);
            return sfDate.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 长整型转换为日期java.util.Date
     */
    public static Date valueOf(Long dateLong) {
        if (dateLong == null) {
            return null;
        }

        return new Date(dateLong);
    }

    /**
     * 字符串转换为日期java.util.Date
     *
     * @param dateString 字符串
     */
    public static Date valueOf(String dateString) {
        return valueOf(dateString, ISO_EXPANDED_DATE_FORMAT, LENIENT_DATE);
    }

    /**
     * 字符串转换为日期java.util.Date
     *
     * @param dateString 字符串
     * @param format     日期格式
     * @return
     */
    public static Date valueOf(String dateString, String format) {
        return valueOf(dateString, format, LENIENT_DATE);
    }

    /**
     * 字符串转换为日期java.util.Date
     *
     * @param dateText 字符串
     * @param format   日期格式
     * @param lenient  日期越界标志
     * @return
     */
    public static Date valueOf(String dateText, String format, boolean lenient) {
        if (dateText == null) {
            return null;
        }

        DateFormat df = null;
        try {
            if (format == null) {
                df = new SimpleDateFormat();
            } else {
                df = new SimpleDateFormat(format);
            }

            // setLenient avoids allowing dates like 9/32/2001
            // which would otherwise parse to 10/2/2001
            df.setLenient(lenient);

            return df.parse(dateText);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 根据当前月向前取月份。月份跨度由span指定
     *
     * @return 年月。For example:200907
     */
    public static String getBeforeMonth(Date currentDate, int span) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(currentDate);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, -span);

        // 设置时间为0时
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        String lastMonth = dateToString(cal.getTime(), "yyyyMM");

        return lastMonth;
    }

    /**
     * 根据当前月向前取月份。月份跨度由span指定
     *
     * @return 年月。For example:200907
     */
    public static String getAfterMonth(Date currentDate, int span) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(currentDate);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, span);

        // 设置时间为0时
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        String lastMonth = dateToString(cal.getTime(), "yyyyMM");

        return lastMonth;
    }

    /**
     * 将毫秒数换算成x天x时x分x秒x毫秒
     *
     * @param ms
     * @return
     */
    public static String format(long ms) {// 将毫秒数换算成x天x时x分x秒x毫秒
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day;
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
        return strDay + "天," + strHour + "小时," + strMinute + "分钟," + strSecond + "秒," + strMilliSecond + "毫秒";
    }

    /**
     * 获取第i天后的日期
     *
     * @param date
     * @param i
     */
    public static Date getDateAfterIDay(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, i);
        return c.getTime();
    }

    /**
     * 获取第i天前的日期
     *
     * @param date
     * @param i
     */
    public static Date getDateBeforeIDay(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -1 * i);
        return c.getTime();
    }

    /**
     * 获取第i小时后的日期
     *
     * @param date
     * @param i
     */
    public static Date getDateAfterIHours(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, i);
        return c.getTime();
    }

    /**
     * 获取第i小时前的日期
     *
     * @param date
     * @param i
     */
    public static Date getDateBeforeIHours(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, -1 * i);
        return c.getTime();
    }

    /**
     * 获取第i分钟后的日期
     *
     * @param date
     * @param i
     */
    public static Date getDateAfterIMinutes(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, i);
        return c.getTime();
    }

    /**
     * 获取第i分钟前的日期
     *
     * @param date
     * @param i
     */
    public static Date getDateBeforeIMinutes(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, -1 * i);
        return c.getTime();
    }

    /**
     * 获取第second秒钟后的日期
     *
     * @param date
     * @param second
     */
    public static Date getDateAfterISeconds(Date date, int second) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, second);
        return c.getTime();
    }

    public static Date convertCalToTs(Date date, int hour, int minute, int second) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);

        return c.getTime();
    }

    /**
     * 设置给定日期的时分秒
     *
     * @param c      需要调整的日期
     * @param hour   设定的小时值
     * @param minute 设定的分钟值
     * @param second 设定的秒值
     */
    public static void setHMS(Calendar c, int hour, int minute, int second) {
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
    }

    /**
     * 设置给定日期的时分秒
     *
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date getDateWithSpecificHMS(Date date, int hour, int minute, int second) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setHMS(calendar, hour, minute, second);
        return calendar.getTime();
    }

    /**
     * 给定日期增加时间（分钟）
     *
     * @param date   需要调整的日期
     * @param minute 设定的分钟值
     * @return
     */
    public static Date addMinute(Date date, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, minute);
        return c.getTime();
    }

    /**
     * 给定日期增加时间（小时）
     *
     * @param date 需要调整的日期
     * @param hour 设定的分钟值
     * @return
     */
    public static Date addHour(Date date, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, hour);
        return c.getTime();
    }

    /**
     * 给定日期增加时间（天）
     *
     * @param date 需要调整的日期
     * @param day 设定的天数
     * @return
     */
    public static Date addDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, day);
        return c.getTime();
    }


    /**
     * 设置给定日期的时分秒毫秒
     *
     * @param c           需要调整的日期
     * @param hour        设定的小时值
     * @param minute      设定的分钟值
     * @param second      设定的秒值
     * @param milliSecond 设定的毫秒秒值
     */
    public static void setHMSM(Calendar c, int hour, int minute, int second, int milliSecond) {
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, milliSecond);
    }

    /**
     * 获取日期类型的对应小时数
     *
     * @param date 传入的日期
     * @return
     */
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static boolean afterOrEqual(Date date1, Date date2) {
        return date1.after(date2) || date1.equals(date2);
    }

    public static boolean beforeOrEqual(Date date1, Date date2) {
        return date1.before(date2) || date1.equals(date2);
    }

    /**
     * 设置指定日期的时分秒
     *
     * @param date
     * @param time
     * @return
     */
    public static Date setTimeOfDate(Date date, String time) {
        String[] times = time.split(":");
        int hour = times.length > 0 ? Integer.parseInt(times[0]) : 0;
        int minute = times.length > 1 ? Integer.parseInt(times[1]) : 0;
        int second = times.length > 2 ? Integer.parseInt(times[2]) : 0;

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * 计算两个日期之间间隔的天数
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static Integer getDayInterval(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null) {
            return null;
        }

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        beginCalendar.set(Calendar.HOUR_OF_DAY, 0);
        beginCalendar.set(Calendar.MINUTE, 0);
        beginCalendar.set(Calendar.SECOND, 0);
        beginCalendar.set(Calendar.MILLISECOND, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        endCalendar.set(Calendar.HOUR_OF_DAY, 0);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((endCalendar.getTime().getTime() - beginCalendar.getTime().getTime()) / 1000 / 60 / 60 / 24);
    }

    /**
     * 计算两个日期之间间隔的小时数, 不足一个小时按一个小时计算
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static Integer getHourInterval(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null) {
            return null;
        }

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        beginCalendar.set(Calendar.MILLISECOND, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        endCalendar.set(Calendar.SECOND, 0);

        int secondsInterval = (int) ((endCalendar.getTime().getTime() - beginCalendar.getTime().getTime()) / 1000);
        return (secondsInterval % 3600 == 0) ? (secondsInterval / 3600) : (secondsInterval / 3600 + 1);
    }

    /**
     * 计算两个日期之间间隔的分钟数
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static Integer getMinuteInterval(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null) {
            return null;
        }

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        beginCalendar.set(Calendar.SECOND, 0);
        beginCalendar.set(Calendar.MILLISECOND, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((endCalendar.getTime().getTime() - beginCalendar.getTime().getTime()) / 1000 / 60);
    }

    /**
     * 设置:
     * 星期一为第一周的开始
     * 一周有7天
     * 对于夸年的周，一周的最小天数是 1
     */
    private static void calendarWeekFormat(Calendar calendar, int firstDayOfWeek) {
        calendar.setFirstDayOfWeek(firstDayOfWeek);
        calendar.setMinimalDaysInFirstWeek(1);
    }

    /**
     * 根据跨度获取指定月的的首日日期
     *
     * @param span
     * @return
     */
    public static Date getPrevMonthFirstDay(int span) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.MONTH, span);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        return calendar.getTime();
    }

    /**
     * <p>
     * Description:得到当前时间 包括时间,分,秒
     * </p>
     *
     * @return Timestamp
     */
    public static Date getNowTime() {
//        if (YccGlobalPropertyUtil.isDevOrTest() && TestNowTimeListener.getNowTime() != null) {
//            return TestNowTimeListener.getNowTime();
//        }
//
//        if ("on".equals(
//                YccGlobalPropertyUtil.getPropertyByKey("global.properties", GlobalConstants.TEST_USE_SYSDATE))) {
//            ConfigUtil configUtil = SpringHelper.getBean(ConfigUtil.class);
//            String testUseSysdate = (String) configUtil.getValueByCode(GlobalConstants.TEST_USE_SYSDATE);
//            if (StringUtil.isNotEmpty(testUseSysdate)) {
//                return convertDtToTs(valueOf(testUseSysdate, DATETIME_PATTERN));
//            }
//        }
        return new Date(System.currentTimeMillis());
    }


    public static final int H_M = 0;
    public static final int H_M_S = 1;
    public static final int Y_H_M = 2;
    public static final int Y_H_M_H_M = 3;
    public static final int Y_H_M_H_M_S = 4;
    public static final int Y = 5;
    public static final int Y_H = 6;
    public static final int Y_H_M_H_M_S_2 = 7;
    public static final int YYYYMMDD = 8;
    public static final int YYYY_MM_DD_H_M_S = 9;
    private static SimpleDateFormat dateformat0;
    private static SimpleDateFormat dateformat1;
    private static SimpleDateFormat dateformat2;
    private static SimpleDateFormat dateformat3;
    private static SimpleDateFormat dateformat4;
    private static SimpleDateFormat dateformat5;
    private static SimpleDateFormat dateformat6;
    private static SimpleDateFormat dateformat7;
    private static SimpleDateFormat dateformat8;
    private static SimpleDateFormat dateformat9;

    static {
        dateformat0 = new SimpleDateFormat("HH:mm");
        dateformat1 = new SimpleDateFormat("HH:mm:ss");
        dateformat2 = new SimpleDateFormat("yyyy-MM-dd");
        dateformat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateformat4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateformat5 = new SimpleDateFormat("yyyy年");
        dateformat6 = new SimpleDateFormat("yyyy年MM月");
        dateformat7 = new SimpleDateFormat("yyyyMMddHHmmss");
        dateformat8 = new SimpleDateFormat("yyyyMMdd");
        dateformat9 = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
    }

    /**
     * 根据输入的日期字符串 和 提前天数 ，
     * 获得 指定日期提前几天的字符串日期格式 对象
     *
     * @param dateString 日期对象 ，格式如 1-31-1900
     * @return 指定日期倒推指定天数后的日期对象
     */
    public static String getBeforNDayDate(String dateString, int beforeDays) {
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -beforeDays);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal1.getTime());
    }

    public static String getBeforeNMonthDate(String dateString, int beforeMonths) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal1 = Calendar.getInstance();
        try {
            cal1.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
            System.out.println("格式化日期发生异常，日期:" + dateString);
        }
        cal1.add(Calendar.DATE, 1);
        cal1.add(Calendar.MONTH, -beforeMonths);
        return sdf.format(cal1.getTime());
    }

    /**
     * 根据不同的类型得到不同的格式化工具类
     *
     * @param num 表示类型
     * @return 不同类型的格式化工具类
     */
    private static SimpleDateFormat getFormat(int num) {
        SimpleDateFormat dateformat = null;
        switch (num) {
            case DateUtils.H_M:
                dateformat = dateformat0;
                break;
            case DateUtils.H_M_S:
                dateformat = dateformat1;
                break;
            case DateUtils.Y_H_M:
                dateformat = dateformat2;
                break;
            case DateUtils.Y_H_M_H_M:
                dateformat = dateformat3;
                break;
            case DateUtils.Y_H_M_H_M_S:
                dateformat = dateformat4;
                break;
            case DateUtils.Y:
                dateformat = dateformat5;
                break;
            case DateUtils.Y_H:
                dateformat = dateformat6;
                break;
            case DateUtils.Y_H_M_H_M_S_2:
                dateformat = dateformat7;
                break;
            case DateUtils.YYYYMMDD:
                dateformat = dateformat8;
                break;
            case DateUtils.YYYY_MM_DD_H_M_S:
                dateformat = dateformat9;
                break;
            default:
                break;
        }
        return dateformat;
    }

    /**
     * @param num  类型
     * @param date 需要处理的日期
     * @return 返回需要的日期字符串形式
     * @description 返回格式化字符串型日期
     */
    public static String getFormatString(int num, Date date) {
        if (date == null)
            return "";
        else {
            SimpleDateFormat dateformat = getFormat(num);
            return dateformat == null ? "" : dateformat.format(date);
        }
    }

    /**
     * @param num        格式化类型标识
     * @param stringDate 日期的字符串形式
     * @return Date对象
     * @description 返回格日期
     */
    public static Date getFormatDate(int num, String stringDate) {
        Date resultDate = null;
        SimpleDateFormat dateformat = getFormat(num);
        try {
            if (dateformat != null)
                resultDate = dateformat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }


    /**
     * 获得两个日期的小时差
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 相差小时数
     */
    public static float getTwoDatesDifOfHours(Date begin, Date end) {
        long total = begin.getTime() - end.getTime();
        long seconds = total / 1000;
        return seconds / 3600f;
    }

    /**
     * 获得两个日期的分钟差
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 相差分钟数
     */
    public static long getTwoDatesDifOfMins(Date begin, Date end) {
        long total = begin.getTime() - end.getTime();
        long mins = total / (60 * 1000);
        return mins;
    }

    /**
     * 获得两个日期的天、小时、分、秒差
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 返回格式：0天1小时3分钟45秒
     */
    public static String getTwoDatesDif(Date begin, Date end) {
        long total = begin.getTime() - end.getTime();
        return getFormatTime(total);
    }

    /**
     * 格式化时间
     *
     * @param time 单位(毫秒)
     * @return 格式"X天X小时X分X秒"
     */
    public static String getFormatTime(long time) {
        long seconds = time / 1000;
        long day = seconds / 60 / 60 / 24;
        long hour = (seconds - (day * 24 * 60 * 60)) / 60 / 60;
        long minute = (seconds - (day * 24 * 60 * 60) - (hour * 60 * 60)) / 60;
        long second = seconds - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
        return day + "天" + hour + "小时" + minute + "分" + second + "秒";
    }

    /**
     * 把时间转换成指定格式
     *
     * @param date
     * @param format
     * @return Date
     */
    public static Date dateFormatToDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String dateString = dateFormat.format(date);
        Date rel = null;
        try {
            rel = dateFormat.parse(dateString);
        } catch (ParseException e) {
            System.out.println("日期格式转换出错：DateUtils.dateFormatToDate(String date, String format) ");
            e.printStackTrace();
        }
        return rel;
    }

    /**
     * 两时间是否间隔一天
     *
     * @param fDate
     * @param oDate
     * @return
     */
    public static boolean intervalOneDay(Date fDate, Date oDate) {
        if (null == fDate || null == oDate) {
            return false;
        }
        long intervalMilli = oDate.getTime() - fDate.getTime();
        return ((int) (intervalMilli / (24 * 60 * 60 * 1000)) == 1) && (intervalMilli % (24 * 60 * 60 * 1000) == 0) || (((int) (intervalMilli / (24 * 60 * 60 * 1000))) == 0 && (intervalMilli % (24 * 60 * 60 * 1000) > 0));
    }

    /**
     * 时间比较大小：-1小于、0相等、1大于、500异常
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int compareDateTime(Date d1, Date d2) {
        if (d1 == null || d2 == null) return 500;

        Calendar c1 = null;
        Calendar c2 = null;
        try {
            c1 = Calendar.getInstance();
            c1.setTime(DateUtils.dateFormatToDate(d1, "yyyy-MM-dd HH:mm:ss"));

            c2 = Calendar.getInstance();
            c2.setTime(DateUtils.dateFormatToDate(d2, "yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            System.out.println("格式化日期发生异常，日期d1:" + d1 + "，d2:" + d2 + "，message：" + e.getMessage());
            c1 = null;
            c2 = null;
        }

        if (c1 == null || c2 == null) return 500;
        int result = c1.compareTo(c2);
        if (result == 0)
            return 0; //("c1相等c2");
        else if (result < 0)
            return -1; //("c1小于c2");
        else
            return 1; //("c1大于c2");
    }

    /**
     * 增加秒数
     *
     * @param date    时间
     * @param seconds 秒数
     * @return 增加后的时间
     */
    public static Date addSeconds(Date date, int seconds) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.SECOND, seconds);
        return instance.getTime();
    }

    /**
     * 取得当前日期是一年中的多少周
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        return getWeekOfYear(date, Calendar.MONDAY);
    }

    public static int getWeekOfYear(Date date, int firstDayOfWeek) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(firstDayOfWeek);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 取得一个时间的当前周的星期几的日期
     *
     * @param date 传入的时间
     * @param days 星期几
     * @return 返回传入时间的当前周星期几的日期
     */
    public static Date getDayByWeek(Date date, int days) {
        return getDayByWeek(date, days, Calendar.MONDAY);
    }

    public static Date getDayByWeek(Date date, int days, int firstDayOfWeek) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(firstDayOfWeek);
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, days);
        return cal.getTime();
    }

    public static enum AddpartEnum {
        YY("yy"),
        MM("mm"),
        DD("dd"),
        HH("hh"),
        MI("mi"),
        SS("ss");

        private String value;

        private AddpartEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public static void main(String[] args) {
        System.out.println(getNowTime());
    }
}
