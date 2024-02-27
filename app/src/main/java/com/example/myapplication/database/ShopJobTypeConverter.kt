package com.example.myapplication.database

import androidx.room.TypeConverter
import com.example.myapplication.ShopManager
import com.google.gson.Gson

class ShopJobTypeConverter {
    @TypeConverter
    fun fromShopManager(value: ShopManager) : String
    {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toShopManager(value: String) : ShopManager
    {
        val gson = Gson()
        return gson.fromJson(value,ShopManager::class.java)
    }
}