package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.item.order

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order

@Composable
fun OrderItemButton(
  order: Order,
  onVisible: (Boolean) -> Unit,
  onClick: () -> Unit,
  text: String,
  networkStatus: NetworkStatus,
  modifier: Modifier = Modifier,
) {
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  Button(
    enabled = networkStatus == NetworkStatus.Available,
    modifier = modifier.size(contentWidth.standard, contentHeight.halfStandard),
    shape = RoundedCornerShape(shape.small),
    onClick = onClick,
  ) {
    Row {
      Text(text = text)
    }
  }
}