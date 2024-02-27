package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.ShopManager
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ShopManagerDAO {
    @Query("SELECT * FROM shopmanager")
    fun getShopManagers(): Flow<List<ShopManager>>

    @Query("SELECT * FROM shopmanager WHERE id=(:id)")
    suspend fun getShopManager(id: UUID): ShopManager

    @Query("SELECT * FROM shopmanager WHERE id = (SELECT MIN(id) FROM shopmanager)")
    suspend fun getShopManagerLowest(): ShopManager

    @Update
    suspend fun updateShopManager(shopmanager: ShopManager)

    @Insert
    suspend fun addShopManager(shopManager: ShopManager)

    @Delete
    suspend fun delShopManager(shopManager: ShopManager)




}
