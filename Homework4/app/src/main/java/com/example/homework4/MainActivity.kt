package com.example.homework4

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.homework4.ui.theme.Homework4Theme


class MainActivity : ComponentActivity() {

    // Sensor Variables
    lateinit var sensorManager: SensorManager
    var temperatureSensor: Sensor? = null

    private var temperatureState = mutableStateOf("No DATA")
    private var temperatureLog = mutableStateOf(listOf<String>())

    // Notification Variables
    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationManager: NotificationManager
    lateinit var notificationHandler: NotificationHandler

    // Alert system variables
    private var alertOperator by mutableStateOf(">")
    private var alertTemperature by mutableStateOf(50f)
    private var alertRegistered by mutableStateOf(false)
    private var alertTriggered = false


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize the sensors
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        if (temperatureSensor == null) {
            temperatureState.value = "No SENSOR"
        }
        else {
            sensorManager.registerListener(
                temperatureSensorEventListener,
                temperatureSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        // Initialize notification
        notificationChannel = NotificationChannel(
            "notification_channel_id",
            "TemperatureLogger Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        notificationHandler = NotificationHandler(this)

        // Set Content
        setContent {
            Homework4Theme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text("Temperature Logger")
                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column {
                        TemperatureWindow(
                            temperatureString = temperatureState.value,
                            temperatureLog = temperatureLog.value,
                            alertRegistered = alertRegistered,
                            onAlertValueChange = { operator, temperature ->
                                alertOperator = operator
                                alertTemperature = temperature
                                alertRegistered = true
                                alertTriggered = false
                            },
                            modifier = Modifier.padding(innerPadding)
                        )


                    }
                }
            }
        }
    }

    private val temperatureSensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent) {
            val temp = event.values[0]
            // Display
            temperatureState.value = "$temp"

            // Append to log
            temperatureLog.value += "$temp"

            // Alert handling
            if (alertRegistered) { // button activated

                val conditionSatisfied = when (alertOperator) {
                    ">" -> temp > alertTemperature
                    "<" -> temp < alertTemperature
                    else -> false
                }

                if (conditionSatisfied && !alertTriggered) {
                    notificationHandler.showTemperatureNotification(
                        "%.1f".format(temp) // display notification
                    )
                    alertTriggered = true
                }

                // reset alertTriggered when temperature goes back to normal range
                if (!conditionSatisfied) {
                    alertTriggered = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (temperatureSensor != null) {
            sensorManager.unregisterListener(temperatureSensorEventListener)
        }
    }

}

@Composable
fun TemperatureWindow(
        temperatureString: String,
        temperatureLog: List<String>,
        alertRegistered: Boolean,
        onAlertValueChange: (operator: String, temperature: Float) -> Unit,
        modifier: Modifier
) {
    Column(modifier = modifier) {

        TemperatureDisplay(
            temperatureString = temperatureString,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TemperatureAlertForm(
            alertRegistered = alertRegistered,
            onValueChange = onAlertValueChange
            )

        Spacer(modifier = Modifier.height(16.dp))

        TemperatureLogList(
            temperatureLog = temperatureLog,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TemperatureWindowPreview() {
    Homework4Theme {
        TemperatureWindow(
            "20.4",
            listOf("22.3", "23.5", "24.0", "23.8"),
            false,
            { _, _ -> },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun TemperatureDisplay(temperatureString: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Temperature",
            style = MaterialTheme.typography.titleSmall,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(3.dp))

        val temp = temperatureString.toFloatOrNull()
        val displayText = when {
            temp == null -> temperatureString
            temp > 100 -> "HIGH"
            temp < -273.15 -> "LOW"
            else -> "%.1f °C".format(temp)
        }
        val tempColor = when {
            temp == null -> Color.Gray
            temp < 0f -> Color(0xFF0099FF)
            temp in 0f..18f -> Color.Gray
            temp in 18f..25f -> Color(0xFF00AA00)
            else -> Color(0xFFAA0000)
        }
        Text(
            text =  displayText,
            style = MaterialTheme.typography.headlineMedium,
            color = tempColor,
            fontWeight = FontWeight.Bold
        )

    }
}

@Preview(showBackground = true)
@Composable
fun TemperatureDisplayPreview() {
    Homework4Theme {
        TemperatureDisplay("20.4")
    }
}

@Composable
fun TemperatureLogList(temperatureLog: List<String>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Temperature Log",
            style = MaterialTheme.typography.titleSmall,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(3.dp))

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 6.dp, top = 2.dp, end = 6.dp, bottom = 2.dp)
        ) {
            items(temperatureLog.asReversed()) { temp ->
                Text(
                    text = "$temp °C",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            }
        }
    }
}



@Composable
fun TemperatureAlertForm(
        alertRegistered: Boolean,
        onValueChange: (operator: String, temperature: Float) -> Unit,
        modifier: Modifier = Modifier
) {
    var selectedOperator by remember { mutableStateOf(">") }
    var thresholdTemperature by remember { mutableStateOf("50") }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "Set Temperature Alert:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(4.dp))

            // Dropdown Menu for operator > / <
            Box {
                Button(onClick = { expanded = true }) {
                    Text(selectedOperator)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(">") },
                        onClick = { selectedOperator = ">"; expanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("<") },
                        onClick = { selectedOperator = "<"; expanded = false}
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Temperature field
            OutlinedTextField(
                value = thresholdTemperature,
                onValueChange = { thresholdTemperature = it },
                modifier = Modifier
                    .width(100.dp),
                singleLine = true,
                shape = MaterialTheme.shapes.small
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text("°C")

            Spacer(modifier = Modifier.width(12.dp))

            // Register alert button
            RegisterAlertButton(
                selectedOperator = selectedOperator,
                thresholdTemperature = thresholdTemperature,
                alertRegistered = alertRegistered,
                onAlertRegistered = onValueChange
            )

        }
    }
}


@Composable
fun RegisterAlertButton(
    selectedOperator: String,
    thresholdTemperature: String,
    alertRegistered: Boolean,
    onAlertRegistered: (operator: String, temperature: Float) -> Unit
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // -----------------
            // PERMISSION GRANTED
            // -----------------
            val temp = thresholdTemperature.toFloatOrNull()
            if (temp != null) {
                onAlertRegistered(selectedOperator, temp) // callback UP
            }
            // -----------------
        }
        else {
            // permission denied, but should I show a rationale?
        }
    }

    // button styles
    val buttonColor = if (alertRegistered) Color(0xFF2E7D32) else Color(0xFF4E5E8B)
    val buttonText = if (alertRegistered) "Alert registered" else "Register Alert"

    Button(
        onClick = {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // -----------------
                // PERMISSION GRANTED
                // -----------------
                val temp = thresholdTemperature.toFloatOrNull() ?: return@Button
                onAlertRegistered(selectedOperator, temp) // callback UP
                // -----------------
            }
            else {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
    ) {
        Text(
            text = buttonText,
            color = Color.White)
    }
}
