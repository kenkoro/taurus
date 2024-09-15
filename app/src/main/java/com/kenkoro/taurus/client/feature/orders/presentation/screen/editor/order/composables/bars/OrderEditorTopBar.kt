package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalSize
import com.kenkoro.taurus.client.core.local.LocalStrokeWidth
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.bars.util.OrderEditorScreenExtras
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenSnackbarsHolder
import com.kenkoro.taurus.client.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OrderEditorTopBar(
  modifier: Modifier = Modifier,
  editOrder: Boolean = false,
  states: OrderStatesHolder = OrderStatesHolder(),
  navigator: OrderEditorScreenNavigator,
  extras: OrderEditorScreenExtras,
  snackbarsHolder: OrderEditorScreenSnackbarsHolder,
) {
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current
  val strokeWidth = LocalStrokeWidth.current
  val size = LocalSize.current

  val scope = rememberCoroutineScope()
  var isLoading by remember {
    mutableStateOf(false)
  }

  val onSubmit = {
    scope.launch(Dispatchers.IO) {
      if (extras.validateChanges()) {
        isLoading = true
        val result = extras.saveChanges()
        isLoading = false

        withContext(Dispatchers.Main) {
          if (result) {
            navigator.navUp()
          } else {
            snackbarsHolder.apiError()
          }
        }
      } else {
        withContext(Dispatchers.Main) { snackbarsHolder.failedOrderEditorValidation() }
      }
    }
  }

  Row(
    modifier =
      modifier
        .fillMaxWidth()
        .height(contentHeight.topBar)
        .background(MaterialTheme.colorScheme.background),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Box(
      modifier =
        Modifier
          .size(contentHeight.topBar)
          .clickable { navigator.navUp() },
      contentAlignment = Alignment.Center,
    ) {
      Icon(
        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
        contentDescription = "GoBackToOrderScreen",
      )
    }
    Row(
      modifier =
        Modifier
          .weight(1F)
          .fillMaxHeight(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Spacer(modifier = Modifier.width(contentWidth.medium))
      Text(
        text =
          if (editOrder) {
            stringResource(id = R.string.edit_order_label)
          } else {
            stringResource(id = R.string.create_order_label)
          },
      )
    }
    Box(
      modifier =
        Modifier
          .size(contentHeight.topBar)
          .clickable { onSubmit() },
      contentAlignment = Alignment.Center,
    ) {
      if (isLoading) {
        CircularProgressIndicator(
          strokeWidth = strokeWidth.small,
          modifier = Modifier.size(size.small),
        )
      } else {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = "",
        )
      }
    }
  }
}

@Preview
@Composable
private fun OrderEditorTopBarPrev() {
  AppTheme {
    val navigator = OrderEditorScreenNavigator {}
    val extras = OrderEditorScreenExtras()
    val snackbarsHolder = OrderEditorScreenSnackbarsHolder()
    OrderEditorTopBar(navigator = navigator, extras = extras, snackbarsHolder = snackbarsHolder)
  }
}