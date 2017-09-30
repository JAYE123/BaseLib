package core.base.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import core.base.log.L;

/**
 * 日期时间转换工具
 *
 * @author wangjie
 * @version 创建时间：2013-2-19 上午11:35:53
 */
public class ABTimeUtil {
    public static final String TAG = ABTimeUtil.class.getSimpleName();

    /**
     * 把一个毫秒数转化成时间字符串。格式为小时/分/秒/毫秒（如：24903600 --> 06小时55分03秒600毫秒）。
     *
     * @param millis   要转化的毫秒数。
     * @param isWhole  是否强制全部显示小时/分/秒/毫秒。
     * @param isFormat 时间数字是否要格式化，如果true：少位数前面补全；如果false：少位数前面不补全。
     * @return 返回时间字符串：小时/分/秒/毫秒的格式（如：24903600 --> 06小时55分03秒600毫秒）。
     * @author wangjie
     */
    public static String millisToString(long millis, boolean isWhole, boolean isFormat) {
        String h = "";
        String m = "";
        String s = "";
        String mi = "";
        if (isWhole) {
            h = isFormat ? "00小时" : "0小时";
            m = isFormat ? "00分" : "0分";
            s = isFormat ? "00秒" : "0秒";
            mi = isFormat ? "00毫秒" : "0毫秒";
        }

        long temp = millis;

        long hper = 60 * 60 * 1000;
        long mper = 60 * 1000;
        long sper = 1000;

        if (temp / hper > 0) {
            if (isFormat) {
                h = temp / hper < 10 ? "0" + temp / hper : temp / hper + "";
            } else {
                h = temp / hper + "";
            }
            h += "小时";
        }
        temp = temp % hper;

        if (temp / mper > 0) {
            if (isFormat) {
                m = temp / mper < 10 ? "0" + temp / mper : temp / mper + "";
            } else {
                m = temp / mper + "";
            }
            m += "分";
        }
        temp = temp % mper;

        if (temp / sper > 0) {
            if (isFormat) {
                s = temp / sper < 10 ? "0" + temp / sper : temp / sper + "";
            } else {
                s = temp / sper + "";
            }
            s += "秒";
        }
        temp = temp % sper;
        mi = temp + "";

        if (isFormat) {
            if (temp < 100 && temp >= 10) {
                mi = "0" + temp;
            }
            if (temp < 10) {
                mi = "00" + temp;
            }
        }

        mi += "毫秒";
        return h + m + s + mi;
    }

    /**
     * 把一个毫秒数转化成时间字符串。格式为小时/分/秒/毫秒（如：24903600 --> 06小时55分03秒）。
     *
     * @param millis   要转化的毫秒数。
     * @param isWhole  是否强制全部显示小时/分/秒/毫秒。
     * @param isFormat 时间数字是否要格式化，如果true：少位数前面补全；如果false：少位数前面不补全。
     * @return 返回时间字符串：小时/分/秒/毫秒的格式（如：24903600 --> 06小时55分03秒）。
     * @author wangjie
     */
    public static String millisToStringMiddle(long millis, boolean isWhole, boolean isFormat) {
        return millisToStringMiddle(millis, isWhole, isFormat, "小时", "分钟", "秒");
    }

    public static String millisToStringMiddle(long millis, boolean isWhole, boolean isFormat, String hUnit, String mUnit, String sUnit) {
        String h = "";
        String m = "";
        String s = "";
        if (isWhole) {
            h = isFormat ? "00" + hUnit : "0" + hUnit;
            m = isFormat ? "00" + mUnit : "0" + mUnit;
            s = isFormat ? "00" + sUnit : "0" + sUnit;
        }

        long temp = millis;

        long hper = 60 * 60 * 1000;
        long mper = 60 * 1000;
        long sper = 1000;

        if (temp / hper > 0) {
            if (isFormat) {
                h = temp / hper < 10 ? "0" + temp / hper : temp / hper + "";
            } else {
                h = temp / hper + "";
            }
            h += hUnit;
        }
        temp = temp % hper;

        if (temp / mper > 0) {
            if (isFormat) {
                m = temp / mper < 10 ? "0" + temp / mper : temp / mper + "";
            } else {
                m = temp / mper + "";
            }
            m += mUnit;
        }
        temp = temp % mper;

        if (temp / sper > 0) {
            if (isFormat) {
                s = temp / sper < 10 ? "0" + temp / sper : temp / sper + "";
            } else {
                s = temp / sper + "";
            }
            s += sUnit;
        }
        return h + m + s;
    }

