package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder

@Composable
fun OrderEditorTextFields(
  modifier: Modifier = Modifier,
  orderStatesHolder: OrderStatesHolder = OrderStatesHolder(),
) {
  val contentHeight = LocalContentHeight.current

  val focusRequester = remember { FocusRequester() }

  val onSubmit = {
    // TODO: Check all states and then send a request to edit an order
  }

  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
  ) {
    OrderId(
      orderIdState = orderStatesHolder.orderIdState,
      onImeAction = { focusRequester.requestFocus() },
    )
    Spacer(modifier = Modifier.height(contentHeight.medium))
    OrderCustomer(
      customerState = orderStatesHolder.customerState,
      modifier = Modifier.focusRequester(focusRequester),
    )
    OrderTitle(
      titleState = orderStatesHolder.titleState,
      modifier = Modifier.focusRequester(focusRequester),
    )
    Spacer(modifier = Modifier.height(contentHeight.medium))
    OrderModel(
      modelState = orderStatesHolder.modelState,
      modifier = Modifier.focusRequester(focusRequester),
    )
    Spacer(modifier = Modifier.height(contentHeight.medium))
    OrderSize(
      sizeState = orderStatesHolder.sizeState,
      modifier = Modifier.focusRequester(focusRequester),
    )
    Spacer(modifier = Modifier.height(contentHeight.medium))
    OrderColor(
      colorState = orderStatesHolder.colorState,
      modifier = Modifier.focusRequester(focusRequester),
    )
    Spacer(modifier = Modifier.height(contentHeight.medium))
    OrderCategory(
      categoryState = orderStatesHolder.categoryState,
      modifier = Modifier.focusRequester(focusRequester),
    )
    Spacer(modifier = Modifier.height(contentHeight.medium))
    OrderQuantity(
      quantityState = orderStatesHolder.quantityState,
      modifier = Modifier.focusRequester(focusRequester),
      onImeAction = { onSubmit() },
    )
  }
}