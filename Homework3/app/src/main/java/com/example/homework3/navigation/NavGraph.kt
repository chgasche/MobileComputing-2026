package com.example.homework3.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.homework3.data.FoodItemDao
import com.example.homework3.screens.AddItemScreen
import com.example.homework3.screens.HomeScreen

@Composable
fun NavGraph(navController: NavHostController, foodItemDao: FoodItemDao, modifier: Modifier = Modifier){
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    )
    {
        composable(route = Screens.Home.route) {
            HomeScreen(navController, foodItemDao)
        }
        composable(route = Screens.AddItem.route) {
            AddItemScreen(navController, foodItemDao)
        }
    }
}