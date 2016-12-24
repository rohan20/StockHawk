package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;

/**
 * Created by Rohan on 24-Dec-16.
 */

public class StocksWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetID : appWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

            Intent widgetIntent = new Intent(context, StocksWidgetService.class);
            remoteViews.setRemoteAdapter(R.id.widget_stocks_list_view, widgetIntent);
            remoteViews.setTextViewText(R.id.widget_title_text_view, context.getString(R.string.app_name));

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_stocks_list_view);
            appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
