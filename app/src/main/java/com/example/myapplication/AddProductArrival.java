package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.ProductArrivalTable;
import com.example.myapplication.entity.ProductListTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddProductArrival extends AppCompatActivity {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private EditText editDateText, editCategory, editUnit, editQuantity, editPrice;
    private Spinner productSpinner;
    private List<ProductListTable> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_arrival);

        initViews();
        loadProductsIntoSpinner();
    }

    private void initViews() {
        editDateText = findViewById(R.id.editDateText);
        editCategory = findViewById(R.id.editCategory);
        editUnit = findViewById(R.id.editUnit);
        editQuantity = findViewById(R.id.editQuantity);
        editPrice = findViewById(R.id.editPrice);
        productSpinner = findViewById(R.id.mySpinner);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        editDateText.setText(sdf.format(new Date()));
    }

    private void loadProductsIntoSpinner() {
        MyApp.database.ProductListDao().getAllProducts().observe(this, products -> {
            this.products = products;
            List<String> productNames = new ArrayList<>();

            for (ProductListTable product : products) {
                if (product.quantity >= 0) {
                    productNames.add(product.name);
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    productNames
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            productSpinner.setAdapter(adapter);

            productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position >= 0 && position < products.size()) {
                        ProductListTable selectedProduct = products.get(position);
                        editCategory.setText(selectedProduct.category);
                        editUnit.setText(selectedProduct.unit);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        });
    }

    public void save(View v) {
        try {
            executor.execute(() -> {
                // Проверка выбранного товара
                int selectedPosition = productSpinner.getSelectedItemPosition();
                if (selectedPosition == AdapterView.INVALID_POSITION || products.isEmpty()) {
                    Toast.makeText(this, "Выберите товар", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Проверка заполнения полей
                if (editQuantity.getText().toString().isEmpty() ||
                        editPrice.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Получаем данные
                Date arrivalDate = parseManualDate();
                if (arrivalDate == null) return;

                ProductListTable selectedProduct = products.get(selectedPosition);
                int quantity = Integer.parseInt(editQuantity.getText().toString().trim());
                int price = Integer.parseInt(editPrice.getText().toString().trim());

                // Создаем и сохраняем приход
                ProductArrivalTable arrival = new ProductArrivalTable();
                arrival.arrivalDate = arrivalDate;
                arrival.productName = selectedProduct.name;
                arrival.quantity = quantity;
                arrival.buyPrice = price;
                arrival.idProduct = selectedProduct.id;

                // Обновляем количество
                selectedProduct.quantity = (selectedProduct.quantity == null ? 0 : selectedProduct.quantity) + quantity;

                // Сохраняем
                MyApp.database.ProductArrivalDao().insert(arrival);
                MyApp.database.ProductListDao().update(selectedProduct);

                runOnUiThread(() -> {
                    Toast.makeText(AddProductArrival.this, "Данные сохранены", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Проверьте числовые значения", Toast.LENGTH_SHORT).show();
        }
    }

    private Date parseManualDate() {
        String dateString = editDateText.getText().toString().trim();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        sdf.setLenient(false);

        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            editDateText.setError("Некорректный формат! Пример: 31.12.2023");
            return null;
        }
    }

    public void goBack(View v) {
        finish();
    }

    public void cancel(View v) {
        editDateText.setText("");
        editCategory.setText("");
        editUnit.setText("");
        editQuantity.setText("");
        editPrice.setText("");
    }
}