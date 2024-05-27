package com.kenkoro.taurus.client.feature.shared.navigation

sealed class Screen(val route: String) {
  data object LoginScreen : Screen("login_screen")

  data object OrderScreen : Screen("order_screen")

  data object ProfileScreen : Screen("profile_screen")
}