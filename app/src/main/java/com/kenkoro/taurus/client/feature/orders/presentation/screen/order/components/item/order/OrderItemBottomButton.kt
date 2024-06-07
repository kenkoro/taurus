package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.item.order

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.orders.domain.NewOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Customer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OrderItemBottomButton(
  modifier: Modifier = Modifier,
  order: Order,
  profile: UserProfile,
  networkStatus: NetworkStatus,
  deleterSubject: String? = null,
  onAddNewOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onEditOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderRemotely: suspend (orderId: Int, deleterSubject: String) -> Boolean,
  onEditOrderRemotely: suspend (NewOrderDto, Int, String, String) -> Boolean,
  onHide: () -> Unit = {},
) {
  val scope = rememberCoroutineScope()

  when (profile) {
    Customer -> {
      OrderItemButton(
        order = order,
        text = stringResource(id = R.string.delete_button),
        networkStatus = networkStatus,
        onClick = {
          scope.launch {
            launch(Dispatchers.IO) {
              onHide()
              delay(400L)

              onDeleteOrderLocally(order)
              onDeleteOrderRemotely(order.orderId, deleterSubject ?: "")
            }
          }
        },
      )
    }

    /*
    Cutter -> {
      if (order.status == Idle) {
        val onClick = {
          scope.launch(Dispatchers.IO) {
            onEditOrderLocally(order.toCutOrder())
            /**
             * TODO: Release this completely
             * val wasAcknowledged = onEditOrderRemotely(
             *   order.toCutOrder().toNewOrderDto(),
             *   order.orderId,
             *   "editorSubject",
             *   "token",
             * )
             * Log.d("kenkoro", "Attempted to edit the order remotely! Result: $wasAcknowledged")
             */
          }
          Unit
        }

        OrderItemButton(
          order = order,
          onVisible = onVisible,
          onClick = onClick,
          text = stringResource(id = R.string.order_was_cut),
          networkStatus = networkStatus,
        )
      }
    }

    Inspector -> {
      if (order.status == Cut) {
        val onClick = {
          scope.launch(Dispatchers.IO) {
            onEditOrderLocally(order.toCheckedOrder())
            /**
             * TODO: Release this as well
             * val wasAcknowledged = onEditOrderRemotely(
             *   order.toCheckedOrder().toNewOrderDto(),
             *   order.orderId,
             *   "editorSubject",
             *   "token",
             * )
             * Log.d("kenkoro", "Attempted to edit the order remotely! Result: $wasAcknowledged")
             */
          }
          Unit
        }

        OrderItemButton(
          order = order,
          onVisible = onVisible,
          onClick = onClick,
          text = stringResource(id = R.string.order_was_checked),
          networkStatus = networkStatus,
        )
      }
    }
    */

    else -> {}
  }
}