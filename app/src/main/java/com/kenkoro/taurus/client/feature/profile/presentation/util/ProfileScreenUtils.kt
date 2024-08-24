package com.kenkoro.taurus.client.feature.profile.presentation.util

data class ProfileScreenUtils(
  val deleteAllStoredUserCredentials: () -> Boolean = { false },
  val resetLoginState: () -> Unit = {},
  val restart: () -> Unit = {},
)