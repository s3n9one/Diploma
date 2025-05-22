package com.example.myapplication.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.myapplication.dao.ProductListDao;
import com.example.myapplication.entity.ProductList;
@Database(entities = {ProductList.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductListDao ProductListDao();
}
