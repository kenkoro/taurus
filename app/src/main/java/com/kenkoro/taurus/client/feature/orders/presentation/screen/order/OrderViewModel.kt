package com.kenkoro.taurus.client.feature.orders.presentation.screen.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.room.withTransaction
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.core.crypto.EncryptedCredentialService
import com.kenkoro.taurus.client.feature.orders.data.local.OrderEntity
import com.kenkoro.taurus.client.feature.orders.data.mappers.toNewOrderDto
import com.kenkoro.taurus.client.feature.orders.data.mappers.toOrder
import com.kenkoro.taurus.client.feature.orders.data.mappers.toOrderEntity
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.repository.OrderRepositoryImpl
import com.kenkoro.taurus.client.feature.orders.domain.NewOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderFilterContext
import com.kenkoro.taurus.client.feature.shared.data.local.LocalDatabase
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.DeleteDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class OrderViewModel
  @Inject
  constructor(
    private val pager: Pager<Int, OrderEntity>,
    private val localDb: LocalDatabase,
    private val orderRepository: OrderRepositoryImpl,
    private val decryptedCredentialService: DecryptedCredentialService,
    private val encryptedCredentialService: EncryptedCredentialService,
  ) : ViewModel() {
    var ordersPagingFlow: Flow<PagingData<Order>>? = null
      private set

    var selectedOrderRecordId by mutableStateOf<Int?>(null)
      private set

    fun ordersPagingFlow(orderFilterContext: OrderFilterContext) {
      ordersPagingFlow =
        pager.flow
          .map { pagingData ->
            orderFilterContext.filter(pagingData)
            pagingData.map { it.toOrder() }
          }
          .flowOn(Dispatchers.IO)
          .cachedIn(viewModelScope)
    }

    fun onSelectOrder(selectedOrderRecordId: Int?) {
      this.selectedOrderRecordId = selectedOrderRecordId
    }

    suspend fun deleteOrderLocally(order: Order) {
      localDb.withTransaction {
        localDb.orderDao.delete(order.toOrderEntity())
      }
    }

    suspend fun addNewOrderLocally(newOrder: NewOrder) {
      localDb.withTransaction {
        localDb.orderDao.upsert(newOrder.toOrderEntity())
      }
    }

    suspend fun editOrderLocally(newOrder: NewOrder) {
      localDb.withTransaction {
        localDb.orderDao.upsert(newOrder.toOrderEntity())
      }
    }

    suspend fun deleteOrderRemotely(
      orderId: Int,
      deleterSubject: String,
    ): Boolean {
      val result =
        orderRepository.deleteOrder(
          dto = DeleteDto(deleterSubject = deleterSubject),
          orderId = orderId,
          token = decryptedCredentialService.storedToken(),
        )

      return result.isSuccess
    }

    suspend fun addNewOrderRemotely(newOrder: NewOrder): Result<OrderDto> =
      orderRepository.addNewOrder(
        dto = newOrder.toNewOrderDto(),
        token = decryptedCredentialService.storedToken(),
      )

    suspend fun editOrderRemotely(
      dto: NewOrderDto,
      orderId: Int,
      editorSubject: String,
      token: String,
    ): Boolean {
      val result =
        orderRepository.editOrder(
          dto = dto,
          orderId = orderId,
          editorSubject = editorSubject,
          token = token,
        )

      return result.isSuccess
    }

    fun encryptToken(token: String) {
      encryptedCredentialService.putToken(token)
    }

    fun deleteAllCredentials(): Boolean = decryptedCredentialService.deleteAll()
  }