package com.example.flightlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.flightlog.data.FlightDatabase
import com.example.flightlog.data.FlightItem
import com.example.flightlog.data.FlightItemDao
import com.example.flightlog.navigation.NavGraph
import com.example.flightlog.navigation.Screens
import com.example.flightlog.navigation.getTitleForRoute
import com.example.flightlog.ui.theme.FlightLogTheme


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlightLogTheme {
                // Navigation
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Access database
                val db = Room.databaseBuilder(
                    applicationContext,
                    FlightDatabase::class.java, "flightitem"
                ).build()
                val flightItemDao = db.flightItemDao()

                // Insert sample data
                LaunchedEffect(Unit) {
                    // insertSampleData(flightItemDao)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (currentRoute != Screens.FlightsScreen.route || currentRoute == "") { // hide on home screen
                            TopAppBar(
                                title = { Text(getTitleForRoute(currentRoute)) },
                                navigationIcon = {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                }
                            )
                        }
                    }
                    //bottomBar = { BottomNavigationBar(navController) }
                ) { contentPadding ->
                    NavGraph(
                        navController = navController,
                        flightItemDao = flightItemDao,
                        modifier = Modifier.padding(contentPadding)
                    )
                }
            }
        }
    }
}


suspend fun insertSampleData(flightItemDao: FlightItemDao) {

    // Insert sample items
    flightItemDao.insert(
        FlightItem(flightNr = "AY440", flightFrom = "OUL", flightTo = "HEL", flightDate = "2026-03-09", flightSTD = "15:25", flightSTA = "16:25")
    )
    flightItemDao.insert(
        FlightItem(flightNr = "AY121", flightFrom = "HEL", flightTo = "DEL", flightDate = "2026-03-09", flightSTD = "18:00", flightSTA = "06:05")
    )
}