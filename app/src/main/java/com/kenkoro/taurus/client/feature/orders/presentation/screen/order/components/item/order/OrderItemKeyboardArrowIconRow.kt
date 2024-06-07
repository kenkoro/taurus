package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.item.order

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalSize

@Composable
fun OrderItemKeyboardArrowIconRow(
  modifier: Modifier = Modifier,
  selected: Boolean,
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
    label = "AnimatedRotationOfKeyboardArrow",
  )

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.End,
  ) {
    Box(
      modifier =
        Modifier
          .size(size.large)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primary),
      contentAlignment = Alignment.Center,
    ) {
      Icon(
        imageVector = Icons.Default.KeyboardArrowDown,
        contentDescription = "ExpandOrderItemIcon",
        tint = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.rotate(animatedRotationOfKeyboardArrow),
      )
    }
    Spacer(modifier = Modifier.width(contentWidth.orderItemButtonToExpand))
  }
}