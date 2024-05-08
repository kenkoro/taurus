package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalStrokeWidth
import com.kenkoro.taurus.client.feature.sewing.domain.model.NewOrder
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import java.util.UUID

@Composable
fun LazyOrdersContent(
  orders: LazyPagingItems<Order>,
  onAddNewOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onDeleteOrderRemotely: suspend (orderId: Int, deleterSubject: String) -> Boolean,
  onDeleteOrderShowSnackbar: suspend () -> SnackbarResult,
  onAppendNewOrdersErrorShowSnackbar: suspend () -> SnackbarResult,
  networkStatus: NetworkStatus,
  modifier: Modifier = Modifier,
) {
  val strokeWidth = LocalStrokeWidth.current
  val contentHeight = LocalContentHeight.current

  if (orders.loadState.append is LoadState.Error) {
    LaunchedEffect(Unit) { onAppendNewOrdersErrorShowSnackbar() }
  }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    item {
      Spacer(modifier = Modifier.height(contentHeight.large))
    }
    items(
      count = orders.itemCount,
      key = { index -> orders[index]?.recordId ?: UUID.randomUUID().toString() },
    ) { index ->
      val order = orders[index]
      if (order != null) {
        OrderItem(
          order = order,
          onAddNewOrderLocally = onAddNewOrderLocally,
          onDeleteOrderLocally = onDeleteOrderLocally,
          onDeleteOrderRemotely = onDeleteOrderRemotely,
          onDeleteOrderShowSnackbar = onDeleteOrderShowSnackbar,
          networkStatus = networkStatus,
        )
      }
    }
    item {
      if (orders.loadState.append is LoadState.Loading) {
        CircularProgressIndicator(strokeWidth = strokeWidth.standard)
      }
    }
    item {
      if (orders.itemCount == 0) {
        Text(text = stringResource(id = R.string.no_orders))
      }
    }
    item {
      Spacer(modifier = Modifier.height(contentHeight.large))
    }
  }
}