    /**
     * 把一个毫秒数转化成时间字符串。格式为小时/分/秒/毫秒（如：24903600 --> 06小时55分钟）。
     *
     * @param millis   要转化的毫秒数。
     * @param isWhole  是否强制全部显示小时/分。
     * @param isFormat 时间数字是否要格式化，如果true：少位数前面补全；如果false：少位数前面不补全。
     * @return 返回时间字符串：小时/分/秒/毫秒的格式（如：24903600 --> 06小时55分钟）。
     * @author wangjie
     */
    public static String millisToStringShort(long millis, boolean isWhole, boolean isFormat) {
        String h = "";
        String m = "";
        if (isWhole) {
            h = isFormat ? "00小时" : "0小时";
            m = isFormat ? "00分钟" : "0分钟";
        }

        long temp = millis;

        long hper = 60 * 60 * 1000;
        long mper = 60 * 1000;
        long sper = 1000;

        if (temp / hper > 0) {
            if (isFormat) {
                h = temp / hper < 10 ? "0" + temp / hper : temp / hper + "";
            } else {
                h = temp / hper + "";
            }
            h += "小时";
        }
        temp = temp % hper;

        if (temp / mper > 0) {
            if (isFormat) {
                m = temp / mper < 10 ? "0" + temp / mper : temp / mper + "";
            } else {
                m = temp / mper + "";
            }
            m += "分钟";
        }

        return h + m;
    }

    /**
     * 把日期毫秒转化为字符串。默认格式：yyyy-MM-dd HH:mm:ss。
     *
     * @param millis 要转化的日期毫秒数。
     * @return 返回日期字符串（如："2013-02-19 11:48:31"）。
     * @author wangjie
     */
    public static String millisToStringDate(long millis) {
        return millisToStringDate(millis, "yyyy-MM-dd HH:mm:ss");
    }

    //时间戳转化为Sting或Date
    public static String millisToStringDateTwo(String seconds, String format) {

        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 把日期毫秒转化为字符串。
     *
     * @param millis  要转化的日期毫秒数。
     * @param pattern 要转化为的字符串格式（如：yyyy-MM-dd HH:mm:ss）。
     * @return 返回日期字符串。
     * @author wangjie
     */
    public static String millisToStringDate(long millis, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(Long.valueOf(millis + "000")));
    }

    /**
     * 把日期毫秒转化为字符串（文件名）。
     *
     * @param millis  要转化的日期毫秒数。
     * @param pattern 要转化为的字符串格式（如：yyyy-MM-dd HH:mm:ss）。
     * @return 返回日期字符串（yyyy_MM_dd_HH_mm_ss）。
     * @author wangjie
     */
    public static String millisToStringFilename(long millis, String pattern) {
        String dateStr = millisToStringDate(millis, pattern);
        return dateStr.replaceAll("[- :]", "_");
    }

    /**
     * 把日期毫秒转化为字符串（文件名）。
     *
     * @param millis 要转化的日期毫秒数。
     * @return 返回日期字符串（yyyy_MM_dd_HH_mm_ss）。
     * @author wangjie
     */
    public static String millisToStringFilename(long millis) {
        String dateStr = millisToStringDate(millis, "yyyy-MM-dd HH:mm:ss");
        return dateStr.replaceAll("[- :]", "_");
    }


    public static long oneHourMillis = 60 * 60 * 1000; // 一小时的毫秒数
    public static long oneDayMillis = 24 * oneHourMillis; // 一天的毫秒数
    public static long oneYearMillis = 365 * oneDayMillis; // 一年的毫秒数

