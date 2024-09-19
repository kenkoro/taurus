package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.compose.collectAsLazyPagingItems
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.OrderDetails
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenSnackbarsHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenUtils
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.CutterOrderFilter
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.InspectorOrderFilter
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.ManagerOrderFilter
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.OrderFilterStrategy
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.viewmodels.OrderContentViewModel
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Cutter
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Inspector
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Manager
import com.kenkoro.taurus.client.feature.shared.states.AuthStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OrderContent(
  modifier: Modifier = Modifier,
  navigator: OrderScreenNavigator,
  utils: OrderScreenUtils,
  details: OrderDetails,
  snackbarsHolder: OrderScreenSnackbarsHolder,
  user: User?,
) {
  val viewModel: OrderContentViewModel = hiltViewModel()

  val network = utils.network

  LaunchedEffect(network, Dispatchers.Main) {
    if (network != NetworkStatus.Available) {
      snackbarsHolder.internetConnectionError()
    }
  }

  if (utils.authStatus == AuthStatus.WaitingForAuth) {
    LaunchedEffect(Unit, Dispatchers.IO) {
      val (subject, password) = viewModel.decryptUserCredentials()
      val authRequest = viewModel.auth(subject, password)

      authRequest.onSuccess { token ->
        viewModel.encryptJWToken(token)
        val isFailure = viewModel.getUser(subject, token) { utils.proceedAuth(AuthStatus.Success) }
        if (isFailure) {
          withContext(Dispatchers.Main) { snackbarsHolder.loginError() }
        }
      }

      authRequest.onFailure {
        withContext(Dispatchers.Main) { snackbarsHolder.loginError() }
      }
    }
  }

  LaunchedEffect(user) {
    if (user != null) {
      viewModel.filterStrategy(findStrategy(user.profile))
    }
  }

  if (utils.authStatus.isSuccess && user != null) {
    val orders = viewModel.ordersPagingFlow.collectAsLazyPagingItems()

    PullToRefreshLazyOrdersContent(
      orders = orders,
      user = user,
      onRefreshOrders = {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
          orders.refresh()
        }
      },
      ordersScope = viewModel.viewModelScope,
      navigator = navigator,
      utils = utils,
      statesHolder = details,
      snackbarsHolder = snackbarsHolder,
    )
  }
}

private fun findStrategy(userProfile: UserProfile): OrderFilterStrategy? {
  return when (userProfile) {
    Cutter -> {
      CutterOrderFilter()
    }

    Inspector -> {
      InspectorOrderFilter()
    }

    Manager -> {
      ManagerOrderFilter()
    }

    else -> {
      null
    }
  }
}