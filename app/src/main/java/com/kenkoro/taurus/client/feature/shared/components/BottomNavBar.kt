package com.kenkoro.taurus.client.feature.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kenkoro.taurus.client.feature.shared.components.util.NavItemWithNavigation

@Composable
fun BottomNavBar(
  modifier: Modifier = Modifier,
  items: List<NavItemWithNavigation>,
  currentRoute: String,
) {
  NavigationBar(
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.background,
  ) {
    Row(
      modifier =
        Modifier
          .wrapContentHeight()
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.background),
      horizontalArrangement = Arrangement.Center,
    ) {
      items.forEach {
        NavigationBarItem(
          label = it.details.label,
          selected = currentRoute == it.details.route,
          onClick = { it.navigateToSelectedRoute() },
          icon = it.details.icon,
        )
      }
    }
  }
}