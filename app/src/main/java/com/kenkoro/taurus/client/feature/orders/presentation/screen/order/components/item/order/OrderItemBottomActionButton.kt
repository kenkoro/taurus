package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.item.order

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.orders.data.mappers.toCheckedOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus.Cut
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus.Idle
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.ActualCutOrdersQuantityDialog
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
  networkStatus: NetworkStatus,
  order: Order,
  userId: Int,
  userProfile: UserProfile,
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
      withContext(Dispatchers.Main) {
        snackbarsHolder.errorSnackbarHostState.showSnackbar(
          message = apiRequestErrorMessage,
          actionLabel = okActionLabel,
        )
      }
    }
  val onHideWithDelay =
    suspend {
      onHide()
      delay(400L)
    }

  val scope = rememberCoroutineScope()
  var showCutterDialog by remember {
    mutableStateOf(false)
  }
  val openCutterDialog = { showCutterDialog = true }

  if (showCutterDialog) {
    ActualCutOrdersQuantityDialog(
      order = order,
      cutterId = userId,
      userSubject = userSubject,
      localHandler = localHandler,
      remoteHandler = remoteHandler,
      closeCutterDialog = { showCutterDialog = false },
      onHideWithDelay = onHideWithDelay,
      onRefresh = onRefresh,
      onApiErrorShowSnackbar = onApiErrorShowSnackbar,
    )
  }

  when (userProfile) {
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
              Log.d("kenkoro", wasAcknowledged.toString())

              if (wasAcknowledged) {
                onRefresh()
              } else {
                onApiErrorShowSnackbar()
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
          networkStatus = networkStatus,
          onClick = {
            scope.launch(Dispatchers.IO) {
              onHideWithDelay()

              val checkedOrder = order.toCheckedOrder()
              localHandler.editOrder(checkedOrder, order.orderId)
              val wasAcknowledged =
                remoteHandler.editOrder(
                  checkedOrder,
                  userSubject ?: "",
                )
              if (wasAcknowledged) {
                onRefresh()
              } else {
                onApiErrorShowSnackbar()
              }
            }
          },
        )
      }
    }

    else -> {}
  }
}