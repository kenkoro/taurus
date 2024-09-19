package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.viewmodels

import androidx.lifecycle.ViewModel
import androidx.room.withTransaction
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.feature.orders.data.mappers.toOrderEntity
import com.kenkoro.taurus.client.feature.orders.data.remote.repository.OrderRepositoryImpl
import com.kenkoro.taurus.client.feature.orders.domain.EditOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.shared.data.local.LocalDatabase
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.DeleteDto
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderItemActionButtonViewModel
  @Inject
  constructor(
    private val orderRepository: OrderRepositoryImpl,
    private val decryptedCredentialService: DecryptedCredentialService,
    private val localDb: LocalDatabase,
  ) : ViewModel() {
    suspend fun editOrder(
      dto: EditOrder,
      editor: String,
      postAction: () -> Unit,
    ): Boolean {
      val result =
        orderRepository.editOrder(
          dto.toEditOrderDto(),
          editor,
          decryptedCredentialService.storedToken(),
        )
      result.onSuccess {
        localDb.withTransaction { localDb.orderDao.upsert(dto.toOrderEntity(dto.orderId)) }
        postAction()
      }

      return result.isFailure
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