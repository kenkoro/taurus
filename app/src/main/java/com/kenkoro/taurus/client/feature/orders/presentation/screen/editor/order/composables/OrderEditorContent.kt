package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenNavigator
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

@Composable
fun OrderEditorContent(
  modifier: Modifier = Modifier,
  networkStatus: NetworkStatus,
  states: OrderStatesHolder = OrderStatesHolder(),
  navigator: OrderEditorScreenNavigator,
  onStateChangeOrderDetailsSearchBehavior: (TaurusTextFieldState) -> Unit = {},
) {
  val contentWidth = LocalContentWidth.current
  val focusManager = LocalFocusManager.current

  val interactionSource = remember { MutableInteractionSource() }

  Column(
    modifier =
      modifier
        .fillMaxWidth()
        .clickable(interactionSource = interactionSource, indication = null) {
          focusManager.clearFocus()
        },
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    OrderEditorTextFields(
      modifier = Modifier.width(contentWidth.standard),
      states = states,
      navigator = navigator,
      onStateChangeOrderDetailsSearchBehavior = onStateChangeOrderDetailsSearchBehavior,
    )
  }
}