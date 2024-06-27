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
    Category(
      categoryState = orderStatesHolder.categoryState,
      modifier = Modifier.focusRequester(focusRequester),
    )
  }
}