package com.kenkoro.taurus.client.feature.sewing.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kenkoro.taurus.client.core.connectivity.ConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.NetworkConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.LoginScreen
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.feature.sewing.presentation.util.Screen
import com.kenkoro.taurus.client.feature.sewing.presentation.viewmodels.LoginViewModel

@Composable
fun AppNavHost(
  navController: NavHostController = rememberNavController(),
  startDestination: (String, String) -> Screen,
) {
  val context = LocalContext.current
  val credentialService = DecryptedCredentialService(context)
  val networkConnectivityObserver: ConnectivityObserver = NetworkConnectivityObserver(context)
  val networkStatus by networkConnectivityObserver
    .observer()
    .collectAsState(initial = NetworkStatus.Unavailable)

  val loginViewModel: LoginViewModel = hiltViewModel()
  NavHost(
    navController = navController,
    startDestination =
    startDestination(
      credentialService.storedSubject(),
      credentialService.storedPassword(),
    ).route,
  ) {
    composable(route = Screen.LoginScreen.route) {
      LoginScreen(
        subject = loginViewModel.subject,
        password = loginViewModel.password,
        onSubject = loginViewModel::subject,
        onPassword = loginViewModel::password,
        onLogin = loginViewModel::login,
        onLoginResult = loginViewModel::loginResult,
        onEncryptAll = loginViewModel::encryptAll,
        onNavigateToOrderScreen = { navController.navigate(Screen.OrderScreen.route) },
        networkStatus = networkStatus,
      )
    }
  }
}