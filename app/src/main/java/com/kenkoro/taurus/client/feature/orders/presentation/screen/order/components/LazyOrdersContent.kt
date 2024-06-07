package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalSize
import com.kenkoro.taurus.client.core.local.LocalStrokeWidth
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.orders.domain.NewOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.item.order.OrderItem
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Other
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Tailor
import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import kotlinx.coroutines.Dispatchers
import java.util.UUID

@Composable
fun LazyOrdersContent(
  modifier: Modifier = Modifier,
  user: User? = null,
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
  onAppendNewOrdersErrorShowSnackbar: suspend () -> SnackbarResult,
  onOrderAccessErrorShowSnackbar: suspend () -> SnackbarResult,
  onApiErrorShowSnackbar: suspend () -> SnackbarResult,
  onDecryptToken: () -> String,
) {
  val size = LocalSize.current
  val strokeWidth = LocalStrokeWidth.current
  val contentHeight = LocalContentHeight.current

  if (orders.loadState.append is LoadState.Error) {
    LaunchedEffect(Unit) { onAppendNewOrdersErrorShowSnackbar() }
  }

  if (userProfile == Tailor || userProfile == Other) {
    LaunchedEffect(Unit, Dispatchers.Main) { onOrderAccessErrorShowSnackbar() }
  }

  if (allowedToSeeOrders(userProfile)) {
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
            profile = userProfile,
            order = order,
            networkStatus = networkStatus,
            user = user,
            selectedOrderRecordId = selectedOrderRecordId,
            onSelectOrder = onSelectOrder,
            onAddNewOrderLocally = onAddNewOrderLocally,
            onDeleteOrderLocally = onDeleteOrderLocally,
            onEditOrderLocally = onEditOrderLocally,
            onEditOrderRemotely = onEditOrderRemotely,
            onDeleteOrderRemotely = onDeleteOrderRemotely,
            onDecryptToken = onDecryptToken,
            onApiErrorShowSnackbar = onApiErrorShowSnackbar,
          )
        }
      }
      item {
        Spacer(modifier = Modifier.height(contentHeight.large))
      }
      item {
        if (orders.loadState.append is LoadState.Loading) {
          CircularProgressIndicator(
            modifier = Modifier.size(size.medium),
            strokeWidth = strokeWidth.standard,
          )
        }
      }
      item {
        Spacer(modifier = Modifier.height(contentHeight.bottomBar))
      }
    }
  }
}

fun allowedToSeeOrders(profile: UserProfile): Boolean {
  return profile != Tailor && profile != Other
}