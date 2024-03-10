package com.kenkoro.taurus.client.feature.sewing.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kenkoro.taurus.client.feature.sewing.presentation.dashboard.screen.DashboardScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.LoginScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType
import com.kenkoro.taurus.client.feature.sewing.presentation.util.Screen

@Composable
fun AppNavHost(
  navController: NavHostController = rememberNavController(),
  loginResponseType: LoginResponseType
) {
  val startDestination =
    if (
      loginResponseType != LoginResponseType.FAILURE &&
      loginResponseType != LoginResponseType.BAD_ENCRYPTED_CREDENTIALS
    ) {
      Screen.DashboardScreen.route
    } else {
      Screen.LoginScreen.route
    }

  NavHost(navController = navController, startDestination = startDestination) {
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
        })
    }
  }
}