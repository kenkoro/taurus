package com.kenkoro.taurus.client.feature.orders.presentation.screen.order

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
import com.kenkoro.taurus.client.feature.orders.data.mappers.toOrderEntity
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.ActualCutOrdersQuantityDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.CutOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.repository.CutOrderRepositoryImpl
import com.kenkoro.taurus.client.feature.orders.data.remote.repository.OrderRepositoryImpl
import com.kenkoro.taurus.client.feature.orders.domain.EditOrder
import com.kenkoro.taurus.client.feature.orders.domain.NewCutOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.OrderFilterContext
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.OrderFilterStrategy
import com.kenkoro.taurus.client.feature.profile.data.remote.repository.UserRepositoryImpl
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.shared.data.SharedViewModelUtils
import com.kenkoro.taurus.client.feature.shared.data.local.LocalDatabase
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.DeleteDto
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class OrderViewModel
  @Inject
  constructor(
    private val sharedUtils: SharedViewModelUtils,
    private val decryptedCredentialService: DecryptedCredentialService,
    private val encryptedCredentialService: EncryptedCredentialService,
    private val userRepository: UserRepositoryImpl,
    private val orderRepository: OrderRepositoryImpl,
    private val cutOrderRepository: CutOrderRepositoryImpl,
    private val localDb: LocalDatabase,
    pager: Pager<Int, OrderEntity>,
  ) : ViewModel() {
    private val orderFilterContext = OrderFilterContext()

    val ordersPagingFlow =
      pager.flow
        .map { pagingData ->
          pagingData
            .map { it.toOrder() }
            .filter(orderFilterContext::filter)
        }
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    var user by mutableStateOf<User?>(null)
      private set

    var selectedOrderRecordId by mutableStateOf<Int?>(null)
      private set

    fun filterStrategy(strategy: OrderFilterStrategy?) {
      orderFilterContext.strategy(strategy)
    }

    fun newOrderSelection(recordId: Int) {
      selectedOrderRecordId = recordId
    }

    fun clearOrderSelection() {
      selectedOrderRecordId = null
    }

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

    suspend fun getActualQuantityOfCutMaterial(orderId: Int): Result<ActualCutOrdersQuantityDto> {
      return cutOrderRepository.getActualCutOrdersQuantity(
        orderId = orderId,
        token = decryptedCredentialService.storedToken(),
      )
    }

    suspend fun addNewCutOrder(cutOrder: NewCutOrder): Result<CutOrderDto> {
      return sharedUtils.addNewCutOrder(cutOrder)
    }

    suspend fun editOrder(
      dto: EditOrder,
      editor: String,
      postAction: () -> Unit,
    ): Boolean {
      return sharedUtils.editOrder(dto, editor, postAction)
    }

    suspend fun deleteOrder(
      order: Order,
      deleter: String,
      postAction: () -> Unit,
    ): Boolean {
      val result =
        orderRepository.deleteOrder(
          dto = DeleteDto(deleter),
          orderId = order.orderId,
          token = decryptedCredentialService.storedToken(),
        )
      result.onSuccess {
        localDb.orderDao.delete(order.toOrderEntity())
        postAction()
      }

      return result.isFailure
    }
  }