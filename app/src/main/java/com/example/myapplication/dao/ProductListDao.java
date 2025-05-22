package com.example.myapplication.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.myapplication.entity.ProductList;
import java.util.List;

@Dao
public interface ProductListDao {
    @Insert
    void insert(ProductList product);

    @Query("SELECT * FROM ProductList")
    LiveData<List<ProductList>> getAllProducts();

    @Query("SELECT * FROM ProductList WHERE name LIKE :searchName")
    List<ProductList> searchProductByName(String searchName);
}
