package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.item.order

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus

@Composable
fun OrderItemDateAndMainInfoRow(
  modifier: Modifier = Modifier,
  orderDate: Long,
  orderId: Int,
  orderStatus: OrderStatus? = null,
) {
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.Start,
  ) {
    Column(
      modifier = Modifier.width(contentWidth.halfStandard),
      horizontalAlignment = Alignment.Start,
    ) {
      Row {
        Spacer(modifier = Modifier.width(contentWidth.large))
        Text(
          text = dateFromMillis(orderDate),
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          style = MaterialTheme.typography.bodySmall,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
      Spacer(modifier = Modifier.height(contentHeight.small))
      Row {
        Spacer(modifier = Modifier.width(contentWidth.large))
        Text(
          modifier = Modifier.width(contentWidth.orderTitle),
          text = orderId.toString(),
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          style = MaterialTheme.typography.bodyMedium,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.width(contentWidth.small))
        if (orderStatus != null) {
          OrderStatusIcon(orderStatus = orderStatus)
        }
      }
    }
  }
}

private fun dateFromMillis(
  dateInMillis: Long,
  inFormat: String = "dd.MM.yyyy hh:mm:ss",
): String {
  return DateFormat
    .format(inFormat, dateInMillis)
    .toString()
}