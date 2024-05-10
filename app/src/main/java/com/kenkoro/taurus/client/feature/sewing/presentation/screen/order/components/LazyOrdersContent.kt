package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.sewing.domain.model.NewOrder
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.UserProfile

@Composable
fun LazyOrdersContent(
  profile: UserProfile,
  orders: LazyPagingItems<Order>,
  onAddNewOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onDeleteOrderRemotely: suspend (orderId: Int, deleterSubject: String) -> Boolean,
  onDeleteOrderShowSnackbar: suspend () -> SnackbarResult,
  onAppendNewOrdersErrorShowSnackbar: suspend () -> SnackbarResult,
  networkStatus: NetworkStatus,
  modifier: Modifier = Modifier,
) {
  if (orders.loadState.append is LoadState.Error) {
    LaunchedEffect(Unit) { onAppendNewOrdersErrorShowSnackbar() }
  }

  if (allowedToSeeOrders(profile)) {
    LazyOrdersColumn(
      profile = profile,
      orders = orders,
      onAddNewOrderLocally = onAddNewOrderLocally,
      onDeleteOrderLocally = onDeleteOrderLocally,
      onDeleteOrderRemotely = onDeleteOrderRemotely,
      onDeleteOrderShowSnackbar = onDeleteOrderShowSnackbar,
      networkStatus = networkStatus,
    )
  }
}

fun allowedToSeeOrders(profile: UserProfile): Boolean {
  return profile != UserProfile.Tailor && profile != UserProfile.Other
}