    /**
     * 时间格式：
     * 1小时内用，多少分钟前；
     * 超过1小时，显示时间而无日期；
     * 如果是昨天，则显示昨天
     * 超过昨天再显示日期；
     * 超过1年再显示年。
     *
     * @param millis
     * @return
     */
    public static String millisToLifeString(long millis) {
        long now = System.currentTimeMillis();
        long todayStart = string2Millis(millisToStringDate(now, "yyyy-MM-dd"), "yyyy-MM-dd");

        if (now - millis <= oneHourMillis && now - millis > 0l) { // 一小时内
            String m = millisToStringShort(now - millis, false, false);
            return "".equals(m) ? "1分钟内" : m + "前";
        }

        if (millis >= todayStart && millis <= oneDayMillis + todayStart) { // 大于今天开始开始值，小于今天开始值加一天（即今天结束值）
            return "今天 " + millisToStringDate(millis, "HH:mm");
        }

        if (millis > todayStart - oneDayMillis) { // 大于（今天开始值减一天，即昨天开始值）
            return "昨天 " + millisToStringDate(millis, "HH:mm");
        }

        long thisYearStart = string2Millis(millisToStringDate(now, "yyyy"), "yyyy");
        if (millis > thisYearStart) { // 大于今天小于今年
            return millisToStringDate(millis, "MM月dd日 HH:mm");
        }

        return millisToStringDate(millis, "yyyy年MM月dd日 HH:mm");
    }

    /**
     * 时间格式：
     * 今天，显示时间而无日期；
     * 如果是昨天，则显示昨天
     * 超过昨天再显示日期；
     * 超过1年再显示年。
     *
     * @param millis
     * @return
     */
    public static String millisToLifeString2(long millis) {
        long now = System.currentTimeMillis();
        long todayStart = string2Millis(millisToStringDate(now, "yyyy-MM-dd"), "yyyy-MM-dd");

        if (millis > todayStart + oneDayMillis && millis < todayStart + 2 * oneDayMillis) { // 明天
            return "明天" + millisToStringDate(millis, "HH:mm");
        }
        if (millis > todayStart + 2 * oneDayMillis && millis < todayStart + 3 * oneDayMillis) { // 后天
            return "后天" + millisToStringDate(millis, "HH:mm");
        }

        if (millis >= todayStart && millis <= oneDayMillis + todayStart) { // 大于今天开始开始值，小于今天开始值加一天（即今天结束值）
            return "今天 " + millisToStringDate(millis, "HH:mm");
        }

        if (millis > todayStart - oneDayMillis && millis < todayStart) { // 大于（今天开始值减一天，即昨天开始值）
            return "昨天 " + millisToStringDate(millis, "HH:mm");
        }

        long thisYearStart = string2Millis(millisToStringDate(now, "yyyy"), "yyyy");
        if (millis > thisYearStart) { // 大于今天小于今年
            return millisToStringDate(millis, "MM月dd日 HH:mm");
        }

        return millisToStringDate(millis, "yyyy年MM月dd日 HH:mm");
    }

    /**
     * 时间格式：
     * 今天，显示时间而无日期；
     * 如果是昨天，则显示昨天
     * 超过昨天再显示日期；
     * 超过1年再显示年。
     */
    public static String millisToLifeString3(long millis) {
        long now = System.currentTimeMillis();
        long todayStart = string2Millis(millisToStringDate(now, "yyyy-MM-dd"), "yyyy-MM-dd");

        if (millis > todayStart + oneDayMillis && millis < todayStart + 2 * oneDayMillis) { // 明天
            return "明天";
        }
        if (millis > todayStart + 2 * oneDayMillis && millis < todayStart + 3 * oneDayMillis) { // 后天
            return "后天";
        }

        if (millis >= todayStart && millis <= oneDayMillis + todayStart) { // 大于今天开始开始值，小于今天开始值加一天（即今天结束值）
            return millisToStringDate(millis, "HH:mm");
        }

        if (millis > todayStart - oneDayMillis && millis < todayStart) { // 大于（今天开始值减一天，即昨天开始值）
            return "昨天 ";
        }

        return millisToStringDate(millis, "MM月dd日");
    }

    /**
     * 字符串解析成毫秒数
     *
     * @param str
     * @param pattern
     * @return
     */
    public static long string2Millis(String str, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        long millis = 0;
        try {
            millis = format.parse(str).getTime();
        } catch (ParseException e) {
            L.e(TAG, e);
        }
        return millis;
    }

    /**
     * 获得今天开始的毫秒值
     *
     * @return
     */
    public static long getTodayStartMillis() {
//        String dateStr = millisToStringDate(System.currentTimeMillis(), "yyyy-MM-dd");
//        return string2Millis(dateStr, "yyyy-MM-dd");
        return getOneDayStartMillis(System.currentTimeMillis());
    }

    public static long getOneDayStartMillis(long millis) {
        String dateStr = millisToStringDate(millis, "yyyy-MM-dd");
        return string2Millis(dateStr, "yyyy-MM-dd");
    }


