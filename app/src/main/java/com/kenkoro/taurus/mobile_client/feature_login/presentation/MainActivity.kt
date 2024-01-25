package com.kenkoro.taurus.mobile_client.feature_login.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kenkoro.taurus.mobile_client.feature_login.presentation.login_screen.LoginScreen
import com.kenkoro.taurus.mobile_client.feature_login.presentation.util.Screen
import com.kenkoro.taurus.mobile_client.ui.theme.TaurusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      TaurusTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          val navController = rememberNavController()
          NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
            composable(route = Screen.LoginScreen.route) {
              LoginScreen(navController = navController)
            }
          }
        }
      }
    }
  }
}