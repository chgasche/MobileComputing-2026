package com.example.homework2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.homework2.navigation.Screens
import com.example.homework2.ui.theme.Homework2Theme

@Composable
fun DetailScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = "Detail Screen",
                fontWeight = FontWeight.Bold
            )
        }
        Row( modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ){
            Button(
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
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    Homework2Theme {
        val navController = rememberNavController()
        DetailScreen(navController)
    }
}