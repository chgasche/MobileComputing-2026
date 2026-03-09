@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.example.flightlog.screens

import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

import com.example.flightlog.R
import com.example.flightlog.data.FlightItemDao
import com.example.flightlog.util.getCityName
import com.example.flightlog.util.getAirportLatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.MarkerState
import androidx.core.net.toUri

@Composable
fun ShowFlightScreen(navController: NavController, flightItemDao: FlightItemDao, flightId: Int) {

    // Fetch all Flight items from the DAO
    val fetchedFlightItem by flightItemDao.getById(flightId).collectAsState(initial = null)

    // Check for non null
    fetchedFlightItem?.let { flight ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {

            // Headline
            Text(
                text = "${getCityName(flight.flightFrom)} to ${getCityName(flight.flightTo)}",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = flight.flightDate,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Logo and Flight Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFECEFF1), shape = RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Airline logo
                val airlineLogo = when {
                    flight.flightNr.contains("AY") -> R.drawable.ay
                    flight.flightNr.contains("SK") -> R.drawable.sk
                    else -> R.drawable.default_airline
                }

                Image(
                    painter = painterResource(id = airlineLogo),
                    contentDescription = "Airline Logo",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Flight number
                Text(
                    text = flight.flightNr,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(end = 16.dp)
                )

                Spacer(modifier = Modifier.width(24.dp))

                // Flight info
                Column(modifier = Modifier.weight(1f)) {

                    // Departure row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = flight.flightFrom,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = flight.flightSTD,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "✈",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = flight.flightTo,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = flight.flightSTA,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Map Placeholder
            Text(
                text = "Flight Map",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {

                // Include the Google Map
                FlightMap(flight.flightFrom, flight.flightTo)

            }

            Spacer(modifier = Modifier.height(24.dp))


            // Additional flight info
            Text(
                text = "Flight Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Column(modifier = Modifier.padding(top = 8.dp, start = 4.dp)) {
                Row {
                    Text("Seat: ", fontWeight = FontWeight.Bold)
                    Text(flight.seatNumber ?: "")
                }
                Row {
                    Text("Cabin Class: ", fontWeight = FontWeight.Bold)
                    Text(flight.cabinClass ?: "")
                }
                Row {
                    Text("Booking Class: ", fontWeight = FontWeight.Bold)
                    Text(flight.bookingClass ?: "")
                }
                Row {
                    Text("Aircraft Type: ", fontWeight = FontWeight.Bold)
                    Text(flight.aircraftType ?: "")
                }
                Row {
                    Text("Booking Reference: ", fontWeight = FontWeight.Bold)
                    Text(flight.bookingReference ?: "")
                }
            }


            // Display Photo is there is one
            if (!flight.imagePath.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))

                // Photo Text
                Text(
                    text = "Photo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Display the image
                val uri = flight.imagePath.toUri()
                AsyncImage(
                    model = uri,
                    contentDescription = "Flight Photo",
                    modifier = Modifier
                        //.background(Color.LightGray)
                        .height(150.dp)
                        .fillMaxWidth(),
                    //contentScale = ContentScale.FillHeight
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@Composable
fun FlightMap(flightAirportFrom: String, flightAirportTo: String) {
    val flightFromLatLng = getAirportLatLng(flightAirportFrom) ?: LatLng(0.0,0.0)
    val flightToLatLng = getAirportLatLng(flightAirportTo) ?: LatLng(0.0,0.0)

    //val flightFromLatLng = LatLng(64.9280117, 25.3669931) // Oulu
    //val flightToLatLng = LatLng(60.3157629, 24.953687) // Vantaa

    // Initialize the camera position state, which controls the camera's position on the map
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom( flightFromLatLng, 5f)
    }

    // Display the Google Map without
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Markers
        Marker(
            state = MarkerState(position = flightFromLatLng),
            title = "Departure"
        )
        Marker(
            state = MarkerState(position = flightToLatLng),
            title = "Arrival"
        )

        // Polyline
        Polyline(
            points = listOf(flightFromLatLng, flightToLatLng),
            color = Color.Red,
            width = 8f
        )
    }

    // Re-center using LatLngBounds.Builder()
    val bounds = createBounds(listOf(flightFromLatLng, flightToLatLng))
    LaunchedEffect(Unit) {
        cameraPositionState.move(
            update = CameraUpdateFactory.newLatLngBounds(bounds, 100)
        )
    }
}


private fun createBounds(coordinates: List<LatLng>): LatLngBounds {
    val boundsBuilder = LatLngBounds.builder()
    coordinates.forEach {
        boundsBuilder.include(LatLng(it.latitude, it.longitude))
    }
    return boundsBuilder.build()
}