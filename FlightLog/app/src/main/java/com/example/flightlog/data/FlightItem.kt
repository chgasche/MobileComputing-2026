package com.example.flightlog.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FlightItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,           // Auto-incremented ID
    @ColumnInfo(name = "flight_nr") val flightNr: String,       // Flight Number
    @ColumnInfo(name = "flight_from") val flightFrom: String,   // From
    @ColumnInfo(name = "flight_to") val flightTo: String,       // To Destination
    @ColumnInfo(name = "flight_date") val flightDate: String,   // Date of the Flight
    @ColumnInfo(name = "flight_std") val flightSTD: String,     // Scheduled Departure Time
    @ColumnInfo(name = "flight_sta") val flightSTA: String,     // Scheduled Arrival Time

    // optional attributes
    @ColumnInfo(name = "booking_reference") val bookingReference: String? = null,
    @ColumnInfo(name = "cabin_class") val cabinClass: String? = null,
    @ColumnInfo(name = "booking_class") val bookingClass: String? = null,
    @ColumnInfo(name = "seat_number") val seatNumber: String? = null,
    @ColumnInfo(name = "aircraft_type") val aircraftType: String? = null,
    @ColumnInfo(name = "imagepath") val imagePath: String? = null      // Path to the image
)