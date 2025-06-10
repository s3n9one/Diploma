package com.example.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.ProductArrivalTable;
import com.example.myapplication.entity.ProductListTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductArrival extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_arrival);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.productArrival), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loadProducts();
    }

    private void loadProducts() {
        TableLayout tableLayout = findViewById(R.id.tableProductsArrival);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        // Очищаем таблицу (кроме заголовка)
        int childCount = tableLayout.getChildCount();
        if (childCount > 1) {
            tableLayout.removeViews(1, childCount - 1);
        }

        // Получаем закупки из базы данных
        MyApp.database.ProductArrivalDao().getAllArrivals().observe(this, arrivals -> {
            Collections.sort(arrivals, (o1, o2) -> o1.arrivalDate.compareTo(o2.arrivalDate));

            int runningTotal = 0;
            String currentDate = "";

            for (int i = 0; i < arrivals.size(); i++) {
                ProductArrivalTable arrival = arrivals.get(i);
                String arrivalDate = dateFormat.format(arrival.arrivalDate);
                int rowTotal = arrival.quantity * arrival.buyPrice;

                if (!arrivalDate.equals(currentDate)) {
                    runningTotal = 0;
                    currentDate = arrivalDate;
                }

                runningTotal += rowTotal;

                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                row.setPadding(8, 8, 8, 8);

                // Общие параметры для TextView
                float textSize = 11;
                int padding = 4;

                // Дата
                TextView dateTextView = new TextView(this);
                dateTextView.setText(dateFormat.format(arrival.arrivalDate));
                dateTextView.setTextSize(textSize);
                dateTextView.setPadding(padding, padding, padding, padding);
                dateTextView.setMinWidth(70);
                row.addView(dateTextView);

                // Название товара
                TextView nameTextView = new TextView(this);
                nameTextView.setText(arrival.productName);
                nameTextView.setTextSize(textSize);
                nameTextView.setPadding(padding, padding, padding, padding);
                nameTextView.setLayoutParams(new TableRow.LayoutParams(
                        0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                nameTextView.setMinWidth(100);
                row.addView(nameTextView);

                // Количество
                TextView quantityTextView = new TextView(this);
                quantityTextView.setText(String.valueOf(arrival.quantity));
                quantityTextView.setTextSize(textSize);
                quantityTextView.setPadding(padding, padding, padding, padding);
                quantityTextView.setMinWidth(25);
                row.addView(quantityTextView);

                // Цена
                TextView priceTextView = new TextView(this);
                priceTextView.setText(String.valueOf(arrival.buyPrice));
                priceTextView.setTextSize(textSize);
                priceTextView.setPadding(padding, padding, padding, padding);
                priceTextView.setMinWidth(60);
                row.addView(priceTextView);

                // Итог
                TextView rowTotalTextView = new TextView(this);
                rowTotalTextView.setText(String.valueOf(rowTotal));
                rowTotalTextView.setTextSize(textSize);
                rowTotalTextView.setPadding(padding, padding, padding, padding);
                rowTotalTextView.setMinWidth(60);
                row.addView(rowTotalTextView);

                // Затраты
                TextView runningTotalTextView = new TextView(this);
                runningTotalTextView.setText(String.valueOf(runningTotal));
                runningTotalTextView.setTextSize(textSize);
                runningTotalTextView.setPadding(padding, padding, padding, padding);
                runningTotalTextView.setMinWidth(60);
                row.addView(runningTotalTextView);

                tableLayout.addView(row);
            }
        });
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void plus(View v) {
        Intent intent = new Intent(this, AddProductArrival.class);
        startActivity(intent);
    }
}