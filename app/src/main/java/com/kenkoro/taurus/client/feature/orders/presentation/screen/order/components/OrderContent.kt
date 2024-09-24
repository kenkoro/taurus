package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.OrderDetails
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenShared
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenSnackbarsHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenUtils
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.CutterOrderFilter
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.InspectorOrderFilter
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.ManagerOrderFilter
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.OrderFilterStrategy
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Cutter
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Inspector
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Manager
import com.kenkoro.taurus.client.feature.shared.states.AuthStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun OrderContent(
  modifier: Modifier = Modifier,
  user: User?,
  navigator: OrderScreenNavigator,
  shared: OrderScreenShared,
  details: OrderDetails,
  snackbarsHolder: OrderScreenSnackbarsHolder,
  utils: OrderScreenUtils,
) {
  val network = shared.network

  LaunchedEffect(network, Dispatchers.Main) {
    if (network != NetworkStatus.Available) {
      snackbarsHolder.internetConnectionError()
    }
  }

  if (shared.authStatus == AuthStatus.WaitingForAuth) {
    LaunchedEffect(Unit, Dispatchers.IO) {
      val (subject, password) = utils.decryptUserCredentials()
      val authRequest = utils.auth(subject, password)

      authRequest.onSuccess { token ->
        utils.encryptJWToken(token)
        val isFailure = utils.getUser(subject, token) { shared.proceedAuth(AuthStatus.Success) }
        if (isFailure) {
          withContext(Dispatchers.Main) {
            snackbarsHolder.loginError()
            shared.proceedAuth(AuthStatus.Failure)
          }
        }
      }

      authRequest.onFailure {
        withContext(Dispatchers.Main) {
          shared.proceedAuth(AuthStatus.Failure)
          snackbarsHolder.loginError()
        }
      }
    }
  }

  LaunchedEffect(user) {
    if (user != null) {
      utils.filter(findStrategy(user.profile))
    }
  }

  if (shared.authStatus.isSuccess && user != null) {
    val orders = utils.ordersPagingFlow.collectAsLazyPagingItems()

    PullToRefreshLazyOrdersContent(
      orders = orders,
      user = user,
      navigator = navigator,
      shared = shared,
      statesHolder = details,
      snackbarsHolder = snackbarsHolder,
      utils = utils,
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