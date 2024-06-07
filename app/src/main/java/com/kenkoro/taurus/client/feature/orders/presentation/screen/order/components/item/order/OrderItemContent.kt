package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.item.order

import android.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderItemContent(
  modifier: Modifier = Modifier,
  order: Order,
  selected: Boolean,
) {
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current
  val orderInfo =
    listOf(
      Pair(stringResource(id = R.string.order_id), order.orderId.toString()),
      Pair(stringResource(id = R.string.order_size), order.size),
      Pair(stringResource(id = R.string.order_model), order.model),
      Pair(stringResource(id = R.string.order_category), order.category),
      Pair(stringResource(id = R.string.order_color), order.color),
      Pair(stringResource(id = R.string.order_customer), order.customer),
      Pair(stringResource(id = R.string.order_quantity), order.quantity.toString()),
    )

  Column(verticalArrangement = Arrangement.Top) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      OrderItemDateAndTitleRow(
        orderDate = order.date,
        orderTitle = order.title,
        orderStatus = order.status,
      )
      OrderItemKeyboardArrowIconRow(selected = selected)
    }

    Spacer(modifier = Modifier.height(contentHeight.large))
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
      items(orderInfo) { pair ->
        Row(
          modifier =
            Modifier
              .height(contentHeight.orderItemField)
              .clickable {},
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Spacer(modifier = Modifier.width(contentWidth.large))
          Text(
            modifier = Modifier.fillMaxWidth(),
            text = "${pair.first}: ${pair.second}",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }
    }
  }
}

@Preview(showBackground = true, backgroundColor = Color.WHITE.toLong())
@Composable
private fun OrderItemContentPrev() {
  val order =
    Order(
      recordId = 0,
      orderId = 0,
      customer = "Customer",
      date = 1717427278111L,
      title = "Title",
      model = "Model",
      size = "Size",
      color = "Color",
      category = "Category",
      quantity = 0,
      status = OrderStatus.Checked,
      creatorId = 0,
    )

  AppTheme {
    OrderItemContent(order = order, selected = true)
  }
}