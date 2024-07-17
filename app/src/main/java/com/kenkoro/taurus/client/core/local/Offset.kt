package com.kenkoro.taurus.client.core.local

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Offset(
  val standard: Dp = (-80).dp,
  val pullToRefreshIndicator: Dp = (-30).dp,
  val bottomBar: Dp = 60.dp,
  val none: Dp = 0.dp,
)

val LocalOffset = compositionLocalOf { Offset() }