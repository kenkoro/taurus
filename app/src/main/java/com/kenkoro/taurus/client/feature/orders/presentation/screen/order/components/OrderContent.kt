package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotInterested
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.feature.login.data.local.UserEntity
import com.kenkoro.taurus.client.feature.login.data.mappers.toUser
import com.kenkoro.taurus.client.feature.login.data.mappers.toUserEntity
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.orders.domain.NewOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.LoginState
import com.kenkoro.taurus.client.feature.profile.data.remote.dto.UserDto
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Cutter
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Inspector
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Other
import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun OrderContent(
  modifier: Modifier = Modifier,
  networkStatus: NetworkStatus,
  user: User?,
  onUser: (User) -> Unit,
  ordersFlow: Flow<PagingData<Order>>,
  loginState: LoginState,
  lazyOrdersState: LazyListState,
  selectedOrderRecordId: Int? = null,
  onSelectOrder: (Int?) -> Unit = {},
  onLoginState: (LoginState) -> Unit,
  onAddNewUserLocally: suspend (UserEntity) -> Unit,
  onAddNewOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onEditOrderLocally: suspend (NewOrder) -> Unit,
  onLogin: suspend (storedSubject: String, storedPassword: String) -> Result<TokenDto>,
  onGetUserRemotely: suspend (subject: String, token: String) -> Result<UserDto>,
  onDeleteOrderRemotely: suspend (orderId: Int, deleterSubject: String) -> Boolean,
  onEditOrderRemotely: suspend (NewOrderDto, Int, String, String) -> Boolean,
  onInternetConnectionErrorShowSnackbar: suspend () -> SnackbarResult,
  onLoginErrorShowSnackbar: suspend () -> SnackbarResult,
  onDeleteOrderShowSnackbar: suspend () -> SnackbarResult,
  onAppendNewOrdersErrorShowSnackbar: suspend () -> SnackbarResult,
  onOrderAccessErrorShowSnackbar: suspend () -> SnackbarResult,
  onEncryptToken: (String) -> Unit,
  onDecryptSubjectAndPassword: () -> Pair<String, String>,
  onDecryptToken: () -> String,
) {
  val contentHeight = LocalContentHeight.current

  if (networkStatus != NetworkStatus.Available) {
    LaunchedEffect(networkStatus) { onInternetConnectionErrorShowSnackbar() }
  }

  if (loginState == LoginState.NotLoggedYet) {
    LaunchedEffect(Unit) {
      withContext(Dispatchers.IO) {
        val (subject, password) = onDecryptSubjectAndPassword()
        val result = onLogin(subject, password)
        result.onSuccess { dto ->
          onEncryptToken(dto.token)
        }

        if (result.isSuccess) {
          val token = onDecryptToken()
          val getUserResult = onGetUserRemotely(subject, token)
          getUserResult.onSuccess { userDto ->
            onAddNewUserLocally(userDto.toUserEntity())
            onUser(userDto.toUser())
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

  if (loginState == LoginState.Success) {
    val userProfile = user?.profile ?: Other
    val orders = ordersFlow.collectAsLazyPagingItems()

    // WARN: For now this is hard-coded
    if (isNotImplementedYetFunctionalityFor(userProfile)) {
      Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
      ) {
        Icon(
          imageVector = Icons.Default.NotInterested,
          contentDescription = "NotImplementedOrdersFilter",
          modifier = Modifier.size(30.dp),
        )
        Spacer(modifier = Modifier.height(contentHeight.medium))
        Text(text = "Пока ведутся работы...")
      }
    } else {
      LazyOrdersContent(
        networkStatus = networkStatus,
        userProfile = userProfile,
        orders = orders,
        lazyOrdersState = lazyOrdersState,
        selectedOrderRecordId = selectedOrderRecordId,
        onSelectOrder = onSelectOrder,
        onAddNewOrderLocally = onAddNewOrderLocally,
        onDeleteOrderLocally = onDeleteOrderLocally,
        onEditOrderLocally = onEditOrderLocally,
        onEditOrderRemotely = onEditOrderRemotely,
        onDeleteOrderRemotely = onDeleteOrderRemotely,
        onDeleteOrderShowSnackbar = onDeleteOrderShowSnackbar,
        onAppendNewOrdersErrorShowSnackbar = onAppendNewOrdersErrorShowSnackbar,
        onOrderAccessErrorShowSnackbar = onOrderAccessErrorShowSnackbar,
      )
    }
  }
}

private fun isNotImplementedYetFunctionalityFor(userProfile: UserProfile): Boolean {
  return userProfile == Cutter || userProfile == Inspector
}