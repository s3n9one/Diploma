package com.example.myapplication.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "ProductListTable")
public class ProductListTable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String category;
    public String unit;
    public Integer quantity;

}
