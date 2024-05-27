package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.sewing.domain.model.NewOrder
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.UserProfile
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.UserProfile.Other
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.UserProfile.Tailor
import kotlinx.coroutines.Dispatchers

@Composable
fun LazyOrdersContent(
  modifier: Modifier = Modifier,
  networkStatus: NetworkStatus,
  userProfile: UserProfile,
  orders: LazyPagingItems<Order>,
  lazyOrdersState: LazyListState,
  selectedOrderRecordId: Int? = null,
  onSelectOrder: (Int?) -> Unit = {},
  onAddNewOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onEditOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderRemotely: suspend (orderId: Int, deleterSubject: String) -> Boolean,
  onEditOrderRemotely: suspend (NewOrderDto, Int, String, String) -> Boolean,
  onDeleteOrderShowSnackbar: suspend () -> SnackbarResult,
  onAppendNewOrdersErrorShowSnackbar: suspend () -> SnackbarResult,
  onOrderAccessErrorShowSnackbar: suspend () -> SnackbarResult,
) {
  if (orders.loadState.append is LoadState.Error) {
    LaunchedEffect(Unit) { onAppendNewOrdersErrorShowSnackbar() }
  }

  if (userProfile == Tailor || userProfile == Other) {
    LaunchedEffect(Unit, Dispatchers.Main) { onOrderAccessErrorShowSnackbar() }
  }

  if (allowedToSeeOrders(userProfile)) {
    LazyOrdersColumn(
      networkStatus = networkStatus,
      userProfile = userProfile,
      orders = orders,
      lazyOrdersState = lazyOrdersState,
      selectedOrderRecordId = selectedOrderRecordId,
      onSelectOrder = onSelectOrder,
      onAddNewOrderLocally = onAddNewOrderLocally,
      onDeleteOrderLocally = onDeleteOrderLocally,
      onEditOrderLocally = onEditOrderLocally,
      onDeleteOrderRemotely = onDeleteOrderRemotely,
      onEditOrderRemotely = onEditOrderRemotely,
      onDeleteOrderShowSnackbar = onDeleteOrderShowSnackbar,
    )
  }
}

fun allowedToSeeOrders(profile: UserProfile): Boolean {
  return profile != Tailor && profile != Other
}