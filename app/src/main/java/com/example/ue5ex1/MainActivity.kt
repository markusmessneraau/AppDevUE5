package com.example.ue5ex1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "start"
                    ) {
                        composable("start") { StartScreen(navController) }
                        composable("ex1") { TouchScreen() }
                        composable("ex2") {
                            GestureScreen()
                        }
                        composable("ex3") { MultiTouchScreen() }

                    }
                }
            }
        }
    }
}
