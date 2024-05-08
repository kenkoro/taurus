package com.kenkoro.taurus.client.feature.sewing.presentation.navigation

sealed class Screen(val route: String) {
  data object LoginScreen : Screen("login_screen")

  data object OrderScreen : Screen("order_screen")
}