package com.example.flightlog.screens

import com.example.flightlog.navigation.Screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flightlog.R
import com.example.flightlog.data.FlightItem
import com.example.flightlog.data.FlightItemDao
import com.example.flightlog.util.getCityName

@Composable
fun FlightsScreen(navController: NavController, flightItemDao: FlightItemDao) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {

        // FlightItemList
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Fetch all Flight items from the DAO
            val flightItems by flightItemDao.getAll().collectAsState(initial = emptyList())

            // Display Flight Items list
            FlightItemList(
                flightItems = flightItems,
                navController = navController,
                modifier = Modifier.weight(1f)
            )
        }


        // Floating Action Button (+ add flight)
        FloatingActionButton(
            onClick = {
                navController.navigate(Screens.InsertFlightScreen.route){
                    popUpTo(Screens.InsertFlightScreen.route) { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add flight")
        }
    }
}


@Composable
fun FlightItemList(flightItems: List<FlightItem>, navController: NavController, modifier: Modifier = Modifier) {
    if(flightItems.isEmpty()) {
        // Display "No flights"
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 42.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "- No flights -",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
    else {
        // Display FlightCard
        LazyColumn( modifier = modifier ) {
            items(flightItems) { flightItem ->
                FlightItemCard(
                    flightItem = flightItem,
                    navController = navController
                )
            }
        }
    }
}

//@SuppressLint("UseKtx")
@Composable
fun FlightItemCard(flightItem: FlightItem, navController: NavController) {

    // Determine airline logo
    val airlineLogo = when {
        flightItem.flightNr.contains("AY") -> R.drawable.ay
        flightItem.flightNr.contains("SK") -> R.drawable.sk
        else -> R.drawable.default_airline
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable{
                // Click action -> ShowFlightScreen
                navController.navigate(Screens.ShowFlightScreen.route + "/${flightItem.id}") { launchSingleTop = true }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Airline Logo
        Image(
            painter = painterResource(id = airlineLogo),
            contentDescription = "Airline logo",
            modifier = Modifier
                .size(48.dp)
                .clip(shape = RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Middle columns (codes & cities)
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Top row: airport codes (bold, bigger)
            Row {
                Text(
                    text = flightItem.flightFrom,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " ✈ ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = flightItem.flightTo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Bottom row: city names (smaller, gray)
            Row {
                Text(
                    text = getCityName(flightItem.flightFrom),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = " to ",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = getCityName(flightItem.flightTo),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Right column (date & flight number)
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = flightItem.flightDate,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = flightItem.flightNr,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

/*
@Preview
@Composable
fun PreviewFlightItemCard() {
    FlightLogTheme {
        Surface {
            val flightItem = FlightItem(flightNr = "AY440", flightFrom = "OUL", flightTo = "HEL", flightDate = "2026-03-09", flightSTD = "15:25", flightSTA = "16:25", imagePath = null)
            FlightItemCard(flightItem)
        }
    }
}
*/