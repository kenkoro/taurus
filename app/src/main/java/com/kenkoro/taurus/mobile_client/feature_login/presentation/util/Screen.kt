package com.kenkoro.taurus.mobile_client.feature_login.presentation.util

sealed class Screen(val route: String) {
  object LoginScreen : Screen("login_screen")
}