package com.jln.wguprogresstracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM-dd-yyyy h:mm a z", Locale.getDefault());

    public static long getDateTimestamp(String dateInput) {
        try {
            Date date = DateUtil.dateTimeFormat.parse(dateInput + TimeZone.getDefault().getDisplayName());
            return date.getTime();
        }
        catch (ParseException e) {
            return 0;
        }
    }

    public static long getDate(String dateInput) {
        try {
            Date date = DateUtil.dateFormat.parse(dateInput + TimeZone.getDefault().getDisplayName());
            date.setTime(date.getTime() +  60*1000*60*10);
            return date.getTime();
        }
        catch (ParseException e) {
            return 0;
        }
    }

    public static long todayLong() {
        String currentDate = DateUtil.dateFormat.format(new Date());
        return getDateTimestamp(currentDate);
    }

    public static long todayLongWithTime() {
        return System.currentTimeMillis();
    }
}
