package com.example.myapplication.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.myapplication.DateConverter;
import com.example.myapplication.dao.ProductArrivalDao;
import com.example.myapplication.dao.ProductListDao;
import com.example.myapplication.dao.SaleProductDao;
import com.example.myapplication.entity.ProductArrivalTable;
import com.example.myapplication.entity.ProductListTable;
import com.example.myapplication.entity.SaleProductTable;

@Database(
        entities = {ProductListTable.class, ProductArrivalTable.class, SaleProductTable.class},
        version = 5
)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductListDao ProductListDao();
    public abstract ProductArrivalDao ProductArrivalDao();
    public abstract SaleProductDao SaleProductDao();

    public void executeInTransaction(Runnable transaction) {
        runInTransaction(transaction);
    }
}
