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
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenUtils
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.OrderScreen
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.OrderViewModel
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenLocalHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenRemoteHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenUtils
import com.kenkoro.taurus.client.feature.profile.presentation.ProfileScreen
import com.kenkoro.taurus.client.feature.profile.presentation.UserViewModel
import com.kenkoro.taurus.client.feature.profile.presentation.util.ProfileScreenNavigator
import com.kenkoro.taurus.client.feature.profile.presentation.util.ProfileScreenUtils
import com.kenkoro.taurus.client.feature.search.order.details.presentation.OrderDetailsSearchScreen
import com.kenkoro.taurus.client.feature.search.order.details.presentation.OrderDetailsSearchViewModel
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.OrderDetailsSearchScreenNavigator
import com.kenkoro.taurus.client.feature.shared.navigation.util.AppNavHostUtils
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

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
  val orderDetailsSearchViewModel: OrderDetailsSearchViewModel = hiltViewModel()

  val context = LocalContext.current
  val networkConnectivityObserver: ConnectivityObserver = NetworkConnectivityObserver(context)
  val networkStatus by networkConnectivityObserver
    .observer()
    .collectAsState(initial = NetworkStatus.Unavailable)
  val (subject, password) = loginViewModel.decryptSubjectAndPassword()
  val startDestination = utils.startDestination(subject, password).route

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

  fun orderScreenParams(): Triple<OrderScreenNavigator, OrderScreenUtils, OrderStatesHolder> {
    val orderScreenNavigator =
      OrderScreenNavigator(
        toProfileScreen = { navController.navigate(Screen.ProfileScreen.route) },
        toOrderEditorScreen = { editOrder: Boolean ->
          navController.navigate(Screen.OrderEditorScreen.route + "?editOrder=$editOrder")
        },
      )
    val orderScreenUtils =
      OrderScreenUtils(
        ordersPagingFlow = orderViewModel.ordersPagingFlow,
        user = userViewModel.user,
        loginState = loginViewModel.loginState,
        network = networkStatus,
        selectedOrderRecordId = orderViewModel.selectedOrderRecordId,
        saveUser = userViewModel::user,
        newOrdersFilter = orderViewModel::filterStrategy,
        selectOrder = orderViewModel::selectOrder,
        newLoginState = loginViewModel::loginState,
        encryptJWToken = userViewModel::encryptToken,
        decryptUserSubjectAndItsPassword = loginViewModel::decryptSubjectAndPassword,
        decryptJWToken = userViewModel::decryptToken,
        resetAllOrderStates = orderEditorViewModel::resetAll,
        saveOrderStatus = orderEditorViewModel::status,
        saveOrderId = orderEditorViewModel::orderId,
        saveDate = orderEditorViewModel::date,
        viewModelScope = orderViewModel.viewModelScope,
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

    return Triple(orderScreenNavigator, orderScreenUtils, orderStatesHolder)
  }

  fun orderScreenHandlers(): Pair<OrderScreenLocalHandler, OrderScreenRemoteHandler> {
    val orderScreenLocalHandler =
      OrderScreenLocalHandler(
        addNewUser = userViewModel::addNewUserLocally,
        addNewOrder = orderViewModel::addNewOrderLocally,
        deleteOrder = orderViewModel::deleteOrderLocally,
        editOrder = orderViewModel::editOrderLocally,
      )
    val orderScreenRemoteHandler =
      OrderScreenRemoteHandler(
        login = loginViewModel::login,
        getUser = userViewModel::getUser,
        addNewOrder = orderViewModel::addNewOrderRemotely,
        deleteOrder = orderViewModel::deleteOrderRemotely,
        editOrder = orderEditorViewModel::editOrderRemotely,
        addNewCutOrder = orderViewModel::addNewCutOrderRemotely,
        getActualCutOrdersQuantity = orderViewModel::getActualCutOrdersQuantity,
      )

    return Pair(orderScreenLocalHandler, orderScreenRemoteHandler)
  }

  val (loginScreenNavigator, loginScreenRemoteHandler, loginScreenUtils) = loginScreenParams()
  val (profileScreenNavigator, profileScreenUtils) = profileScreenParams()
  val (orderScreenNavigator, orderScreenUtils, orderStatesHolder) = orderScreenParams()
  val (orderScreenLocalHandler, orderScreenRemoteHandler) = orderScreenHandlers()

  val orderEditorScreenNavigator =
    OrderEditorScreenNavigator(
      navUp = navController::navigateUp,
      toOrderDetailsSearchScreen = {
        orderDetailsSearchViewModel.selectedSearchState = it
        navController.navigate(Screen.OrderDetailsSearchScreen.route)
      },
    )

  NavHost(
    navController = navController,
    startDestination = startDestination,
  ) {
    composable(route = Screen.LoginScreen.route) {
      LoginScreen(
        remoteHandler = loginScreenRemoteHandler,
        navigator = loginScreenNavigator,
        utils = loginScreenUtils,
      )
    }

    composable(route = Screen.OrderScreen.route) {
      OrderScreen(
        localHandler = orderScreenLocalHandler,
        remoteHandler = orderScreenRemoteHandler,
        navigator = orderScreenNavigator,
        utils = orderScreenUtils,
        states = orderStatesHolder,
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
      val orderEditorScreenUtils =
        OrderEditorScreenUtils(
          user = userViewModel.user,
          orderStatus = orderEditorViewModel.status,
          network = networkStatus,
          editOrder = editOrder,
        )

      OrderEditorScreen(
        remoteHandler = orderScreenRemoteHandler,
        navigator = orderEditorScreenNavigator,
        utils = orderEditorScreenUtils,
        states = orderStatesHolder,
        onStateChangeOrderDetailsSearchBehavior =
          orderDetailsSearchViewModel::changeOrderDetailsSearchBehavior,
      )
    }

    composable(route = Screen.OrderDetailsSearchScreen.route) {
      OrderDetailsSearchScreen(
        navigator = OrderDetailsSearchScreenNavigator { navController.navigateUp() },
        selectedSearchState =
          orderDetailsSearchViewModel.selectedSearchState ?: object :
            TaurusTextFieldState() {},
        onFetchData = orderDetailsSearchViewModel::fetch,
        onNavUp = { navController.navigateUp() },
      )
    }
  }
}