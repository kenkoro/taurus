package com.kenkoro.taurus.client.feature.sewing.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.room.withTransaction
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.core.crypto.EncryptedCredentialService
import com.kenkoro.taurus.client.feature.sewing.data.source.local.LocalDatabase
import com.kenkoro.taurus.client.feature.sewing.data.source.local.OrderEntity
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toNewOrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toOrder
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toOrderEntity
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.DeleteDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.repository.OrderRepositoryImpl
import com.kenkoro.taurus.client.feature.sewing.domain.model.NewOrder
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class OrderViewModel
  @Inject
  constructor(
    pager: Pager<Int, OrderEntity>,
    private val localDb: LocalDatabase,
    private val orderRepository: OrderRepositoryImpl,
    private val decryptedCredentialService: DecryptedCredentialService,
    private val encryptedCredentialService: EncryptedCredentialService,
  ) : ViewModel() {
    val orderPagingFlow =
      pager
        .flow
        .map { pagingData ->
          pagingData.map { it.toOrder() }
        }
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

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