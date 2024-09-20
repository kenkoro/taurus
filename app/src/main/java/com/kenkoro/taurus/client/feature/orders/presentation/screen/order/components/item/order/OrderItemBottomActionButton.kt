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
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenShared
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenSnackbarsHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenUtils
import com.kenkoro.taurus.client.feature.profile.domain.User
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
  user: User,
  onHide: () -> Unit = {},
  onRefresh: () -> Unit = {},
  shared: OrderScreenShared,
  snackbarsHolder: OrderScreenSnackbarsHolder,
  utils: OrderScreenUtils,
) {
  val ordersScope = utils.scope
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
      user = user,
      closeCutterDialog = { showCutterDialog = false },
      onHideWithDelay = onHideWithDelay,
      onRefresh = onRefresh,
      shared = shared,
      snackbarsHolder = snackbarsHolder,
      utils = utils,
    )
  }

  when (user.profile) {
    Customer -> {
      OrderItemButton(
        modifier = modifier,
        order = order,
        text = stringResource(id = R.string.delete_button),
        networkStatus = shared.network,
        onClick = {
          ordersScope.launch(Dispatchers.IO) {
            onHideWithDelay()

            val isFailure =
              utils.deleteOrder(order, user.subject) {
                onRefresh()
                ordersScope.launch(Dispatchers.Main) {
                  snackbarsHolder.orderWasDeleted(order.orderId)
                }
              }
            if (isFailure) {
              withContext(Dispatchers.Main) { snackbarsHolder.apiError() }
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
          networkStatus = shared.network,
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
          networkStatus = shared.network,
          onClick = {
            ordersScope.launch(Dispatchers.IO) {
              onHideWithDelay()

              val checkedOrder = order.toCheckedOrder()
              val isFailure =
                utils.editOrder(checkedOrder, user.subject) {
                  onRefresh()
                }
              if (isFailure) {
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