package com.example.homework3.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FoodItem::class], version = 1, exportSchema = false)
abstract class FoodItemDatabase : RoomDatabase() {
    abstract fun foodItemDao(): FoodItemDao
}