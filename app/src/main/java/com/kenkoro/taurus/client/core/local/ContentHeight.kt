package com.kenkoro.taurus.client.core.local

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ContentHeight(
  val standard: Dp = 80.dp,
  val small: Dp = 5.dp,
  val medium: Dp = 10.dp,
  val large: Dp = 15.dp
)

val LocalContentHeight = compositionLocalOf { ContentHeight() }