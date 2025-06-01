package com.example.myapplication;

import android.app.Application;

import androidx.room.Room;

import com.example.myapplication.database.AppDatabase;

public class MyApp extends Application {
    public static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "market-database"
        ).fallbackToDestructiveMigration().build();
    }
}