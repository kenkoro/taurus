package com.kenkoro.taurus.client.feature.login.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kenkoro.taurus.client.feature.login.presentation.login.screen.LoginScreen
import com.kenkoro.taurus.client.feature.login.presentation.util.Screen
import com.kenkoro.taurus.client.feature.login.presentation.welcome.screen.WelcomeScreen
import com.kenkoro.taurus.client.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.light(
        scrim = Color.Transparent.toArgb(),
        darkScrim = Color.Transparent.toArgb()
      )
    )

    setContent {
      AppTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          val navController = rememberNavController()
          NavHost(navController = navController, startDestination = Screen.WelcomeScreen.route) {
            composable(route = Screen.WelcomeScreen.route) {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WelcomeScreen(navController = navController)
              }
            }

            composable(route = Screen.LoginScreen.route) {
              LoginScreen(navController = navController)
            }
          }
        }
      }
    }
  }
}