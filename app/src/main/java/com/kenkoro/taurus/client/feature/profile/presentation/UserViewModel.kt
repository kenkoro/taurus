package com.kenkoro.taurus.client.feature.profile.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.room.withTransaction
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.core.crypto.EncryptedCredentialService
import com.kenkoro.taurus.client.feature.auth.data.mappers.toUserEntity
import com.kenkoro.taurus.client.feature.profile.data.remote.dto.UserDto
import com.kenkoro.taurus.client.feature.profile.data.remote.repository.UserRepositoryImpl
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.shared.data.local.LocalDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel
  @Inject
  constructor(
    private val userRepository: UserRepositoryImpl,
    private val localDb: LocalDatabase,
    private val decryptedCredentialService: DecryptedCredentialService,
    private val encryptedCredentialService: EncryptedCredentialService,
  ) : ViewModel() {
    var user by mutableStateOf<User?>(null)
      private set

    fun user(user: User?) {
      this.user = user
    }

    suspend fun getUser(
      subject: String,
      token: String,
    ): Result<UserDto> = userRepository.getUser(subject, token)

    suspend fun addNewUserLocally(user: User) {
      localDb.withTransaction {
        localDb.userDao.upsert(user.toUserEntity())
      }
    }

    fun decryptToken(): String = decryptedCredentialService.storedToken()

    fun encryptToken(token: String) {
      encryptedCredentialService.putToken(token)
    }

    fun deleteAllCredentials(): Boolean = decryptedCredentialService.deleteAll()
  }