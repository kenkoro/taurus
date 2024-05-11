package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

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
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.OrderStatus

@Composable
fun OrderItemContent(
  order: Order,
  clicked: Boolean,
  modifier: Modifier = Modifier,
) {
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  Spacer(modifier = Modifier.height(contentHeight.large))
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(.5F),
      horizontalArrangement = Arrangement.Start,
    ) {
      Spacer(modifier = Modifier.width(contentWidth.large))
      Column(
        modifier = Modifier.width(contentWidth.halfStandard),
        horizontalAlignment = Alignment.Start,
      ) {
        Text(
          text = order.date.toString(),
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          style = MaterialTheme.typography.bodySmall,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(contentHeight.small))
        Text(
          text = order.title,
          color = MaterialTheme.colorScheme.onBackground,
          style = MaterialTheme.typography.bodyMedium,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }

    val orderStatus =
      when (order.status) {
        OrderStatus.Idle -> stringResource(id = R.string.order_status_idle)
        OrderStatus.Cut -> stringResource(id = R.string.order_status_cut)
        OrderStatus.Checked -> stringResource(id = R.string.order_status_checked)
      }
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.End,
    ) {
      Text(text = orderStatus)
      Spacer(modifier = Modifier.width(contentWidth.large))
    }
  }

  if (clicked) {
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

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
      item {
        Spacer(modifier = Modifier.height(contentHeight.large))
      }
      items(orderInfo) { pair ->
        Row {
          Spacer(modifier = Modifier.width(contentWidth.large))
          Text(
            text = "${pair.first}: ${pair.second}",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }
    }
  }
}