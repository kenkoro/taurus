package com.kenkoro.taurus.client.feature.sewing.presentation.login.screen

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.AuthResponse
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.UserRepositoryImpl
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.util.LoginCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.EncryptedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LocalCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import javax.inject.Inject

@JvmInline
value class JwtToken(val value: String)

@HiltViewModel
class LoginViewModel
  @Inject
  constructor(
    private val userRepository: UserRepositoryImpl,
  ) : ViewModel() {
    private val _subject = mutableStateOf("")
    val subject = _subject

    private val _password = mutableStateOf("")
    val password = _password

    fun subject(subject: String) {
      _subject.value = subject
    }

    fun password(password: String) {
      _password.value = password
    }

    suspend fun loginAndEncryptCredentials(
      request: LoginRequest,
      context: Context,
      encryptSubjectAndPassword: Boolean = false,
    ): LoginResponseType {
      return try {
        login(request).run {
          val status = this.status
          if (status.isSuccess()) {
            if (encryptSubjectAndPassword) {
              EncryptedCredentials.encryptCredentials(
                credentials =
                  LoginCredentials(
                    subject = request.subject,
                    password = request.password,
                    token = takeToken(this).value,
                  ),
                context = context,
              )
            } else {
              EncryptedCredentials.encryptCredential(
                credential = takeToken(this).value,
                filename = LocalCredentials.TOKEN_FILENAME,
                context = context,
              )
            }
            LoginResponseType.Success
          } else {
            if (apiUrlNotFound(status)) {
              LoginResponseType.RequestFailure
            } else {
              LoginResponseType.Failure
            }
          }
        }
      } catch (_: Exception) {
        LoginResponseType.RequestFailure
      }
    }

    private fun apiUrlNotFound(statusCode: HttpStatusCode): Boolean {
      return statusCode == HttpStatusCode.NotFound
    }

    private suspend fun takeToken(response: HttpResponse): JwtToken {
      return JwtToken(
        value =
          try {
            response.body<AuthResponse>().token
          } catch (_: Exception) {
            ""
          },
      )
    }

    private suspend fun login(request: LoginRequest): HttpResponse {
      return userRepository.login(request)
    }
  }