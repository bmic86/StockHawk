package com.udacity.stockhawk.data.entities;


import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

public class StockValueInTime implements Comparable<StockValueInTime> {

    private long timeStamp;
    private float value;

    public StockValueInTime(long timeStamp, float value) {
       this.timeStamp = timeStamp;
       this.value = value;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float getValue() {
        return value;
    }

    public static ArrayList<StockValueInTime> parseHistoryData(String history) {
        ArrayList<StockValueInTime> result = new ArrayList<>();
        String[] records = history.split("\n");
        for (String record : records) {
            String[] values = record.split(",");
            if(values.length == 2) {
                long time = Long.parseLong(values[0]);
                float price = Float.parseFloat(values[1]);
                result.add(new StockValueInTime(time, price));
            } else {
                Log.e("StockValueInTime", "Error");
            }
        }
        return result;
    }

    @Override
    public int compareTo(@NonNull StockValueInTime stockValueInTime) {
        return Long.compare(getTimeStamp(), stockValueInTime.getTimeStamp()) ;
    }
}
