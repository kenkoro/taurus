package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.item.order

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kenkoro.taurus.client.core.local.LocalSize
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus

@Composable
fun OrderStatusIcon(
  modifier: Modifier = Modifier,
  orderStatus: OrderStatus,
) {
  val size = LocalSize.current
  modifier.size(size.medium)

  when (orderStatus) {
    OrderStatus.Idle -> {
      Icon(
        imageVector = Icons.Default.AccessTime,
        contentDescription = "order status icon composable: idle",
        modifier = modifier,
        tint = MaterialTheme.colorScheme.primary,
      )
    }

    OrderStatus.Cut -> {
      Icon(
        imageVector = Icons.Default.ContentCut,
        contentDescription = "order status icon composable: cut",
        modifier = modifier,
        tint = MaterialTheme.colorScheme.primary,
      )
    }

    OrderStatus.Checked -> {
      Icon(
        imageVector = Icons.Default.Check,
        contentDescription = "order status icon composable: checked",
        modifier = modifier,
        tint = MaterialTheme.colorScheme.primary,
      )
    }

    OrderStatus.InWork -> {
      Icon(
        imageVector = Icons.Default.WorkOutline,
        contentDescription = "order status icon composable: in work",
        modifier = modifier,
        tint = MaterialTheme.colorScheme.primary,
      )
    }
  }
}