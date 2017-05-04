package com.udacity.stockhawk.ui;


import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.PrefUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartValueSelectedView extends MarkerView {

    @BindView(R.id.tv_chart_selected_value)
    TextView selectedValue;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public ChartValueSelectedView(Context context, int layoutResource) {
        super(context, layoutResource);
        ButterKnife.bind(this);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        Locale locale = PrefUtils.getCurrentLocale(getContext());
        String value = String.format(locale, "$%.2f", e.getY());
        selectedValue.setText(value);
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -(getHeight() + 20f));
    }
}
