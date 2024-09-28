package com.kenkoro.taurus.client.feature.search.presentation.screen.orders.util

import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.shared.components.util.NavItemWithNavigation

data class SearchOrdersScreenShared(
  val user: User?,
  val currentRoute: String,
  val items: List<NavItemWithNavigation>,
)