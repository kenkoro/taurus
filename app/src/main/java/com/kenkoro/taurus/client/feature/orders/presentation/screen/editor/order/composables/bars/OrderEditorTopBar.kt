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
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.TextFieldValidationService
import com.kenkoro.taurus.client.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OrderEditorTopBar(
  modifier: Modifier = Modifier,
  editOrder: Boolean = false,
  orderStatesHolder: OrderStatesHolder = OrderStatesHolder(),
  onNavUp: () -> Unit = {},
  saveChanges: suspend () -> Boolean = { false },
  validateChanges: () -> Boolean = { false },
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
          .clickable { onNavUp() },
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
              if (validateChanges()) {
                val result = saveChanges()
                if (result) {
                  withContext(Dispatchers.Main) { onNavUp() }
                }
              } else {
                TextFieldValidationService.checkAll(orderStatesHolder)
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

@Preview
@Composable
private fun OrderEditorTopBarPrev() {
  val orderDto =
    OrderDto(
      recordId = 0,
      orderId = 0,
      customer = "",
      date = 0L,
      title = "",
      model = "",
      size = "",
      color = "",
      category = "",
      quantity = 0,
      status = OrderStatus.Idle,
      creatorId = 0,
    )

  AppTheme {
    OrderEditorTopBar()
  }
}