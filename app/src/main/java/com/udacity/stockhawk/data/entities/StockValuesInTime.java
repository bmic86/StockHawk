package com.udacity.stockhawk.data.entities;


import com.github.mikephil.charting.data.Entry;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.Collections;

public class StockValuesInTime {

    private long minTimeStamp = Long.MAX_VALUE;
    private ArrayList<StockValueInTime> values;
    private ArrayList<Entry> entries;
    

    public StockValuesInTime() {
        values = new ArrayList<>();
        entries = new ArrayList<>();
    }

    public void add(ArrayList<StockValueInTime> vals) {
        values.addAll(vals);
    }

    public void prepareDataForChart() {
        entries = new ArrayList<>();
        
        for(int i=0; i < values.size(); ++i) {
            long currentTimeStamp = values.get(i).getTimeStamp();
            minTimeStamp = (currentTimeStamp < minTimeStamp) ? currentTimeStamp : minTimeStamp;
        }

        for(int i=0; i < values.size(); ++i) {
            long currentTimeStamp = values.get(i).getTimeStamp();
            Days diffInDays = Days.daysBetween(new DateTime(minTimeStamp), new DateTime(currentTimeStamp));
            values.get(i).setTimeStamp( diffInDays.getDays() );
        }

        Collections.sort(values);

        for(int i=0; i < values.size(); ++i) {
            entries.add(new Entry(values.get(i).getTimeStamp(), values.get(i).getValue()));
        }
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public long getMinTimeStamp() {
        return minTimeStamp;
    }
}
