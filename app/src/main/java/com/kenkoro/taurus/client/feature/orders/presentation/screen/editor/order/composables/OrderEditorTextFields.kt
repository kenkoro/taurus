package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenNavigator
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

@Composable
fun OrderEditorTextFields(
  modifier: Modifier = Modifier,
  states: OrderStatesHolder = OrderStatesHolder(),
  navigator: OrderEditorScreenNavigator,
  onStateChangeOrderDetailsSearchBehavior: (TaurusTextFieldState) -> Unit = {},
) {
  val focusManager = LocalFocusManager.current
  val contentHeight = LocalContentHeight.current

  val scrollState = rememberScrollState()

  Column(
    modifier = modifier.verticalScroll(scrollState),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
  ) {
    OrderDetailDropDown(
      navigator = navigator,
      state = states.customerState,
      dropDownTitle = stringResource(id = R.string.order_editor_customer),
      onStateChangeOrderDetailsSearchBehavior = onStateChangeOrderDetailsSearchBehavior,
    )
    Spacer(modifier = Modifier.height(contentHeight.extraMedium))
    OrderDetailDropDown(
      navigator = navigator,
      state = states.titleState,
      dropDownTitle = stringResource(id = R.string.order_editor_title),
      onStateChangeOrderDetailsSearchBehavior = onStateChangeOrderDetailsSearchBehavior,
    )
    Spacer(modifier = Modifier.height(contentHeight.extraMedium))
    OrderDetailDropDown(
      navigator = navigator,
      state = states.modelState,
      dropDownTitle = stringResource(id = R.string.order_editor_model),
      onStateChangeOrderDetailsSearchBehavior = onStateChangeOrderDetailsSearchBehavior,
    )
    Spacer(modifier = Modifier.height(contentHeight.extraMedium))
    OrderDetailDropDown(
      navigator = navigator,
      state = states.sizeState,
      dropDownTitle = stringResource(id = R.string.order_editor_size),
      onStateChangeOrderDetailsSearchBehavior = onStateChangeOrderDetailsSearchBehavior,
    )
    Spacer(modifier = Modifier.height(contentHeight.extraMedium))
    OrderDetailDropDown(
      navigator = navigator,
      state = states.colorState,
      dropDownTitle = stringResource(id = R.string.order_editor_color),
      onStateChangeOrderDetailsSearchBehavior = onStateChangeOrderDetailsSearchBehavior,
    )
    Spacer(modifier = Modifier.height(contentHeight.extraMedium))
    OrderDetailDropDown(
      navigator = navigator,
      state = states.categoryState,
      dropDownTitle = stringResource(id = R.string.order_editor_category),
      onStateChangeOrderDetailsSearchBehavior = onStateChangeOrderDetailsSearchBehavior,
    )
    Spacer(modifier = Modifier.height(contentHeight.extraLarge))
    OrderQuantity(
      quantityState = states.quantityState,
      onImeAction = { focusManager.clearFocus() },
    )
    Spacer(modifier = Modifier.height(contentHeight.extraMedium))
  }
}