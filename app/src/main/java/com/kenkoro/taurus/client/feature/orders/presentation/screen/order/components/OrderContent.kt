package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.login.data.mappers.toUser
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers.LocalHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers.RemoteHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.states.LoginState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.CutterOrderFilter
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.InspectorOrderFilter
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.ManagerOrderFilter
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderFilterStrategy
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.SnackbarsHolder
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Cutter
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Inspector
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Manager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun OrderContent(
  ordersPagingFlow: Flow<PagingData<Order>>,
  user: User?,
  networkStatus: NetworkStatus,
  loginState: LoginState,
  selectedOrderRecordId: Int? = null,
  lazyOrdersState: LazyListState,
  orderStatesHolder: OrderStatesHolder = OrderStatesHolder(),
  onFilterStrategy: (OrderFilterStrategy?) -> Unit = {},
  onUser: (User) -> Unit,
  onSelectOrder: (Int?) -> Unit = {},
  onLoginState: (LoginState) -> Unit,
  localHandler: LocalHandler = LocalHandler(),
  remoteHandler: RemoteHandler,
  snackbarsHolder: SnackbarsHolder,
  onEncryptToken: (String) -> Unit,
  onDecryptToken: () -> String,
  onDecryptSubjectAndPassword: () -> Pair<String, String>,
  onOrderStatus: (OrderStatus) -> Unit = {},
  onNavigateToOrderEditorScreen: (editOrder: Boolean) -> Unit = {},
) {
  val loginErrorMessage = stringResource(id = R.string.login_fail)
  val internetConnectionErrorMessage = stringResource(id = R.string.check_internet_connection)

  val okActionLabel = stringResource(id = R.string.ok)

  val onLoginErrorShowSnackbar =
    suspend {
      snackbarsHolder.errorSnackbarHostState.showSnackbar(
        message = loginErrorMessage,
        actionLabel = okActionLabel,
      )
    }

  if (networkStatus != NetworkStatus.Available) {
    LaunchedEffect(networkStatus) {
      snackbarsHolder.internetErrorSnackbarHostState.showSnackbar(
        message = internetConnectionErrorMessage,
        duration = SnackbarDuration.Indefinite,
      )
    }
  }

  if (loginState == LoginState.NotLoggedYet) {
    LaunchedEffect(Unit) {
      withContext(Dispatchers.IO) {
        val (subject, password) = onDecryptSubjectAndPassword()
        val result = remoteHandler.login(subject, password)
        result.onSuccess { dto ->
          onEncryptToken(dto.token)
        }

        if (result.isSuccess) {
          val token = onDecryptToken()
          val getUserResult = remoteHandler.getUser(subject, token)
          getUserResult.onSuccess { userDto ->
            val userModel = userDto.toUser()
            localHandler.addNewUser(userModel)
            onUser(userModel)
            onLoginState(LoginState.Success)
          }
          getUserResult.onFailure {
            Log.d("kenkoro", it.message!!)
            withContext(Dispatchers.Main) { onLoginErrorShowSnackbar() }
          }
        }

        result.onFailure {
          withContext(Dispatchers.Main) { onLoginErrorShowSnackbar() }
        }
      }
    }
  }

  LaunchedEffect(user) {
    if (user != null) {
      onFilterStrategy(findStrategy(user.profile))
    }
  }

  if (loginState.isSuccess() && user != null) {
    val orders = ordersPagingFlow.collectAsLazyPagingItems()

    LazyOrdersContent(
      orders = orders,
      user = user,
      networkStatus = networkStatus,
      loginState = loginState,
      selectedOrderRecordId = selectedOrderRecordId,
      lazyOrdersState = lazyOrdersState,
      orderStatesHolder = orderStatesHolder,
      onUser = onUser,
      onFilterStrategy = onFilterStrategy,
      onSelectOrder = onSelectOrder,
      onLoginState = onLoginState,
      localHandler = localHandler,
      remoteHandler = remoteHandler,
      snackbarsHolder = snackbarsHolder,
      onDecryptToken = onDecryptToken,
      onOrderStatus = onOrderStatus,
      onNavigateToOrderEditorScreen = onNavigateToOrderEditorScreen,
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