package com.kenkoro.taurus.client.feature.sewing.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.dashboard.DashboardScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.LoginScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.util.DecryptedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LocalCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.Screen

@Composable
fun AppNavHost(
  navController: NavHostController = rememberNavController(),
  startDestination: (String, String) -> Screen,
) {
  val context = LocalContext.current
  val locallyStoredSubject =
    DecryptedCredentials.getDecryptedCredential(
      filename = LocalCredentials.SUBJECT_FILENAME,
      context = context,
    ).value
  val locallyStoredPassword =
    DecryptedCredentials.getDecryptedCredential(
      filename = LocalCredentials.PASSWORD_FILENAME,
      context = context,
    ).value

  NavHost(
    navController = navController,
    startDestination = startDestination(locallyStoredSubject, locallyStoredPassword).route,
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
        onDashboardNavigate = {
          navController.navigate(Screen.LoginScreen.route)
        },
      )
    }
  }
}