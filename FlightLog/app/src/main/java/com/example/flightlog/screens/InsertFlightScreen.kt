package com.example.flightlog.screens

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.flightlog.data.FlightItem
import com.example.flightlog.data.FlightItemDao
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun InsertFlightScreen(navController: NavController, flightItemDao: FlightItemDao) {

    // Define the variables using remember
    var textFlightNr by remember { mutableStateOf("") }
    var textFlightFrom by remember { mutableStateOf("") }
    var textFlightTo by remember { mutableStateOf("") }
    var textFlightDate by remember { mutableStateOf("") }
    var textFlightSTD by remember { mutableStateOf("") }
    var textFlightSTA by remember { mutableStateOf("") }

    // optional fields
    var textSeatNumber by remember { mutableStateOf("") }
    var textCabinClass by remember { mutableStateOf("") }
    var textBookingClass by remember { mutableStateOf("") }
    var textAircraftType by remember { mutableStateOf("") }
    var textBookingReference by remember { mutableStateOf("") }

    var textImageURI by remember { mutableStateOf<Uri?>(null) }
    var savedPath = ""

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            textImageURI = uri
        }
    }

    // Photo capture
    val context = LocalContext.current
    var capturedPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val hasPermissions = remember { mutableStateOf(false) } // Assuming 'hasCameraPermission' is a boolean in your viewModel's state
    val getPermissions =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                hasPermissions.value = permissions[android.Manifest.permission.CAMERA] == true
            })


    // AlertDialog to either pick photo or open camera
    var showDialog by remember { mutableStateOf(false) }

    // UI
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // Title
        /*Text(
            text = "Add new Flight",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)


        Spacer(modifier = Modifier.height(24.dp))
        */

        // Mandatory Fields
        Text(
            text = "Mandatory Information",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Flight Number
        CompactTextField(
            value = textFlightNr,
            label = "Flight Number",
            onValueChange = { textFlightNr = it },
            modifier = Modifier.fillMaxWidth()
        )

        // From -> To
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            CompactTextField(
                value = textFlightFrom,
                label = "From",
                onValueChange = { textFlightFrom = it },
                modifier = Modifier.weight(1f)
            )

            CompactTextField(
                value = textFlightTo,
                label = "To",
                onValueChange = { textFlightTo = it },
                modifier = Modifier.weight(1f)
            )
        }


        // Date
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            CompactTextField(
                value = textFlightDate,
                label = "Date",
                onValueChange = { textFlightDate = it },
                modifier = Modifier.weight(1.4f),
                placeholder = "YYYY-MM-DD"
            )

            CompactTextField(
                value = textFlightSTD,
                label = "Departure",
                onValueChange = { textFlightSTD = it },
                modifier = Modifier.weight(1f),
                placeholder = "HH:MM"
            )

            CompactTextField(
                value = textFlightSTA,
                label = "Arrival",
                onValueChange = { textFlightSTA = it },
                modifier = Modifier.weight(1f),
                placeholder = "HH:MM"
            )
        }



        Spacer(modifier = Modifier.height(16.dp))

        // Optional Fields Text
        Text(
            text = "Optional Flight Information",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(2.dp))


        // Seat and Aircraft
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            CompactTextField(
                value = textSeatNumber,
                label = "Seat",
                onValueChange = { textSeatNumber = it },
                modifier = Modifier.weight(1f)
            )

            CompactTextField(
                value = textAircraftType,
                label = "Aircraft",
                onValueChange = { textAircraftType = it },
                modifier = Modifier.weight(1f)
            )
        }

        // Cabin Class and Booking Class
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            CompactTextField(
                value = textCabinClass,
                label = "Cabin",
                onValueChange = { textCabinClass = it },
                modifier = Modifier.weight(1f)
            )

            CompactTextField(
                value = textBookingClass,
                label = "Booking Class",
                onValueChange = { textBookingClass = it },
                modifier = Modifier.weight(1f)
            )
        }

        // Booking Reference
        CompactTextField(
            value = textBookingReference,
            label = "Booking Reference",
            onValueChange = { textBookingReference = it },
            modifier = Modifier.fillMaxWidth()
        )





        // Flight Photo / Image selector
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Flight photo",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Add photo + button
                Button(
                    onClick = { // Open AlertDialog
                        showDialog = true
                              },
                    modifier = Modifier.size(56.dp)
                ) {
                    Text("+")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (textImageURI == null) "Add Image" else "Image selected",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        // Handle Add Image

        // Launcher for TakePicture()
        val systemCameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { success ->
                if (success) {
                    // success, set the flight photo uri to the captured image uri
                    capturedPhotoUri?.let { uri ->
                        textImageURI = uri // Update your preview / flight photo
                    }
                }
            })

        // Ask whether to pick photo or open camera
        if(showDialog) {
            AlertDialog(

                // set dismiss request
                onDismissRequest = {  },

                title = { Text("Add Photo") },
                text = { Text("Choose an option:") },

                // configure confirm button
                confirmButton = {
                    Button(onClick = {
                        // Camera picker
                        showDialog = false


                        if (hasPermissions.value) {

                            // Create empty file in DIRECTORY_PICTURES
                            val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                            val photoFile = File.createTempFile("camera_${System.currentTimeMillis()}", ".jpg", storageDir)

                            // Get the URI
                            capturedPhotoUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)

                            // Launch the intent
                            capturedPhotoUri?.let { uri ->
                                systemCameraLauncher.launch(uri)
                            }

                        }
                        else {
                            getPermissions.launch(arrayOf(android.Manifest.permission.CAMERA))
                        }

                    }) {
                        // set button text
                        Text("Open Camera")
                    }
                },
                // configure dismiss button
                dismissButton = {
                    Button(onClick = {
                        // Photo picker / PickVisualMedia
                        showDialog = false

                        val mimeType = "image/jpeg"
                        pickMedia.launch(
                            PickVisualMediaRequest(
                                PickVisualMedia.SingleMimeType(mimeType)
                            )
                        )

                    }) {
                        // set button text
                        Text("Pick Photo")
                    }
                }
            )
        }
        //--------------

        Spacer(modifier = Modifier.height(24.dp))

        // Buttons
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            OutlinedButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Text("Go back")
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Submit button
            val scope = rememberCoroutineScope()
            val context = LocalContext.current

            val isValid =
                textFlightNr.isNotBlank() &&
                        textFlightFrom.isNotBlank() &&
                        textFlightTo.isNotBlank() &&
                        textFlightDate.isNotBlank()

            Button(
                enabled = isValid,
                onClick = {

                    // Insert food
                    scope.launch {

                        // Copy file to app storage
                        if (textImageURI != null) {
                            val newFilename = "${System.currentTimeMillis()}.jpg"

                            savedPath = saveImageToInternalStorage(
                                context = context,
                                uri = textImageURI!!,
                                filename = newFilename
                            )
                        }

                        // Create new FlightItem
                        val flightItem = FlightItem(
                            flightNr = textFlightNr,
                            flightFrom = textFlightFrom,
                            flightTo = textFlightTo,
                            flightDate = textFlightDate,
                            flightSTD = textFlightSTD,
                            flightSTA = textFlightSTA,
                            bookingReference = textBookingReference.ifBlank { null },
                            cabinClass = textCabinClass.ifBlank { null },
                            bookingClass = textBookingClass.ifBlank { null },
                            seatNumber = textSeatNumber.ifBlank { null },
                            aircraftType = textAircraftType.ifBlank { null },
                            imagePath = savedPath
                        )

                        // Pass to DAO and insert into DB
                        flightItemDao.insert(flightItem)

                        // Navigate back to home
                        navController.popBackStack()
                    }
                }
            ) {
                Text("Insert Flight")
            }
        }



        // Image preview
        Spacer(modifier = Modifier.height(12.dp))

        textImageURI?.let { uri ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "Preview",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(2.dp))

                AsyncImage(
                    model = uri,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@Composable
fun CompactTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 15.sp),
        placeholder = {
            placeholder?.let {
                Text(it)
            }
        },
        modifier = modifier
            .height(55.dp) // a bit smaller
            .fillMaxWidth()
    )
}

fun saveImageToInternalStorage(context: Context, uri: Uri, filename: String): String {
    // Open streams
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)

    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }

    // Return the absolute path of the saved file
    return File(context.filesDir, filename).absolutePath
}
