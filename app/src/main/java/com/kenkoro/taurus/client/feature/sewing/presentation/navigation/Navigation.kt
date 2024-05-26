package com.kenkoro.taurus.client.feature.sewing.presentation.navigation

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
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.OrderScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.profile.ProfileScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.viewmodels.LoginViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.viewmodels.OrderViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.viewmodels.UserViewModel

@Composable
fun AppNavHost(
  navController: NavHostController = rememberNavController(),
  startDestination: (String, String) -> Screen,
  onExit: () -> Unit = {},
) {
  val context = LocalContext.current
  val networkConnectivityObserver: ConnectivityObserver = NetworkConnectivityObserver(context)
  val networkStatus by networkConnectivityObserver
    .observer()
    .collectAsState(initial = NetworkStatus.Unavailable)

  val loginViewModel: LoginViewModel = hiltViewModel()
  val orderViewModel: OrderViewModel = hiltViewModel()
  val userViewModel: UserViewModel = hiltViewModel()

  val (subject, password) = loginViewModel.decryptSubjectAndPassword()
  NavHost(
    navController = navController,
    startDestination = startDestination(subject, password).route,
  ) {
    composable(route = Screen.LoginScreen.route) {
      LoginScreen(
        networkStatus = networkStatus,
        subject = loginViewModel.subject,
        password = loginViewModel.password,
        onSetErrorMessages = loginViewModel::setErrorMessages,
        onLogin = loginViewModel::login,
        onEncryptAll = loginViewModel::encryptAll,
        onNavigateToOrderScreen = { navController.navigate(Screen.OrderScreen.route) },
        onExit = onExit,
        onShowErrorTitle = loginViewModel::showErrorTitle,
      )
    }

    composable(route = Screen.OrderScreen.route) {
      OrderScreen(
        user = userViewModel.user,
        onUser = userViewModel::user,
        ordersFlow = orderViewModel.orderPagingFlow,
        loginState = loginViewModel.loginState,
        onLoginResult = loginViewModel::loginResult,
        onAddNewUserLocally = userViewModel::addNewUserLocally,
        onAddNewOrderLocally = orderViewModel::addNewOrderLocally,
        onDeleteOrderLocally = orderViewModel::deleteOrderLocally,
        onEditOrderLocally = orderViewModel::editOrderLocally,
        onLogin = { subject, password ->
          loginViewModel.login(subject, password)
        },
        onGetUserRemotely = userViewModel::getUser,
        onAddNewOrderRemotely = orderViewModel::addNewOrderRemotely,
        onDeleteOrderRemotely = orderViewModel::deleteOrderRemotely,
        onEditOrderRemotely = orderViewModel::editOrderRemotely,
        onEncryptToken = orderViewModel::encryptToken,
        onDecryptSubjectAndPassword = loginViewModel::decryptSubjectAndPassword,
        onDecryptToken = userViewModel::decryptToken,
        onNavigateToProfileScreen = { navController.navigate(Screen.ProfileScreen.route) },
        networkStatus = networkStatus,
      )
    }

    composable(route = Screen.ProfileScreen.route) {
      ProfileScreen(
        onDeleteAllCredentials = orderViewModel::deleteAllCredentials,
        onNavigateToLoginScreen = { navController.navigate(Screen.LoginScreen.route) },
        onLoginResult = loginViewModel::loginResult,
      )
    }
  }
}