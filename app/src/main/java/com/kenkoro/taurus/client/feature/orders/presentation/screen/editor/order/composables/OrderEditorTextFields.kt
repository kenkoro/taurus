package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.bars.util.OrderEditorScreenExtras
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder

@Composable
fun OrderEditorTextFields(
  modifier: Modifier = Modifier,
  states: OrderStatesHolder = OrderStatesHolder(),
  navigator: OrderEditorScreenNavigator,
  extras: OrderEditorScreenExtras,
) {
  val scrollState = rememberScrollableState { it }
  val contentHeight = LocalContentHeight.current

  val onSubmit = {
  }

  Column(
    modifier = modifier.scrollable(state = scrollState, orientation = Orientation.Vertical),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
  ) {
    OrderDetailDropDown(
      state = states.customerState,
      dropDownTitle = stringResource(id = R.string.order_editor_customer),
      supportingText = {
        Text(text = stringResource(id = R.string.order_customer_supporting_text))
      },
    )
  }
}