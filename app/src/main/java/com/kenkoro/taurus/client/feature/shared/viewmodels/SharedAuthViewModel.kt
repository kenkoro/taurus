package com.kenkoro.taurus.client.feature.shared.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.room.withTransaction
import com.kenkoro.taurus.client.feature.auth.data.mappers.toUser
import com.kenkoro.taurus.client.feature.auth.data.mappers.toUserEntity
import com.kenkoro.taurus.client.feature.profile.data.remote.repository.UserRepositoryImpl
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.shared.data.local.LocalDatabase
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.shared.states.AuthStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedAuthViewModel
  @Inject
  constructor(
    private val userRepository: UserRepositoryImpl,
    private val localDb: LocalDatabase,
  ) : ViewModel() {
    var authStatus by mutableStateOf(AuthStatus.WaitingForAuth)
      private set

    var user by mutableStateOf<User?>(null)
      private set

    fun proceedAuth(authStatus: AuthStatus) {
      this.authStatus = authStatus
    }

    fun reset() {
      authStatus = AuthStatus.WaitingForAuth
    }

    suspend fun getUser(
      subject: String,
      tokenDto: TokenDto,
      postAction: () -> Unit,
    ): Boolean {
      val result = userRepository.getUser(subject, tokenDto.token)
      result.onSuccess { userDto ->
        localDb.withTransaction { localDb.userDao.upsert(userDto.toUserEntity()) }
        user = userDto.toUser()
        postAction()
      }

      return result.isFailure
    }
  }