package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.core.local.LocalSize
import com.kenkoro.taurus.client.core.local.LocalStrokeWidth
import com.kenkoro.taurus.client.feature.orders.data.mappers.toCutOrder
import com.kenkoro.taurus.client.feature.orders.domain.NewCutOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenShared
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenSnackbarsHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenUtils
import com.kenkoro.taurus.client.feature.profile.domain.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ActualCutOrdersQuantityDialog(
  modifier: Modifier = Modifier,
  order: Order,
  user: User,
  closeCutterDialog: () -> Unit = {},
  onHideWithDelay: suspend () -> Unit = {},
  onRefresh: () -> Unit = {},
  shared: OrderScreenShared,
  snackbarsHolder: OrderScreenSnackbarsHolder,
  utils: OrderScreenUtils,
) {
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current
  val focusManager = LocalFocusManager.current
  val strokeWidth = LocalStrokeWidth.current
  val size = LocalSize.current

  val ordersScope = utils.scope
  var actualCutOrdersQuantity by remember {
    mutableStateOf("")
  }
  val interactionSource = remember { MutableInteractionSource() }
  var isLoading by remember {
    mutableStateOf(false)
  }
  val actualCutOrdersQuantityToInt = {
    if (actualCutOrdersQuantity.isNotEmpty()) {
      actualCutOrdersQuantity.toInt()
    } else {
      0
    }
  }
  val onAddNewCutOrder =
    suspend {
      val newCutOrder =
        NewCutOrder(
          orderId = order.orderId,
          date = System.currentTimeMillis(),
          quantity = actualCutOrdersQuantityToInt(),
          cutterId = user.userId,
          comment = "",
        )
      utils.addNewCutOrder(newCutOrder)
    }
  val onChangeOrderStateToCut =
    suspend {
      onHideWithDelay()

      val cutOrder = order.toCutOrder()
      val isFailure =
        utils.editOrder(cutOrder, user.subject) {
          onRefresh()
        }
      if (isFailure) {
        withContext(Dispatchers.Main) { snackbarsHolder.apiError() }
      }
    }
  val onSubmit = {
    if (actualCutOrdersQuantity.isNotBlank()) {
      ordersScope.launch(Dispatchers.IO) {
        isLoading = true
        onAddNewCutOrder()
        isLoading = false
        closeCutterDialog()
        onChangeOrderStateToCut()
      }
    }
  }

  val preConfiguredModifier =
    Modifier
      .width(contentWidth.actualCutOrdersQuantityTextField)
      .height(contentHeight.actualCutOrdersQuantityTextField)

  Dialog(onDismissRequest = closeCutterDialog) {
    Column(
      modifier =
        modifier
          .clip(RoundedCornerShape(shape.medium))
          .size(
            width = contentWidth.orderItem,
            height = contentHeight.actualCutOrdersQuantityDialog,
          )
          .background(MaterialTheme.colorScheme.background)
          .clickable(interactionSource = interactionSource, indication = null) {
            focusManager.clearFocus()
          },
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = stringResource(id = R.string.actual_cut_orders_quantity_dialog_label),
        fontWeight = FontWeight.Medium,
      )
      Spacer(modifier = Modifier.height(contentHeight.medium))
      Row {
        OutlinedTextField(
          modifier = preConfiguredModifier,
          value = actualCutOrdersQuantity,
          onValueChange = {
            if (it.isNotBlank() && it.isDigitsOnly()) {
              actualCutOrdersQuantity = it
            }
          },
          placeholder = { Text(text = stringResource(id = R.string.empty_text_field)) },
          keyboardOptions =
            KeyboardOptions.Default.copy(
              keyboardType = KeyboardType.Number,
            ),
          keyboardActions = KeyboardActions(onAny = { onSubmit() }),
          shape = RoundedCornerShape(shape.medium),
        )
        Spacer(modifier = Modifier.width(contentWidth.small))
        Button(
          modifier = preConfiguredModifier,
          onClick = onSubmit,
        ) {
          if (isLoading) {
            CircularProgressIndicator(
              strokeWidth = strokeWidth.small,
              modifier = Modifier.size(size.small),
              color = MaterialTheme.colorScheme.onPrimary,
            )
          } else {
            Icon(
              imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
              contentDescription = "actual quantity dialog composable: save changes",
            )
          }
        }
      }
    }
  }
}