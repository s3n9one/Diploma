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

public class AddProductSale extends AppCompatActivity {
    private EditText editDateText;
    private EditText editProductName;
    private EditText editQuantity;
    private EditText editPrice;
    private EditText editPaymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product_sale);

        editDateText = findViewById(R.id.editDateText);
        editProductName = findViewById(R.id.editProductName);
        editQuantity = findViewById(R.id.editQuantity);
        editPrice = findViewById(R.id.editPrice);
        editPaymentMethod = findViewById(R.id.editPaymentMethod);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addProductSale), (v, insets) -> {
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
        editQuantity.setText("");
        editPrice.setText("");
        editPaymentMethod.setText("");
    }
}