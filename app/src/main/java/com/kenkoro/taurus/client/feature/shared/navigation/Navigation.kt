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
import com.kenkoro.taurus.client.feature.auth.presentation.AuthScreen
import com.kenkoro.taurus.client.feature.auth.presentation.util.AuthScreenNavigator
import com.kenkoro.taurus.client.feature.auth.presentation.util.AuthScreenShared
import com.kenkoro.taurus.client.feature.dictionaries.presentation.DictionariesScreen
import com.kenkoro.taurus.client.feature.dictionaries.presentation.util.DictionariesScreenShared
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.OrderEditorScreen
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenShared
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenUtils
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.OrderScreen
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenShared
import com.kenkoro.taurus.client.feature.profile.presentation.ProfileScreen
import com.kenkoro.taurus.client.feature.profile.presentation.util.ProfileScreenNavigator
import com.kenkoro.taurus.client.feature.profile.presentation.util.ProfileScreenShared
import com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.OrderDetailsSearchScreen
import com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.OrderDetailsSearchScreenNavigator
import com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.OrderDetailsSearchScreenShared
import com.kenkoro.taurus.client.feature.search.presentation.screen.orders.SearchOrdersScreen
import com.kenkoro.taurus.client.feature.search.presentation.screen.orders.util.SearchOrdersScreenShared
import com.kenkoro.taurus.client.feature.shared.navigation.util.AppNavHostUtils
import com.kenkoro.taurus.client.feature.shared.navigation.util.NavBarUtils
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState
import com.kenkoro.taurus.client.feature.shared.viewmodels.NavHostViewModel
import com.kenkoro.taurus.client.feature.shared.viewmodels.SharedAuthViewModel
import com.kenkoro.taurus.client.feature.shared.viewmodels.SharedOrderDetailsSearchViewModel
import com.kenkoro.taurus.client.feature.shared.viewmodels.SharedOrderDetailsViewModel
import com.kenkoro.taurus.client.feature.shared.viewmodels.util.sharedHiltViewModel

