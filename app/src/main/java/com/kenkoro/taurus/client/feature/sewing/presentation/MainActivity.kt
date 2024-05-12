package com.kenkoro.taurus.client.feature.sewing.presentation

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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.navigation.AppNavHost
import com.kenkoro.taurus.client.feature.sewing.presentation.navigation.Screen
import com.kenkoro.taurus.client.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen()

    setContent {
      enableEdgeToEdge(
        statusBarStyle =
          SystemBarStyle.auto(
            lightScrim = Color.Transparent.toArgb(),
            darkScrim = Color.Transparent.toArgb(),
            detectDarkMode = { resources ->
              return@auto if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                resources.configuration.isNightModeActive
              } else {
                false
              }
            },
          ),
      )

      AppTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background,
        ) {
          AppNavHost(
            startDestination = { subject, password ->
              if (subject.isNotBlank() && password.isNotBlank()) {
                Screen.OrderScreen
              } else {
                Screen.LoginScreen
              }
            },
            onRestartApp = {
              finish()
              startActivity(intent)
            },
          )
        }
      }
    }
  }
}