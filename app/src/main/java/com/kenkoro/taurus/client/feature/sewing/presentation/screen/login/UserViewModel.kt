package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toUser
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequestDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.GetUserResponseDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.LoginResponseDto
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.UserRepositoryImpl
import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.LoginCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.inline.JwtToken
import com.kenkoro.taurus.client.feature.sewing.presentation.util.EncryptedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LocalCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import javax.inject.Inject

@HiltViewModel
class UserViewModel
  @Inject
  constructor(
    private val userRepository: UserRepositoryImpl,
  ) : ViewModel() {
    var subject by mutableStateOf("")
      private set

    var password by mutableStateOf("")
      private set

    var loginResponse by mutableStateOf(LoginResponse.Pending)
      private set

    var user by mutableStateOf<User?>(null)
      private set

    var isLoginFailed by mutableStateOf(false)
      private set

    fun subject(subject: String) {
      this.subject = subject
    }

    fun password(password: String) {
      this.password = password
    }

    fun loginResponse(loginResponse: LoginResponse) {
      this.loginResponse = loginResponse
      isLoginFailed = isLoginFailed(this.loginResponse)
    }

    fun onGetUserResponseDto(userDto: GetUserResponseDto) {
      user = userDto.toUser()
    }

    suspend fun login(
      request: LoginRequestDto = LoginRequestDto(subject, password),
      context: Context,
      encryptSubjectAndPassword: Boolean = false,
    ): LoginResponse {
      return try {
        loginWithRequest(request).run {
          val status = this.status
          if (status.isSuccess()) {
            if (encryptSubjectAndPassword) {
              EncryptedCredentials.encryptCredentials(
                credentials =
                  LoginCredentials(
                    subject = request.subject,
                    password = request.password,
                    token = takeToken(this),
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

    suspend fun getUser(
      subject: String,
      token: String,
    ): HttpResponse {
      return userRepository.run {
        token(token)
        getUser(subject)
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

    private suspend fun loginWithRequest(request: LoginRequestDto): HttpResponse {
      return userRepository.login(request)
    }

    private fun isLoginFailed(loginResponse: LoginResponse): Boolean {
      return loginResponse == LoginResponse.Failure ||
        loginResponse == LoginResponse.BadCredentials ||
        loginResponse == LoginResponse.RequestFailure
    }
  }