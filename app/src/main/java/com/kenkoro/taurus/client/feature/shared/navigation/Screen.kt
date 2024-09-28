package com.kenkoro.taurus.client.feature.shared.navigation

sealed class Screen(val route: String) {
  data object AuthScreen : Screen("auth_screen")

  data object OrderScreen : Screen("order_screen")

  data object ProfileScreen : Screen("profile_screen")

  data object OrderEditorScreen : Screen("order_editor_screen")

  data object OrderDetailsSearchScreen : Screen("order_details_search_screen")

  data object SearchOrdersScreen : Screen("search_orders_screen")

  data object DictionariesScreen : Screen("dictionaries_screen")
}