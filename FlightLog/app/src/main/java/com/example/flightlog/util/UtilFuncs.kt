package com.example.flightlog.util

import com.google.android.gms.maps.model.LatLng

// Dictionary with the city name
private val airportCityMap = mapOf(
    "OUL" to "Oulu",
    "HEL" to "Helsinki",
    "OSL" to "Oslo",
    "CPH" to "Copenhagen",
    "ARN" to "Stockholm",
    "RVN" to "Rovaniemi",
    "DEL" to "Delhi",
)

// Airport coordinates
private val airportLatLngMap = mapOf(
    "OUL" to LatLng(64.9280, 25.3669),
    "HEL" to LatLng(60.3157, 24.9536),
    "OSL" to LatLng(60.1942, 11.0887),
    "CPH" to LatLng(55.6282, 12.6459),
    "ARN" to LatLng(59.6492, 17.9210),
    "RVN" to LatLng(66.5579, 25.8249),
    "DEL" to LatLng(28.5569, 77.0795),
)

// Get the city name of an airport
fun getCityName(airportCode: String): String {
    return airportCityMap[airportCode.uppercase()] ?: ""
}

// Get LatLng for airport code
fun getAirportLatLng(airportCode: String): LatLng? {
    return airportLatLngMap[airportCode.uppercase()]
}