package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequestDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.LoginResponseDto
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.UserRepositoryImpl
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.LoginCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.EncryptedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LocalCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import javax.inject.Inject

@JvmInline
value class JwtToken(val value: String)

@HiltViewModel
class LoginFieldsViewModel
  @Inject
  constructor(
    private val userRepository: UserRepositoryImpl,
  ) : ViewModel() {
    var subject by mutableStateOf("")
      private set

    var password by mutableStateOf("")
      private set

    fun subject(subject: String) {
      this.subject = subject
    }

    fun password(password: String) {
      this.password = password
    }

    suspend fun loginAndEncryptCredentials(
      request: LoginRequestDto,
      context: Context,
      encryptSubjectAndPassword: Boolean = false,
    ): LoginResponse {
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
            LoginResponse.Success
          } else {
            if (apiUrlNotFound(status)) {
              LoginResponse.RequestFailure
            } else {
              LoginResponse.Failure
            }
          }
        }
      } catch (_: Exception) {
        LoginResponse.RequestFailure
      }
    }

    private fun apiUrlNotFound(statusCode: HttpStatusCode): Boolean {
      return statusCode == HttpStatusCode.NotFound
    }

    private suspend fun takeToken(response: HttpResponse): JwtToken {
      return JwtToken(
        value =
          try {
            response.body<LoginResponseDto>().token
          } catch (_: Exception) {
            ""
          },
      )
    }

    private suspend fun login(request: LoginRequestDto): HttpResponse {
      return userRepository.login(request)
    }
  }