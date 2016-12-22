package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import static android.R.attr.x;

public class StockDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        Intent i = getIntent();
        String symbol = i.getExtras().getString("symbol");
//        Toast.makeText(this, i.getExtras().getString("symbol") + "", Toast.LENGTH_SHORT).show();

        Cursor c = getContentResolver().query(
                Contract.Quote.URI,
                Contract.Quote.QUOTE_COLUMNS,
                Contract.Quote.COLUMN_SYMBOL + "= ?",
                new String[]{symbol},
                null
        );

        if (c != null) {
            c.moveToFirst();
            Toast.makeText(this, c.getString(c.getColumnIndex(Contract.Quote.COLUMN_HISTORY)) + " ... ", Toast.LENGTH_SHORT).show();
        }
    }
}