@Composable
fun AppNavHost(
  navController: NavHostController = rememberNavController(),
  navHostUtils: AppNavHostUtils,
) {
  val viewModel: NavHostViewModel = hiltViewModel()

  val context = LocalContext.current
  val networkConnectivityObserver: ConnectivityObserver = NetworkConnectivityObserver(context)
  val networkStatus by networkConnectivityObserver
    .observer()
    .collectAsState(initial = NetworkStatus.Unavailable)

  val (subject, password) = viewModel.decryptUserCredentials()
  val startDestination = navHostUtils.startDestination(subject, password).route
  val navBarUtils = NavBarUtils(navController)

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
          getUser = sharedAuthViewModel::getUser,
        )

      AuthScreen(
        navigator = navigator,
        shared = shared,
      )
    }

    composable(route = Screen.OrderScreen.route) { entry ->
      val sharedAuthViewModel = entry.sharedHiltViewModel<SharedAuthViewModel>(navController)
      val sharedOrderDetailsViewModel =
        entry.sharedHiltViewModel<SharedOrderDetailsViewModel>(
          navController,
        )
      val navigator =
        OrderScreenNavigator(
          toProfileScreen = { navController.navigate(Screen.ProfileScreen.route) },
          toOrderEditorScreen = { isInEdit, subject ->
            if (!isInEdit) {
              sharedOrderDetailsViewModel.resetAllOrderDetails()
            }

            navController.navigate(
              buildString {
                append(Screen.OrderEditorScreen.route)
                append("?editOrder=$isInEdit&subject=$subject")
              },
            )
          },
        )
      val shared =
        OrderScreenShared(
          user = sharedAuthViewModel.user,
          authStatus = sharedAuthViewModel.authStatus,
          network = networkStatus,
          resetAllOrderDetails = sharedOrderDetailsViewModel::resetAllOrderDetails,
          proceedAuth = sharedAuthViewModel::proceedAuth,
          saveTheRestOfDetails = { orderId: Int, date: Long, status: OrderStatus ->
            sharedOrderDetailsViewModel.changeOrderId(orderId)
            sharedOrderDetailsViewModel.changeDate(date)
            sharedOrderDetailsViewModel.changeOrderStatus(status)
          },
          getUser = sharedAuthViewModel::getUser,
          currentRoute = entry.destination.route ?: Screen.OrderScreen.route,
          items = navBarUtils.listOfNavItems(sharedAuthViewModel.user),
        )
      val details = sharedOrderDetailsViewModel.getDetails()

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
          user = sharedAuthViewModel.user,
          resetAuthStatus = sharedAuthViewModel::reset,
          restartApp = navHostUtils.restart,
          currentRoute = entry.destination.route ?: Screen.OrderScreen.route,
          items = navBarUtils.listOfNavItems(sharedAuthViewModel.user),
        )

      ProfileScreen(
        navigator = navigator,
        shared = shared,
      )
    }

    composable(
      route =
        Screen.OrderEditorScreen.route +
          "?editOrder={editOrder}&subject={subject}",
      arguments =
        listOf(
          navArgument("editOrder") {
            type = NavType.BoolType
            defaultValue = false
          },
          navArgument("subject") {
            type = NavType.StringType
            defaultValue = ""
          },
        ),
    ) { entry ->
      val sharedAuthViewModel = entry.sharedHiltViewModel<SharedAuthViewModel>(navController)
      val sharedOrderDetailsViewModel =
        entry.sharedHiltViewModel<SharedOrderDetailsViewModel>(
          navController,
        )
      val sharedOrderDetailsSearchViewModel =
        entry.sharedHiltViewModel<SharedOrderDetailsSearchViewModel>(navController = navController)
      val navigator =
        OrderEditorScreenNavigator(
          navUp = { navController.navigateUp() },
          toOrderDetailsSearchScreen = { selectedDropDownState ->
            sharedOrderDetailsSearchViewModel.selectDropDown(selectedDropDownState)
            navController.navigate(Screen.OrderDetailsSearchScreen.route)
          },
        )
      val shared =
        OrderEditorScreenShared(
          user = sharedAuthViewModel.user,
          network = networkStatus,
          changeBehaviorOfOrderDetailsSearch = sharedOrderDetailsSearchViewModel::selectDropDown,
        )
      val utils =
        OrderEditorScreenUtils(
          isInEdit = entry.arguments?.getBoolean("editOrder") ?: false,
          subject = entry.arguments?.getString("subject") ?: "",
        )
      val details = sharedOrderDetailsViewModel.getDetails()

      OrderEditorScreen(
        navigator = navigator,
        details = details,
        shared = shared,
        utils = utils,
      )
    }

    composable(route = Screen.OrderDetailsSearchScreen.route) { entry ->
      val sharedOrderDetailsSearchViewModel =
        entry.sharedHiltViewModel<SharedOrderDetailsSearchViewModel>(navController = navController)

      val shared =
        OrderDetailsSearchScreenShared(
          selectedDropDownState =
            sharedOrderDetailsSearchViewModel.selectedDropDown ?: object :
              TaurusTextFieldState() {},
          selectDropDown = sharedOrderDetailsSearchViewModel::selectDropDown,
          fetch = sharedOrderDetailsSearchViewModel::fetch,
        )

      OrderDetailsSearchScreen(
        navigator = OrderDetailsSearchScreenNavigator { navController.navigateUp() },
        shared = shared,
      )
    }

    composable(route = Screen.SearchOrdersScreen.route) { entry ->
      val sharedAuthViewModel = entry.sharedHiltViewModel<SharedAuthViewModel>(navController)
      val shared =
        SearchOrdersScreenShared(
          user = sharedAuthViewModel.user,
          currentRoute = entry.destination.route ?: Screen.OrderScreen.route,
          items = navBarUtils.listOfNavItems(sharedAuthViewModel.user),
        )

      SearchOrdersScreen(shared = shared)
    }

    composable(route = Screen.DictionariesScreen.route) { entry ->
      val sharedAuthViewModel = entry.sharedHiltViewModel<SharedAuthViewModel>(navController)
      val shared =
        DictionariesScreenShared(
          user = sharedAuthViewModel.user,
          currentRoute = entry.destination.route ?: Screen.OrderScreen.route,
          items = navBarUtils.listOfNavItems(sharedAuthViewModel.user),
        )

      DictionariesScreen(shared = shared)
    }
  }
}