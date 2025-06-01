package com.example.myapplication.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(
        tableName = "SaleProductTable"
)
public class SaleProductTable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String productName;
    public Date saleDate;
    public int saleQuantity;
    public String paymentMethod;
    public int salePrice;
}
