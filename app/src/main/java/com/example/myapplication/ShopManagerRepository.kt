package com.example.myapplication

import android.content.Context
import androidx.room.Room
import com.example.myapplication.database.ShopManagerDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

private const val DATABASE_NAME = "shop-manager-database"

class ShopManagerRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {

    private val database: ShopManagerDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            ShopManagerDatabase::class.java,
            DATABASE_NAME
        )
        .build()

    fun getShopManagers(): Flow<List<ShopManager>> = database.shopManagerDAO().getShopManagers()

    suspend fun getShopManager(id: UUID): ShopManager = database.shopManagerDAO().getShopManager(id)
    suspend fun getShopManagerLowest(): ShopManager = database.shopManagerDAO().getShopManagerLowest()
    fun updateShopManager(shopManager: ShopManager) {
        coroutineScope.launch {
            database.shopManagerDAO().updateShopManager(shopManager)
        }
    }
    fun addShopManager(shopManager: ShopManager) {
        coroutineScope.launch {
            database.shopManagerDAO().addShopManager(shopManager)
        }
    }
    fun delShopManager(shopManager: ShopManager) {
        coroutineScope.launch {
            database.shopManagerDAO().delShopManager(shopManager)
        }
    }


    companion object {
        private var INSTANCE: ShopManagerRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ShopManagerRepository(context)
            }
        }

        fun get(): ShopManagerRepository {
            return INSTANCE
                ?: throw IllegalStateException("ShopManagerRepository must be initialized")
        }
    }
}
