package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.item.order

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.orders.data.mappers.toCheckedOrder
import com.kenkoro.taurus.client.feature.orders.data.mappers.toCutOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus.Cut
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus.Idle
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers.LocalHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers.RemoteHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.SnackbarsHolder
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile
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
  profile: UserProfile,
  networkStatus: NetworkStatus,
  userSubject: String? = null,
  localHandler: LocalHandler = LocalHandler(),
  remoteHandler: RemoteHandler,
  snackbarsHolder: SnackbarsHolder,
  onHide: () -> Unit = {},
  onDecryptToken: () -> String,
  onRefresh: () -> Unit = {},
) {
  val apiRequestErrorMessage = stringResource(id = R.string.request_error)

  val okActionLabel = stringResource(id = R.string.ok)

  val onApiErrorShowSnackbar =
    suspend {
      snackbarsHolder.errorSnackbarHostState.showSnackbar(
        message = apiRequestErrorMessage,
        actionLabel = okActionLabel,
      )
    }

  val scope = rememberCoroutineScope()
  val onHideWithDelay =
    suspend {
      onHide()
      delay(400L)
    }

  when (profile) {
    Customer -> {
      OrderItemButton(
        modifier = modifier,
        order = order,
        text = stringResource(id = R.string.delete_button),
        networkStatus = networkStatus,
        onClick = {
          scope.launch {
            launch(Dispatchers.IO) {
              onHideWithDelay()

              localHandler.deleteOrder(order)
              val wasAcknowledged =
                remoteHandler.deleteOrder(
                  order.orderId,
                  userSubject ?: "",
                )
              if (wasAcknowledged) {
                onRefresh()
              } else {
                withContext(Dispatchers.Main) { onApiErrorShowSnackbar() }
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
          networkStatus = networkStatus,
          onClick = {
            scope.launch(Dispatchers.IO) {
              onHideWithDelay()

              val cutOrder = order.toCutOrder()
              localHandler.editOrder(cutOrder)
              val wasAcknowledged =
                remoteHandler.editOrder(
                  cutOrder,
                  order.orderId,
                  userSubject ?: "",
                )
              if (wasAcknowledged) {
                onRefresh()
              } else {
                withContext(Dispatchers.Main) { onApiErrorShowSnackbar() }
              }
            }
          },
        )
      }
    }

    Inspector -> {
      if (order.status == Cut) {
        OrderItemButton(
          modifier = modifier,
          order = order,
          text = stringResource(id = R.string.order_was_checked),
          networkStatus = networkStatus,
          onClick = {
            scope.launch(Dispatchers.IO) {
              onHideWithDelay()

              val checkedOrder = order.toCheckedOrder()
              localHandler.editOrder(checkedOrder)
              val wasAcknowledged =
                remoteHandler.editOrder(
                  checkedOrder,
                  order.orderId,
                  userSubject ?: "",
                )
              if (wasAcknowledged) {
                onRefresh()
              } else {
                withContext(Dispatchers.Main) { onApiErrorShowSnackbar() }
              }
            }
          },
        )
      }
    }

    else -> {}
  }
}