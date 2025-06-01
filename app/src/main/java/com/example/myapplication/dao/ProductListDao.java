package com.example.myapplication.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.entity.ProductListTable;
import java.util.List;

@Dao
public interface ProductListDao {
    @Insert
    void insert(ProductListTable product);
    @Update
    void update(ProductListTable product);
    @Query("SELECT * FROM ProductListTable")
    LiveData<List<ProductListTable>> getAllProducts();

    @Query("SELECT * FROM ProductListTable WHERE id = :id")
    ProductListTable getProductById(int id);
}
