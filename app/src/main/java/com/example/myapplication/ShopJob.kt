package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class ShopJob (
    @PrimaryKey val id: UUID,
    val name: String,
    val gpsLat: Double,
    val gpsLong : Double,
    val shopManager: ShopManager
)