package com.kenkoro.taurus.client.feature.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.core.local.LocalSize
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun LoadingBadge(modifier: Modifier = Modifier) {
  val shape = LocalShape.current
  val size = LocalSize.current

  Box(
    modifier =
      modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
    contentAlignment = Alignment.Center,
  ) {
    Box(
      modifier =
        Modifier
          .shadow(
            elevation = 4.dp,
            shape = RoundedCornerShape(shape.small),
          )
          .size(size.loadingBadge)
          .background(MaterialTheme.colorScheme.onPrimary),
      contentAlignment = Alignment.Center,
    ) {
      CircularProgressIndicator()
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun LoadingBadgePrev() {
  AppTheme {
    LoadingBadge()
  }
}