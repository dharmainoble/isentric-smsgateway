//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    static GregorianCalendar calendar = new GregorianCalendar();

    public static Date getStartDate(Date date) {
        calendar.setTime(date);
        calendar.set(11, calendar.getActualMinimum(11));
        calendar.set(12, calendar.getActualMinimum(12));
        calendar.set(13, calendar.getActualMinimum(13));
        calendar.set(14, calendar.getActualMinimum(14));
        return (Date)calendar.getTime().clone();
    }

    public static Date getEndDate(Date date) {
        calendar.setTime(date);
        calendar.set(11, calendar.getActualMaximum(11));
        calendar.set(12, calendar.getActualMaximum(12));
        calendar.set(13, calendar.getActualMaximum(13));
        calendar.set(14, calendar.getActualMaximum(14));
        return (Date)calendar.getTime().clone();
    }

    public static Date getNextDay(Date date) {
        calendar.setTime(date);
        calendar.add(5, 1);
        return (Date)calendar.getTime().clone();
    }

    public static Date getNextNDay(Date date, int n) {
        calendar.setTime(date);
        calendar.add(5, n);
        return (Date)calendar.getTime().clone();
    }

    public static Date getBeginOfMonth(Date date, int months) {
        calendar.setTime(date);
        if (months != 0) {
            calendar.add(2, months);
        }

        calendar.set(5, calendar.getActualMinimum(5));
        return (Date)calendar.getTime().clone();
    }

    public static Date getEndOfMonth(Date date, int months) {
        calendar.setTime(date);
        if (months != 0) {
            calendar.add(2, months);
        }

        calendar.set(5, calendar.getActualMaximum(5));
        return (Date)calendar.getTime().clone();
    }

    public static Date getBeginOfYear(Date date) {
        calendar.setTime(date);
        calendar.set(2, calendar.getActualMinimum(2));
        calendar.set(5, calendar.getActualMinimum(5));
        calendar.set(11, calendar.getActualMinimum(11));
        calendar.set(12, calendar.getActualMinimum(12));
        calendar.set(13, calendar.getActualMinimum(13));
        calendar.set(14, calendar.getActualMinimum(14));
        return (Date)calendar.getTime().clone();
    }

    public static Date getEndOfYear(Date date) {
        calendar.setTime(date);
        calendar.set(2, calendar.getActualMaximum(2));
        calendar.set(5, calendar.getActualMaximum(5));
        calendar.set(11, calendar.getActualMaximum(11));
        calendar.set(12, calendar.getActualMaximum(12));
        calendar.set(13, calendar.getActualMaximum(13));
        calendar.set(14, calendar.getActualMaximum(14));
        return (Date)calendar.getTime().clone();
    }

    public static int getYearOfDate(Date date) {
        return calendar.get(1);
    }

    public static Date getEndOfYear(int year) {
        calendar.set(1, year);
        calendar.set(2, calendar.getActualMaximum(2));
        calendar.set(5, calendar.getActualMaximum(5));
        calendar.set(11, calendar.getActualMaximum(11));
        calendar.set(12, calendar.getActualMaximum(12));
        calendar.set(13, calendar.getActualMaximum(13));
        calendar.set(14, calendar.getActualMaximum(14));
        return (Date)calendar.getTime().clone();
    }

    public static int monthsBetween(Date startDate, Date endDate) {
        calendar.setTime(startDate);
        int startMonth = calendar.get(2);
        int startYear = calendar.get(1);
        calendar.setTime(endDate);
        int endMonth = calendar.get(2);
        int endYear = calendar.get(1);
        return (endYear - startYear) * 12 + (endMonth - startMonth);
    }

    public static int daysBetween(Date startDate, Date endDate) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(startDate);
        c2.setTime(endDate);
        return daysBetween(c1, c2);
    }

    public static int daysBetween(Calendar early, Calendar late) {
        return (int)(toJulian(late) - toJulian(early));
    }

    public static final float toJulian(Calendar c) {
        int Y = c.get(1);
        int M = c.get(2);
        int D = c.get(5);
        int A = Y / 100;
        int B = A / 4;
        int C = 2 - A + B;
        float E = (float)((int)(365.25F * (float)(Y + 4716)));
        float F = (float)((int)(30.6001F * (float)(M + 1)));
        float JD = (float)(C + D) + E + F - 1524.5F;
        return JD;
    }

    public static String getDateYYYYMMDDHHMMSS(Date date) {
        if (date != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(date);
        } else {
            return "";
        }
    }

    public static String getDateYYYYMMDD(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
