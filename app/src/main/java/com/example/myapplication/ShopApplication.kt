package com.example.myapplication

import android.app.Application
import com.example.myapplication.database.ShopManagerDatabase

class ShopApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ShopManagerRepository.initialize(this)
        ShopJobRepository.initialize(this)
    }
}
