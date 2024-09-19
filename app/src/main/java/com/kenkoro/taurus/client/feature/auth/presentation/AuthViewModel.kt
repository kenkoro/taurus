package com.kenkoro.taurus.client.feature.auth.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.core.crypto.EncryptedCredentialService
import com.kenkoro.taurus.client.feature.auth.presentation.states.PasswordState
import com.kenkoro.taurus.client.feature.auth.presentation.states.SubjectState
import com.kenkoro.taurus.client.feature.shared.data.ViewModelUtils
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
  @Inject
  constructor(
    private val encryptedCredentialService: EncryptedCredentialService,
    private val sharedUtils: ViewModelUtils,
  ) : ViewModel() {
    var subject by mutableStateOf(SubjectState(subject = ""))
      private set

    var password by mutableStateOf(PasswordState(password = ""))
      private set

    fun showErrorTitle(): Boolean {
      return subject.showErrors() || password.showErrors()
    }

    suspend fun auth(
      subject: String,
      password: String,
    ): Result<TokenDto> = sharedUtils.auth(subject, password)

    fun encryptAll(
      subject: String,
      password: String,
      token: String,
    ) {
      encryptedCredentialService.putSubject(subject)
      encryptedCredentialService.putPassword(password)
      encryptedCredentialService.putToken(token)
    }
  }