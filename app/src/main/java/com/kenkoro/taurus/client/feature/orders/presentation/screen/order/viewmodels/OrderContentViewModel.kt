package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.core.crypto.EncryptedCredentialService
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.shared.data.SharedViewModelUtils
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.shared.viewmodels.util.UserStateManager
import com.kenkoro.taurus.client.feature.shared.viewmodels.util.UserStateObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderContentViewModel
  @Inject
  constructor(
    private val userStateManager: UserStateManager,
    private val sharedUtils: SharedViewModelUtils,
    private val decryptedCredentialService: DecryptedCredentialService,
    private val encryptedCredentialService: EncryptedCredentialService,
  ) : ViewModel(), UserStateObserver {
    init {
      userStateManager.addNewObserver(this)
    }

    var user by mutableStateOf<User?>(null)
      private set

    override fun updateUserState(user: User) {
      this.user = user
    }

    private fun updateUserStateEverywhere(user: User) = userStateManager.notifyAll(user)

    suspend fun auth(
      subject: String,
      password: String,
    ): Result<TokenDto> = sharedUtils.auth(subject, password)

    fun decryptUserCredentials(): Pair<String, String> {
      return decryptedCredentialService.decryptUserCredentials()
    }

    fun encryptJWToken(tokenDto: TokenDto) {
      encryptedCredentialService.putToken(tokenDto.token)
    }

    fun getUser() {
      // Use here updateUserStateEverywhere()
    }
  }