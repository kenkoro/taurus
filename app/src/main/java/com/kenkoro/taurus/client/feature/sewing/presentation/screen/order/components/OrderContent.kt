package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.paging.compose.itemKey
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.Status
import com.kenkoro.taurus.client.core.local.LocalArrangement
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.feature.sewing.data.util.UserProfile
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.showErrorSnackbar

@Composable
fun OrderContent(
  orders: LazyPagingItems<Order>,
  snackbarHostState: SnackbarHostState,
  user: User?,
  networkStatus: Status,
) {
  val arrangement = LocalArrangement.current
  val contentHeight = LocalContentHeight.current

  if (orders.loadState.append is LoadState.Error) {
    showErrorSnackbar(
      snackbarHostState = snackbarHostState,
      key = orders.loadState,
      message = stringResource(id = R.string.request_error),
    )
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(arrangement.standard),
  ) {
    item { Spacer(modifier = Modifier.height(contentHeight.medium)) }
    items(
      count = orders.itemCount,
      key = orders.itemKey { it.orderId },
    ) { index ->
      val order = orders[index]
      if (order != null) {
        OrderItem(order = order, profile = user?.profile ?: UserProfile.Others)
      }
    }
    item {
      if (orders.loadState.append is LoadState.Loading) {
        CircularProgressIndicator(strokeWidth = 3.dp)
      }
    }
    item {
      Spacer(modifier = Modifier.height(contentHeight.halfStandard + 10.dp))
    }
  }
}