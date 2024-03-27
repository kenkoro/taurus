package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth

@Composable
fun OrderTopBar(
  modifier: Modifier = Modifier
) {
  val contentHeight = LocalContentHeight.current
  val contentWidth = LocalContentWidth.current

  Spacer(modifier = modifier.height(contentHeight.medium))
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Spacer(modifier = Modifier.width(contentWidth.medium))
    Text(text = "Здесь будет заказчик", style = MaterialTheme.typography.headlineSmall)
    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Drop down arrow")
  }
}