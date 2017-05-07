package com.udacity.stockhawk.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.FormatUtils;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.ui.DetailsActivity;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StocksWidgetRemoteViewsService extends RemoteViewsService {

    private final String[] PROJECTION_COLUMNS = {
            Contract.Quote._ID,
            Contract.Quote.COLUMN_SYMBOL,
            Contract.Quote.COLUMN_PRICE,
            Contract.Quote.COLUMN_ABSOLUTE_CHANGE,
            Contract.Quote.COLUMN_PERCENTAGE_CHANGE
    };

    private final int COLUMN_ID_INDEX = 0;
    private final int COLUMN_SYMBOL_INDEX = 1;
    private final int COLUMN_PRICE_INDEX = 2;
    private final int COLUMN_ABSOLUTE_CHANGE_INDEX = 3;
    private final int COLUMN_PERCENTAGE_CHANGE_INDEX = 4;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                onDestroy();

                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(Contract.Quote.URI,
                        PROJECTION_COLUMNS,
                        null,
                        null,
                        Contract.Quote.COLUMN_SYMBOL + " ASC");
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if(data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return (data == null) ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int i) {
                if (i == AdapterView.INVALID_POSITION || data == null || !data.moveToPosition(i)) {
                    return null;
                }

                String symbol = data.getString(COLUMN_SYMBOL_INDEX);
                float price = data.getFloat(COLUMN_PRICE_INDEX);

                String formattedChange = null;
                float change;

                if (PrefUtils.getDisplayMode(StocksWidgetRemoteViewsService.this)
                        .equals(getString(R.string.pref_display_mode_absolute_key))) {
                    change = data.getFloat(COLUMN_ABSOLUTE_CHANGE_INDEX);
                    formattedChange = FormatUtils.absoluteChangeToString(StocksWidgetRemoteViewsService.this, change);
                } else {
                    change = data.getFloat(COLUMN_PERCENTAGE_CHANGE_INDEX);
                    formattedChange = FormatUtils.percentageChangeToString(StocksWidgetRemoteViewsService.this, change);
                }

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_list_item_quote);

                views.setTextViewText(R.id.symbol, symbol);
                views.setTextViewText(R.id.price, FormatUtils.priceToString(StocksWidgetRemoteViewsService.this, price));
                views.setTextViewText(R.id.change, formattedChange);

                int colorId = FormatUtils.getChangeDisplayBackgroundColorId(change);
                views.setInt(R.id.change, getString(R.string.set_background_resource), colorId);

                Intent intent = new Intent();
                intent.putExtra(getString(R.string.extra_stock_symbol_key), symbol);
                views.setOnClickFillInIntent(R.id.widget_list_item, intent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list_item_quote);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                if (data.moveToPosition(i))
                    return data.getLong(COLUMN_ID_INDEX);
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }

}
