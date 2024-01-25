package com.kenkoro.taurus.mobile_client.feature_login.presentation.login_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.mobile_client.feature_login.data.source.mappers.toUser
import com.kenkoro.taurus.mobile_client.feature_login.data.source.mappers.toUserEntity
import com.kenkoro.taurus.mobile_client.feature_login.data.source.remote.UserDto
import com.kenkoro.taurus.mobile_client.feature_login.domain.model.AuthRequest
import com.kenkoro.taurus.mobile_client.feature_login.domain.model.InvalidUserException
import com.kenkoro.taurus.mobile_client.feature_login.domain.model.User
import com.kenkoro.taurus.mobile_client.feature_login.domain.repository.ApiRepository
import com.kenkoro.taurus.mobile_client.feature_login.domain.repository.UserRepository
import com.kenkoro.taurus.mobile_client.feature_login.presentation.login_screen.inline.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val dummyApi: ApiRepository,
  private val userRepository: UserRepository
) : ViewModel() {
  private val _username = mutableStateOf("")
  val username: MutableState<String> = _username

  private val _password = mutableStateOf("")
  val password: MutableState<String> = _password

  @Throws(InvalidUserException::class)
  private suspend fun authenticateOrThrowInvalidUserException(): UserDto {
    return try {
      dummyApi.auth(
        AuthRequest(
          username = username.value,
          password = password.value
        )
      )
    } catch (iue: InvalidUserException) {
      return UserDto(
        id = 0,
        username = "",
        email = "",
        firstName = "",
        lastName = "",
        gender = "",
        image = "",
        token = ""
      )
    }
  }

  private suspend fun authenticateAndStoreLocally(): User {
    val userEntity = authenticateOrThrowInvalidUserException().toUserEntity()

    if (userEntity.firstName.isNotBlank()) userRepository.upsert(userEntity)

    return userEntity.toUser()
  }

  suspend fun onEvent(event: UserEvent): UserResponse {
    when (event) {
      UserEvent.AUTH -> {
        return UserResponse(authenticateAndStoreLocally())
      }
    }
  }
}