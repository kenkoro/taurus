package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.showSnackbar
import kotlinx.coroutines.CoroutineScope

@Composable
fun OrderContent(
  orders: LazyPagingItems<Order>,
  snackbarHostState: SnackbarHostState,
  user: User?,
  networkStatus: NetworkStatus,
  isLoginFailed: Boolean,
  scope: CoroutineScope,
  onDeleteOrderRemotely: suspend (Int, String, String) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onUpsertOrderLocally: suspend (Order) -> Unit,
) {
  val contentHeight = LocalContentHeight.current

  if (orders.loadState.append is LoadState.Error) {
    showSnackbar(
      snackbarHostState = snackbarHostState,
      key = orders.loadState,
      message = stringResource(id = R.string.request_error),
    )
  }

  if (isLoginFailed) {
    showSnackbar(
      snackbarHostState = snackbarHostState,
      key = Unit,
      message = stringResource(id = R.string.request_error),
      delayInMillis = 1500L,
    )
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    item {
      Spacer(modifier = Modifier.height(contentHeight.large))
    }
    items(count = orders.itemCount) { index ->
      val order = orders[index]
      if (order != null) {
        OrderItem(
          order = order,
          user = user,
          scope = scope,
          snackbarHostState = snackbarHostState,
          networkStatus = networkStatus,
          isLoginFailed = isLoginFailed,
          onDeleteOrderRemotely = onDeleteOrderRemotely,
          onDeleteOrderLocally = onDeleteOrderLocally,
          onUpsertOrderLocally = onUpsertOrderLocally,
        )
      }
    }
    item {
      if (orders.loadState.append is LoadState.Loading) {
        CircularProgressIndicator(strokeWidth = 4.dp)
      }
    }
    item {
      Spacer(modifier = Modifier.height(contentHeight.large))
    }
  }
}