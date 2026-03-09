package com.example.flightlog.navigation

sealed class Screens(val route: String, val title: String) {
    object FlightsScreen: Screens("flights_screen", "All Flights")

    object InsertFlightScreen: Screens("insert_flight_screen", "Add Flight")

    object ShowFlightScreen: Screens("show_flight_screen}", "Flight Details")
}


// Define TitleBar texts
val screenTitles = mapOf(
    Screens.FlightsScreen.route to Screens.FlightsScreen.title,
    Screens.InsertFlightScreen.route to Screens.InsertFlightScreen.title,
    Screens.ShowFlightScreen.route to Screens.ShowFlightScreen.title
)

// Getter
fun getTitleForRoute(route: String?): String {
    if (route == null) {
        return "FlightLog App"
    }

    // Check the screenTitles maps (ShowFlightScreen is composite route)
    for ((key, value) in screenTitles) {
        if (route.startsWith(key)) {
            return value
        }
    }

    return "FlightLog App"
}