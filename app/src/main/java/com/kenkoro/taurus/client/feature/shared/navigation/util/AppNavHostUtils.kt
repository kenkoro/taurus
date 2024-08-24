package com.kenkoro.taurus.client.feature.shared.navigation.util

import com.kenkoro.taurus.client.feature.shared.navigation.Screen

data class AppNavHostUtils(
  val startDestination: (String, String) -> Screen,
  val exit: () -> Unit = {},
  val restart: () -> Unit = {},
)