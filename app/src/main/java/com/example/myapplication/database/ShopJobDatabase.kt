package com.example.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.ShopJob
import com.example.myapplication.ShopManager


@Database(entities = [ShopJob::class], version = 1)
@TypeConverters(ShopJobTypeConverter::class)
abstract class ShopJobDatabase : RoomDatabase() {
    abstract fun shopJobDAO(): ShopJobDAO
}
