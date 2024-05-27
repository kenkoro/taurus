package com.kenkoro.taurus.client.feature.shared.components

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun TaurusIcon(
  modifier: Modifier = Modifier,
  imageVector: ImageVector,
  contentDescription: String,
  isError: Boolean = false,
) {
  val color =
    if (isError) {
      MaterialTheme.colorScheme.error
    } else {
      LocalContentColor.current
    }

  Icon(
    imageVector = imageVector,
    contentDescription = contentDescription,
    tint = color,
    modifier = modifier,
  )
}