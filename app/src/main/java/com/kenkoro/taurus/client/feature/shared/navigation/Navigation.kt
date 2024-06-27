package com.kenkoro.taurus.client.feature.shared.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kenkoro.taurus.client.core.connectivity.ConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.NetworkConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.login.presentation.LoginScreen
import com.kenkoro.taurus.client.feature.login.presentation.LoginViewModel
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.OrderEditorScreen
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.OrderEditorViewModel
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.OrderScreen
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.OrderViewModel
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers.LocalHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers.RemoteHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.states.LoginState
import com.kenkoro.taurus.client.feature.profile.presentation.ProfileScreen
import com.kenkoro.taurus.client.feature.profile.presentation.UserViewModel

@Composable
fun AppNavHost(
  navController: NavHostController = rememberNavController(),
  onLoginChooseDestination: (String, String) -> Screen,
  onExit: () -> Unit = {},
  onRestart: () -> Unit = {},
) {
  val loginViewModel: LoginViewModel = hiltViewModel()
  val orderViewModel: OrderViewModel = hiltViewModel()
  val userViewModel: UserViewModel = hiltViewModel()
  val orderEditorViewModel: OrderEditorViewModel = hiltViewModel()

  val context = LocalContext.current
  val networkConnectivityObserver: ConnectivityObserver = NetworkConnectivityObserver(context)
  val networkStatus by networkConnectivityObserver
    .observer()
    .collectAsState(initial = NetworkStatus.Unavailable)
  val (subject, password) = loginViewModel.decryptSubjectAndPassword()
  val startDestination = onLoginChooseDestination(subject, password).route

  val localHandler =
    LocalHandler(
      addNewUser = userViewModel::addNewUserLocally,
      addNewOrder = orderViewModel::addNewOrderLocally,
      deleteOrder = orderViewModel::deleteOrderLocally,
      editOrder = orderViewModel::editOrderLocally,
    )
  val remoteHandler =
    RemoteHandler(
      login = loginViewModel::login,
      getUser = userViewModel::getUser,
      addNewOrder = orderViewModel::addNewOrderRemotely,
      deleteOrder = orderViewModel::deleteOrderRemotely,
      editOrder = orderViewModel::editOrderRemotely,
    )

  NavHost(
    navController = navController,
    startDestination = startDestination,
  ) {
    composable(route = Screen.LoginScreen.route) {
      LoginScreen(
        networkStatus = networkStatus,
        subject = loginViewModel.subject,
        password = loginViewModel.password,
        onLogin = loginViewModel::login,
        onEncryptAll = loginViewModel::encryptAll,
        onExit = onExit,
        onShowErrorTitle = loginViewModel::showErrorTitle,
        onNavigateToOrderScreen = { navController.navigate(Screen.OrderScreen.route) },
      )
    }

    composable(route = Screen.OrderScreen.route) {
      OrderScreen(
        ordersPagingFlow = orderViewModel.ordersPagingFlow,
        user = userViewModel.user,
        loginState = loginViewModel.loginState,
        networkStatus = networkStatus,
        selectedOrderRecordId = orderViewModel.selectedOrderRecordId,
        orderIdState = orderEditorViewModel.orderId,
        onUser = userViewModel::user,
        onFilterStrategy = orderViewModel::filterStrategy,
        onSelectOrder = orderViewModel::selectOrder,
        onLoginState = loginViewModel::loginState,
        localHandler = localHandler,
        remoteHandler = remoteHandler,
        onEncryptToken = userViewModel::encryptToken,
        onDecryptSubjectAndPassword = loginViewModel::decryptSubjectAndPassword,
        onDecryptToken = userViewModel::decryptToken,
        onResetAllOrderFields = orderEditorViewModel::resetAll,
        onNavigateToProfileScreen = { navController.navigate(Screen.ProfileScreen.route) },
        onNavigateToOrderEditorScreen = { editOrder ->
          navController.navigate(Screen.OrderEditorScreen.route + "?editOrder=$editOrder")
        },
      )
    }

    composable(route = Screen.ProfileScreen.route) {
      ProfileScreen(
        onDeleteAllCredentials = userViewModel::deleteAllCredentials,
        onResetLoginState = { loginViewModel.loginState(LoginState.NotLoggedYet) },
        onRestart = onRestart,
        onNavigateToLoginScreen = { navController.navigate(Screen.LoginScreen.route) },
      )
    }

    composable(
      route =
        Screen.OrderEditorScreen.route +
          "?editOrder={editOrder}",
      arguments =
        listOf(
          navArgument("editOrder") {
            type = NavType.BoolType
            defaultValue = false
          },
        ),
    ) {
      val editOrder = it.arguments?.getBoolean("editOrder") ?: false
      OrderEditorScreen(
        networkStatus = networkStatus,
        orderIdState = orderEditorViewModel.orderId,
        editOrder = editOrder,
        onNavUp = navController::navigateUp,
        onSaveChanges = {},
      )
    }
  }
}