package com.example.homework3.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodItemDao {

    @Query("SELECT * FROM fooditem")
    fun getAll(): Flow<List<FoodItem>>

    @Insert
    suspend fun insert(foodItem: FoodItem)

    @Delete
    fun delete(foodItem: FoodItem)

}