package com.example.myapplication.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.ProductList;
import com.example.myapplication.entity.ProductArrivalTable;
import com.example.myapplication.entity.ProductListTable;

import java.util.List;

@Dao
public interface ProductArrivalDao {
    @Insert
    void insert(ProductArrivalTable arrival);

    @Query("SELECT * FROM ProductArrivalTable WHERE arrivalDate BETWEEN :from AND :to")
    List<ProductArrivalTable> getArrivalsBetweenDates(long from, long to);

    @Query("SELECT * FROM ProductArrivalTable")
    LiveData<List<ProductArrivalTable>> getAllArrivals();

    @Query("SELECT * FROM ProductArrivalTable WHERE id = :idProduct")
    List<ProductArrivalTable> getArrivalByProduct(int idProduct);
}
