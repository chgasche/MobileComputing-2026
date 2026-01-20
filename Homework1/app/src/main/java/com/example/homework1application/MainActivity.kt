package com.example.homework1application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.homework1application.sampledata.SampleData
import com.example.homework1application.ui.theme.Homework1ApplicationTheme

class MainActivity : ComponentActivity() {
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Homework1ApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Homework1ApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    /*FlightCard(Flight("19:30", "AY560", "HEL", "scheduled", "C2", "A2", "20:35"))*/
                    FlightList(SampleData.flightlistSample)
                }
            }
        }
    }
}

data class Flight(val time: String, val flightnr: String, val dest: String, val status: String, val checkin: String, val gate: String, val timeSTA: String)

@Composable
fun FlightCard(flight: Flight) {

    // Set airline logo
    val airlineLogo = if(flight.flightnr.contains("AY")) {
        R.drawable.ay
    } else if(flight.flightnr.contains("SK")) {
        R.drawable.sk
    } else {
        R.drawable.default_airline
    }

    // We keep track if the message is expanded or not in this
    var isExpanded by remember { mutableStateOf(false) }

    // Proceed
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(airlineLogo),
            contentDescription = "Airline Logo",
            modifier = Modifier
                // Set image size
                .size(30.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
                // Set border
                .border(1.2.dp, MaterialTheme.colorScheme.secondary, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Surface(
            shape = MaterialTheme.shapes.extraSmall,
            shadowElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize().padding(1.dp)
        ) {
            Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
                Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)) {
                    Text(
                        text = flight.time,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = flight.flightnr)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = flight.dest,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = flight.status.uppercase(),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
                if (isExpanded) {
                    Row(modifier = Modifier.padding(all = 4.dp)) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "OUL",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = flight.time,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = " ✈ ")
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = flight.timeSTA,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = flight.dest,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(modifier = Modifier.padding(all = 4.dp)) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Gate",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = flight.gate,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Row(modifier = Modifier.padding(all = 4.dp)) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Check-In",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = flight.checkin,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
    }
    }
}

@Preview
@Composable
fun PreviewFlightCard() {
    Homework1ApplicationTheme {
        Surface {
            FlightCard(
                flight = Flight("19:30", "AY560", "HEL", "scheduled", "C12", "A2", "20:35")
            )
        }
    }
}


@Composable
fun FlightList(flights: List<Flight>) {
    LazyColumn {
        items(flights) { flight ->
            FlightCard(flight)
        }
    }
}

@Preview
@Composable
fun PreviewFlightList() {
    Homework1ApplicationTheme {
        FlightList(SampleData.flightlistSample)
    }
}

/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Homework1ApplicationTheme {
        Greeting("Android")
    }
}*/