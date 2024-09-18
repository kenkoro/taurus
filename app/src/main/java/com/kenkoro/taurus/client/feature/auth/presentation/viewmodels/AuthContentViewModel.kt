package com.kenkoro.taurus.client.feature.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.core.crypto.EncryptedCredentialService
import com.kenkoro.taurus.client.feature.auth.data.remote.repository.AuthRepositoryImpl
import com.kenkoro.taurus.client.feature.shared.data.ViewModelUtils
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthContentViewModel
  @Inject
  constructor(
    private val repository: AuthRepositoryImpl,
    private val encryptedCredentialService: EncryptedCredentialService,
    private val sharedUtils: ViewModelUtils,
  ) : ViewModel() {
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