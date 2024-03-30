package com.kenkoro.taurus.client.feature.sewing.presentation.screen.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toUser
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.GetUserResponseDto
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.UserRepositoryImpl
import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

@HiltViewModel
class UserViewModel
@Inject
constructor(
  private val userRepository: UserRepositoryImpl,
) : ViewModel() {
  var user by mutableStateOf<User?>(null)
    private set

  fun onGetUserResponseDto(userDto: GetUserResponseDto) {
    user = userDto.toUser()
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