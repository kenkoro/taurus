package com.kenkoro.taurus.client.core.local

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Elevation(
  val standard: Dp = 4.dp,
)

val LocalElevation = compositionLocalOf { Elevation() }