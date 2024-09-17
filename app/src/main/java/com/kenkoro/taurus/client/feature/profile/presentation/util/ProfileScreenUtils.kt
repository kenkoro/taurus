package com.kenkoro.taurus.client.feature.profile.presentation.util

data class ProfileScreenUtils(
  val resetAuthStatus: () -> Unit = {},
  val restartApp: () -> Unit = {},
)