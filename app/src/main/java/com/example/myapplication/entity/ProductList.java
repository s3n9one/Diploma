package com.example.myapplication.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "ProductList")
public class ProductList {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String category;
    public String unit;

    public ProductList(String name, String category, String unit) {
        this.name = name;
        this.category = category;
        this.unit = unit;
    }
}
