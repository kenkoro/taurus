package com.kenkoro.taurus.client.feature.shared.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
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
import com.kenkoro.taurus.client.feature.login.presentation.util.LoginScreenNavigator
import com.kenkoro.taurus.client.feature.login.presentation.util.LoginScreenRemoteHandler
import com.kenkoro.taurus.client.feature.login.presentation.util.LoginScreenUtils
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.OrderEditorScreen
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.OrderEditorViewModel
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.OrderScreen
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.OrderViewModel
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers.LocalHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers.RemoteHandler
import com.kenkoro.taurus.client.feature.profile.presentation.ProfileScreen
import com.kenkoro.taurus.client.feature.profile.presentation.UserViewModel
import com.kenkoro.taurus.client.feature.profile.presentation.util.ProfileScreenNavigator
import com.kenkoro.taurus.client.feature.profile.presentation.util.ProfileScreenUtils
import com.kenkoro.taurus.client.feature.shared.navigation.util.AppNavHostUtils

typealias PSNavigator = ProfileScreenNavigator
typealias PSUtils = ProfileScreenUtils

typealias LSNavigator = LoginScreenNavigator
typealias LSRemoteHandler = LoginScreenRemoteHandler
typealias LSUtils = LoginScreenUtils

@Composable
fun AppNavHost(
  navController: NavHostController = rememberNavController(),
  utils: AppNavHostUtils,
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
  val startDestination = utils.startDestination(subject, password).route

  fun profileScreenParams(): Pair<PSNavigator, PSUtils> {
    val profileScreenNavigator =
      ProfileScreenNavigator {
        navController.navigate(Screen.LoginScreen.route)
      }
    val profileScreenUtils =
      ProfileScreenUtils(
        deleteAllStoredUserCredentials = userViewModel::deleteAllCredentials,
        resetLoginState = loginViewModel::resetLoginState,
        restart = utils.restart,
      )

    return Pair(profileScreenNavigator, profileScreenUtils)
  }

  fun loginScreenParams(): Triple<LSNavigator, LSRemoteHandler, LSUtils> {
    val loginScreenNavigator =
      LoginScreenNavigator {
        navController.navigate(Screen.OrderScreen.route)
      }
    val loginScreenRemoteHandler = LoginScreenRemoteHandler(loginViewModel::login)
    val loginScreenUtils =
      LoginScreenUtils(
        subject = loginViewModel.subject,
        password = loginViewModel.password,
        network = networkStatus,
        encryptAllUserCredentials = loginViewModel::encryptAll,
        exit = utils.exit,
        showErrorTitle = loginViewModel::showErrorTitle,
      )

    return Triple(loginScreenNavigator, loginScreenRemoteHandler, loginScreenUtils)
  }

  // NOTE: Refactor all these guys as well
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
      editOrder = orderEditorViewModel::editOrderRemotely,
      addNewCutOrder = orderViewModel::addNewCutOrderRemotely,
      getActualCutOrdersQuantity = orderViewModel::getActualCutOrdersQuantity,
    )
  val orderStatesHolder =
    OrderStatesHolder(
      categoryState = orderEditorViewModel.category,
      colorState = orderEditorViewModel.color,
      customerState = orderEditorViewModel.customer,
      modelState = orderEditorViewModel.model,
      orderIdState = orderEditorViewModel.orderId,
      quantityState = orderEditorViewModel.quantity,
      sizeState = orderEditorViewModel.size,
      titleState = orderEditorViewModel.title,
    )
  val (profileScreenNavigator, profileScreenUtils) = profileScreenParams()
  val (loginScreenNavigator, loginScreenRemoteHandler, loginScreenUtils) = loginScreenParams()

  NavHost(
    navController = navController,
    startDestination = startDestination,
  ) {
    composable(route = Screen.LoginScreen.route) {
      LoginScreen(
        utils = loginScreenUtils,
        remoteHandler = loginScreenRemoteHandler,
        navigator = loginScreenNavigator,
      )
    }

    composable(route = Screen.OrderScreen.route) {
      OrderScreen(
        ordersPagingFlow = orderViewModel.ordersPagingFlow,
        user = userViewModel.user,
        loginState = loginViewModel.loginState,
        networkStatus = networkStatus,
        selectedOrderRecordId = orderViewModel.selectedOrderRecordId,
        orderStatesHolder = orderStatesHolder,
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
        onOrderStatus = orderEditorViewModel::status,
        onOrderId = orderEditorViewModel::orderId,
        onNavigateToProfileScreen = { navController.navigate(Screen.ProfileScreen.route) },
        onNavigateToOrderEditorScreen = { editOrder ->
          navController.navigate(Screen.OrderEditorScreen.route + "?editOrder=$editOrder")
        },
        viewModelScope = orderViewModel.viewModelScope,
      )
    }

    composable(route = Screen.ProfileScreen.route) {
      ProfileScreen(
        navigator = profileScreenNavigator,
        utils = profileScreenUtils,
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
        userId = userViewModel.user?.userId ?: 0,
        userSubject = userViewModel.user?.subject ?: "",
        orderStatus = orderEditorViewModel.status,
        networkStatus = networkStatus,
        orderStatesHolder = orderStatesHolder,
        editOrder = editOrder,
        onNavUp = navController::navigateUp,
        onAddNewOrderRemotely = orderViewModel::addNewOrderRemotely,
        onEditOrderRemotely = orderEditorViewModel::editOrderRemotely,
      )
    }
  }
}