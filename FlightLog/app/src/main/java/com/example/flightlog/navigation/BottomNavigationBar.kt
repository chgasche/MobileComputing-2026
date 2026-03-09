package com.example.flightlog.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    /*val selectedNavigationIndex = rememberSaveable {
        mutableIntStateOf(0)
    }*/
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val navigationItems = listOf(
        NavigationItem(
            title = "Home",
            icon = Icons.Default.Info,
            route = Screens.FlightsScreen.route
        )
    )

    NavigationBar(
        containerColor = Color.White
    ) {
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                //selected = selectedNavigationIndex.intValue == index,
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    //selectedNavigationIndex.intValue = index
                    //navController.navigate(item.route)
                    navController.navigate(item.route) {
                        popUpTo(item.route) { inclusive = true }
                    }
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = {
                    Text(
                        item.title,
                        //color = if (index == selectedNavigationIndex.intValue)
                        color = if (currentDestination?.hierarchy?.any { it.route == item.route } == true)
                            Color.Black
                        else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.surface,
                    indicatorColor = MaterialTheme.colorScheme.primary
                )

            )
        }
    }
}

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)