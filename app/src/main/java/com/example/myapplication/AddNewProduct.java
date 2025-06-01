package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.ProductListTable;

public class AddNewProduct extends AppCompatActivity {
    private EditText editProductName;
    private EditText editCategory;
    private EditText editUnit;
    private EditText editQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_product);

        editProductName = findViewById(R.id.editProductName);
        editCategory = findViewById(R.id.editCategory);
        editUnit = findViewById(R.id.editUnit);
        editQuantity = findViewById(R.id.editQuantity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addNewProduct), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, ProductList.class);
        startActivity(intent);
    }

    public void cancel(View v) {
        editProductName.setText("");
        editCategory.setText("");
        editUnit.setText("");
        editQuantity.setText("");
    }

    public void save(View v) {
        ProductListTable product = new ProductListTable();

        product.name = editProductName.getText().toString();
        product.category = editCategory.getText().toString();
        product.unit = editUnit.getText().toString();
        product.quantity = Integer.parseInt(editQuantity.getText().toString().trim());

        new Thread(() -> {
            MyApp.database.ProductListDao().insert(product);
        }).start();
    }
}