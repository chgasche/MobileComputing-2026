@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.homework3.screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Date
import com.example.homework3.data.FoodItem
import com.example.homework3.data.FoodItemDao
import com.example.homework3.navigation.Screens
import com.example.homework3.ui.theme.Homework3Theme
@Composable
fun HomeScreen(navController: NavController, foodItemDao: FoodItemDao) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = "Love My Food App",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // FoodItemList
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // take available space between title and button
        ) {

            // Fetch all Food items from the DAO
            val foodItems by foodItemDao.getAll().collectAsState(initial = emptyList())
            FoodItemList(foodItems)
        }

        Spacer(modifier = Modifier.height(10.dp))
        
        // Add Button
        Row( modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {
                    navController.navigate(Screens.AddItem.route){
                        popUpTo(Screens.AddItem.route) { inclusive = true }
                    }
                }
            ) {
                Text(
                    text = "Add Food"
                )
            }
        }
    }
}


@Composable
fun FoodItemList(foodItems: List<FoodItem>) {
    if(foodItems.isEmpty()) {
        // Display "No food"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "- No food -",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
    else {
        // Display FoodItemCards
        LazyColumn {
            items(foodItems) { foodItem ->
                FoodItemCard(foodItem)
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun FoodItemCard(foodItem: FoodItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
    {
        // Show image or placeholder
        if (foodItem.imagePath.isNullOrBlank()) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.Gray, RoundedCornerShape(8.dp))
            )
        }
        else {
            // Place AsyncImage
            val uri = Uri.parse(foodItem.imagePath)
            AsyncImage(
                model = uri,
                contentDescription = "",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                    //.background(Color.LightGray, shape = RoundedCornerShape(size = 8.dp)),
                contentScale = ContentScale.FillHeight
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
        ) {
            // Title
            Text(
                text = foodItem.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Determine color of expiry date
            val expiry: Date = SimpleDateFormat("yyyy-MM-dd").parse(foodItem.expiryDate)
            val currentTime = System.currentTimeMillis()
            val diffMillis = expiry.time - currentTime
            val diffDays = diffMillis / (1000 * 60 * 60 * 24)
            val expiryColor = when {
                diffDays < 0 -> Color.Red
                diffDays <= 7 -> Color(0xFFFFA500) // orange
                else -> Color(0xFF007700) // dark green
            }

            Text(
                text = "Expires: " + foodItem.expiryDate,
                color = expiryColor
            )
        }
    }
}

@Preview
@Composable
fun PreviewFoodItemCard() {
    Homework3Theme {
        Surface {
            val foodItem = FoodItem(0, "Milk", "2026-03-01", "")
            FoodItemCard(foodItem)
        }
    }
}