    /**
     * 以下来源于聊天的timeinfo工具类中
     */
    private static final long MILLIS_PER_DAY = 86400000L; // 86400000=24*60*60*1000
    // 一天
    private static final long INTERVAL_IN_MILLISECONDS = 30000L;

    public static String getTimestampString(Date paramDate) {
        String str = null;
        long l1 = paramDate.getTime();
        long l2 = System.currentTimeMillis();
        if (isSameDay(l1, l2)) {
            Calendar localCalendar = GregorianCalendar.getInstance();
            localCalendar.setTime(paramDate);
            int i = localCalendar.get(11);
            if (i > 17)
                str = "晚上 hh:mm";
            else if ((i >= 0) && (i <= 6))
                str = "凌晨 hh:mm";
            else if ((i > 11) && (i <= 17))
                str = "下午 hh:mm";
            else
                str = "上午 hh:mm";
        } else if (isYesterday(l1, l2)) {
            str = "昨天 HH:mm";
        } else {
            str = "M月d日 HH:mm";
        }
        return new SimpleDateFormat(str, Locale.CHINA).format(paramDate);
    }

    public static boolean isCloseEnough(long paramLong1, long paramLong2) {
        long l = paramLong1 - paramLong2;
        if (l < 0L)
            l = -l;
        return l < 30000L;
    }

    public static boolean isSameDay(long paramLong1, long paramLong2) {
        long l1 = paramLong1 / 86400000L;
        long l2 = paramLong2 / 86400000L;
        return l1 == l2;
    }

    public static boolean isYesterday(long paramLong1, long paramLong2) {
        long l1 = paramLong1 / 86400000L;
        long l2 = paramLong2 / 86400000L;
        return l1 + 1L == l2;
    }

    public static Date StringToDate(String paramString1, String paramString2) {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(paramString2);
        Date localDate = null;
        try {
            localDate = localSimpleDateFormat.parse(paramString1);
        } catch (ParseException localParseException) {
            localParseException.printStackTrace();
        }
        return localDate;
    }

