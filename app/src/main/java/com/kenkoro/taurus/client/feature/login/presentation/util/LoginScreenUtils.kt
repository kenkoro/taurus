package com.kenkoro.taurus.client.feature.login.presentation.util

import com.kenkoro.taurus.client.core.connectivity.NetworkStatus

data class LoginScreenUtils(
  val subject: SubjectState,
  val password: PasswordState,
  val network: NetworkStatus,
  val encryptAllUserCredentials: (String, String, String) -> Unit,
  val exit: () -> Unit = {},
  val showErrorTitle: () -> Boolean = { false },
)