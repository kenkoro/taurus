package com.kenkoro.taurus.client.core.local

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Padding(val snackbar: Dp = 18.dp)

val LocalPadding = compositionLocalOf { Padding() }