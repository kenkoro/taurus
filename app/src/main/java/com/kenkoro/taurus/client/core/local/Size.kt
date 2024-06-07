package com.kenkoro.taurus.client.core.local

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Size(
  val small: Dp = 15.dp,
  val medium: Dp = 20.dp,
  val large: Dp = 25.dp,
)

val LocalSize = compositionLocalOf { Size() }