    public static String toTime(int paramInt) {
        paramInt /= 1000;
        int i = paramInt / 60;
        int j = 0;
        if (i >= 60) {
            j = i / 60;
            i %= 60;
        }
        int k = paramInt % 60;
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(i), Integer.valueOf(k)});
    }

    public static String toTimeBySecond(int paramInt) {
        int i = paramInt / 60;
        int j = 0;
        if (i >= 60) {
            j = i / 60;
            i %= 60;
        }
        int k = paramInt % 60;
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(i), Integer.valueOf(k)});
    }

    public static TimeInfo getYesterdayStartAndEndTime() {
        Calendar localCalendar1 = Calendar.getInstance();
        localCalendar1.add(5, -1);
        localCalendar1.set(11, 0);
        localCalendar1.set(12, 0);
        localCalendar1.set(13, 0);
        localCalendar1.set(14, 0);
        Date localDate1 = localCalendar1.getTime();
        long l1 = localDate1.getTime();
        Calendar localCalendar2 = Calendar.getInstance();
        localCalendar2.add(5, -1);
        localCalendar2.set(11, 23);
        localCalendar2.set(12, 59);
        localCalendar2.set(13, 59);
        localCalendar2.set(14, 999);
        Date localDate2 = localCalendar2.getTime();
        long l2 = localDate2.getTime();
        TimeInfo localTimeInfo = new TimeInfo();
        localTimeInfo.setStartTime(l1);
        localTimeInfo.setEndTime(l2);
        return localTimeInfo;
    }

    public static TimeInfo getTodayStartAndEndTime() {
        Calendar localCalendar1 = Calendar.getInstance();
        localCalendar1.set(11, 0);
        localCalendar1.set(12, 0);
        localCalendar1.set(13, 0);
        localCalendar1.set(14, 0);
        Date localDate1 = localCalendar1.getTime();
        long l1 = localDate1.getTime();
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
        Calendar localCalendar2 = Calendar.getInstance();
        localCalendar2.set(11, 23);
        localCalendar2.set(12, 59);
        localCalendar2.set(13, 59);
        localCalendar2.set(14, 999);
        Date localDate2 = localCalendar2.getTime();
        long l2 = localDate2.getTime();
        TimeInfo localTimeInfo = new TimeInfo();
        localTimeInfo.setStartTime(l1);
        localTimeInfo.setEndTime(l2);
        return localTimeInfo;
    }

    public static TimeInfo getBeforeYesterdayStartAndEndTime() {
        Calendar localCalendar1 = Calendar.getInstance();
        localCalendar1.add(5, -2);
        localCalendar1.set(11, 0);
        localCalendar1.set(12, 0);
        localCalendar1.set(13, 0);
        localCalendar1.set(14, 0);
        Date localDate1 = localCalendar1.getTime();
        long l1 = localDate1.getTime();
        Calendar localCalendar2 = Calendar.getInstance();
        localCalendar2.add(5, -2);
        localCalendar2.set(11, 23);
        localCalendar2.set(12, 59);
        localCalendar2.set(13, 59);
        localCalendar2.set(14, 999);
        Date localDate2 = localCalendar2.getTime();
        long l2 = localDate2.getTime();
        TimeInfo localTimeInfo = new TimeInfo();
        localTimeInfo.setStartTime(l1);
        localTimeInfo.setEndTime(l2);
        return localTimeInfo;
    }

    public static TimeInfo getCurrentMonthStartAndEndTime() {
        Calendar localCalendar1 = Calendar.getInstance();
        localCalendar1.set(5, 1);
        localCalendar1.set(11, 0);
        localCalendar1.set(12, 0);
        localCalendar1.set(13, 0);
        localCalendar1.set(14, 0);
        Date localDate1 = localCalendar1.getTime();
        long l1 = localDate1.getTime();
        Calendar localCalendar2 = Calendar.getInstance();
        Date localDate2 = localCalendar2.getTime();
        long l2 = localDate2.getTime();
        TimeInfo localTimeInfo = new TimeInfo();
        localTimeInfo.setStartTime(l1);
        localTimeInfo.setEndTime(l2);
        return localTimeInfo;
    }

    public static TimeInfo getLastMonthStartAndEndTime() {
        Calendar localCalendar1 = Calendar.getInstance();
        localCalendar1.add(2, -1);
        localCalendar1.set(5, 1);
        localCalendar1.set(11, 0);
        localCalendar1.set(12, 0);
        localCalendar1.set(13, 0);
        localCalendar1.set(14, 0);
        Date localDate1 = localCalendar1.getTime();
        long l1 = localDate1.getTime();
        Calendar localCalendar2 = Calendar.getInstance();
        localCalendar2.add(2, -1);
        localCalendar2.set(5, 1);
        localCalendar2.set(11, 23);
        localCalendar2.set(12, 59);
        localCalendar2.set(13, 59);
        localCalendar2.set(14, 999);
        localCalendar2.roll(5, -1);
        Date localDate2 = localCalendar2.getTime();
        long l2 = localDate2.getTime();
        TimeInfo localTimeInfo = new TimeInfo();
        localTimeInfo.setStartTime(l1);
        localTimeInfo.setEndTime(l2);
        return localTimeInfo;
    }

    /**
     * 返回时间戳，精确毫秒
     *
     * @return 格式：1413442915492
     */
    public static String getTimestampStr() {

        return Long.toString(System.currentTimeMillis());
    }

    /**
     * 把时间戳变成普通时间
     *
     * @param timestampString
     * @return 格式：2014-10-16 15:01:55:492
     */
    public static String TimeStamp2Date2(String timestampString) {
        Long timestamp = Long.parseLong(timestampString);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date(timestamp));
        return date;
    }


    /**
     * 获取昨天 yyyy-MM-dd
     *
     * @return
     */
    public static String getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return yesterday;
    }


    public static int getAge(Date birthDay) throws Exception {
        //获取当前系统时间
        Calendar cal = Calendar.getInstance();
        //如果出生日期大于当前时间，则抛出异常
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        //取出系统当前时间的年、月、日部分
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        //将日期设置为出生日期
        cal.setTime(birthDay);
        //取出出生日期的年、月、日部分
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        //当前年份与出生年份相减，初步计算年龄
        int age = yearNow - yearBirth;
        //当前月份与出生日期的月份相比，如果月份小于出生月份，则年龄上减1，表示不满多少周岁
        if (monthNow <= monthBirth) {
            //如果月份相等，在比较日期，如果当前日，小于出生日，也减1，表示不满多少周岁
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        System.out.println("age:" + age);
        return age;
    }


    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        return year;
    }

    public static int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        return month;
    }

    public static int getDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    public static boolean compareDate(String date1, Date d2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = format.parse(date1);
            return d1.before(d2);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
