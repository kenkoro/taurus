package com.kenkoro.taurus.client.core.local

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Blur(
  val standard: Dp = 16.dp,
)

val LocalBlur = compositionLocalOf { Blur() }