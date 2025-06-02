package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.ProductArrivalTable;
import com.example.myapplication.entity.ProductListTable;
import com.example.myapplication.entity.SaleProductTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddProductSale extends AppCompatActivity {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private EditText editDateText, editQuantity, editPrice;
    private Spinner productSpinner, paymentMethodSpinner;
    private List<ProductListTable> products = new ArrayList<>();
    private List<ProductArrivalTable> arrivals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_sale);

        initViews();
        // setupDatePicker();
        loadProductsIntoSpinner();
    }

    private void initViews() {
        editDateText = findViewById(R.id.editDateText);
        editQuantity = findViewById(R.id.editQuantity);
        editPrice = findViewById(R.id.editPrice);
        productSpinner = findViewById(R.id.productSpinner);
        paymentMethodSpinner = findViewById(R.id.paymentMethodSpinner);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        editDateText.setText(sdf.format(new Date()));
    }

    private void setupDatePicker() {
        editDateText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                String date = String.format(Locale.getDefault(), "%02d.%02d.%04d", day, month+1, year);
                editDateText.setText(date);
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void loadProductsIntoSpinner() {
        MyApp.database.ProductListDao().getAllProducts().observe(this, products -> {
            this.products = products;
            List<String> productNames = new ArrayList<>();

            for (ProductListTable product : products) {
                if (product.quantity > 0) {
                    productNames.add(product.name + " (остаток: " + product.quantity + ")");
                }
            }

            if (productNames.isEmpty()) {
                Toast.makeText(this, "Нет доступных товаров для продажи", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    productNames
            );

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            productSpinner.setAdapter(adapter);
        });
    }

    public void save(View v) {
        try {
            executor.execute(() -> {
                int selectedPosition = productSpinner.getSelectedItemPosition();
                int selectedPosition1 = paymentMethodSpinner.getSelectedItemPosition();
                if (selectedPosition == AdapterView.INVALID_POSITION || products.isEmpty()) {
                    Toast.makeText(this, "Выберите товар", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Проверка заполнения полей
                if (editQuantity.getText().toString().isEmpty() || editPrice.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }

                Date saleDate = parseManualDate();
                if (saleDate == null) return;

                ProductListTable selectedProduct = products.get(selectedPosition);
                int quantity = Integer.parseInt(editQuantity.getText().toString().trim());
                int price = Integer.parseInt(editPrice.getText().toString().trim());

                String selectedPayment = "Наличный";

                paymentMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedPayment = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                // Создаем и сохраняем продажу
                SaleProductTable sale = new SaleProductTable();
                sale.saleDate = saleDate;
                sale.productName = selectedProduct.name;
                sale.saleQuantity = quantity;
                sale.salePrice = price;
                sale.paymentMethod = selectedPayment;

                selectedProduct.quantity = (selectedProduct.quantity == null ? 0 : selectedProduct.quantity) - quantity;

                MyApp.database.SaleProductDao().insert(sale);
                MyApp.database.ProductListDao().update(selectedProduct);

                // Показ Toast через UI поток
                runOnUiThread(() -> {
                    Toast.makeText(AddProductSale.this, "Данные сохранены", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });


        } catch (NumberFormatException e) {
            Toast.makeText(this, "Некорректные числовые значения", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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
        editQuantity.setText("");
        editPrice.setText("");
    }
}