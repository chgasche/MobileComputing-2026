package com.example.homework3.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import kotlinx.coroutines.*
import com.example.homework3.data.FoodItem
import com.example.homework3.data.FoodItemDao
import com.example.homework3.navigation.Screens
import com.example.homework3.ui.theme.Homework3Theme
import java.io.File
import java.io.FileOutputStream

@Composable
fun AddItemScreen(navController: NavController, foodItemDao: FoodItemDao) {
    val defaultTitle = "Title"
    val defaultExpiryDate = "YYYY-MM-DD"
    var textItemTitle by remember { mutableStateOf(defaultTitle) }
    var textItemExpiryDate by remember { mutableStateOf(defaultExpiryDate) }
    var textImageURI by remember { mutableStateOf<Uri?>(null) }
    var savedPath = ""

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the photo picker.
        if (uri != null) {
            textImageURI = uri
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Add new Food",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Input fields
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ){
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Text field for Food title
                OutlinedTextField(
                    value = textItemTitle,
                    onValueChange = {textItemTitle = it},
                    label = { Text("Food Title") }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Text field for expiry date
                OutlinedTextField(
                    value = textItemExpiryDate,
                    onValueChange = {textItemExpiryDate = it},
                    label = { Text("Expiry Date (YYYY-MM-DD)") }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Image selection
                Button (
                    onClick = {
                        // image picker
                        val mimeType = "image/jpeg"
                        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.SingleMimeType(mimeType)))
                        //pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    },
                    modifier = Modifier.size(56.dp)
                ) {
                    Text("+")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text (
                    text = if (textImageURI == null) "Add Image" else "Image selected",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }


        // Two buttons
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ){
            OutlinedButton (
                onClick = {
                    navController.navigate(Screens.Home.route){
                        popUpTo(Screens.Home.route) { inclusive = true }
                    }
                }
            ) {
                Text(
                    text = "Go back"
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Submit button
            val scope = rememberCoroutineScope()
            val context = LocalContext.current

            val isValid = textItemTitle.isNotBlank() && textItemTitle != defaultTitle && textItemExpiryDate.isNotBlank() && textItemExpiryDate != defaultExpiryDate
            Button (
                enabled = isValid,
                onClick = {
                    // Insert food
                    scope.launch {

                        // Copy file to app storage
                        if(textImageURI != null) {
                            val newFilename = "${System.currentTimeMillis()}.jpg"
                            savedPath = saveImageToInternalStorage(
                                context = context,
                                uri = textImageURI!!,
                                filename = newFilename)
                        }

                        // Pass to DAO and insert into DB
                        insertFoodItem(
                            dao = foodItemDao,
                            title = textItemTitle,
                            expiryDate = textItemExpiryDate,
                            savedPath = savedPath
                        )

                        // Navigate back
                        navController.navigate(Screens.Home.route){
                            popUpTo(Screens.Home.route) { inclusive = true }
                        }
                    }
                }
            ) {
                Text(
                    text = "Insert Food"
                )
            }
        }


        // Image preview
        Spacer(modifier = Modifier.height(24.dp))

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



suspend fun insertFoodItem(dao: FoodItemDao, title: String, expiryDate: String, savedPath: String?) {
    val imagePath = savedPath ?: ""

    // Create new FoodItem
    val foodItem = FoodItem(
        title = title,
        expiryDate = expiryDate,
        imagePath = imagePath
    )

    dao.insert(foodItem)
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



/*@Preview(showBackground = true)
@Composable
fun AddItemScreenPreview() {
    Homework3Theme {
        val navController = rememberNavController()
        AddItemScreen(navController)
    }
}*/