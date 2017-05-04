package com.udacity.stockhawk.ui;

import android.content.Context;
import android.os.Build;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.data.PrefUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public class DateAxisValueFormatter implements IAxisValueFormatter {

    private Context context;
    private long minTimeStamp = Long.MAX_VALUE;

    public DateAxisValueFormatter(Context context, long minTimeStamp) {
        this.context = context;
        this.minTimeStamp = minTimeStamp;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        DateTime baseDate = new DateTime(minTimeStamp);
        DateTime result = baseDate.plusDays((int)value);

        Locale locale = PrefUtils.getCurrentLocale(context);
        DateTimeFormatter formater = DateTimeFormat.shortDate().withLocale(locale);
        return result.toString(formater);
    }
}
