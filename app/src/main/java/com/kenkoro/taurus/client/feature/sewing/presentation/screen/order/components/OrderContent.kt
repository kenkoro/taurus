package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.core.local.LocalArrangement
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderContent() {
  val arrangement = LocalArrangement.current

  Surface(
    modifier =
    Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(arrangement.standard)
    ) {
      OrderTopBar()
    }
  }
}

@Preview
@Composable
private fun OrderContentPrev() {
  AppTheme {
    OrderContent()
  }
}