package com.kenkoro.taurus.client.feature.shared.components.util

import com.kenkoro.taurus.client.feature.shared.navigation.util.BottomNavItem

data class NavItemWithNavigation(
  val details: BottomNavItem,
  val navigateToSelectedRoute: () -> Unit = {},
)