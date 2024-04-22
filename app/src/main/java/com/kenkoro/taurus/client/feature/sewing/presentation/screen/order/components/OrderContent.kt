package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalStrokeWidth
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun OrderContent(
  orders: LazyPagingItems<Order>,
  user: User?,
  loginFailed: Boolean,
  networkStatus: NetworkStatus,
  snackbarHostState: SnackbarHostState,
  errorSnackbarHostState: SnackbarHostState,
  onDeleteOrderRemotely: suspend (Int, String, String) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onUpsertOrderLocally: suspend (Order) -> Unit,
) {
  val contentHeight = LocalContentHeight.current
  val strokeWidth = LocalStrokeWidth.current
  val scope = rememberCoroutineScope()

  val errorRequestMessage = stringResource(id = R.string.request_error)
  val okActionLabel = stringResource(id = R.string.ok)

  if (orders.loadState.append is LoadState.Error || loginFailed) {
    LaunchedEffect(Unit) {
      launch {
        errorSnackbarHostState.showSnackbar(
          message = errorRequestMessage,
          actionLabel = okActionLabel,
        )
      }
    }
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    item {
      Spacer(modifier = Modifier.height(contentHeight.large))
    }
    items(
      count = orders.itemCount,
      key = { index -> orders[index]?.orderId ?: UUID.randomUUID().toString() },
    ) { index ->
      val order = orders[index]
      if (order != null) {
        OrderItem(
          order = order,
          user = user,
          isLoginFailed = loginFailed,
          onDeleteOrderRemotely = onDeleteOrderRemotely,
          onDeleteOrderLocally = onDeleteOrderLocally,
          onUpsertOrderLocally = onUpsertOrderLocally,
          networkStatus = networkStatus,
          snackbarHostState = snackbarHostState,
          scope = scope,
        )
      }
    }
    item {
      if (orders.loadState.append is LoadState.Loading) {
        CircularProgressIndicator(strokeWidth = strokeWidth.standard)
      }
    }
    item {
      Spacer(modifier = Modifier.height(contentHeight.large))
    }
  }
}