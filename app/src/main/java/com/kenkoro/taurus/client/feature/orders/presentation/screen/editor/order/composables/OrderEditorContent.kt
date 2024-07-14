package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderEditorContent(
  modifier: Modifier = Modifier,
  networkStatus: NetworkStatus,
  orderStatesHolder: OrderStatesHolder = OrderStatesHolder(),
  onNavUp: () -> Unit = {},
  saveChanges: suspend () -> Boolean = { false },
  validateChanges: () -> Boolean = { false },
) {
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current
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
    Spacer(modifier = Modifier.height(contentHeight.extraLarge))
    OrderEditorTextFields(
      modifier = Modifier.width(contentWidth.standard),
      orderStatesHolder = orderStatesHolder,
      saveChanges = saveChanges,
      onNavUp = onNavUp,
      validateChanges = validateChanges,
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun OrderEditorContentPrev() {
  AppTheme {
    OrderEditorContent(networkStatus = NetworkStatus.Available)
  }
}