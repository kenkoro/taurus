package com.kenkoro.taurus.client.core.local

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ContentWidth(
  val standard: Dp = 330.dp,
  val halfStandard: Dp = 165.dp,
  val small: Dp = 5.dp,
  val medium: Dp = 10.dp,
  val extraMedium: Dp = 20.dp,
  val orderItemButtonToExpand: Dp = 20.dp,
  val large: Dp = 40.dp,
  val orderItem: Dp = 350.dp,
  val orderTitle: Dp = 100.dp,
  val editOrderButton: Dp = 70.dp,
  val actualCutOrdersQuantityTextField: Dp = 128.dp,
  val dropDown: Dp = orderItem,
)

val LocalContentWidth = compositionLocalOf { ContentWidth() }