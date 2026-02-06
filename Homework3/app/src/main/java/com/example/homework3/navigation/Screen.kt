package com.example.homework3.navigation

sealed class Screens(val route: String) {
    object Home: Screens("home_screen")
    object AddItem: Screens("additem_screen")
}