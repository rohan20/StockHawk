package com.udacity.stockhawk.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Rohan on 25-Dec-16.
 */

public class StocksWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StocksRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

