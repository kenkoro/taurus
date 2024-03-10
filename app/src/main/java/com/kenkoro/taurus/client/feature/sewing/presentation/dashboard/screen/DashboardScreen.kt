package com.kenkoro.taurus.client.feature.sewing.presentation.dashboard.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.feature.sewing.presentation.dashboard.screen.components.BottomBarHost
import com.kenkoro.taurus.client.feature.sewing.presentation.order.screen.OrderScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.user.screen.UserScreen
import com.kenkoro.taurus.client.ui.theme.AppTheme

object BottomBarHostIndices {
  const val ORDER_SCREEN = 1
}

@Composable
fun DashboardScreen(
  onLoginNavigate: () -> Unit = {}
) {
  AppTheme {
    Surface(
      modifier =
      Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
    ) {
      BottomBarHost { index ->
        when (index) {
          BottomBarHostIndices.ORDER_SCREEN -> OrderScreen()
          else -> UserScreen()
        }
      }
    }
  }
}

@Preview
@Composable
private fun DashboardScreenPrev() {
  DashboardScreen()
}