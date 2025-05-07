package com.example.ue5ex1

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun StartScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Wähle eine Übung",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { navController.navigate("ex1") }) {
            Text("Ex1: Touch & Distance")
        }

        Button(onClick = { navController.navigate("ex2") }) {
            Text("Ex2: Zeichenfläche & Gesten")
        }

        Button(onClick = { navController.navigate("ex3") }) {
            Text("Exercise 3 - MultiTouch")
        }


    }
}