package com.kenkoro.taurus.client.feature.profile.presentation.util

import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.shared.components.util.NavItemWithNavigation

data class ProfileScreenShared(
  val user: User?,
  val resetAuthStatus: () -> Unit = {},
  val restartApp: () -> Unit = {},
  val currentRoute: String,
  val items: List<NavItemWithNavigation>,
)