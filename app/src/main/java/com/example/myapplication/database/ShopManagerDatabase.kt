package com.example.myapplication.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.ShopManager

@Database(entities = [ShopManager::class], version = 1)
abstract class ShopManagerDatabase : RoomDatabase() {
    abstract fun shopManagerDAO(): ShopManagerDAO
}

