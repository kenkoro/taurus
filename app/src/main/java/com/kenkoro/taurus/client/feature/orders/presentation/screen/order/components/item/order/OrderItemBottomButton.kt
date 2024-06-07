package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.item.order

import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.orders.data.mappers.toCheckedOrder
import com.kenkoro.taurus.client.feature.orders.data.mappers.toCutOrder
import com.kenkoro.taurus.client.feature.orders.data.mappers.toNewOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.orders.domain.NewOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus.Cut
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus.Idle
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Customer
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Cutter
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Inspector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OrderItemBottomButton(
  modifier: Modifier = Modifier,
  order: Order,
  profile: UserProfile,
  networkStatus: NetworkStatus,
  userSubject: String? = null,
  onAddNewOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onEditOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderRemotely: suspend (orderId: Int, deleterSubject: String) -> Boolean,
  onEditOrderRemotely: suspend (NewOrderDto, Int, String, String) -> Boolean,
  onHide: () -> Unit = {},
  onDecryptToken: () -> String,
  onApiErrorShowSnackbar: suspend () -> SnackbarResult
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
              val wasAcknowledged = onDeleteOrderRemotely(
                order.orderId,
                userSubject ?: ""
              )
              if (!wasAcknowledged) {
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
          order = order,
          text = stringResource(id = R.string.order_was_cut),
          networkStatus = networkStatus,
          onClick = {
            scope.launch(Dispatchers.IO) {
              val cutOrder = order.toCutOrder()
              onEditOrderLocally(cutOrder)
              val wasAcknowledged = onEditOrderRemotely(
                cutOrder.toNewOrderDto(),
                order.orderId,
                userSubject ?: "",
                onDecryptToken(),
              )
              if (!wasAcknowledged) {
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
          order = order,
          text = stringResource(id = R.string.order_was_checked),
          networkStatus = networkStatus,
          onClick = {
            scope.launch(Dispatchers.IO) {
              val checkedOrder = order.toCheckedOrder()
              onEditOrderLocally(checkedOrder)
              val wasAcknowledged = onEditOrderRemotely(
                checkedOrder.toNewOrderDto(),
                order.orderId,
                userSubject ?: "",
                onDecryptToken(),
              )
              if (!wasAcknowledged) {
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