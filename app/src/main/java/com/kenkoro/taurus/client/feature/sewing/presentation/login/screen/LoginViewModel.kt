package com.kenkoro.taurus.client.feature.sewing.presentation.login.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

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

    suspend fun login(request: LoginRequest): HttpResponse {
      return userRepository.login(request)
    }
  }