package com.kenkoro.taurus.client.feature.sewing.presentation.login.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

@JvmInline
value class Message(val value: String)

@HiltViewModel
class LoginViewModel
  @Inject
  constructor(
    private val userRepository: UserRepositoryImpl,
  ) : ViewModel() {
    private val _subject = mutableStateOf("")
    val subject: MutableState<String> = _subject

    private val _password = mutableStateOf("")
    val password: MutableState<String> = _password

    suspend fun login(request: LoginRequest): Message {
      return try {
        val response = userRepository.login(request)
        if (response.status.isSuccess()) {
          Message("")
        } else if (response.status == HttpStatusCode.NotFound) {
          Message("Backend is not running")
        } else {
          Message("Authentication failed")
        }
      } catch (te: HttpRequestTimeoutException) {
        Message(HttpStatusCode.RequestTimeout.toString())
      } catch (uae: UnresolvedAddressException) {
        Message("Check the Internet connection")
      }
    }
  }