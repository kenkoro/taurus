package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toNewOrder
import com.kenkoro.taurus.client.feature.sewing.domain.model.NewOrder
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OrderItemButton(
  order: Order,
  onAddNewOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onDeleteOrderRemotely: suspend (orderId: Int, deleterSubject: String) -> Boolean,
  onDeleteOrderShowSnackbar: suspend () -> SnackbarResult,
  onVisible: (Boolean) -> Unit,
  networkStatus: NetworkStatus,
  modifier: Modifier = Modifier,
) {
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current
  val scope = rememberCoroutineScope()

  Spacer(modifier = Modifier.height(contentHeight.large))
  Button(
    enabled = networkStatus == NetworkStatus.Available,
    modifier = modifier.size(contentWidth.standard, contentHeight.halfStandard),
    shape = RoundedCornerShape(shape.small),
    onClick = {
      scope.launch(Dispatchers.IO) {
        onVisible(false)

        delay(400L)
        onDeleteOrderLocally(order)
        val snackbarResult = onDeleteOrderShowSnackbar()

        // TODO: Logic to delete an order remotely and also recover it
        when (snackbarResult) {
          SnackbarResult.Dismissed -> {
            val deletionResult = onDeleteOrderRemotely(order.orderId, "")
            Log.d("kenkoro", "Attempted to delete the order remotely! Result: $deletionResult")
          }

          SnackbarResult.ActionPerformed -> {
            onAddNewOrderLocally(order.toNewOrder())
          }
        }
      }
    },
  ) {
    Row {
      Text(text = stringResource(id = R.string.delete_button))
    }
  }
}