package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalOffset
import com.kenkoro.taurus.client.core.local.LocalSize
import com.kenkoro.taurus.client.core.local.LocalStrokeWidth
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.OrderDetails
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.item.order.OrderItem
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenShared
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenSnackbarsHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenUtils
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Other
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Tailor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshLazyOrdersContent(
  modifier: Modifier = Modifier,
  orders: LazyPagingItems<Order>,
  user: User,
  navigator: OrderScreenNavigator,
  shared: OrderScreenShared,
  statesHolder: OrderDetails,
  snackbarsHolder: OrderScreenSnackbarsHolder,
  utils: OrderScreenUtils,
) {
  val size = LocalSize.current
  val offset = LocalOffset.current
  val strokeWidth = LocalStrokeWidth.current
  val contentHeight = LocalContentHeight.current

  val pullToRefreshState = rememberPullToRefreshState()
  val lazyListState = rememberLazyListState()

  if (orders.loadState.append is LoadState.Error) {
    LaunchedEffect(Unit, Dispatchers.Main) { snackbarsHolder.getPaginatedOrdersError() }
  }

  if (user.profile == Tailor || user.profile == Other) {
    LaunchedEffect(Unit, Dispatchers.Main) { snackbarsHolder.accessToOrdersError() }
  }

  if (allowedToSeeOrders(user.profile)) {
    Box(modifier = modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {
      LazyColumn(
        state = lazyListState,
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
              user = user,
              onRefresh = orders::refresh,
              navigator = navigator,
              shared = shared,
              details = statesHolder,
              snackbarsHolder = snackbarsHolder,
              utils = utils,
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
              strokeWidth = strokeWidth.small,
            )
          }
        }
        item {
          Spacer(modifier = Modifier.height(contentHeight.bottomBar))
        }
      }

      if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(Unit) {
          utils.scope.launch(Dispatchers.IO) {
            orders.refresh()
          }
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

fun allowedToSeeOrders(profile: UserProfile?): Boolean {
  return profile != null && profile != Tailor && profile != Other
}