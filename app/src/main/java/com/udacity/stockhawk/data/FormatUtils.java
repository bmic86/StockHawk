package com.udacity.stockhawk.data;


import android.content.Context;
import android.os.Build;

import com.udacity.stockhawk.R;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public class FormatUtils {

    private static Locale getCurrentLocale(Context context) {
        Locale locale;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }

        if(locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }

    public static String priceToString(Context context, float price) {
        Locale locale = getCurrentLocale(context);
        return String.format(locale, context.getString(R.string.price_format), price);
    }

    public static String absoluteChangeToString(Context context, float absoluteChange) {
       Locale locale = getCurrentLocale(context);
        return String.format(locale, context.getString(R.string.absolute_change_format), absoluteChange);
    }

    public static String percentageChangeToString(Context context, float percentageChange) {
        Locale locale = getCurrentLocale(context);
        return String.format(locale, context.getString(R.string.percentage_change_format), percentageChange / 100);
    }

    public static String dateTimeToString(Context context, DateTime dateTime) {
        Locale locale = getCurrentLocale(context);
        DateTimeFormatter formatter = DateTimeFormat.shortDate().withLocale(locale);
        return dateTime.toString(formatter);
    }

    public static int getChangeDisplayBackgroundColorId(float change) {
        return (change > 0) ? R.drawable.percent_change_pill_green : R.drawable.percent_change_pill_red;
    }
}
