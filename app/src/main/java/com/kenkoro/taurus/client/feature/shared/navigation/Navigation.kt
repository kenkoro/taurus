package com.kenkoro.taurus.client.feature.shared.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
import com.kenkoro.taurus.client.feature.auth.presentation.util.AuthScreenShared
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.OrderEditorScreen
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.OrderDetails
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenUtils
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.OrderScreen
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenShared
import com.kenkoro.taurus.client.feature.profile.presentation.ProfileScreen
import com.kenkoro.taurus.client.feature.profile.presentation.util.ProfileScreenNavigator
import com.kenkoro.taurus.client.feature.profile.presentation.util.ProfileScreenShared
import com.kenkoro.taurus.client.feature.search.order.details.presentation.OrderDetailsSearchScreen
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.OrderDetailsSearchScreenNavigator
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.OrderDetailsSearchScreenRemoteHandler
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.OrderDetailsSearchScreenUtils
import com.kenkoro.taurus.client.feature.shared.navigation.util.AppNavHostUtils
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState
import com.kenkoro.taurus.client.feature.shared.viewmodels.SharedAuthViewModel
import com.kenkoro.taurus.client.feature.shared.viewmodels.SharedOrderDetailsViewModel
import com.kenkoro.taurus.client.feature.shared.viewmodels.util.sharedHiltViewModel

typealias OESNavigator = OrderEditorScreenNavigator
typealias OESUtils = OrderEditorScreenUtils

@Composable
fun AppNavHost(
  navController: NavHostController = rememberNavController(),
  navHostUtils: AppNavHostUtils,
) {
  val context = LocalContext.current
  val networkConnectivityObserver: ConnectivityObserver = NetworkConnectivityObserver(context)
  val networkStatus by networkConnectivityObserver
    .observer()
    .collectAsState(initial = NetworkStatus.Unavailable)

  val decryptedCredentialService = DecryptedCredentialService(context)
  val (subject, password) = decryptedCredentialService.decryptUserCredentials()
  val startDestination = navHostUtils.startDestination(subject, password).route

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

  NavHost(
    navController = navController,
    startDestination = startDestination,
  ) {
    composable(route = Screen.AuthScreen.route) { entry ->
      val sharedAuthViewModel = entry.sharedHiltViewModel<SharedAuthViewModel>(navController)
      val navigator = AuthScreenNavigator { navController.navigate(Screen.OrderScreen.route) }
      val shared =
        AuthScreenShared(
          network = networkStatus,
          exit = navHostUtils.exit,
          proceedAuth = sharedAuthViewModel::proceedAuth,
        )

      AuthScreen(
        navigator = navigator,
        shared = shared,
      )
    }

    composable(route = Screen.OrderScreen.route) { entry: NavBackStackEntry ->
      val sharedAuthViewModel = entry.sharedHiltViewModel<SharedAuthViewModel>(navController)
      val sharedOrderDetailsViewModel =
        entry.sharedHiltViewModel<SharedOrderDetailsViewModel>(
          navController,
        )
      val navigator =
        OrderScreenNavigator(
          toProfileScreen = { navController.navigate(Screen.ProfileScreen.route) },
          toOrderEditorScreen = { editOrder: Boolean ->
            navController.navigate(Screen.OrderEditorScreen.route + "?editOrder=$editOrder")
          },
        )
      val shared =
        OrderScreenShared(
          authStatus = sharedAuthViewModel.authStatus,
          network = networkStatus,
          resetAllOrderDetails = sharedOrderDetailsViewModel::resetAllOrderDetails,
          proceedAuth = sharedAuthViewModel::proceedAuth,
          saveTheRestDetails = { orderId: Int, date: Long, status: OrderStatus ->
            sharedOrderDetailsViewModel.changeOrderId(orderId)
            sharedOrderDetailsViewModel.changeDate(date)
            sharedOrderDetailsViewModel.changeOrderStatus(status)
          },
        )
      val details =
        OrderDetails(
          orderIdState = sharedOrderDetailsViewModel.orderId,
          dateState = sharedOrderDetailsViewModel.date,
          statusState = sharedOrderDetailsViewModel.status,
          categoryState = sharedOrderDetailsViewModel.category,
          colorState = sharedOrderDetailsViewModel.color,
          customerState = sharedOrderDetailsViewModel.customer,
          modelState = sharedOrderDetailsViewModel.model,
          quantityState = sharedOrderDetailsViewModel.quantity,
          sizeState = sharedOrderDetailsViewModel.size,
          titleState = sharedOrderDetailsViewModel.title,
        )

      OrderScreen(
        navigator = navigator,
        shared = shared,
        details = details,
      )
    }

    composable(route = Screen.ProfileScreen.route) { entry ->
      val sharedAuthViewModel = entry.sharedHiltViewModel<SharedAuthViewModel>(navController)
      val navigator =
        ProfileScreenNavigator {
          navController.navigate(Screen.AuthScreen.route)
        }
      val shared =
        ProfileScreenShared(
          resetAuthStatus = sharedAuthViewModel::reset,
          restartApp = navHostUtils.restart,
        )

      ProfileScreen(
        navigator = navigator,
        shared = shared,
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
    ) { entry ->
      val sharedOrderDetailsViewModel =
        entry.sharedHiltViewModel<SharedOrderDetailsViewModel>(
          navController,
        )

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