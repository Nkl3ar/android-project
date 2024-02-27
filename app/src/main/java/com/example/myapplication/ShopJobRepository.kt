package com.example.myapplication

import android.content.Context
import androidx.room.Room
import com.example.myapplication.database.ShopJobDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

private const val DATABASE_NAME = "shop-database"

class ShopJobRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {

    private val database: ShopJobDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            ShopJobDatabase::class.java,
            DATABASE_NAME
        )
        .build()

    fun getShopJobs(): Flow<List<ShopJob>> = database.shopJobDAO().getShopJobs()

    suspend fun getShopJob(id: UUID): ShopJob = database.shopJobDAO().getShopJob(id)

    fun updateShopJob(shopJob: ShopJob) {
        coroutineScope.launch {
            database.shopJobDAO().updateShopJob(shopJob)
        }
    }
    fun addShopJob(shopJob: ShopJob) {
        coroutineScope.launch {
            database.shopJobDAO().addShopJob(shopJob)
        }
    }
    fun delShopJob(shopJob: ShopJob) {
        coroutineScope.launch {
            database.shopJobDAO().delShopJob(shopJob)
        }
    }

    companion object {
        private var INSTANCE: ShopJobRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ShopJobRepository(context)
            }
        }

        fun get(): ShopJobRepository {
            return INSTANCE
                ?: throw IllegalStateException("ShopJobRepository must be initialized")
        }
    }
}
