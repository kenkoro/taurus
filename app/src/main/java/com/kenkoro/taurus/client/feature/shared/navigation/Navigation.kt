package com.kenkoro.taurus.client.feature.shared.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kenkoro.taurus.client.core.connectivity.ConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.NetworkConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.feature.auth.presentation.AuthScreen
import com.kenkoro.taurus.client.feature.auth.presentation.util.AuthScreenNavigator
import com.kenkoro.taurus.client.feature.auth.presentation.util.AuthUtils
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.OrderEditorScreen
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.OrderEditorViewModel
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenUtils
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
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.OrderDetailsSearchScreenRemoteHandler
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.OrderDetailsSearchScreenUtils
import com.kenkoro.taurus.client.feature.shared.navigation.util.AppNavHostUtils
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState
import com.kenkoro.taurus.client.feature.shared.viewmodels.SharedAuthViewModel
import com.kenkoro.taurus.client.feature.shared.viewmodels.util.sharedHiltViewModel

typealias PSNavigator = ProfileScreenNavigator
typealias PSUtils = ProfileScreenUtils

typealias OESNavigator = OrderEditorScreenNavigator
typealias OESUtils = OrderEditorScreenUtils

@Composable
fun AppNavHost(
  navController: NavHostController = rememberNavController(),
  navHostUtils: AppNavHostUtils,
) {
  val orderViewModel: OrderViewModel = hiltViewModel()
  val userViewModel: UserViewModel = hiltViewModel()
  val orderEditorViewModel: OrderEditorViewModel = hiltViewModel()
  val orderDetailsSearchViewModel: OrderDetailsSearchViewModel = hiltViewModel()

  val context = LocalContext.current
  val networkConnectivityObserver: ConnectivityObserver = NetworkConnectivityObserver(context)
  val networkStatus by networkConnectivityObserver
    .observer()
    .collectAsState(initial = NetworkStatus.Unavailable)

  val decryptedCredentialService = DecryptedCredentialService(context)
  val (subject, password) = decryptedCredentialService.decryptUserCredentials()
  val startDestination = navHostUtils.startDestination(subject, password).route

  fun profileScreenParams(): Pair<PSNavigator, PSUtils> {
    val profileScreenNavigator =
      ProfileScreenNavigator {
        navController.navigate(Screen.AuthScreen.route)
      }
    val profileScreenUtils =
      ProfileScreenUtils(
        deleteAllStoredUserCredentials = userViewModel::deleteAllCredentials,
        resetLoginState = authViewModel::resetLoginState,
        restart = navHostUtils.restart,
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
        authStatus = authViewModel.loginState,
        network = networkStatus,
        selectedOrderRecordId = orderViewModel.selectedOrderRecordId,
        saveUser = userViewModel::user,
        newOrdersFilter = orderViewModel::filterStrategy,
        selectOrder = orderViewModel::selectOrder,
        newLoginState = authViewModel::loginState,
        encryptJWToken = userViewModel::encryptToken,
        decryptUserSubjectAndItsPassword = authViewModel::decryptSubjectAndPassword,
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
        login = authViewModel::login,
        getUser = userViewModel::getUser,
        addNewOrder = orderViewModel::addNewOrderRemotely,
        deleteOrder = orderViewModel::deleteOrderRemotely,
        editOrder = orderEditorViewModel::editOrderRemotely,
        addNewCutOrder = orderViewModel::addNewCutOrderRemotely,
        getActualCutOrdersQuantity = orderViewModel::getActualCutOrdersQuantity,
      )

    return Pair(orderScreenLocalHandler, orderScreenRemoteHandler)
  }

  fun orderEditorScreenParams(editOrder: Boolean = false): Pair<OESNavigator, OESUtils> {
    val orderEditorScreenNavigator =
      OrderEditorScreenNavigator(
        navUp = navController::navigateUp,
        toOrderDetailsSearchScreen = {
          orderDetailsSearchViewModel.selectedSearchState = it
          navController.navigate(Screen.OrderDetailsSearchScreen.route)
        },
      )
    val orderEditorScreenUtils =
      OrderEditorScreenUtils(
        user = userViewModel.user,
        orderStatus = orderEditorViewModel.status,
        network = networkStatus,
        editOrder = editOrder,
        changeOrderDetailsSearchScreenBehavior =
          orderDetailsSearchViewModel::changeOrderDetailsSearchBehavior,
      )

    return Pair(orderEditorScreenNavigator, orderEditorScreenUtils)
  }

  val (profileScreenNavigator, profileScreenUtils) = profileScreenParams()
  val (orderScreenNavigator, orderScreenUtils, orderStatesHolder) = orderScreenParams()
  val (orderScreenLocalHandler, orderScreenRemoteHandler) = orderScreenHandlers()

  NavHost(
    navController = navController,
    startDestination = startDestination,
  ) {
    composable(route = Screen.AuthScreen.route) {
      val navigator = AuthScreenNavigator { navController.navigate(Screen.OrderScreen.route) }
      val utils =
        AuthUtils(
          network = networkStatus,
          exit = navHostUtils.exit,
        )

      AuthScreen(
        navigator = navigator,
        utils = utils,
      )
    }

    composable(route = Screen.OrderScreen.route) { entry: NavBackStackEntry ->
      val sharedAuthStatus = entry.sharedHiltViewModel<SharedAuthViewModel>(navController)
      // Here, you can now access shared states

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
      val (orderEditorScreenNavigator, orderEditorScreenUtils) = orderEditorScreenParams(editOrder)

      OrderEditorScreen(
        remoteHandler = orderScreenRemoteHandler,
        navigator = orderEditorScreenNavigator,
        utils = orderEditorScreenUtils,
        states = orderStatesHolder,
      )
    }

    composable(route = Screen.OrderDetailsSearchScreen.route) {
      OrderDetailsSearchScreen(
        remoteHandler = OrderDetailsSearchScreenRemoteHandler(orderDetailsSearchViewModel::fetch),
        navigator = OrderDetailsSearchScreenNavigator { navController.navigateUp() },
        utils =
          OrderDetailsSearchScreenUtils(
            orderDetailsSearchViewModel.selectedSearchState ?: object : TaurusTextFieldState() {},
          ),
      )
    }
  }
}