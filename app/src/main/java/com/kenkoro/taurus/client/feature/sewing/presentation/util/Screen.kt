package com.kenkoro.taurus.client.feature.sewing.presentation.util

sealed class Screen(val route: String) {
  data object LoginScreen : Screen("login_screen")

  data object DashboardScreen : Screen("dashboard_screen")

  data object UserScreen : Screen("user_screen")

  data object OrderScreen : Screen("order_screen")
}