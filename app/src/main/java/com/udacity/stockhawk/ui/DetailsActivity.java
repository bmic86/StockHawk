package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.entities.StockValueInTime;
import com.udacity.stockhawk.data.entities.StockValuesInTime;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    private final String[] PROJECTION_COLUMNS = {
            Contract.Quote.COLUMN_HISTORY
    };

    private final int COLUMN_HISTORY_INDEX = 0;

    @BindView(R.id.stock_chart)
    protected LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        final String key = getString(R.string.extra_stock_symbol_key);
        String symbol = null;
        if(intent != null) {
            symbol = intent.getStringExtra(key);
        }

        if(symbol == null || symbol.isEmpty()) {
            Log.e(getClass().getSimpleName(), getString(R.string.error_stock_symbol_not_found));
            return;
        }

        chart.setDrawGridBackground(false);
        chart.setScaleEnabled(true);

        chart.getDescription().setEnabled(false);
        chart.setContentDescription(getString(R.string.chart_content_description, symbol));

        ChartValueSelectedView selectedValueView = new ChartValueSelectedView(this, R.layout.chart_value_selected);
        selectedValueView.setChartView(chart);
        chart.setMarker(selectedValueView);

        StockValuesInTime stockValues = loadData(symbol);
        stockValues.prepareDataForChart();

        DateAxisValueFormatter xFormatter = new DateAxisValueFormatter(this, stockValues.getMinTimeStamp());
        drawChart(symbol, stockValues.getEntries(), xFormatter);
    }

    private void drawChart(String symbol, ArrayList<Entry> data, DateAxisValueFormatter xFormatter) {
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            LineDataSet dataSet = (LineDataSet)chart.getData().getDataSetByIndex(0);
            dataSet.setValues(data);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        }
        else {
            XAxis xAxis = chart.getXAxis();
            xAxis.setValueFormatter(xFormatter);
            xAxis.setGranularity(1.0f);
            xAxis.setTextColor(ContextCompat.getColor(this, R.color.chart_text_color));

            chart.getAxisLeft().setTextColor(ContextCompat.getColor(this, R.color.chart_text_color));
            chart.getAxisRight().setTextColor(ContextCompat.getColor(this, R.color.chart_text_color));

            LineDataSet dataSet = new LineDataSet(data, getString(R.string.historical_data_dataset_label, symbol));
            dataSet.setDrawIcons(false);
            dataSet.setDrawFilled(true);
            dataSet.setCircleColor(ContextCompat.getColor(this,R.color.chart_circle_color));
            dataSet.setColor(ContextCompat.getColor(this,R.color.chart_line_color));
            dataSet.setFillColor(ContextCompat.getColor(this,R.color.chart_fill_color));
            dataSet.setLineWidth(2f);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);

            LineData lineData = new LineData(dataSets);
            chart.setData(lineData);

            chart.getLegend().setTextColor(ContextCompat.getColor(this, R.color.chart_text_color));
            chart.invalidate();
        }
    }

    private StockValuesInTime loadData(String symbol) {
        Uri stockUri = Contract.Quote.makeUriForStock(symbol);
        Cursor cursor = getContentResolver().query(stockUri, PROJECTION_COLUMNS, null, null, null);
        final int count = cursor.getCount();
        if(count > 0) {
            cursor.moveToFirst();
        }

        StockValuesInTime result = new StockValuesInTime();
        for(int i=0; i<count; ++i) {
            String history = cursor.getString(COLUMN_HISTORY_INDEX);
            if(history != null && !history.isEmpty()) {
                result.add(StockValueInTime.parseHistoryData(history));
            }
        }
        cursor.close();
        return result;
    }

}
