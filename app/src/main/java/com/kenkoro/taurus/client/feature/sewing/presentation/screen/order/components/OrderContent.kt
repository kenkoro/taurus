package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.Status
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
  networkStatus: Status,
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
          networkStatus = networkStatus,
          isLoginFailed = isLoginFailed,
          onDeleteOrderRemotely = onDeleteOrderRemotely,
          onDeleteOrderLocally = onDeleteOrderLocally,
          scope = scope,
          onUpsertOrderLocally = onUpsertOrderLocally,
          snackbarHostState = snackbarHostState,
        )
      }
    }
    item {
      if (orders.loadState.append is LoadState.Loading) {
        CircularProgressIndicator(strokeWidth = 4.dp)
      }
    }
    item {
      if (orders.itemCount == 0 && orders.loadState.append is LoadState.NotLoading) {
        Text(
          text = stringResource(id = R.string.no_orders),
          fontWeight = FontWeight.Light,
        )
      }
    }
    item {
      Spacer(modifier = Modifier.height(contentHeight.large))
    }
  }
}