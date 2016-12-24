package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

/**
 * Created by Rohan on 25-Dec-16.
 */
public class StocksRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Cursor mCursor;
    private Context mContext;
//    private int mAppWidgetID;

    public StocksRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
//        mAppWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        populateCursor();
    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null)
            mCursor.close();

        populateCursor();
    }

    @Override
    public void onDestroy() {
        if (mCursor != null)
            mCursor.close();
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        if (mCursor == null)
            populateCursor();

        mCursor.moveToPosition(i);
        remoteViews.setTextViewText(R.id.widget_item_symbol, mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL)));
        remoteViews.setTextViewText(R.id.widget_item_price, mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_PRICE)));
        String stockPercentageChange = mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE));
        remoteViews.setTextViewText(R.id.widget_item_price_change, stockPercentageChange + "%");

        if (!stockPercentageChange.contains("-")) {
            remoteViews.setInt(R.id.widget_item_price_change, mContext.getString(R.string.set_background_resource), R.drawable.percent_change_pill_green);
        } else {
            remoteViews.setInt(R.id.widget_item_price_change, mContext.getString(R.string.set_background_resource), R.drawable.percent_change_pill_red);
        }


        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void populateCursor() {

        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }

        mCursor = mContext.getContentResolver().query(
                Contract.Quote.URI,
                new String[]{Contract.Quote.COLUMN_SYMBOL, Contract.Quote.COLUMN_PRICE, Contract.Quote.COLUMN_PERCENTAGE_CHANGE},
                null,
                null,
                null
        );
    }
}
