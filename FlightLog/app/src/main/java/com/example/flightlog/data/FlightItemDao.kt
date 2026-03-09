package com.example.flightlog.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightItemDao {

    @Query("SELECT * FROM flightItem")
    fun getAll(): Flow<List<FlightItem>>

    @Query("SELECT * FROM flightItem WHERE id = :id LIMIT 1")
    fun getById(id: Int): Flow<FlightItem?>

    @Insert
    suspend fun insert(flightItem: FlightItem): Long

    @Delete
    fun delete(flightItem: FlightItem)

}