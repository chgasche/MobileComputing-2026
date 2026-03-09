package com.example.flightlog.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FlightItem::class], version = 1, exportSchema = false)
abstract class FlightDatabase : RoomDatabase() {
    abstract fun flightItemDao(): FlightItemDao
}