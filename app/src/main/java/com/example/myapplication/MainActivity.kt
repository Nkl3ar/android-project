package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var smr = ShopManagerRepository.get()
        GlobalScope.launch {
            var sm = smr.getShopManagerLowest()
            if (sm == null)
            {
                smr.addShopManager(ShopManager(UUID.randomUUID(),"New"))
            }
        }


    }
}