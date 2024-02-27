package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.ShopJob
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ShopJobDAO {
    @Query("SELECT * FROM shopjob")
    fun getShopJobs(): Flow<List<ShopJob>>

    @Query("SELECT * FROM shopjob WHERE id=(:id)")
    suspend fun getShopJob(id: UUID): ShopJob

    @Update
    suspend fun updateShopJob(shopJob: ShopJob)

    @Insert
    suspend fun addShopJob(shopJob: ShopJob)

    @Delete
    suspend fun delShopJob(shopJob: ShopJob)




}

