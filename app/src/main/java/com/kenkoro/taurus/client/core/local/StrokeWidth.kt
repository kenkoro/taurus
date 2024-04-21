package com.kenkoro.taurus.client.core.local

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class StrokeWidth(val standard: Dp = 4.dp)

val LocalStrokeWidth = compositionLocalOf { StrokeWidth() }