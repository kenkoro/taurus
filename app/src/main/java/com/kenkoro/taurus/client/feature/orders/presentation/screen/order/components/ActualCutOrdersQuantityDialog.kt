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
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.core.local.LocalSize
import com.kenkoro.taurus.client.core.local.LocalStrokeWidth
import com.kenkoro.taurus.client.feature.login.data.mappers.toUserDto
import com.kenkoro.taurus.client.feature.orders.data.mappers.toCutOrder
import com.kenkoro.taurus.client.feature.orders.data.mappers.toCutOrderDto
import com.kenkoro.taurus.client.feature.orders.data.mappers.toOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.ActualCutOrdersQuantityDto
import com.kenkoro.taurus.client.feature.orders.domain.CutOrder
import com.kenkoro.taurus.client.feature.orders.domain.NewCutOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers.LocalHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers.RemoteHandler
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Other
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import com.kenkoro.taurus.client.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ActualCutOrdersQuantityDialog(
  modifier: Modifier = Modifier,
  order: Order,
  cutterId: Int = 0,
  userSubject: String? = null,
  localHandler: LocalHandler = LocalHandler(),
  remoteHandler: RemoteHandler,
  onClose: () -> Unit = {},
  onHideWithDelay: suspend () -> Unit = {},
  onRefresh: () -> Unit = {},
  onApiErrorShowSnackbar: suspend () -> SnackbarResult,
) {
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current
  val focusManager = LocalFocusManager.current
  val strokeWidth = LocalStrokeWidth.current
  val size = LocalSize.current

  var actualCutOrdersQuantity by remember {
    mutableStateOf("")
  }
  val interactionSource = remember { MutableInteractionSource() }
  val scope = rememberCoroutineScope()
  var isLoading by remember {
    mutableStateOf(false)
  }

  val preConfiguredModifier =
    Modifier
      .width(contentWidth.actualCutOrdersQuantityTextField)
      .height(contentHeight.actualCutOrdersQuantityTextField)

  val onSubmit = {
    if (actualCutOrdersQuantity.isNotBlank()) {
      scope.launch(Dispatchers.IO) {
        isLoading = true

        val isSuccess =
          remoteHandler.addNewCutOrder(
            NewCutOrder(
              orderId = order.orderId,
              date = System.currentTimeMillis(),
              quantity = actualCutOrdersQuantity.toInt(),
              cutterId = cutterId,
              comment = "",
            ),
          ).isSuccess

        if (isSuccess) {
          onHideWithDelay()

          val cutOrder = order.toCutOrder()
          localHandler.editOrder(cutOrder, order.orderId)
          val wasAcknowledged =
            remoteHandler.editOrder(
              cutOrder,
              userSubject ?: "",
            )
          if (wasAcknowledged) {
            onRefresh()
          } else {
            withContext(Dispatchers.Main) { onApiErrorShowSnackbar() }
          }
        }

        isLoading = false
        onClose()
      }
    }
  }

  Dialog(onDismissRequest = onClose) {
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
              imageVector = Icons.Default.KeyboardDoubleArrowRight,
              contentDescription = "ActualCutOrdersQuantityButton",
            )
          }
        }
      }
    }
  }
}

@Preview
@Composable
private fun ActualCutOrdersQuantityDialogPrev() {
  val cutOrder =
    CutOrder(
      cutOrderId = 0,
      orderId = 419,
      date = 0L,
      quantity = 3,
      cutterId = 0,
      comment = "",
    )
  val order =
    Order(
      recordId = 0,
      orderId = 0,
      customer = "Customer",
      date = 0L,
      title = "Title",
      model = "Model",
      size = "Size",
      color = "Color",
      category = "Category",
      quantity = 0,
      status = OrderStatus.Idle,
      creatorId = 0,
    )
  val user =
    User(
      userId = 0,
      subject = "Subject",
      password = "Password",
      image = "Image",
      firstName = "FirstName",
      lastName = "LastName",
      email = "Email",
      profile = Other,
      salt = "Salt",
    )
  val remoteHandler =
    RemoteHandler(
      login = { _, _ -> Result.success(TokenDto("")) },
      getUser = { _, _ -> Result.success(user.toUserDto()) },
      addNewOrder = { _ -> Result.success(order.toOrderDto()) },
      addNewCutOrder = { _ -> Result.success(cutOrder.toCutOrderDto()) },
      getActualCutOrdersQuantity = { _ ->
        Result.success(ActualCutOrdersQuantityDto(cutOrder.quantity))
      },
    )

  AppTheme {
    ActualCutOrdersQuantityDialog(
      remoteHandler = remoteHandler,
      order = order,
      onApiErrorShowSnackbar = { SnackbarResult.Dismissed },
    )
  }
}