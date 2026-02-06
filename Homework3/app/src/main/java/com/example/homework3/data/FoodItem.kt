package com.example.homework3.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FoodItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,        // Auto-incremented ID
    @ColumnInfo(name = "title") val title: String,           // Item title
    @ColumnInfo(name = "expirydate") val expiryDate: String, // Item expiry date as timestamp
    @ColumnInfo(name = "imagepath") val imagePath: String?   // Path to the image (can be NULL)
)
