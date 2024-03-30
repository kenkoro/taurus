package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.sewing.data.util.OrderStatus
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderItem(order: Order) {
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  Row(
    modifier =
      Modifier
        .width(contentWidth.standard + 30.dp)
        .heightIn(min = 90.dp, max = 150.dp)
        .clip(RoundedCornerShape(shape.medium))
        .background(MaterialTheme.colorScheme.primaryContainer)
        .clickable { /* TODO */ },
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Row(
      modifier = Modifier.fillMaxHeight(.75F),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Start,
    ) {
      Spacer(modifier = Modifier.width(contentWidth.medium * 2))
      Column(modifier = Modifier.width(contentWidth.halfStandard)) {
        Text(
          text = "${order.date} | ${order.orderId}",
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
        Spacer(modifier = Modifier.height(contentHeight.small))
        Text(
          text = "${order.model} - ${order.size} (${order.quantity})",
          color = MaterialTheme.colorScheme.onBackground,
          style = MaterialTheme.typography.bodySmall,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
    Row {
      when (order.status) {
        OrderStatus.NotStarted -> {
          Text(
            text = stringResource(id = R.string.order_status_not_started),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.labelMedium,
          )
        }

        OrderStatus.Cutted -> {
          Text(
            text = stringResource(id = R.string.order_status_cutted),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.labelMedium,
          )
        }

        OrderStatus.Checked -> {
          Text(
            text = stringResource(id = R.string.order_status_checked),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.labelMedium,
          )
        }
      }
      Spacer(modifier = Modifier.width(contentWidth.medium * 2))
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun OrderItemPrev() {
  AppTheme {
    OrderItem(
      order =
        Order(
          orderId = 0,
          customer = "Customer",
          date = "29.03.2024",
          title = "Titleasdflnaeopifnasdfonaspdfnasdfnsdjfjasnsfdn",
          model = "Model",
          size = "Size",
          color = "Color",
          category = "Category",
          quantity = 0,
          status = OrderStatus.NotStarted,
        ),
    )
  }
}