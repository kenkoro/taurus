package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.login.data.mappers.toUser
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.states.LoginState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenLocalHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenRemoteHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenSnackbarsHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenUtils
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.CutterOrderFilter
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.InspectorOrderFilter
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.ManagerOrderFilter
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.OrderFilterStrategy
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Cutter
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Inspector
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Manager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OrderContent(
  modifier: Modifier = Modifier,
  localHandler: OrderScreenLocalHandler,
  remoteHandler: OrderScreenRemoteHandler,
  navigator: OrderScreenNavigator,
  utils: OrderScreenUtils,
  statesHolder: OrderStatesHolder,
  snackbarsHolder: OrderScreenSnackbarsHolder,
) {
  val user = utils.user
  val network = utils.network

  LaunchedEffect(network, Dispatchers.Main) {
    if (network != NetworkStatus.Available) {
      snackbarsHolder.internetConnectionError()
    }
  }

  if (utils.loginState == LoginState.NotLoggedYet) {
    LaunchedEffect(Unit, Dispatchers.IO) {
      val (subject, password) = utils.decryptUserSubjectAndItsPassword()
      val loginRequest = remoteHandler.login(subject, password)

      loginRequest.onSuccess { result ->
        utils.encryptJWToken(result.token)
        val getUserRequest = remoteHandler.getUser(subject, result.token)
        getUserRequest.onSuccess { userDto ->
          val requestedUser = userDto.toUser()
          localHandler.addNewUser(requestedUser)
          utils.saveUser(requestedUser)
          utils.newLoginState(LoginState.Success)
        }
        getUserRequest.onFailure {
          Log.d("kenkoro", it.message!!)
          withContext(Dispatchers.Main) { snackbarsHolder.loginError() }
        }
      }

      loginRequest.onFailure {
        withContext(Dispatchers.Main) { snackbarsHolder.loginError() }
      }
    }
  }

  LaunchedEffect(user) {
    if (user != null) {
      utils.newOrdersFilter(findStrategy(user.profile))
    }
  }

  if (utils.loginState.isSuccess()) {
    val orders = utils.ordersPagingFlow.collectAsLazyPagingItems()

    PullToRefreshLazyOrdersContent(
      orders = orders,
      localHandler = localHandler,
      remoteHandler = remoteHandler,
      navigator = navigator,
      utils = utils,
      statesHolder = statesHolder,
      snackbarsHolder = snackbarsHolder,
      onRefreshOrders = {
        utils.viewModelScope.launch(Dispatchers.IO) {
          orders.refresh()
        }
      },
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