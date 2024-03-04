package com.kenkoro.taurus.client.feature.sewing.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kenkoro.taurus.client.feature.sewing.presentation.dashboard.screen.DashboardScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.LoginScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.util.Screen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
  NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
    composable(route = Screen.LoginScreen.route) {
      LoginScreen(
        onLogin = {
          navController.navigate(Screen.DashboardScreen.route)
        },
      )
    }
    composable(route = Screen.DashboardScreen.route) {
      DashboardScreen()
    }
  }
}