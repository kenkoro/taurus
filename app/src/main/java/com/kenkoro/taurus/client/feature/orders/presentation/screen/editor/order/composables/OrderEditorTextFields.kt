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
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.OrderDetails
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenShared

@Composable
fun OrderEditorTextFields(
  modifier: Modifier = Modifier,
  details: OrderDetails,
  navigator: OrderEditorScreenNavigator,
  shared: OrderEditorScreenShared,
) {
  val focusManager = LocalFocusManager.current
  val contentHeight = LocalContentHeight.current

  val lazyListState = rememberLazyListState()
  val orderDetailItems =
    listOf(
      OrderDetailItem(
        state = details.customerState,
        dropDownTitle = stringResource(id = R.string.order_editor_customer),
      ),
      OrderDetailItem(
        state = details.titleState,
        dropDownTitle = stringResource(id = R.string.order_editor_title),
      ),
      OrderDetailItem(
        state = details.modelState,
        dropDownTitle = stringResource(id = R.string.order_editor_model),
      ),
      OrderDetailItem(
        state = details.sizeState,
        dropDownTitle = stringResource(id = R.string.order_editor_size),
      ),
      OrderDetailItem(
        state = details.colorState,
        dropDownTitle = stringResource(id = R.string.order_editor_color),
      ),
      OrderDetailItem(
        state = details.categoryState,
        dropDownTitle = stringResource(id = R.string.order_editor_category),
      ),
    )

  LazyColumn(
    modifier = modifier,
    state = lazyListState,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
  ) {
    items(orderDetailItems) {
      OrderDetailDropDown(
        navigator = navigator,
        state = it.state,
        dropDownTitle = it.dropDownTitle,
        onChangeBehaviorOfOrderDetailsSearch = shared.changeBehaviorOfOrderDetailsSearch,
      )
      Spacer(modifier = Modifier.height(contentHeight.extraMedium))
    }
    item {
      Spacer(modifier = Modifier.height(contentHeight.medium))
      OrderQuantity(
        quantityState = details.quantityState,
        onImeAction = { focusManager.clearFocus() },
      )
      Spacer(modifier = Modifier.height(contentHeight.extraMedium))
    }
  }
}