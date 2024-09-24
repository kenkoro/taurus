package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.item.order

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalSize

@Composable
fun OrderItemKeyboardArrowIconRow(
  modifier: Modifier = Modifier,
  selected: Boolean,
  contentColor: Color = MaterialTheme.colorScheme.onPrimary,
  containerColor: Color = MaterialTheme.colorScheme.primary,
) {
  val size = LocalSize.current
  val contentWidth = LocalContentWidth.current

  val animatedRotationOfKeyboardArrow by animateFloatAsState(
    targetValue =
      if (selected) {
        -180F
      } else {
        0F
      },
    label = "order item arrow composable: rotation",
  )

  Row(
    modifier = Modifier.wrapContentWidth(),
    horizontalArrangement = Arrangement.End,
  ) {
    Box(
      modifier =
        Modifier
          .size(size.large)
          .clip(CircleShape)
          .background(containerColor),
      contentAlignment = Alignment.Center,
    ) {
      Icon(
        imageVector = Icons.Default.KeyboardArrowDown,
        contentDescription = "order item arrow composable: icon to rotate",
        tint = contentColor,
        modifier = Modifier.rotate(animatedRotationOfKeyboardArrow),
      )
    }
    Spacer(modifier = Modifier.width(contentWidth.orderItemButtonToExpand))
  }
}