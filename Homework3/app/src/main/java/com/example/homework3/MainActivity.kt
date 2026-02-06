package com.example.homework3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.compose.runtime.LaunchedEffect
import com.example.homework3.data.FoodItem
import com.example.homework3.data.FoodItemDao
import com.example.homework3.data.FoodItemDatabase
import com.example.homework3.navigation.NavGraph
import com.example.homework3.ui.theme.Homework3Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Homework3Theme {
                val navController = rememberNavController()

                // Access database
                val db = Room.databaseBuilder(
                    applicationContext,
                    FoodItemDatabase::class.java, "fooditem"
                ).build()
                val foodItemDao = db.foodItemDao()
                //val foodItems = foodItemDao.getAllFlow()

                // Insert sample data
                LaunchedEffect(Unit) {
                    //insertSampleData(foodItemDao)
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { contentPadding ->
                    NavGraph(
                        navController = navController,
                        foodItemDao = foodItemDao,
                        modifier = Modifier.padding(contentPadding)
                    )
                }
            }
        }
    }
}



suspend fun insertSampleData(foodItemDao: FoodItemDao) {

    // Insert sample items
    foodItemDao.insert(
        FoodItem(title = "Milk", expiryDate = "2026-02-01", imagePath = "")
    )
    foodItemDao.insert(
        FoodItem(title = "Eggs", expiryDate = "2026-02-05", imagePath = "")
    )
    foodItemDao.insert(
        FoodItem(title = "Cheese", expiryDate = "2026-02-15", imagePath = "")
    )
}

