package com.kenkoro.taurus.client.feature.sewing.presentation

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.LoginScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.util.Screen
import com.kenkoro.taurus.client.feature.sewing.presentation.welcome.screen.WelcomeScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
  NavHost(navController = navController, startDestination = Screen.WelcomeScreen.route) {
    composable(route = Screen.WelcomeScreen.route) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WelcomeScreen(onContinue = { navController.navigate(Screen.LoginScreen.route) })
      }
    }

    composable(route = Screen.LoginScreen.route) {
      LoginScreen(onLogin = { navController.navigate(Screen.DashboardScreen.route) })
    }
  }
}