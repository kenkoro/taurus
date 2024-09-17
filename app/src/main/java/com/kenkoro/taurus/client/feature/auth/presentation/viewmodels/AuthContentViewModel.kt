package com.kenkoro.taurus.client.feature.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.core.crypto.EncryptedCredentialService
import com.kenkoro.taurus.client.feature.auth.data.remote.dto.AuthDto
import com.kenkoro.taurus.client.feature.auth.data.remote.repository.LoginRepositoryImpl
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthContentViewModel
  @Inject
  constructor(
    private val repository: LoginRepositoryImpl,
    private val encryptedCredentialService: EncryptedCredentialService,
  ) : ViewModel() {
    suspend fun auth(
      subject: String,
      password: String,
    ): Result<TokenDto> {
      val result =
        repository.logIn(
          AuthDto(
            subject = subject,
            password = password,
          ),
        )

      return result
    }

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