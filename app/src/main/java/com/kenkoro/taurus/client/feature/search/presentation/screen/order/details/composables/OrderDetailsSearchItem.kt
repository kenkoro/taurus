package com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderDetailsSearchItem(
  modifier: Modifier = Modifier,
  text: String = "",
) {
  val contentHeight = LocalContentHeight.current

  Column(
    modifier =
      modifier
        .height(contentHeight.orderDetailsSearchItem)
        .fillMaxWidth(),
    verticalArrangement = Arrangement.Center,
  ) {
    Text(text = text)
  }
}

@Preview
@Composable
private fun OrderDetailsSearchItemPrev() {
  AppTheme {
    OrderDetailsSearchItem()
  }
}