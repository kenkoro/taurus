package com.kenkoro.taurus.client.core.local

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Shape(
  val default: Dp = 0.dp,
  val small: Dp = 16.dp,
  val medium: Dp = 32.dp,
  val large: Dp = 48.dp
)

val LocalShape = compositionLocalOf { Shape() }
