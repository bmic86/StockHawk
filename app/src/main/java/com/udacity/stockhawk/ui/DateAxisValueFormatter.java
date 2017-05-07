package com.udacity.stockhawk.ui;

import android.content.Context;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.data.FormatUtils;

import org.joda.time.DateTime;

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
        return FormatUtils.dateTimeToString(context, result);
    }
}
