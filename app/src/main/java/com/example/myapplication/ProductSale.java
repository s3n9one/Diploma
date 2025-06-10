package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.ProductListTable;
import com.example.myapplication.entity.SaleProductTable;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ProductSale extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_sale);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.productSale), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loadSales();
    }

    private void loadSales() {
        TableLayout tableLayout = findViewById(R.id.tableProductsSale);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        int childCount = tableLayout.getChildCount();
        if (childCount > 1) {
            tableLayout.removeViews(1, childCount - 1);
        }

        MyApp.database.SaleProductDao().getAllSales().observe(this, sales -> {
            Collections.sort(sales, (o1, o2) -> o1.saleDate.compareTo(o2.saleDate));

            int runningTotal = 0;
            String currentDate = "";

            for (int i = 0; i < sales.size(); i++) {
                SaleProductTable sale = sales.get(i);
                String saleDate = dateFormat.format(sale.saleDate);
                int rowTotal = sale.saleQuantity * sale.salePrice;

                if (!saleDate.equals(currentDate)) {
                    runningTotal = 0;
                    currentDate = saleDate;
                }

                runningTotal += rowTotal;

                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                row.setPadding(8, 8, 8, 8);

                float textSize = 11;
                int padding = 4;

                // Дата
                TextView dateTextView = new TextView(this);
                dateTextView.setText(dateFormat.format(sale.saleDate));
                dateTextView.setTextSize(textSize);
                dateTextView.setPadding(padding, padding, padding, padding);
                dateTextView.setMinWidth(60);
                row.addView(dateTextView);

                // Название товара
                TextView nameTextView = new TextView(this);
                nameTextView.setText(sale.productName);
                nameTextView.setTextSize(textSize);
                nameTextView.setPadding(padding, padding, padding, padding);
                nameTextView.setLayoutParams(new TableRow.LayoutParams(
                        0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                nameTextView.setMinWidth(150);
                row.addView(nameTextView);

                // Количество
                TextView quantityTextView = new TextView(this);
                quantityTextView.setText(String.valueOf(sale.saleQuantity));
                quantityTextView.setTextSize(textSize);
                quantityTextView.setPadding(padding, padding, padding, padding);
                quantityTextView.setMinWidth(25);
                row.addView(quantityTextView);

                // Цена
                TextView priceTextView = new TextView(this);
                priceTextView.setText(String.valueOf(sale.salePrice));
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

                // Способ оплаты
                TextView paymentMethodTextView = new TextView(this);
                paymentMethodTextView.setText(String.valueOf(sale.paymentMethod));
                paymentMethodTextView.setTextSize(textSize);
                paymentMethodTextView.setPadding(padding, padding, padding, padding);
                paymentMethodTextView.setMinWidth(60);
                row.addView(paymentMethodTextView);

                // Выручка
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
        finish();
    }

    public void plus(View v) {
        startActivity(new Intent(this, AddProductSale.class));
    }
}