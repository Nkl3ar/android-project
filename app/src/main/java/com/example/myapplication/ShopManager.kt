package com.example.myapplication


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class ShopManager(
    @PrimaryKey val id: UUID,
    val name: String
)