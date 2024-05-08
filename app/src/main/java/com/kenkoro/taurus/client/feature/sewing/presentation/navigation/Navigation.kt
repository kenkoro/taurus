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
import androidx.paging.compose.collectAsLazyPagingItems
import com.kenkoro.taurus.client.core.connectivity.ConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.NetworkConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.LoginScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.OrderScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.viewmodels.LoginViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.viewmodels.OrderViewModel

@Composable
fun AppNavHost(
  navController: NavHostController = rememberNavController(),
  startDestination: (String, String) -> Screen,
) {
  val context = LocalContext.current
  val networkConnectivityObserver: ConnectivityObserver = NetworkConnectivityObserver(context)
  val networkStatus by networkConnectivityObserver
    .observer()
    .collectAsState(initial = NetworkStatus.Unavailable)

  val loginViewModel: LoginViewModel = hiltViewModel()
  val orderViewModel: OrderViewModel = hiltViewModel()
  val orders = orderViewModel.orderPagingFlow.collectAsLazyPagingItems()

  val (subject, password) = loginViewModel.decryptSubjectAndPassword()
  NavHost(
    navController = navController,
    startDestination = startDestination(subject, password).route,
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

    composable(route = Screen.OrderScreen.route) {
      OrderScreen(
        orders = orders,
        loginResult = loginViewModel.loginResult,
        onLogin = { subject, password ->
          loginViewModel.login(subject, password)
        },
        onLoginResult = loginViewModel::loginResult,
        onEncryptToken = orderViewModel::encryptToken,
        onAddNewOrderLocally = orderViewModel::addNewOrderLocally,
        onAddNewOrderRemotely = orderViewModel::addNewOrderRemotely,
        onDeleteOrderLocally = orderViewModel::deleteOrderLocally,
        onDeleteOrderRemotely = orderViewModel::deleteOrderRemotely,
        onDecryptSubjectAndPassword = loginViewModel::decryptSubjectAndPassword,
        networkStatus = networkStatus,
      )
    }
  }
}