package com.example.flightlog.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flightlog.data.FlightItemDao
import com.example.flightlog.screens.FlightsScreen
import com.example.flightlog.screens.InsertFlightScreen
import com.example.flightlog.screens.ShowFlightScreen

@Composable
fun NavGraph(navController: NavHostController, flightItemDao: FlightItemDao, modifier: Modifier = Modifier){
    NavHost(
        navController = navController,
        startDestination = Screens.FlightsScreen.route,
        modifier = modifier
    )
    {
        // FLightsScreen
        composable(route = Screens.FlightsScreen.route) {
            FlightsScreen(
                navController = navController,
                flightItemDao = flightItemDao
            )
        }

        // InsertFlightScreen
        composable(route = Screens.InsertFlightScreen.route) {
            InsertFlightScreen(
                navController = navController,
                flightItemDao = flightItemDao
            )
        }

        // ShowFlightScreen
        composable(
            route = Screens.ShowFlightScreen.route + "/{flightId}",
            arguments = listOf(navArgument("flightId") { type = NavType.IntType })
        ) { entry ->
            ShowFlightScreen(
                navController = navController,
                flightItemDao = flightItemDao,
                flightId = entry.arguments!!.getInt("flightId"))
        }
    }
}