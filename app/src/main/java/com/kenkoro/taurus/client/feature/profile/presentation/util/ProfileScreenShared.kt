package com.kenkoro.taurus.client.feature.profile.presentation.util

data class ProfileScreenShared(
  val resetAuthStatus: () -> Unit = {},
  val restartApp: () -> Unit = {},
)