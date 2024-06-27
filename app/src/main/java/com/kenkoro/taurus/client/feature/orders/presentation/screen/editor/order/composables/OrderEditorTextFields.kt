package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderIdState

@Composable
fun OrderEditorTextFields(
  modifier: Modifier = Modifier,
  orderIdState: OrderIdState = OrderIdState(),
) {
  val focusRequester = remember { FocusRequester() }

  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
  ) {
    OrderId(
      orderIdState = orderIdState,
      onImeAction = { focusRequester.requestFocus() },
    )
  }
}