package com.kenkoro.taurus.client.feature.sewing.presentation.screen.user

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.GetUserResponse
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.UserRepositoryImpl
import com.kenkoro.taurus.client.feature.sewing.data.util.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

@HiltViewModel
class UserViewModel
  @Inject
  constructor(
    private val userRepository: UserRepositoryImpl,
  ) : ViewModel() {
    private val _user =
      mutableStateOf(
        GetUserResponse(
          id = -1,
          subject = "None",
          password = "None",
          image = "None",
          firstName = "None",
          lastName = "None",
          role = UserRole.Others,
          salt = "None",
        ),
      )
    val user = _user

    private val _isLoading = mutableStateOf(true)
    val isLoading = _isLoading

    fun onLoad(isLoading: Boolean) {
      _isLoading.value = isLoading
    }

    fun onGetUserResponse(userResponse: GetUserResponse) {
      _user.value = userResponse
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
  }