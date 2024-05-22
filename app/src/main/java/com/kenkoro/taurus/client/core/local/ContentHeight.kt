package com.kenkoro.taurus.client.core.local

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ContentHeight(
  val standard: Dp = 80.dp,
  val loginButton: Dp = 60.dp,
  val halfStandard: Dp = 40.dp,
  val small: Dp = 5.dp,
  val medium: Dp = 10.dp,
  val large: Dp = 15.dp,
  val topBar: Dp = 48.dp,
  val orderItemExpanded: Dp = 320.dp,
  val orderItemExpandedWithoutActionButton: Dp = 250.dp,
  val orderItemNotExpanded: Dp = 90.dp,
  val orderBottomBarExpanded: Dp = 720.dp,
  val orderBottomBarNotExpanded: Dp = standard,
)

val LocalContentHeight = compositionLocalOf { ContentHeight() }