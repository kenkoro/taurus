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
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.PasswordState
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.SubjectState
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.util.LoginState
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.TaurusTextFieldState
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
    var subject by mutableStateOf(SubjectState(subject = ""))
      private set

    var password by mutableStateOf(PasswordState(password = ""))
      private set

    var loginState by mutableStateOf(LoginState.NotLoggedYet)
      private set

    fun setErrorMessages(
      state: TaurusTextFieldState,
      errorMessage: String,
      emptyTextFieldErrorMessage: String,
    ) {
      state.setErrorMessages(errorMessage, emptyTextFieldErrorMessage)
    }

    fun loginState(loginState: LoginState) {
      this.loginState = loginState
    }

    suspend fun login(
      subject: String = this.subject.text,
      password: String = this.password.text,
    ): Result<TokenDto> =
      loginRepository.login(
        LoginDto(
          subject = subject,
          password = password,
        ),
      )

    fun encryptAll(
      subject: String,
      password: String,
      token: String,
    ) {
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

    fun showErrorTitle(): Boolean {
      return subject.showErrors() || password.showErrors()
    }
  }