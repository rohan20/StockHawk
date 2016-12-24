package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.x;

public class StockDetailActivity extends AppCompatActivity {

    GraphView mStockHistoryGraphView;
    TextView mStockNameTextView;
    static final int NUMBER_OF_VALUES_FOR_GRAPH = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        mStockHistoryGraphView = (GraphView) findViewById(R.id.detail_graph_view);
        mStockNameTextView = (TextView) findViewById(R.id.detail_stock_name_text_view);

        Intent intent = getIntent();
        String symbol = intent.getExtras().getString("symbol");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        Cursor c = getContentResolver().query(
                Contract.Quote.URI,
                Contract.Quote.QUOTE_COLUMNS,
                Contract.Quote.COLUMN_SYMBOL + "= ?",
                new String[]{symbol},
                null
        );

        if (c != null) {
            c.moveToFirst();
            String stocksHistoryString = c.getString(c.getColumnIndex(Contract.Quote.COLUMN_HISTORY));
            mStockNameTextView.setText(symbol);

            String lines[] = stocksHistoryString.split("\\r?\\n");
            String[] stockValue = new String[lines.length];
            String[] stockDate = new String[lines.length];

            for (int i = 0; i < lines.length; i++) {
                String stockValueCommaStockDateString[] = lines[i].split(",[ ]*");
                stockDate[i] = stockValueCommaStockDateString[0];
                stockValue[i] = stockValueCommaStockDateString[1];
            }

            int n = Math.min(lines.length, NUMBER_OF_VALUES_FOR_GRAPH);
            DataPoint[] dataPoints = new DataPoint[n];
            for (int j = 0; j < n; j++) {
                dataPoints[j] = new DataPoint(new Date(Long.parseLong(stockDate[j])), Double.parseDouble(stockValue[j]));
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);

            // styling series
            series.setTitle("Stock value");
            series.setColor(Color.GREEN);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(10);
            series.setThickness(8);

            mStockHistoryGraphView.addSeries(series);

            // set date label formatter
            mStockHistoryGraphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
            mStockHistoryGraphView.getGridLabelRenderer().setNumHorizontalLabels(3);

            // as we use dates as labels, the human rounding to nice readable numbers is not necessary
            mStockHistoryGraphView.getGridLabelRenderer().setHumanRounding(false);

            mStockHistoryGraphView.getLegendRenderer().setVisible(true);
            mStockHistoryGraphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

            mStockHistoryGraphView.getViewport().setScrollable(true); // enables horizontal scrolling
            mStockHistoryGraphView.getViewport().setScrollableY(true); // enables vertical scrolling
            mStockHistoryGraphView.getViewport().setScalable(true); // enables horizontal zooming and scrolling
            mStockHistoryGraphView.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
