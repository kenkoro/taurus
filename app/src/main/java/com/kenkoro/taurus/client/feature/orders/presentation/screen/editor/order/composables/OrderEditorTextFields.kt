package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.util.OrderDetailItem
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

  val lazyListState = rememberLazyListState()
  val details =
    listOf(
      OrderDetailItem(
        state = states.customerState,
        dropDownTitle = stringResource(id = R.string.order_editor_customer),
      ),
      OrderDetailItem(
        state = states.titleState,
        dropDownTitle = stringResource(id = R.string.order_editor_title),
      ),
      OrderDetailItem(
        state = states.modelState,
        dropDownTitle = stringResource(id = R.string.order_editor_model),
      ),
      OrderDetailItem(
        state = states.sizeState,
        dropDownTitle = stringResource(id = R.string.order_editor_size),
      ),
      OrderDetailItem(
        state = states.colorState,
        dropDownTitle = stringResource(id = R.string.order_editor_color),
      ),
      OrderDetailItem(
        state = states.categoryState,
        dropDownTitle = stringResource(id = R.string.order_editor_category),
      ),
    )

  LazyColumn(
    modifier = modifier,
    state = lazyListState,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
  ) {
    items(details) {
      OrderDetailDropDown(
        navigator = navigator,
        state = it.state,
        dropDownTitle = it.dropDownTitle,
        onStateChangeOrderDetailsSearchBehavior = onStateChangeOrderDetailsSearchBehavior,
      )
      Spacer(modifier = Modifier.height(contentHeight.extraMedium))
    }
    item {
      Spacer(modifier = Modifier.height(contentHeight.medium))
      OrderQuantity(
        quantityState = states.quantityState,
        onImeAction = { focusManager.clearFocus() },
      )
      Spacer(modifier = Modifier.height(contentHeight.extraMedium))
    }
  }
}