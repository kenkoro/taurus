package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toCheckedOrder
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toCutOrder
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toNewOrder
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.sewing.domain.model.NewOrder
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.OrderStatus.Cut
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.OrderStatus.Idle
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.UserProfile
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.UserProfile.Customer
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.UserProfile.Cutter
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.UserProfile.Inspector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OrderItemButtonHandler(
  order: Order,
  profile: UserProfile,
  onAddNewOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onEditOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderRemotely: suspend (orderId: Int, deleterSubject: String) -> Boolean,
  onEditOrderRemotely: suspend (NewOrderDto, Int, String, String) -> Boolean,
  onDeleteOrderShowSnackbar: suspend () -> SnackbarResult,
  onVisible: (Boolean) -> Unit,
  networkStatus: NetworkStatus,
  modifier: Modifier = Modifier,
) {
  val scope = rememberCoroutineScope()

  if (profile == Customer) {
    OrderItemButton(
      order = order,
      onVisible = onVisible,
      onClick = {
        scope.launch(Dispatchers.IO) {
          onVisible(false)
          delay(400L)
          onDeleteOrderLocally(order)

          withContext(Dispatchers.Main) {
            val snackbarResult = onDeleteOrderShowSnackbar()

            when (snackbarResult) {
              SnackbarResult.Dismissed -> {
                /**
                 * TODO: Come up with this
                 * val wasAcknowledged = onDeleteOrderRemotely(order.orderId, "")
                 * Log.d("kenkoro", "Attempted to delete the order remotely! Result: $wasAcknowledged")
                 */
              }

              SnackbarResult.ActionPerformed -> {
                onAddNewOrderLocally(order.toNewOrder())
              }
            }
          }
        }
      },
      text = stringResource(id = R.string.delete_button),
      networkStatus = networkStatus,
    )
  }
  if (profile == Cutter && order.status == Idle) {
    OrderItemButton(
      order = order,
      onVisible = onVisible,
      onClick = {
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
      },
      text = stringResource(id = R.string.order_was_cut),
      networkStatus = networkStatus,
    )
  }
  if (profile == Inspector && order.status == Cut) {
    OrderItemButton(
      order = order,
      onVisible = onVisible,
      onClick = {
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
      },
      text = stringResource(id = R.string.order_was_checked),
      networkStatus = networkStatus,
    )
  }
}