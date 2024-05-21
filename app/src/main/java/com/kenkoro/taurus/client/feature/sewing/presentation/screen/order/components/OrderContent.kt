package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import android.util.Log
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.sewing.data.source.local.UserEntity
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toUser
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toUserEntity
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.UserDto
import com.kenkoro.taurus.client.feature.sewing.domain.model.NewOrder
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.UserProfile
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.util.LoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun OrderContent(
  user: User?,
  onUser: (User) -> Unit,
  ordersFlow: Flow<PagingData<Order>>,
  loginState: LoginState,
  onLoginResult: (LoginState) -> Unit,
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
  onEncryptToken: (String) -> Unit,
  onDecryptSubjectAndPassword: () -> Pair<String, String>,
  onDecryptToken: () -> String,
  networkStatus: NetworkStatus,
  modifier: Modifier = Modifier,
) {
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
            onLoginResult(LoginState.Success)
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
    val orders = ordersFlow.collectAsLazyPagingItems()
    LazyOrdersContent(
      profile = user?.profile ?: UserProfile.Other,
      orders = orders,
      onAddNewOrderLocally = onAddNewOrderLocally,
      onDeleteOrderLocally = onDeleteOrderLocally,
      onEditOrderLocally = onEditOrderLocally,
      onEditOrderRemotely = onEditOrderRemotely,
      onDeleteOrderRemotely = onDeleteOrderRemotely,
      onDeleteOrderShowSnackbar = onDeleteOrderShowSnackbar,
      onAppendNewOrdersErrorShowSnackbar = onAppendNewOrdersErrorShowSnackbar,
      networkStatus = networkStatus,
    )
  }
}