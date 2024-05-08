package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.sewing.domain.model.NewOrder
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.presentation.viewmodels.LoginResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun OrderContent(
  orders: LazyPagingItems<Order>,
  loginResult: LoginResult,
  onLoginResult: (LoginResult) -> Unit,
  onLogin: suspend (storedSubject: String, storedPassword: String) -> Result<TokenDto>,
  onEncryptToken: (String) -> Unit,
  onAddNewOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onDeleteOrderRemotely: suspend (orderId: Int, deleterSubject: String) -> Boolean,
  onInternetConnectionErrorShowSnackbar: suspend () -> SnackbarResult,
  onLoginErrorShowSnackbar: suspend () -> SnackbarResult,
  onDeleteOrderShowSnackbar: suspend () -> SnackbarResult,
  onAppendNewOrdersErrorShowSnackbar: suspend () -> SnackbarResult,
  onDecryptSubjectAndPassword: () -> Pair<String, String>,
  networkStatus: NetworkStatus,
  modifier: Modifier = Modifier,
) {
  if (networkStatus != NetworkStatus.Available) {
    LaunchedEffect(networkStatus) { onInternetConnectionErrorShowSnackbar() }
  }

  if (loginResult == LoginResult.NotLoggedYet) {
    LaunchedEffect(Unit) {
      withContext(Dispatchers.IO) {
        val (subject, password) = onDecryptSubjectAndPassword()
        val result = onLogin(subject, password)
        result.onSuccess { dto ->
          onLoginResult(LoginResult.Success)
          onEncryptToken(dto.token)
        }

        result.onFailure {
          withContext(Dispatchers.Main) { onLoginErrorShowSnackbar() }
        }
      }
    }
  }

  if (loginResult == LoginResult.Success) {
    LazyOrdersContent(
      orders = orders,
      onAddNewOrderLocally = onAddNewOrderLocally,
      onDeleteOrderLocally = onDeleteOrderLocally,
      onDeleteOrderRemotely = onDeleteOrderRemotely,
      onDeleteOrderShowSnackbar = onDeleteOrderShowSnackbar,
      onAppendNewOrdersErrorShowSnackbar = onAppendNewOrdersErrorShowSnackbar,
      networkStatus = networkStatus,
    )
  }
}