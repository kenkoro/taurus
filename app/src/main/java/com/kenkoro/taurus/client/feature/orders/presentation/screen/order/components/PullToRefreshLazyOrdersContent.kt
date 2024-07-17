@file:OptIn(ExperimentalMaterial3Api::class)

package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalOffset
import com.kenkoro.taurus.client.core.local.LocalSize
import com.kenkoro.taurus.client.core.local.LocalStrokeWidth
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.item.order.OrderItem
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers.LocalHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers.RemoteHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.SnackbarsHolder
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Other
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Tailor
import kotlinx.coroutines.Dispatchers
import java.util.UUID

@Composable
fun PullToRefreshLazyOrdersContent(
  modifier: Modifier = Modifier,
  orders: LazyPagingItems<Order>,
  user: User,
  networkStatus: NetworkStatus,
  selectedOrderRecordId: Int? = null,
  onRefreshOrders: () -> Unit,
  lazyOrdersState: LazyListState,
  orderStatesHolder: OrderStatesHolder = OrderStatesHolder(),
  onSelectOrder: (Int?) -> Unit = {},
  localHandler: LocalHandler,
  remoteHandler: RemoteHandler,
  snackbarsHolder: SnackbarsHolder,
  onDecryptToken: () -> String,
  onOrderStatus: (OrderStatus) -> Unit = {},
  onOrderId: (Int) -> Unit = {},
  onNavigateToOrderEditorScreen: (editOrder: Boolean) -> Unit = {},
) {
  val paginatedOrdersErrorMessage = stringResource(id = R.string.paginated_orders_error)
  val orderAccessErrorMessage = stringResource(id = R.string.orders_access_error)

  val okActionLabel = stringResource(id = R.string.ok)

  val size = LocalSize.current
  val offset = LocalOffset.current
  val strokeWidth = LocalStrokeWidth.current
  val contentHeight = LocalContentHeight.current

  val pullToRefreshState = rememberPullToRefreshState()

  if (orders.loadState.append is LoadState.Error) {
    LaunchedEffect(Unit) {
      snackbarsHolder.errorSnackbarHostState.showSnackbar(
        message = paginatedOrdersErrorMessage,
        actionLabel = okActionLabel,
      )
    }
  }

  if (user.profile == Tailor || user.profile == Other) {
    LaunchedEffect(Unit, Dispatchers.Main) {
      snackbarsHolder.errorSnackbarHostState.showSnackbar(
        message = orderAccessErrorMessage,
        actionLabel = okActionLabel,
      )
    }
  }

  if (allowedToSeeOrders(user.profile)) {
    Box(modifier = modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {
      LazyColumn(
        state = lazyOrdersState,
        modifier = Modifier.fillMaxSize(),
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
              order = order,
              networkStatus = networkStatus,
              user = user,
              selectedOrderRecordId = selectedOrderRecordId,
              orderStatesHolder = orderStatesHolder,
              onSelectOrder = onSelectOrder,
              localHandler = localHandler,
              remoteHandler = remoteHandler,
              snackbarsHolder = snackbarsHolder,
              onDecryptToken = onDecryptToken,
              onRefresh = orders::refresh,
              onOrderStatus = onOrderStatus,
              onOrderId = onOrderId,
              onNavigateToOrderEditorScreen = onNavigateToOrderEditorScreen,
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

      if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(Unit) {
          onRefreshOrders()
        }
      }

      LaunchedEffect(orders.loadState.refresh) {
        if (orders.loadState.refresh is LoadState.Loading) {
          pullToRefreshState.startRefresh()
        } else {
          pullToRefreshState.endRefresh()
        }
      }

      PullToRefreshContainer(
        modifier =
          Modifier
            .align(Alignment.TopCenter)
            .offset(y = offset.pullToRefreshIndicator),
        state = pullToRefreshState,
        contentColor = MaterialTheme.colorScheme.primary,
      )
    }
  }
}

fun allowedToSeeOrders(profile: UserProfile): Boolean {
  return profile != Tailor && profile != Other
}