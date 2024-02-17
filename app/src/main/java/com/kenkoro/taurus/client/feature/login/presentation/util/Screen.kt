package com.kenkoro.taurus.client.feature.login.presentation.util

sealed class Screen(val route: String) {
  data object WelcomeScreen : Screen("welcome_screen")
  data object LoginScreen : Screen("login_screen")
  data object UserManagementScreen : Screen("user_management_screen")
}