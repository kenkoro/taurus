package com.kenkoro.taurus.client.core.local

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Offset(
  val loginContentIsFocused: Dp = (-80).dp,
  val none: Dp = 0.dp,
)

val LocalOffset = compositionLocalOf { Offset() }