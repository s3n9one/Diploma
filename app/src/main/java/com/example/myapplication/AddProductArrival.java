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

public class AddProductArrival extends AppCompatActivity {
    private EditText editDateText;
    private EditText editProductName;
    private EditText editCategory;
    private EditText editUnit;
    private EditText editQuantity;
    private EditText editPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product_arrival);

        editDateText = findViewById(R.id.editDateText);
        editProductName = findViewById(R.id.editProductName);
        editCategory = findViewById(R.id.editCategory);
        editUnit = findViewById(R.id.editUnit);
        editQuantity = findViewById(R.id.editQuantity);
        editPrice = findViewById(R.id.editPrice);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addProductArrival), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, ProductArrival.class);
        startActivity(intent);
    }
    public void cancel(View v) {
        editDateText.setText("");
        editProductName.setText("");
        editCategory.setText("");
        editUnit.setText("");
        editQuantity.setText("");
        editPrice.setText("");
    }
}