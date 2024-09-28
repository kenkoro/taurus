package com.kenkoro.taurus.client.feature.dictionaries.presentation.util

import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.shared.components.util.NavItemWithNavigation

data class DictionariesScreenShared(
  val user: User?,
  val currentRoute: String,
  val items: List<NavItemWithNavigation>,
)