package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalStrokeWidth
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.sewing.domain.model.NewOrder
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.UserProfile
import java.util.UUID

@Composable
fun LazyOrdersColumn(
  modifier: Modifier = Modifier,
  networkStatus: NetworkStatus,
  profile: UserProfile,
  orders: LazyPagingItems<Order>,
  lazyOrdersState: LazyListState,
  onAddNewOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onEditOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderRemotely: suspend (orderId: Int, deleterSubject: String) -> Boolean,
  onEditOrderRemotely: suspend (NewOrderDto, Int, String, String) -> Boolean,
  onDeleteOrderShowSnackbar: suspend () -> SnackbarResult,
) {
  val strokeWidth = LocalStrokeWidth.current
  val contentHeight = LocalContentHeight.current

  LazyColumn(
    state = lazyOrdersState,
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
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
          profile = profile,
          order = order,
          onAddNewOrderLocally = onAddNewOrderLocally,
          onDeleteOrderLocally = onDeleteOrderLocally,
          onEditOrderLocally = onEditOrderLocally,
          onEditOrderRemotely = onEditOrderRemotely,
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
      Spacer(modifier = Modifier.height(contentHeight.large))
    }
  }
}