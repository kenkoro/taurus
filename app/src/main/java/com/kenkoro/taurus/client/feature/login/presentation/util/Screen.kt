package com.kenkoro.taurus.client.feature.login.presentation.util

sealed class Screen(val route: String) {
  data object LoginScreen : Screen("login_screen")
  data object CustomerScreen : Screen("customer_screen")
}