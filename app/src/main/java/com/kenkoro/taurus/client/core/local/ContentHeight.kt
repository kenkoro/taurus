package com.kenkoro.taurus.client.core.local

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ContentHeight(
  val standard: Dp = 80.dp,
  val loginButton: Dp = 60.dp,
  val halfStandard: Dp = 40.dp,
  val none: Dp = 0.dp,
  val small: Dp = 5.dp,
  val medium: Dp = 10.dp,
  val large: Dp = 15.dp,
  val extraLarge: Dp = 30.dp,
  val orderItemField: Dp = 25.dp,
  val topBar: Dp = 48.dp,
  val orderItemExpanded: Dp = 320.dp,
  val orderItemExpandedWithoutActionButton: Dp = 260.dp,
  val orderItemNotExpanded: Dp = 70.dp,
  val bottomBar: Dp = loginButton,
  val taurusDropDownItemContent: Dp = 56.dp,
  val taurusDropDownMaxHeight: Dp = 285.dp,
  val actualCutOrdersQuantityDialog: Dp = 128.dp,
  val actualCutOrdersQuantityTextField: Dp = 52.dp,
)

val LocalContentHeight = compositionLocalOf { ContentHeight() }