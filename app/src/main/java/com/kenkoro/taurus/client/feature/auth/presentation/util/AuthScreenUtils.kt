package com.kenkoro.taurus.client.feature.auth.presentation.util

import com.kenkoro.taurus.client.feature.auth.presentation.states.PasswordState
import com.kenkoro.taurus.client.feature.auth.presentation.states.SubjectState
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto

data class AuthScreenUtils(
  val subject: SubjectState,
  val password: PasswordState,
  val showErrorTitle: () -> Boolean,
  val auth: suspend (subject: String, password: String) -> Result<TokenDto>,
  val encryptAll: (subject: String, password: String, token: String) -> Unit,
)