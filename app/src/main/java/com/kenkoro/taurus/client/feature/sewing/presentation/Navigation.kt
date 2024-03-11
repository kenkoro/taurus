package com.kenkoro.taurus.client.feature.sewing.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kenkoro.taurus.client.feature.sewing.presentation.dashboard.screen.DashboardScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.LoginScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.util.Screen

@Composable
fun AppNavHost(
  navController: NavHostController = rememberNavController(),
  mainViewModel: MainViewModel = hiltViewModel(),
) {
  NavHost(
    navController = navController,
    startDestination = mainViewModel.startDestination().route,
  ) {
    composable(route = Screen.LoginScreen.route) {
      LoginScreen(
        onLoginNavigate = {
          navController.navigate(Screen.DashboardScreen.route)
        },
      )
    }
    composable(route = Screen.DashboardScreen.route) {
      DashboardScreen(
        onLoginNavigate = {
          navController.navigate(Screen.LoginScreen.route)
        },
        mainViewModel = mainViewModel,
      )
    }
  }
}