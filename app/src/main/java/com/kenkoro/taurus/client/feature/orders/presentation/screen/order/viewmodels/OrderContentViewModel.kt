package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import androidx.room.withTransaction
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.core.crypto.EncryptedCredentialService
import com.kenkoro.taurus.client.feature.auth.data.mappers.toUser
import com.kenkoro.taurus.client.feature.auth.data.mappers.toUserEntity
import com.kenkoro.taurus.client.feature.orders.data.local.OrderEntity
import com.kenkoro.taurus.client.feature.orders.data.mappers.toOrder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.OrderFilterContext
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.OrderFilterStrategy
import com.kenkoro.taurus.client.feature.profile.data.remote.repository.UserRepositoryImpl
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.shared.data.SharedViewModelUtils
import com.kenkoro.taurus.client.feature.shared.data.local.LocalDatabase
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.shared.viewmodels.util.UserStateManager
import com.kenkoro.taurus.client.feature.shared.viewmodels.util.UserStateObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class OrderContentViewModel
  @Inject
  constructor(
    private val userStateManager: UserStateManager,
    private val sharedUtils: SharedViewModelUtils,
    private val decryptedCredentialService: DecryptedCredentialService,
    private val encryptedCredentialService: EncryptedCredentialService,
    private val userRepository: UserRepositoryImpl,
    private val localDb: LocalDatabase,
    pager: Pager<Int, OrderEntity>,
  ) : ViewModel(), UserStateObserver {
    private val orderFilterContext = OrderFilterContext()

    init {
      userStateManager.addNewObserver(this)
    }

    var user by mutableStateOf<User?>(null)
      private set

    val ordersPagingFlow =
      pager.flow
        .map { pagingData ->
          pagingData
            .map { it.toOrder() }
            .filter(orderFilterContext::filter)
        }
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    override fun updateUserState(user: User) {
      this.user = user
    }

    fun filterStrategy(strategy: OrderFilterStrategy?) {
      orderFilterContext.strategy(strategy)
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

    suspend fun getUser(
      subject: String,
      tokenDto: TokenDto,
      authWasSuccessful: () -> Unit,
    ): Boolean {
      val result = userRepository.getUser(subject, tokenDto.token)
      result.onSuccess { userDto ->
        localDb.withTransaction { localDb.userDao.upsert(userDto.toUserEntity()) }
        updateUserStateEverywhere(userDto.toUser())
        authWasSuccessful()
      }

      return result.isFailure
    }
  }