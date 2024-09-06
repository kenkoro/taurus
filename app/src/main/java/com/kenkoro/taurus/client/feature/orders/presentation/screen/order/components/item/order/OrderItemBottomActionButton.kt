package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.item.order

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.feature.orders.data.mappers.toCheckedOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus.Cut
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus.Idle
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.ActualCutOrdersQuantityDialog
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenLocalHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenRemoteHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenSnackbarsHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenUtils
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Customer
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Cutter
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Inspector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OrderItemBottomActionButton(
  modifier: Modifier = Modifier,
  order: Order,
  localHandler: OrderScreenLocalHandler,
  remoteHandler: OrderScreenRemoteHandler,
  utils: OrderScreenUtils,
  snackbarsHolder: OrderScreenSnackbarsHolder,
  onHide: () -> Unit = {},
  onRefresh: () -> Unit = {},
) {
  val scope = utils.viewModelScope
  val user = utils.user

  val onHideWithDelay =
    suspend {
      onHide()
      delay(400L)
    }

  var showCutterDialog by remember {
    mutableStateOf(false)
  }
  val openCutterDialog = { showCutterDialog = true }

  if (showCutterDialog) {
    ActualCutOrdersQuantityDialog(
      order = order,
      localHandler = localHandler,
      remoteHandler = remoteHandler,
      utils = utils,
      snackbarsHolder = snackbarsHolder,
      closeCutterDialog = { showCutterDialog = false },
      onHideWithDelay = onHideWithDelay,
      onRefresh = onRefresh,
    )
  }

  when (user?.profile) {
    Customer -> {
      OrderItemButton(
        modifier = modifier,
        order = order,
        text = stringResource(id = R.string.delete_button),
        networkStatus = utils.network,
        onClick = {
          scope.launch {
            launch(Dispatchers.IO) {
              onHideWithDelay()

              localHandler.deleteOrder(order)
              val wasAcknowledged =
                remoteHandler.deleteOrder(order.orderId, user.subject)

              if (wasAcknowledged) {
                onRefresh()
                withContext(Dispatchers.Main) { snackbarsHolder.orderWasDeleted(order.orderId) }
              } else {
                withContext(Dispatchers.Main) { snackbarsHolder.apiError() }
              }
            }
          }
        },
      )
    }

    Cutter -> {
      if (order.status == Idle) {
        OrderItemButton(
          modifier = modifier,
          order = order,
          text = stringResource(id = R.string.order_was_cut),
          networkStatus = utils.network,
          onClick = openCutterDialog,
        )
      }
    }

    Inspector -> {
      if (order.status == Cut) {
        OrderItemButton(
          modifier = modifier,
          order = order,
          text = stringResource(id = R.string.order_was_checked),
          networkStatus = utils.network,
          onClick = {
            scope.launch(Dispatchers.IO) {
              onHideWithDelay()

              val checkedOrder = order.toCheckedOrder()
              localHandler.editOrder(checkedOrder, order.orderId)
              val wasAcknowledged =
                remoteHandler.editOrder(checkedOrder, user.subject)
              if (wasAcknowledged) {
                onRefresh()
              } else {
                withContext(Dispatchers.Main) { snackbarsHolder.apiError() }
              }
            }
          },
        )
      }
    }

    else -> {}
  }
}