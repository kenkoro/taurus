package com.kenkoro.taurus.client.feature.sewing.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.core.crypto.EncryptedCredentialService
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.LoginDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.repository.LoginRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
  @Inject
  constructor(
    private val loginRepository: LoginRepositoryImpl,
    private val encryptedCredentialService: EncryptedCredentialService,
    private val decryptedCredentialService: DecryptedCredentialService,
  ) : ViewModel() {
    var subject by mutableStateOf("")
      private set

    var password by mutableStateOf("")
      private set

    var loginResult by mutableStateOf(LoginResult.NotLoggedYet)
      private set

    fun subject(subject: String) {
      this.subject = subject
    }

    fun password(password: String) {
      this.password = password
    }

    fun loginResult(loginResult: LoginResult) {
      this.loginResult = loginResult
    }

    suspend fun login(
      subject: String = this.subject,
      password: String = this.password,
    ): Result<TokenDto> =
      loginRepository.login(
        LoginDto(
          subject = subject,
          password = password,
        ),
      )

    fun encryptAll(token: String) {
      encryptedCredentialService.putSubject(subject)
      encryptedCredentialService.putPassword(password)
      encryptedCredentialService.putToken(token)
    }

    fun decryptSubjectAndPassword(): Pair<String, String> {
      return Pair(
        decryptedCredentialService.storedSubject(),
        decryptedCredentialService.storedPassword(),
      )
    }
  }