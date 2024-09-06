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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.bars.util.OrderEditorScreenExtras
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.TextFieldValidationService
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
) {
  val scope = rememberCoroutineScope()
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

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
          .clickable {
            scope.launch(Dispatchers.IO) {
              if (extras.validateChanges()) {
                val result = extras.saveChanges()
                if (result) {
                  withContext(Dispatchers.Main) { navigator.navUp() }
                }
              } else {
                TextFieldValidationService.checkAll(states)
              }
            }
          },
      contentAlignment = Alignment.Center,
    ) {
      Icon(
        imageVector = Icons.Default.Check,
        contentDescription = "SaveOrderDetailsChanges",
      )
    }
  }
}