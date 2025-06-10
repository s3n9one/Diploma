package com.example.myapplication.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.entity.ProductArrivalTable;
import com.example.myapplication.entity.SaleProductTable;

import java.util.List;

@Dao
public interface SaleProductDao {
    @Insert
    void insert(SaleProductTable sale);

    @Query("SELECT * FROM SaleProductTable")
    LiveData<List<SaleProductTable>> getAllSales();

    @Query("SELECT * FROM SaleProductTable WHERE saleDate BETWEEN :from AND :to")
    List<SaleProductTable> getSalesBetweenDates(long from, long to);

    @Query("SELECT * FROM SaleProductTable WHERE id = :idArrival")
    List<SaleProductTable> getSaleByArrival(int idArrival);
}
