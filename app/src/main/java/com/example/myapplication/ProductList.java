package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.myapplication.entity.ProductListTable;

import java.util.List;

public class ProductList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.productList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Загрузка и отображение продуктов
        loadProducts();
    }

    private void loadProducts() {
        TableLayout tableLayout = findViewById(R.id.tableProducts);

        // Очищаем таблицу (кроме заголовка)
        int childCount = tableLayout.getChildCount();
        if (childCount > 1) {
            tableLayout.removeViews(1, childCount - 1);
        }

        // Получаем продукты из базы данных
        MyApp.database.ProductListDao().getAllProducts().observe(this, products -> {
            // Добавляем каждый продукт в таблицу
            for (int i = 0; i < products.size(); i++) {
                ProductListTable product = products.get(i);

                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                row.setPadding(8, 8, 8, 8);

                // Номер
                TextView numTextView = new TextView(this);
                numTextView.setText(String.valueOf(i + 1));
                numTextView.setMinWidth(30);
                row.addView(numTextView);

                // Название
                TextView nameTextView = new TextView(this);
                nameTextView.setText(product.name);
                nameTextView.setLayoutParams(new TableRow.LayoutParams(
                        0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                nameTextView.setMinWidth(150);
                row.addView(nameTextView);

                // Категория
                TextView categoryTextView = new TextView(this);
                categoryTextView.setText(product.category);
                categoryTextView.setMinWidth(50);
                row.addView(categoryTextView);

                // Единица измерения
                TextView unitTextView = new TextView(this);
                unitTextView.setText(product.unit);
                unitTextView.setMinWidth(50);
                TableRow.LayoutParams params = new TableRow.LayoutParams();
                params.setMargins(5, 0, 0, 0); // left, top, right, bottom
                unitTextView.setLayoutParams(params);
                row.addView(unitTextView);

                // Количество
                TextView quantityTextView = new TextView(this);
                quantityTextView.setText(String.valueOf(product.quantity));
                quantityTextView.setMinWidth(50);
                row.addView(quantityTextView);

                tableLayout.addView(row);
            }
        });
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void plus(View v) {
        Intent intent = new Intent(this, AddNewProduct.class);
        startActivity(intent);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Обновляем список при возвращении на экран
//        loadProducts();
//    }
}