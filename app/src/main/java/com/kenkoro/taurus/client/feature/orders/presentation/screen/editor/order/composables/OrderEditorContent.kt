package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderEditorContent(
  modifier: Modifier = Modifier,
  networkStatus: NetworkStatus,
) {
  Column {
  }
}

@Preview
@Composable
private fun OrderEditorContentPrev() {
  AppTheme {
    OrderEditorContent(networkStatus = NetworkStatus.Available)
  }
}