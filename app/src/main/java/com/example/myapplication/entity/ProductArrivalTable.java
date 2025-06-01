package com.example.myapplication.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(
        tableName = "ProductArrivalTable",
        foreignKeys = @ForeignKey(
                entity = ProductListTable.class,
                parentColumns = "id",
                childColumns = "idProduct",
                onDelete = ForeignKey.CASCADE
        )
)
public class ProductArrivalTable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public Date arrivalDate;
    public String productName;

    public int quantity;
    public int buyPrice;
    public int idProduct;
}
