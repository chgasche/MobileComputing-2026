package com.example.homework2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.homework2.screens.DetailScreen
import com.example.homework2.screens.HomeScreen

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier){
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    )
    {
        composable(route = Screens.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screens.Detail.route) {
            DetailScreen(navController)
        }
    }
}